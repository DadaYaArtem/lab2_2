import com.pizzeria.enums.PaymentMethod;
import com.pizzeria.exceptions.InvalidPaymentException;
import com.pizzeria.factory.PaymentFactory;
import com.pizzeria.model.payment.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PaymentFactory Tests")
class PaymentFactoryTest {

    private PaymentFactory factory;

    @BeforeEach
    void setUp() {
        factory = new PaymentFactory();
    }

    @Test
    @DisplayName("Создание фабрики")
    void testFactoryCreation() {
        assertNotNull(factory);
    }

    // ============= Тесты для createPayment (универсальный метод) =============

    @Test
    @DisplayName("Создание платежа наличными через универсальный метод")
    void testCreatePaymentCash() throws Exception {
        Payment payment = factory.createPayment(PaymentMethod.CASH, "TXN001", 1000.0);

        assertNotNull(payment);
        assertTrue(payment instanceof CashPayment);
        assertEquals("TXN001", payment.getTransactionId());
        assertEquals(1000.0, payment.getAmount(), 0.01);
        assertEquals(PaymentMethod.CASH, payment.getMethod());
    }

    @Test
    @DisplayName("Создание платежа картой через универсальный метод")
    void testCreatePaymentCard() throws Exception {
        Payment payment = factory.createPayment(PaymentMethod.CARD, "TXN002", 2000.0);

        assertNotNull(payment);
        assertTrue(payment instanceof CardPayment);
        assertEquals("TXN002", payment.getTransactionId());
        assertEquals(2000.0, payment.getAmount(), 0.01);
        assertEquals(PaymentMethod.CARD, payment.getMethod());
    }

    @Test
    @DisplayName("Создание онлайн платежа через универсальный метод")
    void testCreatePaymentOnline() throws Exception {
        Payment payment = factory.createPayment(PaymentMethod.ONLINE, "TXN003", 3000.0);

        assertNotNull(payment);
        assertTrue(payment instanceof OnlinePayment);
        assertEquals("TXN003", payment.getTransactionId());
        assertEquals(3000.0, payment.getAmount(), 0.01);
        assertEquals(PaymentMethod.ONLINE, payment.getMethod());
    }

    @Test
    @DisplayName("Платеж картой содержит маскированный номер")
    void testCreatePaymentCardHasMaskedNumber() throws Exception {
        Payment payment = factory.createPayment(PaymentMethod.CARD, "TXN004", 500.0);

        assertTrue(payment instanceof CardPayment);
        CardPayment cardPayment = (CardPayment) payment;
        assertTrue(cardPayment.getCardNumber().contains("****"));
        assertTrue(cardPayment.getCardNumber().contains("3456")); // Последние 4 цифры
    }

    @Test
    @DisplayName("Онлайн платеж содержит email")
    void testCreatePaymentOnlineHasEmail() throws Exception {
        Payment payment = factory.createPayment(PaymentMethod.ONLINE, "TXN005", 1500.0);

        assertTrue(payment instanceof OnlinePayment);
        OnlinePayment onlinePayment = (OnlinePayment) payment;
        assertEquals("customer@example.com", onlinePayment.getEmail());
    }

    // ============= Тесты для createCashPayment =============

    @Test
    @DisplayName("Создание платежа наличными")
    void testCreateCashPayment() throws Exception {
        CashPayment payment = factory.createCashPayment(1000.0);

        assertNotNull(payment);
        assertEquals(1000.0, payment.getAmount(), 0.01);
        assertEquals(PaymentMethod.CASH, payment.getMethod());
        assertNotNull(payment.getTransactionId());
        assertTrue(payment.getTransactionId().startsWith("CASH-"));
    }

    @Test
    @DisplayName("Создание нескольких платежей наличными с уникальными ID")
    void testCreateMultipleCashPaymentsWithUniqueIds() throws Exception {
        CashPayment payment1 = factory.createCashPayment(100.0);
        Thread.sleep(2); // Небольшая задержка для разных timestamp
        CashPayment payment2 = factory.createCashPayment(200.0);
        Thread.sleep(2);
        CashPayment payment3 = factory.createCashPayment(300.0);

        assertNotEquals(payment1.getTransactionId(), payment2.getTransactionId());
        assertNotEquals(payment2.getTransactionId(), payment3.getTransactionId());
        assertNotEquals(payment1.getTransactionId(), payment3.getTransactionId());
    }

