import com.pizzeria.enums.PaymentMethod;
import com.pizzeria.exceptions.InvalidPaymentException;
import com.pizzeria.model.payment.CardPayment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CardPayment Tests")
class CardPaymentTest {

    private CardPayment payment;

    @BeforeEach
    void setUp() throws InvalidPaymentException {
        payment = new CardPayment("CARD001", 2000.0, "1234567890123456");
    }

    @Test
    @DisplayName("Создание платежа картой")
    void testPaymentCreation() {
        assertNotNull(payment);
        assertEquals("CARD001", payment.getTransactionId());
        assertEquals(2000.0, payment.getAmount(), 0.01);
        assertEquals(PaymentMethod.CARD, payment.getMethod());
    }

    @Test
    @DisplayName("Маскирование номера карты")
    void testCardNumberMasking() {
        assertTrue(payment.getCardNumber().contains("****"));
        assertTrue(payment.getCardNumber().contains("3456"));
    }

    @Test
    @DisplayName("Обработка платежа с валидной картой")
    void testProcessWithValidCard() throws InvalidPaymentException {
        assertTrue(payment.process());
        assertTrue(payment.isSuccessful());
    }

    @Test
    @DisplayName("Проверка PIN-кода")
    void testVerifyPin() {
        assertTrue(payment.verifyPin("1234"));
        assertTrue(payment.verifyPin("0000"));
        assertFalse(payment.verifyPin("123"));
        assertFalse(payment.verifyPin("12345"));
        assertFalse(payment.verifyPin(null));
    }

    @Test
    @DisplayName("Возврат успешного платежа")
    void testRefundSuccessfulPayment() throws InvalidPaymentException {
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
    @DisplayName("Установка имени держателя карты")
    void testSetCardHolderName() {
        payment.setCardHolderName("Ivan Ivanov");
        assertEquals("Ivan Ivanov", payment.getCardHolderName());
    }

    @Test
    @DisplayName("Установка срока действия карты")
    void testSetExpiryDate() {
        payment.setExpiryDate("12/25");
        assertEquals("12/25", payment.getExpiryDate());
    }

    @Test
    @DisplayName("Изменение номера карты с маскированием")
    void testSetCardNumber() {
        payment.setCardNumber("9876543210987654");
        assertTrue(payment.getCardNumber().contains("****"));
        assertTrue(payment.getCardNumber().contains("7654"));
    }

    @Test
    @DisplayName("Создание платежа с отрицательной суммой")
    void testCreatePaymentWithNegativeAmount() {
        assertThrows(InvalidPaymentException.class, () -> {
            new CardPayment("CARD002", -500.0, "1234567890123456");
        });
    }

    @Test
    @DisplayName("Создание платежа с коротким номером карты")
    void testCreatePaymentWithShortCardNumber() throws InvalidPaymentException {
        CardPayment shortCard = new CardPayment("CARD003", 100.0, "123");
        assertEquals("****", shortCard.getCardNumber());
    }

    @Test
    @DisplayName("Повторная обработка платежа")
    void testReprocessPayment() throws InvalidPaymentException {
        payment.process();
        assertTrue(payment.isSuccessful());

        // Можно обработать еще раз
        payment.process();
        assertTrue(payment.isSuccessful());
    }
}
