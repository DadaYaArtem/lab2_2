import com.pizzeria.enums.PaymentMethod;
import com.pizzeria.exceptions.InvalidPaymentException;
import com.pizzeria.model.payment.CashPayment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CashPayment Tests")
class CashPaymentTest {

    private CashPayment payment;

    @BeforeEach
    void setUp() throws InvalidPaymentException {
        payment = new CashPayment("CASH001", 1000.0);
    }

    @Test
    @DisplayName("Создание платежа наличными")
    void testPaymentCreation() {
        assertNotNull(payment);
        assertEquals("CASH001", payment.getTransactionId());
        assertEquals(1000.0, payment.getAmount(), 0.01);
        assertEquals(PaymentMethod.CASH, payment.getMethod());
    }

    @Test
    @DisplayName("Обработка платежа с достаточной суммой")
    void testProcessWithSufficientAmount() throws InvalidPaymentException {
        payment.setAmountReceived(1500.0);
        assertTrue(payment.process());
        assertTrue(payment.isSuccessful());
        assertEquals(500.0, payment.getChange(), 0.01);
    }

    @Test
    @DisplayName("Обработка платежа с точной суммой")
    void testProcessWithExactAmount() throws InvalidPaymentException {
        payment.setAmountReceived(1000.0);
        assertTrue(payment.process());
        assertEquals(0.0, payment.getChange(), 0.01);
    }

    @Test
    @DisplayName("Обработка платежа с недостаточной суммой")
    void testProcessWithInsufficientAmount() {
        payment.setAmountReceived(500.0);
        assertThrows(InvalidPaymentException.class, () -> payment.process());
    }

    @Test
    @DisplayName("Расчет сдачи")
    void testCalculateChange() {
        assertEquals(500.0, payment.calculateChange(1500.0), 0.01);
        assertEquals(0.0, payment.calculateChange(1000.0), 0.01);
        assertEquals(100.0, payment.calculateChange(1100.0), 0.01);
    }

    @Test
    @DisplayName("Возврат успешного платежа")
    void testRefundSuccessfulPayment() throws InvalidPaymentException {
        payment.setAmountReceived(1000.0);
        payment.process();
        assertTrue(payment.isSuccessful());

        payment.refund();
        assertFalse(payment.isSuccessful());
    }

    @Test
    @DisplayName("Попытка возврата неуспешного платежа")
    void testRefundUnsuccessfulPayment() {
        assertThrows(InvalidPaymentException.class, () -> payment.refund());
    }

    @Test
    @DisplayName("Получение суммы платежа")
    void testGetAmount() {
        assertEquals(1000.0, payment.getAmount(), 0.01);
    }

    @Test
    @DisplayName("Получение полученной суммы")
    void testGetAmountReceived() {
        payment.setAmountReceived(1200.0);
        assertEquals(1200.0, payment.getAmountReceived(), 0.01);
    }

    @Test
    @DisplayName("Создание платежа с отрицательной суммой")
    void testCreatePaymentWithNegativeAmount() {
        assertThrows(InvalidPaymentException.class, () -> {
            new CashPayment("CASH002", -100.0);
        });
    }
}
