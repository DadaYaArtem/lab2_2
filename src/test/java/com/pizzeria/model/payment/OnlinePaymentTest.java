package com.pizzeria.model.payment;

import com.pizzeria.enums.PaymentMethod;
import com.pizzeria.exceptions.InvalidPaymentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты для OnlinePayment")
class OnlinePaymentTest {

    private OnlinePayment onlinePayment;
    private static final String TRANSACTION_ID = "ONLINE-001";
    private static final double AMOUNT = 750.0;
    private static final String VALID_EMAIL = "user@example.com";

    @BeforeEach
    void setUp() throws InvalidPaymentException {
        onlinePayment = new OnlinePayment(TRANSACTION_ID, AMOUNT, VALID_EMAIL);
    }

    @Test
    @DisplayName("Успешное создание онлайн платежа")
    void testConstructorSuccess() {
        assertNotNull(onlinePayment);
        assertEquals(TRANSACTION_ID, onlinePayment.getTransactionId());
        assertEquals(AMOUNT, onlinePayment.getAmount());
        assertEquals(PaymentMethod.ONLINE, onlinePayment.getMethod());
        assertEquals(VALID_EMAIL, onlinePayment.getEmail());
        assertEquals("PayPal", onlinePayment.getPaymentGateway());
    }

    @Test
    @DisplayName("Исключение при создании платежа с отрицательной суммой")
    void testConstructorWithNegativeAmount() {
        assertThrows(InvalidPaymentException.class, () -> {
            new OnlinePayment(TRANSACTION_ID, -100.0, VALID_EMAIL);
        });
    }

    @Test
    @DisplayName("Исключение при создании платежа с нулевой суммой")
    void testConstructorWithZeroAmount() {
        assertThrows(InvalidPaymentException.class, () -> {
            new OnlinePayment(TRANSACTION_ID, 0.0, VALID_EMAIL);
        });
    }

    @Test
    @DisplayName("Успешная обработка платежа с валидным email")
    void testProcessSuccess() throws InvalidPaymentException {
        boolean result = onlinePayment.process();

        assertTrue(result);
        assertTrue(onlinePayment.isSuccessful());
        assertNotNull(onlinePayment.getConfirmationCode());
        assertTrue(onlinePayment.getConfirmationCode().startsWith("CONF-"));
    }

    @Test
    @DisplayName("Исключение при обработке платежа с null email")
    void testProcessWithNullEmail() throws InvalidPaymentException {
        OnlinePayment nullEmailPayment = new OnlinePayment("ONLINE-002", 500.0, null);

        InvalidPaymentException exception = assertThrows(InvalidPaymentException.class, () -> {
            nullEmailPayment.process();
        });

        assertTrue(exception.getMessage().contains("Некорректный email"));
        assertFalse(nullEmailPayment.isSuccessful());
    }

    @Test
    @DisplayName("Исключение при обработке платежа с невалидным email без @")
    void testProcessWithInvalidEmail() throws InvalidPaymentException {
        OnlinePayment invalidEmailPayment = new OnlinePayment("ONLINE-003", 500.0, "invalidemail.com");

        InvalidPaymentException exception = assertThrows(InvalidPaymentException.class, () -> {
            invalidEmailPayment.process();
        });

        assertTrue(exception.getMessage().contains("Некорректный email"));
    }

    @Test
    @DisplayName("Исключение при обработке платежа с пустым email")
    void testProcessWithEmptyEmail() throws InvalidPaymentException {
        OnlinePayment emptyEmailPayment = new OnlinePayment("ONLINE-004", 500.0, "");

        assertThrows(InvalidPaymentException.class, () -> {
            emptyEmailPayment.process();
        });
    }

    @Test
    @DisplayName("Успешный возврат после успешного платежа")
    void testRefundSuccess() throws InvalidPaymentException {
        onlinePayment.process();

        assertDoesNotThrow(() -> onlinePayment.refund());
        assertFalse(onlinePayment.isSuccessful());
    }

    @Test
    @DisplayName("Исключение при возврате неуспешного платежа")
    void testRefundWithoutSuccessfulPayment() {
        InvalidPaymentException exception = assertThrows(InvalidPaymentException.class, () -> {
            onlinePayment.refund();
        });

        assertTrue(exception.getMessage().contains("Возврат невозможен"));
    }