    @Test
    @DisplayName("Платеж наличными с нулевой суммой выбрасывает исключение")
    void testCreateCashPaymentWithZeroAmount() {
        assertThrows(InvalidPaymentException.class, () -> {
            factory.createCashPayment(0.0);
        });
    }

    @Test
    @DisplayName("Платеж наличными с отрицательной суммой выбрасывает исключение")
    void testCreateCashPaymentWithNegativeAmount() {
        assertThrows(InvalidPaymentException.class, () -> {
            factory.createCashPayment(-100.0);
        });
    }

    @Test
    @DisplayName("Платеж наличными с большой суммой")
    void testCreateCashPaymentWithLargeAmount() throws Exception {
        CashPayment payment = factory.createCashPayment(1000000.0);

        assertNotNull(payment);
        assertEquals(1000000.0, payment.getAmount(), 0.01);
    }

    // ============= Тесты для createCardPayment =============

    @Test
    @DisplayName("Создание платежа картой")
    void testCreateCardPayment() throws Exception {
        CardPayment payment = factory.createCardPayment(2000.0, "1234567890123456");

        assertNotNull(payment);
        assertEquals(2000.0, payment.getAmount(), 0.01);
        assertEquals(PaymentMethod.CARD, payment.getMethod());
        assertNotNull(payment.getTransactionId());
        assertTrue(payment.getTransactionId().startsWith("CARD-"));
    }

    @Test
    @DisplayName("Платеж картой маскирует номер карты")
    void testCreateCardPaymentMasksCardNumber() throws Exception {
        CardPayment payment = factory.createCardPayment(500.0, "9876543210987654");

        assertNotNull(payment.getCardNumber());
        assertTrue(payment.getCardNumber().contains("****"));
        assertTrue(payment.getCardNumber().contains("7654")); // Последние 4 цифры
        assertFalse(payment.getCardNumber().contains("9876")); // Первые цифры скрыты
    }

    @Test
    @DisplayName("Создание нескольких платежей картой с уникальными ID")
    void testCreateMultipleCardPaymentsWithUniqueIds() throws Exception {
        CardPayment payment1 = factory.createCardPayment(100.0, "1111222233334444");
        Thread.sleep(2);
        CardPayment payment2 = factory.createCardPayment(200.0, "5555666677778888");
        Thread.sleep(2);
        CardPayment payment3 = factory.createCardPayment(300.0, "9999000011112222");

        assertNotEquals(payment1.getTransactionId(), payment2.getTransactionId());
        assertNotEquals(payment2.getTransactionId(), payment3.getTransactionId());
    }

    @Test
    @DisplayName("Платеж картой с нулевой суммой выбрасывает исключение")
    void testCreateCardPaymentWithZeroAmount() {
        assertThrows(InvalidPaymentException.class, () -> {
            factory.createCardPayment(0.0, "1234567890123456");
        });
    }

    @Test
    @DisplayName("Платеж картой с отрицательной суммой выбрасывает исключение")
    void testCreateCardPaymentWithNegativeAmount() {
        assertThrows(InvalidPaymentException.class, () -> {
            factory.createCardPayment(-500.0, "1234567890123456");
        });
    }

    @Test
    @DisplayName("Платеж картой с коротким номером карты")
    void testCreateCardPaymentWithShortCardNumber() throws Exception {
        CardPayment payment = factory.createCardPayment(100.0, "123");

        assertNotNull(payment);
        assertEquals("****", payment.getCardNumber()); // Маскируется в ****
    }

    // ============= Тесты для createOnlinePayment =============

    @Test
    @DisplayName("Создание онлайн платежа")
    void testCreateOnlinePayment() throws Exception {
        OnlinePayment payment = factory.createOnlinePayment(3000.0, "user@example.com");

        assertNotNull(payment);
        assertEquals(3000.0, payment.getAmount(), 0.01);
        assertEquals(PaymentMethod.ONLINE, payment.getMethod());
        assertEquals("user@example.com", payment.getEmail());
        assertNotNull(payment.getTransactionId());
        assertTrue(payment.getTransactionId().startsWith("ONLINE-"));
    }

    @Test
    @DisplayName("Создание нескольких онлайн платежей с уникальными ID")
    void testCreateMultipleOnlinePaymentsWithUniqueIds() throws Exception {
        OnlinePayment payment1 = factory.createOnlinePayment(100.0, "user1@test.com");
        Thread.sleep(2);
        OnlinePayment payment2 = factory.createOnlinePayment(200.0, "user2@test.com");
        Thread.sleep(2);
        OnlinePayment payment3 = factory.createOnlinePayment(300.0, "user3@test.com");

        assertNotEquals(payment1.getTransactionId(), payment2.getTransactionId());
        assertNotEquals(payment2.getTransactionId(), payment3.getTransactionId());
    }

