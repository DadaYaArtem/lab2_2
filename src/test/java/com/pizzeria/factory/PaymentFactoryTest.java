package com.pizzeria.factory;

import com.pizzeria.enums.PaymentMethod;
import com.pizzeria.exceptions.InvalidPaymentException;
import com.pizzeria.model.payment.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты для PaymentFactory")
class PaymentFactoryTest {

    private PaymentFactory factory;

    @BeforeEach
    void setUp() {
        factory = new PaymentFactory();
    }

    @Test
    @DisplayName("Создание наличного платежа через метод CASH")
    void testCreatePayment_Cash_Success() throws InvalidPaymentException {
        // Act
        Payment payment = factory.createPayment(PaymentMethod.CASH, "TRANS-123", 500.0);

        // Assert
        assertNotNull(payment);
        assertInstanceOf(CashPayment.class, payment);
        assertEquals("TRANS-123", payment.getTransactionId());
        assertEquals(500.0, payment.getAmount());
    }

    @Test
    @DisplayName("Создание платежа картой через метод CARD")
    void testCreatePayment_Card_Success() throws InvalidPaymentException {
        // Act
        Payment payment = factory.createPayment(PaymentMethod.CARD, "TRANS-456", 1000.0);

        // Assert
        assertNotNull(payment);
        assertInstanceOf(CardPayment.class, payment);
        assertEquals("TRANS-456", payment.getTransactionId());
        assertEquals(1000.0, payment.getAmount());
    }

    @Test
    @DisplayName("Создание онлайн платежа через метод ONLINE")
    void testCreatePayment_Online_Success() throws InvalidPaymentException {
        // Act
        Payment payment = factory.createPayment(PaymentMethod.ONLINE, "TRANS-789", 1500.0);

        // Assert
        assertNotNull(payment);
        assertInstanceOf(OnlinePayment.class, payment);
        assertEquals("TRANS-789", payment.getTransactionId());
        assertEquals(1500.0, payment.getAmount());
    }

    @Test
    @DisplayName("Создание наличного платежа напрямую")
    void testCreateCashPayment_Success() throws InvalidPaymentException {
        // Act
        CashPayment payment = factory.createCashPayment(300.0);

        // Assert
        assertNotNull(payment);
        assertEquals(300.0, payment.getAmount());
        assertTrue(payment.getTransactionId().startsWith("CASH-"));
    }

    @Test
    @DisplayName("Создание нескольких наличных платежей с уникальными ID")
    void testCreateCashPayment_MultiplePayments_UniqueIds() throws InvalidPaymentException {
        // Act
        CashPayment payment1 = factory.createCashPayment(100.0);
        CashPayment payment2 = factory.createCashPayment(200.0);

        // Assert
        assertNotNull(payment1);
        assertNotNull(payment2);
        assertNotEquals(payment1.getTransactionId(), payment2.getTransactionId());
    }

    @Test
    @DisplayName("Создание платежа картой с номером карты")
    void testCreateCardPayment_WithCardNumber_Success() throws InvalidPaymentException {
        // Act
        CardPayment payment = factory.createCardPayment(500.0, "4532123456789012");

        // Assert
        assertNotNull(payment);
        assertEquals(500.0, payment.getAmount());
        assertTrue(payment.getTransactionId().startsWith("CARD-"));
    }

    @Test
    @DisplayName("Создание нескольких платежей картой с уникальными ID")
    void testCreateCardPayment_MultiplePayments_UniqueIds() throws InvalidPaymentException {
        // Act
        CardPayment payment1 = factory.createCardPayment(100.0, "4532111111111111");
        CardPayment payment2 = factory.createCardPayment(200.0, "5555555555554444");

        // Assert
        assertNotNull(payment1);
        assertNotNull(payment2);
        assertNotEquals(payment1.getTransactionId(), payment2.getTransactionId());
    }

    @Test
    @DisplayName("Создание онлайн платежа с email")
    void testCreateOnlinePayment_WithEmail_Success() throws InvalidPaymentException {
        // Act
        OnlinePayment payment = factory.createOnlinePayment(750.0, "test@example.com");

        // Assert
        assertNotNull(payment);
        assertEquals(750.0, payment.getAmount());
        assertTrue(payment.getTransactionId().startsWith("ONLINE-"));
    }

    @Test
    @DisplayName("Создание нескольких онлайн платежей с уникальными ID")
    void testCreateOnlinePayment_MultiplePayments_UniqueIds() throws InvalidPaymentException {
        // Act
        OnlinePayment payment1 = factory.createOnlinePayment(100.0, "user1@example.com");
        OnlinePayment payment2 = factory.createOnlinePayment(200.0, "user2@example.com");

        // Assert
        assertNotNull(payment1);
        assertNotNull(payment2);
        assertNotEquals(payment1.getTransactionId(), payment2.getTransactionId());
    }