    @Test
    @DisplayName("Проверка валидного кода подтверждения")
    void testVerifyConfirmationCodeSuccess() throws InvalidPaymentException {
        onlinePayment.process();
        String confirmationCode = onlinePayment.getConfirmationCode();

        assertTrue(onlinePayment.verifyConfirmationCode(confirmationCode));
    }

    @Test
    @DisplayName("Проверка невалидного кода подтверждения")
    void testVerifyConfirmationCodeFailure() throws InvalidPaymentException {
        onlinePayment.process();

        assertFalse(onlinePayment.verifyConfirmationCode("INVALID-CODE"));
        assertFalse(onlinePayment.verifyConfirmationCode(null));
        assertFalse(onlinePayment.verifyConfirmationCode(""));
    }

    @Test
    @DisplayName("Проверка кода подтверждения до обработки платежа")
    void testVerifyConfirmationCodeBeforeProcess() {
        assertFalse(onlinePayment.verifyConfirmationCode("CONF-123456"));
    }

    @Test
    @DisplayName("Установка и получение email")
    void testEmailGetterSetter() {
        String newEmail = "newemail@example.com";
        onlinePayment.setEmail(newEmail);
        assertEquals(newEmail, onlinePayment.getEmail());
    }

    @Test
    @DisplayName("Установка и получение платежного шлюза")
    void testPaymentGatewayGetterSetter() {
        String newGateway = "Stripe";
        onlinePayment.setPaymentGateway(newGateway);
        assertEquals(newGateway, onlinePayment.getPaymentGateway());
    }

    @Test
    @DisplayName("Значение платежного шлюза по умолчанию")
    void testDefaultPaymentGateway() {
        assertEquals("PayPal", onlinePayment.getPaymentGateway());
    }

    @Test
    @DisplayName("Получение чека платежа")
    void testGetReceipt() {
        String receipt = onlinePayment.getReceipt();
        assertNotNull(receipt);
        assertTrue(receipt.contains(TRANSACTION_ID));
        assertTrue(receipt.contains(String.valueOf(AMOUNT)));
    }

    @Test
    @DisplayName("Обработка платежа с различными email форматами")
    void testProcessWithVariousEmailFormats() throws InvalidPaymentException {
        String[] validEmails = {
            "test@test.com",
            "user.name@example.com",
            "user+tag@example.co.uk",
            "123@test.org"
        };

        for (String email : validEmails) {
            OnlinePayment payment = new OnlinePayment("ONLINE-" + email, 100.0, email);
            assertTrue(payment.process(), "Failed for email: " + email);
        }
    }

    @Test
    @DisplayName("Уникальность кодов подтверждения")
    void testConfirmationCodeUniqueness() throws InvalidPaymentException {
        OnlinePayment payment1 = new OnlinePayment("ONLINE-100", 100.0, "user1@test.com");
        OnlinePayment payment2 = new OnlinePayment("ONLINE-101", 100.0, "user2@test.com");

        payment1.process();
        payment2.process();

        assertNotEquals(payment1.getConfirmationCode(), payment2.getConfirmationCode());
    }

    @Test
    @DisplayName("Формат кода подтверждения")
    void testConfirmationCodeFormat() throws InvalidPaymentException {
        onlinePayment.process();
        String code = onlinePayment.getConfirmationCode();

        assertNotNull(code);
        assertTrue(code.startsWith("CONF-"));
        assertTrue(code.length() > 5);
    }

    @Test
    @DisplayName("Множественные возвраты платежа невозможны")
    void testMultipleRefunds() throws InvalidPaymentException {
        onlinePayment.process();
        onlinePayment.refund();

        assertThrows(InvalidPaymentException.class, () -> {
            onlinePayment.refund();
        });
    }

    @Test
    @DisplayName("Изменение email после создания платежа")
    void testChangeEmailAfterCreation() throws InvalidPaymentException {
        onlinePayment.setEmail("newemail@test.com");
        assertTrue(onlinePayment.process());
        assertEquals("newemail@test.com", onlinePayment.getEmail());
    }
}
