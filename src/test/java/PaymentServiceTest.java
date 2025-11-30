import com.pizzeria.enums.PaymentMethod;
import com.pizzeria.exceptions.InvalidPaymentException;
import com.pizzeria.exceptions.InvalidPriceException;
import com.pizzeria.model.Order;
import com.pizzeria.model.OrderItem;
import com.pizzeria.model.Receipt;
import com.pizzeria.model.payment.CashPayment;
import com.pizzeria.model.payment.CardPayment;
import com.pizzeria.model.payment.Payment;
import com.pizzeria.model.products.Product;
import com.pizzeria.model.users.Customer;
import com.pizzeria.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PaymentService Tests")
class PaymentServiceTest {

    private PaymentService service;
    private Order order;
    private Customer customer;
    private TestProduct product;

    // Тестовая реализация Product
    private static class TestProduct extends Product {
        public TestProduct(String name, double price) throws InvalidPriceException {
            super(name, price);
        }

        @Override
        public double getPrice() {
            return getBasePrice();
        }

        @Override
        public void applyDiscount(double discountPercentage) {
            setDiscountPercentage(discountPercentage);
        }

        @Override
        public double getFinalPrice() {
            return getBasePrice() * (1 - getDiscountPercentage() / 100);
        }

        @Override
        public int getPreparationTime() {
            return 10;
        }

        @Override
        public int getCalories() {
            return 500;
        }

        @Override
        public boolean processPayment(double amount) throws InvalidPaymentException {
            return amount >= getFinalPrice();
        }
    }

    @BeforeEach
    void setUp() throws Exception {
        service = new PaymentService();
        customer = new Customer("CUST001", "Иван", "Иванов");
        order = new Order("ORD-001", customer);

        product = new TestProduct("Тестовый товар", 1000.0);
        order.addItem(new OrderItem(product, 2)); // 2000 руб
    }

    @Test
    @DisplayName("Создание сервиса платежей")
    void testServiceCreation() {
        assertNotNull(service);
    }

    @Test
    @DisplayName("Успешная обработка платежа наличными")
    void testProcessCashPayment() throws Exception {
        CashPayment payment = new CashPayment("TXN001", 2000.0);
        payment.setAmountReceived(2000.0);

        Receipt receipt = service.processPayment(order, payment);

        assertNotNull(receipt);
        assertTrue(order.isPaid());
        assertTrue(payment.isSuccessful());
        assertEquals("RCP-1", receipt.getReceiptNumber());
    }

    @Test
    @DisplayName("Успешная обработка платежа картой")
    void testProcessCardPayment() throws Exception {
        CardPayment payment = new CardPayment("TXN002", 2000.0, "1234567890123456");

        Receipt receipt = service.processPayment(order, payment);

        assertNotNull(receipt);
        assertTrue(order.isPaid());
        assertTrue(payment.isSuccessful());
    }

    @Test
    @DisplayName("Обработка платежа с избыточной суммой")
    void testProcessPaymentWithExcessAmount() throws Exception {
        CashPayment payment = new CashPayment("TXN003", 3000.0);
        payment.setAmountReceived(3000.0);

        Receipt receipt = service.processPayment(order, payment);

        assertNotNull(receipt);
        assertTrue(order.isPaid());
    }

