package com.pizzeria.model.payment;

import com.pizzeria.enums.PaymentMethod;
import com.pizzeria.exceptions.InvalidPaymentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты для CardPayment")
class CardPaymentTest {

    private CardPayment cardPayment;
    private static final String TRANSACTION_ID = "CARD-001";
    private static final double AMOUNT = 1000.0;
    private static final String VALID_CARD_NUMBER = "1234567890123456";

    @BeforeEach
    void setUp() throws InvalidPaymentException {
        cardPayment = new CardPayment(TRANSACTION_ID, AMOUNT, VALID_CARD_NUMBER);
    }

    @Test
    @DisplayName("Успешное создание платежа картой")
    void testConstructorSuccess() {
        assertNotNull(cardPayment);
        assertEquals(TRANSACTION_ID, cardPayment.getTransactionId());
        assertEquals(AMOUNT, cardPayment.getAmount());
        assertEquals(PaymentMethod.CARD, cardPayment.getMethod());
        assertNotNull(cardPayment.getCardNumber());
        assertTrue(cardPayment.getCardNumber().contains("****"));
    }

    @Test
    @DisplayName("Маскирование номера карты")
    void testCardNumberMasking() {
        String maskedNumber = cardPayment.getCardNumber();
        assertTrue(maskedNumber.startsWith("****"));
        assertTrue(maskedNumber.endsWith("3456"));
        assertEquals("**** **** **** 3456", maskedNumber);
    }

    @Test
    @DisplayName("Маскирование короткого номера карты")
    void testCardNumberMaskingWithShortNumber() throws InvalidPaymentException {
        CardPayment shortCardPayment = new CardPayment("CARD-002", 500.0, "123");
        assertEquals("****", shortCardPayment.getCardNumber());
    }

    @Test
    @DisplayName("Маскирование null номера карты")
    void testCardNumberMaskingWithNull() throws InvalidPaymentException {
        CardPayment nullCardPayment = new CardPayment("CARD-003", 500.0, null);
        assertEquals("****", nullCardPayment.getCardNumber());
    }

    @Test
    @DisplayName("Исключение при создании платежа с отрицательной суммой")
    void testConstructorWithNegativeAmount() {
        assertThrows(InvalidPaymentException.class, () -> {
            new CardPayment(TRANSACTION_ID, -100.0, VALID_CARD_NUMBER);
        });
    }

    @Test
    @DisplayName("Исключение при создании платежа с нулевой суммой")
    void testConstructorWithZeroAmount() {
        assertThrows(InvalidPaymentException.class, () -> {
            new CardPayment(TRANSACTION_ID, 0.0, VALID_CARD_NUMBER);
        });
    }

    @Test
    @DisplayName("Успешная обработка платежа с валидной картой")
    void testProcessSuccess() throws InvalidPaymentException {
        boolean result = cardPayment.process();

        assertTrue(result);
        assertTrue(cardPayment.isSuccessful());
    }

    @Test
    @DisplayName("Исключение при обработке платежа с невалидной картой")
    void testProcessWithInvalidCard() throws InvalidPaymentException {
        CardPayment invalidCardPayment = new CardPayment("CARD-004", 500.0, "12345");

        InvalidPaymentException exception = assertThrows(InvalidPaymentException.class, () -> {
            invalidCardPayment.process();
        });

        assertTrue(exception.getMessage().contains("Некорректные данные карты"));
        assertFalse(invalidCardPayment.isSuccessful());
    }

    @Test
    @DisplayName("Исключение при обработке платежа с null картой")
    void testProcessWithNullCard() throws InvalidPaymentException {
        CardPayment nullCardPayment = new CardPayment("CARD-005", 500.0, null);

        assertThrows(InvalidPaymentException.class, () -> {
            nullCardPayment.process();
        });
    }

    @Test
    @DisplayName("Успешный возврат после успешного платежа")
    void testRefundSuccess() throws InvalidPaymentException {
        cardPayment.process();

        assertDoesNotThrow(() -> cardPayment.refund());
        assertFalse(cardPayment.isSuccessful());
    }

    @Test
    @DisplayName("Исключение при возврате неуспешного платежа")
    void testRefundWithoutSuccessfulPayment() {
        InvalidPaymentException exception = assertThrows(InvalidPaymentException.class, () -> {
            cardPayment.refund();
        });

        assertTrue(exception.getMessage().contains("Возврат невозможен"));
    }

    @Test
    @DisplayName("Проверка валидного PIN кода")
    void testVerifyPinWithValidPin() {
        assertTrue(cardPayment.verifyPin("1234"));
        assertTrue(cardPayment.verifyPin("0000"));
        assertTrue(cardPayment.verifyPin("9999"));
    }

    @Test
    @DisplayName("Проверка невалидного PIN кода")
    void testVerifyPinWithInvalidPin() {
        assertFalse(cardPayment.verifyPin("123"));
        assertFalse(cardPayment.verifyPin("12345"));
        assertFalse(cardPayment.verifyPin(null));
        assertFalse(cardPayment.verifyPin(""));
    }

    @Test
    @DisplayName("Установка и получение имени держателя карты")
    void testCardHolderNameGetterSetter() {
        String holderName = "Ivan Petrov";
        cardPayment.setCardHolderName(holderName);
        assertEquals(holderName, cardPayment.getCardHolderName());
    }

    @Test
    @DisplayName("Установка и получение даты истечения")
    void testExpiryDateGetterSetter() {
        String expiryDate = "12/25";
        cardPayment.setExpiryDate(expiryDate);
        assertEquals(expiryDate, cardPayment.getExpiryDate());
    }

    @Test
    @DisplayName("Повторная установка номера карты с маскированием")
    void testSetCardNumberWithMasking() {
        cardPayment.setCardNumber("9876543210987654");
        assertTrue(cardPayment.getCardNumber().endsWith("7654"));
        assertTrue(cardPayment.getCardNumber().startsWith("****"));
    }

    @Test
    @DisplayName("Получение чека платежа")
    void testGetReceipt() {
        String receipt = cardPayment.getReceipt();
        assertNotNull(receipt);
        assertTrue(receipt.contains(TRANSACTION_ID));
        assertTrue(receipt.contains(String.valueOf(AMOUNT)));
    }

    @Test
    @DisplayName("Обработка платежа с минимально валидной длиной карты")
    void testProcessWithMinimalValidCardLength() throws InvalidPaymentException {
        CardPayment minCardPayment = new CardPayment("CARD-006", 500.0, "1234567890123456");
        assertTrue(minCardPayment.process());
    }

    @Test
    @DisplayName("Обработка платежа с картой длиннее минимальной")
    void testProcessWithLongerCard() throws InvalidPaymentException {
        CardPayment longCardPayment = new CardPayment("CARD-007", 500.0, "12345678901234567890");
        assertTrue(longCardPayment.process());
        assertTrue(longCardPayment.getCardNumber().endsWith("7890"));
    }

    @Test
    @DisplayName("Множественные возвраты платежа невозможны")
    void testMultipleRefunds() throws InvalidPaymentException {
        cardPayment.process();
        cardPayment.refund();

        assertThrows(InvalidPaymentException.class, () -> {
            cardPayment.refund();
        });
    }
}