    @Test
    @DisplayName("Онлайн платеж с нулевой суммой выбрасывает исключение")
    void testCreateOnlinePaymentWithZeroAmount() {
        assertThrows(InvalidPaymentException.class, () -> {
            factory.createOnlinePayment(0.0, "user@test.com");
        });
    }

    @Test
    @DisplayName("Онлайн платеж с отрицательной суммой выбрасывает исключение")
    void testCreateOnlinePaymentWithNegativeAmount() {
        assertThrows(InvalidPaymentException.class, () -> {
            factory.createOnlinePayment(-1000.0, "user@test.com");
        });
    }

    @Test
    @DisplayName("Онлайн платеж с разными email адресами")
    void testCreateOnlinePaymentWithDifferentEmails() throws Exception {
        OnlinePayment payment1 = factory.createOnlinePayment(100.0, "user@gmail.com");
        OnlinePayment payment2 = factory.createOnlinePayment(200.0, "admin@company.com");
        OnlinePayment payment3 = factory.createOnlinePayment(300.0, "test@test.ru");

        assertEquals("user@gmail.com", payment1.getEmail());
        assertEquals("admin@company.com", payment2.getEmail());
        assertEquals("test@test.ru", payment3.getEmail());
    }

    // ============= Комплексные тесты =============

    @Test
    @DisplayName("Создание всех типов платежей через универсальный метод")
    void testCreateAllPaymentTypesThroughUniversalMethod() throws Exception {
        Payment cash = factory.createPayment(PaymentMethod.CASH, "T1", 100.0);
        Payment card = factory.createPayment(PaymentMethod.CARD, "T2", 200.0);
        Payment online = factory.createPayment(PaymentMethod.ONLINE, "T3", 300.0);

        assertTrue(cash instanceof CashPayment);
        assertTrue(card instanceof CardPayment);
        assertTrue(online instanceof OnlinePayment);
    }

    @Test
    @DisplayName("Создание всех типов платежей через специфичные методы")
    void testCreateAllPaymentTypesThroughSpecificMethods() throws Exception {
        CashPayment cash = factory.createCashPayment(100.0);
        CardPayment card = factory.createCardPayment(200.0, "1234567890123456");
        OnlinePayment online = factory.createOnlinePayment(300.0, "test@test.com");

        assertNotNull(cash);
        assertNotNull(card);
        assertNotNull(online);

        assertEquals(PaymentMethod.CASH, cash.getMethod());
        assertEquals(PaymentMethod.CARD, card.getMethod());
        assertEquals(PaymentMethod.ONLINE, online.getMethod());
    }

    @Test
    @DisplayName("Транзакционные ID имеют правильные префиксы")
    void testTransactionIdPrefixes() throws Exception {
        CashPayment cash = factory.createCashPayment(100.0);
        CardPayment card = factory.createCardPayment(200.0, "1234567890123456");
        OnlinePayment online = factory.createOnlinePayment(300.0, "test@test.com");

        assertTrue(cash.getTransactionId().startsWith("CASH-"));
        assertTrue(card.getTransactionId().startsWith("CARD-"));
        assertTrue(online.getTransactionId().startsWith("ONLINE-"));
    }

    @Test
    @DisplayName("Все платежи изначально не успешные")
    void testAllPaymentsInitiallyUnsuccessful() throws Exception {
        CashPayment cash = factory.createCashPayment(100.0);
        CardPayment card = factory.createCardPayment(200.0, "1234567890123456");
        OnlinePayment online = factory.createOnlinePayment(300.0, "test@test.com");

        assertFalse(cash.isSuccessful());
        assertFalse(card.isSuccessful());
        assertFalse(online.isSuccessful());
    }

    @Test
    @DisplayName("Создание множества платежей разных типов")
    void testCreateManyPaymentsOfDifferentTypes() throws Exception {
        for (int i = 0; i < 5; i++) {
            CashPayment cash = factory.createCashPayment(100.0 * (i + 1));
            CardPayment card = factory.createCardPayment(200.0 * (i + 1), "1234567890123456");
            OnlinePayment online = factory.createOnlinePayment(300.0 * (i + 1), "user" + i + "@test.com");

            assertNotNull(cash);
            assertNotNull(card);
            assertNotNull(online);
        }
    }
}