    @Test
    @DisplayName("Исключение при недостаточной сумме платежа")
    void testProcessPaymentInsufficientAmount() throws Exception {
        CashPayment payment = new CashPayment("TXN004", 1000.0); // Нужно 2000

        InvalidPaymentException exception = assertThrows(InvalidPaymentException.class, () -> {
            service.processPayment(order, payment);
        });

        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("Недостаточная сумма"));
        assertFalse(order.isPaid());
    }

    @Test
    @DisplayName("Инкремент номера чека при множественных платежах")
    void testReceiptNumberIncrement() throws Exception {
        Customer customer2 = new Customer("CUST002", "Петр", "Петров");
        Order order2 = new Order("ORD-002", customer2);
        order2.addItem(new OrderItem(product, 1));

        CashPayment payment1 = new CashPayment("TXN005", 2000.0);
        payment1.setAmountReceived(2000.0);

        CashPayment payment2 = new CashPayment("TXN006", 1000.0);
        payment2.setAmountReceived(1000.0);

        Receipt receipt1 = service.processPayment(order, payment1);
        Receipt receipt2 = service.processPayment(order2, payment2);

        assertEquals("RCP-1", receipt1.getReceiptNumber());
        assertEquals("RCP-2", receipt2.getReceiptNumber());
    }

    @Test
    @DisplayName("Возврат успешного платежа")
    void testRefundSuccessfulPayment() throws Exception {
        CashPayment payment = new CashPayment("TXN007", 2000.0);
        payment.setAmountReceived(2000.0);

        service.processPayment(order, payment);
        assertTrue(payment.isSuccessful());

        service.refundPayment(payment);
        assertFalse(payment.isSuccessful());
    }

    @Test
    @DisplayName("Исключение при возврате неуспешного платежа")
    void testRefundUnsuccessfulPayment() throws Exception {
        CashPayment payment = new CashPayment("TXN008", 2000.0);
        // Не обрабатываем платеж, он остается неуспешным

        InvalidPaymentException exception = assertThrows(InvalidPaymentException.class, () -> {
            service.refundPayment(payment);
        });

        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("неуспешный"));
    }

    @Test
    @DisplayName("Расчет налога 13%")
    void testCalculateTax() {
        assertEquals(130.0, service.calculateTax(1000.0), 0.01);
        assertEquals(26.0, service.calculateTax(200.0), 0.01);
        assertEquals(1300.0, service.calculateTax(10000.0), 0.01);
    }

    @Test
    @DisplayName("Расчет налога с нулевой суммой")
    void testCalculateTaxZeroAmount() {
        assertEquals(0.0, service.calculateTax(0.0), 0.01);
    }

    @Test
    @DisplayName("Расчет налога с дробными числами")
    void testCalculateTaxDecimalAmount() {
        assertEquals(13.0, service.calculateTax(100.0), 0.01);
        assertEquals(6.5, service.calculateTax(50.0), 0.01);
        assertEquals(16.25, service.calculateTax(125.0), 0.01);
    }

    @Test
    @DisplayName("Расчет сервисного сбора 5%")
    void testCalculateServiceFee() {
        assertEquals(50.0, service.calculateServiceFee(1000.0), 0.01);
        assertEquals(10.0, service.calculateServiceFee(200.0), 0.01);
        assertEquals(500.0, service.calculateServiceFee(10000.0), 0.01);
    }

    @Test
    @DisplayName("Расчет сервисного сбора с нулевой суммой")
    void testCalculateServiceFeeZeroAmount() {
        assertEquals(0.0, service.calculateServiceFee(0.0), 0.01);
    }

    @Test
    @DisplayName("Расчет сервисного сбора с дробными числами")
    void testCalculateServiceFeeDecimalAmount() {
        assertEquals(5.0, service.calculateServiceFee(100.0), 0.01);
        assertEquals(2.5, service.calculateServiceFee(50.0), 0.01);
        assertEquals(6.25, service.calculateServiceFee(125.0), 0.01);
    }

    @Test
    @DisplayName("Комплексный расчет: налог + сервисный сбор")
    void testCombinedTaxAndServiceFee() {
        double amount = 1000.0;
        double tax = service.calculateTax(amount);         // 130
        double fee = service.calculateServiceFee(amount);  // 50
        double total = amount + tax + fee;                 // 1180

        assertEquals(130.0, tax, 0.01);
        assertEquals(50.0, fee, 0.01);
        assertEquals(1180.0, total, 0.01);
    }

    @Test
    @DisplayName("Чек содержит информацию о заказе и платеже")
    void testReceiptContainsOrderAndPayment() throws Exception {
        CashPayment payment = new CashPayment("TXN009", 2000.0);
        payment.setAmountReceived(2000.0);

        Receipt receipt = service.processPayment(order, payment);

        assertEquals(order, receipt.getOrder());
        assertEquals(payment, receipt.getPayment());
    }

    @Test
    @DisplayName("Обработка нескольких платежей подряд")
    void testMultiplePaymentsInSequence() throws Exception {
        Customer c1 = new Customer("C1", "A", "B");
        Customer c2 = new Customer("C2", "C", "D");
        Customer c3 = new Customer("C3", "E", "F");

        Order o1 = new Order("O1", c1);
        Order o2 = new Order("O2", c2);
        Order o3 = new Order("O3", c3);

        o1.addItem(new OrderItem(product, 1));
        o2.addItem(new OrderItem(product, 1));
        o3.addItem(new OrderItem(product, 1));

        CashPayment p1 = new CashPayment("T1", 1000.0);
        CashPayment p2 = new CashPayment("T2", 1000.0);
        CardPayment p3 = new CardPayment("T3", 1000.0, "1234567890123456");

        p1.setAmountReceived(1000.0);
        p2.setAmountReceived(1000.0);

        Receipt r1 = service.processPayment(o1, p1);
        Receipt r2 = service.processPayment(o2, p2);
        Receipt r3 = service.processPayment(o3, p3);

        assertTrue(o1.isPaid());
        assertTrue(o2.isPaid());
        assertTrue(o3.isPaid());

        assertEquals("RCP-1", r1.getReceiptNumber());
        assertEquals("RCP-2", r2.getReceiptNumber());
        assertEquals("RCP-3", r3.getReceiptNumber());
    }
}