    @Test
    @DisplayName("Создание платежа с нулевой суммой выбрасывает исключение")
    void testCreateCashPayment_ZeroAmount_ThrowsException() {
        // Act & Assert
        assertThrows(InvalidPaymentException.class, () -> {
            factory.createCashPayment(0.0);
        });
    }

    @Test
    @DisplayName("Создание платежа с отрицательной суммой выбрасывает исключение")
    void testCreateCashPayment_NegativeAmount_ThrowsException() {
        // Act & Assert
        assertThrows(InvalidPaymentException.class, () -> {
            factory.createCashPayment(-100.0);
        });
    }

    @Test
    @DisplayName("Создание платежа картой с нулевой суммой выбрасывает исключение")
    void testCreateCardPayment_ZeroAmount_ThrowsException() {
        // Act & Assert
        assertThrows(InvalidPaymentException.class, () -> {
            factory.createCardPayment(0.0, "4532123456789012");
        });
    }

    @Test
    @DisplayName("Создание онлайн платежа с нулевой суммой выбрасывает исключение")
    void testCreateOnlinePayment_ZeroAmount_ThrowsException() {
        // Act & Assert
        assertThrows(InvalidPaymentException.class, () -> {
            factory.createOnlinePayment(0.0, "test@example.com");
        });
    }

    @Test
    @DisplayName("Все методы оплаты поддерживаются фабрикой")
    void testCreatePayment_AllMethods_Success() throws InvalidPaymentException {
        // Act
        Payment cash = factory.createPayment(PaymentMethod.CASH, "T1", 100.0);
        Payment card = factory.createPayment(PaymentMethod.CARD, "T2", 200.0);
        Payment online = factory.createPayment(PaymentMethod.ONLINE, "T3", 300.0);

        // Assert
        assertInstanceOf(CashPayment.class, cash);
        assertInstanceOf(CardPayment.class, card);
        assertInstanceOf(OnlinePayment.class, online);
    }

    @Test
    @DisplayName("Создание платежа с большой суммой")
    void testCreatePayment_LargeAmount_Success() throws InvalidPaymentException {
        // Act
        Payment payment = factory.createCashPayment(999999.99);

        // Assert
        assertNotNull(payment);
        assertEquals(999999.99, payment.getAmount());
    }

    @Test
    @DisplayName("Создание платежа с малой суммой")
    void testCreatePayment_SmallAmount_Success() throws InvalidPaymentException {
        // Act
        Payment payment = factory.createCashPayment(0.01);

        // Assert
        assertNotNull(payment);
        assertEquals(0.01, payment.getAmount());
    }

    @Test
    @DisplayName("ID транзакции наличного платежа содержит префикс CASH")
    void testCreateCashPayment_TransactionIdFormat_HasCashPrefix() throws InvalidPaymentException {
        // Act
        CashPayment payment = factory.createCashPayment(100.0);

        // Assert
        assertTrue(payment.getTransactionId().startsWith("CASH-"));
        assertTrue(payment.getTransactionId().length() > 5);
    }

    @Test
    @DisplayName("ID транзакции платежа картой содержит префикс CARD")
    void testCreateCardPayment_TransactionIdFormat_HasCardPrefix() throws InvalidPaymentException {
        // Act
        CardPayment payment = factory.createCardPayment(100.0, "1234567890123456");

        // Assert
        assertTrue(payment.getTransactionId().startsWith("CARD-"));
        assertTrue(payment.getTransactionId().length() > 5);
    }

    @Test
    @DisplayName("ID транзакции онлайн платежа содержит префикс ONLINE")
    void testCreateOnlinePayment_TransactionIdFormat_HasOnlinePrefix() throws InvalidPaymentException {
        // Act
        OnlinePayment payment = factory.createOnlinePayment(100.0, "test@example.com");

        // Assert
        assertTrue(payment.getTransactionId().startsWith("ONLINE-"));
        assertTrue(payment.getTransactionId().length() > 7);
    }

    @Test
    @DisplayName("Созданные платежи имеют корректные типы")
    void testCreatePayment_CorrectTypes() throws InvalidPaymentException {
        // Act
        CashPayment cash = factory.createCashPayment(100.0);
        CardPayment card = factory.createCardPayment(200.0, "1234567890123456");
        OnlinePayment online = factory.createOnlinePayment(300.0, "test@example.com");

        // Assert
        assertTrue(cash instanceof Payment);
        assertTrue(card instanceof Payment);
        assertTrue(online instanceof Payment);
    }
}
