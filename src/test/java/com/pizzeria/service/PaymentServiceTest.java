package com.pizzeria.service;

import com.pizzeria.exceptions.InvalidPaymentException;
import com.pizzeria.model.Order;
import com.pizzeria.model.Receipt;
import com.pizzeria.model.payment.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тесты для PaymentService")
class PaymentServiceTest {

    private PaymentService paymentService;

    @Mock
    private Order mockOrder;

    @Mock
    private Payment mockPayment;

    @BeforeEach
    void setUp() {
        paymentService = new PaymentService();
    }

    @Test
    @DisplayName("Успешная обработка платежа с достаточной суммой")
    void testProcessPayment_SufficientAmount_Success() throws InvalidPaymentException {
        // Arrange
        when(mockOrder.getFinalPrice()).thenReturn(100.0);
        when(mockPayment.getAmount()).thenReturn(150.0);
        when(mockPayment.process()).thenReturn(true);

        // Act
        Receipt receipt = paymentService.processPayment(mockOrder, mockPayment);

        // Assert
        assertNotNull(receipt);
        assertEquals("RCP-1", receipt.getReceiptNumber());
        verify(mockOrder, times(1)).processPayment(150.0);
        verify(mockPayment, times(1)).process();
    }

    @Test
    @DisplayName("Обработка платежа с точной суммой")
    void testProcessPayment_ExactAmount_Success() throws InvalidPaymentException {
        // Arrange
        when(mockOrder.getFinalPrice()).thenReturn(100.0);
        when(mockPayment.getAmount()).thenReturn(100.0);
        when(mockPayment.process()).thenReturn(true);

        // Act
        Receipt receipt = paymentService.processPayment(mockOrder, mockPayment);

        // Assert
        assertNotNull(receipt);
        verify(mockOrder, times(1)).processPayment(100.0);
    }

    @Test
    @DisplayName("Обработка платежа с недостаточной суммой выбрасывает исключение")
    void testProcessPayment_InsufficientAmount_ThrowsException() {
        // Arrange
        when(mockOrder.getFinalPrice()).thenReturn(100.0);
        when(mockPayment.getAmount()).thenReturn(50.0);

        // Act & Assert
        InvalidPaymentException exception = assertThrows(InvalidPaymentException.class, () -> {
            paymentService.processPayment(mockOrder, mockPayment);
        });

        assertTrue(exception.getMessage().contains("Недостаточная сумма"));
        verify(mockPayment, never()).process();
    }

    @Test
    @DisplayName("Обработка платежа с нулевой суммой выбрасывает исключение")
    void testProcessPayment_ZeroAmount_ThrowsException() {
        // Arrange
        when(mockOrder.getFinalPrice()).thenReturn(100.0);
        when(mockPayment.getAmount()).thenReturn(0.0);

        // Act & Assert
        assertThrows(InvalidPaymentException.class, () -> {
            paymentService.processPayment(mockOrder, mockPayment);
        });
    }

    @Test
    @DisplayName("Неудачная обработка платежа выбрасывает исключение")
    void testProcessPayment_ProcessingFails_ThrowsException() {
        // Arrange
        when(mockOrder.getFinalPrice()).thenReturn(100.0);
        when(mockPayment.getAmount()).thenReturn(150.0);
        when(mockPayment.process()).thenReturn(false);

        // Act & Assert
        InvalidPaymentException exception = assertThrows(InvalidPaymentException.class, () -> {
            paymentService.processPayment(mockOrder, mockPayment);
        });

        assertTrue(exception.getMessage().contains("Ошибка обработки платежа"));
        verify(mockOrder, never()).processPayment(anyDouble());
    }

