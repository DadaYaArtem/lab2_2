package com.pizzeria.model.payment;

import com.pizzeria.enums.PaymentMethod;
import com.pizzeria.exceptions.InvalidPaymentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты для CashPayment")
class CashPaymentTest {

    private CashPayment cashPayment;
    private static final String TRANSACTION_ID = "CASH-001";
    private static final double AMOUNT = 500.0;

    @BeforeEach
    void setUp() throws InvalidPaymentException {
        cashPayment = new CashPayment(TRANSACTION_ID, AMOUNT);
    }

    @Test
    @DisplayName("Успешное создание наличного платежа")
    void testConstructorSuccess() {
        assertNotNull(cashPayment);
        assertEquals(TRANSACTION_ID, cashPayment.getTransactionId());
        assertEquals(AMOUNT, cashPayment.getAmount());
        assertEquals(PaymentMethod.CASH, cashPayment.getMethod());
        assertFalse(cashPayment.isSuccessful());
    }

    @Test
    @DisplayName("Исключение при создании платежа с отрицательной суммой")
    void testConstructorWithNegativeAmount() {
        assertThrows(InvalidPaymentException.class, () -> {
            new CashPayment(TRANSACTION_ID, -100.0);
        });
    }

    @Test
    @DisplayName("Исключение при создании платежа с нулевой суммой")
    void testConstructorWithZeroAmount() {
        assertThrows(InvalidPaymentException.class, () -> {
            new CashPayment(TRANSACTION_ID, 0.0);
        });
    }

    @Test
    @DisplayName("Успешная обработка платежа с точной суммой")
    void testProcessWithExactAmount() throws InvalidPaymentException {
        cashPayment.setAmountReceived(AMOUNT);
        boolean result = cashPayment.process();

        assertTrue(result);
        assertTrue(cashPayment.isSuccessful());
        assertEquals(0.0, cashPayment.getChange());
    }

    @Test
    @DisplayName("Успешная обработка платежа с превышением суммы")
    void testProcessWithExcessAmount() throws InvalidPaymentException {
        double receivedAmount = 600.0;
        cashPayment.setAmountReceived(receivedAmount);
        boolean result = cashPayment.process();

        assertTrue(result);
        assertTrue(cashPayment.isSuccessful());
        assertEquals(100.0, cashPayment.getChange());
    }

    @Test
    @DisplayName("Исключение при недостаточной сумме")
    void testProcessWithInsufficientAmount() {
        cashPayment.setAmountReceived(400.0);

        InvalidPaymentException exception = assertThrows(InvalidPaymentException.class, () -> {
            cashPayment.process();
        });

        assertTrue(exception.getMessage().contains("Получено недостаточно средств"));
        assertFalse(cashPayment.isSuccessful());
    }

    @Test
    @DisplayName("Расчет сдачи при разных суммах")
    void testCalculateChange() {
        assertEquals(0.0, cashPayment.calculateChange(500.0));
        assertEquals(100.0, cashPayment.calculateChange(600.0));
        assertEquals(500.0, cashPayment.calculateChange(1000.0));
        assertEquals(-100.0, cashPayment.calculateChange(400.0));
    }

    @Test
    @DisplayName("Успешный возврат после успешного платежа")
    void testRefundSuccess() throws InvalidPaymentException {
        cashPayment.setAmountReceived(AMOUNT);
        cashPayment.process();

        assertDoesNotThrow(() -> cashPayment.refund());
        assertFalse(cashPayment.isSuccessful());
    }

    @Test
    @DisplayName("Исключение при возврате неуспешного платежа")
    void testRefundWithoutSuccessfulPayment() {
        InvalidPaymentException exception = assertThrows(InvalidPaymentException.class, () -> {
            cashPayment.refund();
        });

        assertTrue(exception.getMessage().contains("Возврат невозможен"));
    }

    @Test
    @DisplayName("Получение и установка полученной суммы")
    void testAmountReceivedGetterSetter() {
        cashPayment.setAmountReceived(1000.0);
        assertEquals(1000.0, cashPayment.getAmountReceived());
    }

    @Test
    @DisplayName("Получение сдачи после обработки платежа")
    void testGetChangeAfterProcess() throws InvalidPaymentException {
        cashPayment.setAmountReceived(700.0);
        cashPayment.process();
        assertEquals(200.0, cashPayment.getChange());
    }

    @Test
    @DisplayName("Получение чека платежа")
    void testGetReceipt() {
        String receipt = cashPayment.getReceipt();
        assertNotNull(receipt);
        assertTrue(receipt.contains(TRANSACTION_ID));
        assertTrue(receipt.contains(String.valueOf(AMOUNT)));
    }

    @Test
    @DisplayName("Обработка платежа с очень большой суммой")
    void testProcessWithLargeAmount() throws InvalidPaymentException {
        CashPayment largePayment = new CashPayment("CASH-002", 1000000.0);
        largePayment.setAmountReceived(2000000.0);

        assertTrue(largePayment.process());
        assertEquals(1000000.0, largePayment.getChange());
    }

    @Test
    @DisplayName("Обработка платежа с минимальной положительной суммой")
    void testProcessWithMinimalAmount() throws InvalidPaymentException {
        CashPayment minimalPayment = new CashPayment("CASH-003", 0.01);
        minimalPayment.setAmountReceived(0.01);

        assertTrue(minimalPayment.process());
        assertEquals(0.0, minimalPayment.getChange(), 0.001);
    }
}