    @Test
    @DisplayName("Множественные платежи увеличивают счетчик чеков")
    void testProcessPayment_MultiplePayments_IncrementReceiptCounter() throws InvalidPaymentException {
        // Arrange
        when(mockOrder.getFinalPrice()).thenReturn(100.0);
        when(mockPayment.getAmount()).thenReturn(150.0);
        when(mockPayment.process()).thenReturn(true);

        // Act
        Receipt receipt1 = paymentService.processPayment(mockOrder, mockPayment);
        Receipt receipt2 = paymentService.processPayment(mockOrder, mockPayment);
        Receipt receipt3 = paymentService.processPayment(mockOrder, mockPayment);

        // Assert
        assertEquals("RCP-1", receipt1.getReceiptNumber());
        assertEquals("RCP-2", receipt2.getReceiptNumber());
        assertEquals("RCP-3", receipt3.getReceiptNumber());
    }

    @Test
    @DisplayName("Успешный возврат платежа")
    void testRefundPayment_SuccessfulPayment_Success() throws InvalidPaymentException {
        // Arrange
        when(mockPayment.isSuccessful()).thenReturn(true);

        // Act
        assertDoesNotThrow(() -> {
            paymentService.refundPayment(mockPayment);
        });

        // Assert
        verify(mockPayment, times(1)).refund();
    }

    @Test
    @DisplayName("Возврат неуспешного платежа выбрасывает исключение")
    void testRefundPayment_UnsuccessfulPayment_ThrowsException() {
        // Arrange
        when(mockPayment.isSuccessful()).thenReturn(false);

        // Act & Assert
        InvalidPaymentException exception = assertThrows(InvalidPaymentException.class, () -> {
            paymentService.refundPayment(mockPayment);
        });

        assertTrue(exception.getMessage().contains("Невозможно вернуть неуспешный платеж"));
        verify(mockPayment, never()).refund();
    }

    @Test
    @DisplayName("Расчет налога 13%")
    void testCalculateTax_ValidAmount_Returns13Percent() {
        // Arrange
        double amount = 1000.0;

        // Act
        double tax = paymentService.calculateTax(amount);

        // Assert
        assertEquals(130.0, tax, 0.01);
    }

    @Test
    @DisplayName("Расчет налога для нулевой суммы")
    void testCalculateTax_ZeroAmount_ReturnsZero() {
        // Act
        double tax = paymentService.calculateTax(0.0);

        // Assert
        assertEquals(0.0, tax, 0.01);
    }

    @Test
    @DisplayName("Расчет налога для малых сумм")
    void testCalculateTax_SmallAmount_ReturnsCorrectTax() {
        // Arrange
        double amount = 10.0;

        // Act
        double tax = paymentService.calculateTax(amount);

        // Assert
        assertEquals(1.3, tax, 0.01);
    }

    @Test
    @DisplayName("Расчет сервисного сбора 5%")
    void testCalculateServiceFee_ValidAmount_Returns5Percent() {
        // Arrange
        double amount = 1000.0;

        // Act
        double fee = paymentService.calculateServiceFee(amount);

        // Assert
        assertEquals(50.0, fee, 0.01);
    }

    @Test
    @DisplayName("Расчет сервисного сбора для нулевой суммы")
    void testCalculateServiceFee_ZeroAmount_ReturnsZero() {
        // Act
        double fee = paymentService.calculateServiceFee(0.0);

        // Assert
        assertEquals(0.0, fee, 0.01);
    }

    @Test
    @DisplayName("Расчет сервисного сбора для малых сумм")
    void testCalculateServiceFee_SmallAmount_ReturnsCorrectFee() {
        // Arrange
        double amount = 20.0;

        // Act
        double fee = paymentService.calculateServiceFee(amount);

        // Assert
        assertEquals(1.0, fee, 0.01);
    }

    @Test
    @DisplayName("Расчет налога и сбора для больших сумм")
    void testCalculateTaxAndFee_LargeAmount_ReturnsCorrectValues() {
        // Arrange
        double amount = 10000.0;

        // Act
        double tax = paymentService.calculateTax(amount);
        double fee = paymentService.calculateServiceFee(amount);

        // Assert
        assertEquals(1300.0, tax, 0.01);
        assertEquals(500.0, fee, 0.01);
    }
}
