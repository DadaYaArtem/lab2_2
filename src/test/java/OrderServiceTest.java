import com.pizzeria.enums.OrderStatus;
import com.pizzeria.exceptions.DuplicateOrderException;
import com.pizzeria.exceptions.OrderNotFoundException;
import com.pizzeria.model.Order;
import com.pizzeria.model.users.Customer;
import com.pizzeria.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("OrderService Tests")
class OrderServiceTest {

    private OrderService service;
    private Customer customer1;
    private Customer customer2;

    @BeforeEach
    void setUp() {
        service = new OrderService();
        customer1 = new Customer("CUST001", "Иван", "Иванов");
        customer2 = new Customer("CUST002", "Петр", "Петров");
    }

    @Test
    @DisplayName("Создание сервиса заказов")
    void testServiceCreation() {
        assertNotNull(service);
        assertEquals(0, service.getOrderCount());
    }

    @Test
    @DisplayName("Создание заказа")
    void testCreateOrder() throws Exception {
        Order order = service.createOrder(customer1);

        assertNotNull(order);
        assertEquals("ORD-1", order.getId());
        assertEquals(customer1, order.getCustomer());
        assertEquals(1, service.getOrderCount());
    }

    @Test
    @DisplayName("Создание нескольких заказов с инкрементом ID")
    void testCreateMultipleOrders() throws Exception {
        Order order1 = service.createOrder(customer1);
        Order order2 = service.createOrder(customer2);
        Order order3 = service.createOrder(customer1);

        assertEquals("ORD-1", order1.getId());
        assertEquals("ORD-2", order2.getId());
        assertEquals("ORD-3", order3.getId());
        assertEquals(3, service.getOrderCount());
    }

    @Test
    @DisplayName("Заказ добавляется в историю клиента")
    void testOrderAddedToCustomerHistory() throws Exception {
        Order order = service.createOrder(customer1);

        assertEquals(1, customer1.getTotalOrders());
        assertTrue(customer1.getOrderHistory().contains(order.getId()));
    }

    @Test
    @DisplayName("Получение заказа по ID")
    void testGetOrder() throws Exception {
        Order created = service.createOrder(customer1);
        Order retrieved = service.getOrder("ORD-1");

        assertNotNull(retrieved);
        assertEquals(created, retrieved);
        assertEquals("ORD-1", retrieved.getId());
    }

    @Test
    @DisplayName("Исключение при получении несуществующего заказа")
    void testGetNonExistentOrder() {
        OrderNotFoundException exception = assertThrows(OrderNotFoundException.class, () -> {
            service.getOrder("ORD-999");
        });

        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("ORD-999"));
    }

    @Test
    @DisplayName("Отмена заказа")
    void testCancelOrder() throws Exception {
        Order order = service.createOrder(customer1);
        assertEquals(OrderStatus.PENDING, order.getStatus());

        service.cancelOrder("ORD-1");

        assertEquals(OrderStatus.CANCELLED, order.getStatus());
    }

    @Test
    @DisplayName("Исключение при отмене несуществующего заказа")
    void testCancelNonExistentOrder() {
        assertThrows(OrderNotFoundException.class, () -> {
            service.cancelOrder("ORD-999");
        });
    }

    @Test
    @DisplayName("Обновление статуса заказа")
    void testUpdateOrderStatus() throws Exception {
        Order order = service.createOrder(customer1);

        service.updateOrderStatus("ORD-1", OrderStatus.PREPARING);
        assertEquals(OrderStatus.PREPARING, order.getStatus());

        service.updateOrderStatus("ORD-1", OrderStatus.READY);
        assertEquals(OrderStatus.READY, order.getStatus());

        service.updateOrderStatus("ORD-1", OrderStatus.DELIVERED);
        assertEquals(OrderStatus.DELIVERED, order.getStatus());
    }

    @Test
    @DisplayName("Исключение при обновлении статуса несуществующего заказа")
    void testUpdateNonExistentOrderStatus() {
        assertThrows(OrderNotFoundException.class, () -> {
            service.updateOrderStatus("ORD-999", OrderStatus.DELIVERED);
        });
    }

    @Test
    @DisplayName("Расчет общей выручки с пустыми заказами")
    void testCalculateTotalRevenueEmpty() {
        double revenue = service.calculateTotalRevenue();
        assertEquals(0.0, revenue, 0.01);
    }

    @Test
    @DisplayName("Расчет общей выручки с неоплаченными заказами")
    void testCalculateTotalRevenueUnpaid() throws Exception {
        service.createOrder(customer1);
        service.createOrder(customer2);

        double revenue = service.calculateTotalRevenue();
        assertEquals(0.0, revenue, 0.01); // Заказы не оплачены
    }

    @Test
    @DisplayName("Расчет общей выручки с оплаченными заказами")
    void testCalculateTotalRevenuePaid() throws Exception {
        Order order1 = service.createOrder(customer1);
        Order order2 = service.createOrder(customer2);

        // Имитируем оплату
        order1.processPayment(1000.0);
        order2.processPayment(500.0);

        double revenue = service.calculateTotalRevenue();
        assertEquals(1500.0, revenue, 0.01);
    }

    @Test
    @DisplayName("Расчет выручки с частично оплаченными заказами")
    void testCalculateTotalRevenuePartiallyPaid() throws Exception {
        Order order1 = service.createOrder(customer1);
        Order order2 = service.createOrder(customer2);
        Order order3 = service.createOrder(customer1);

        order1.processPayment(1000.0);
        // order2 не оплачен
        order3.processPayment(300.0);

        double revenue = service.calculateTotalRevenue();
        assertEquals(1300.0, revenue, 0.01);
    }

    @Test
    @DisplayName("Получение количества заказов")
    void testGetOrderCount() throws Exception {
        assertEquals(0, service.getOrderCount());

        service.createOrder(customer1);
        assertEquals(1, service.getOrderCount());

        service.createOrder(customer2);
        assertEquals(2, service.getOrderCount());

        service.createOrder(customer1);
        assertEquals(3, service.getOrderCount());
    }

    @Test
    @DisplayName("Получение всех заказов")
    void testGetAllOrders() throws Exception {
        service.createOrder(customer1);
        service.createOrder(customer2);

        Map<String, Order> allOrders = service.getAllOrders();

        assertNotNull(allOrders);
        assertEquals(2, allOrders.size());
        assertTrue(allOrders.containsKey("ORD-1"));
        assertTrue(allOrders.containsKey("ORD-2"));
    }

    @Test
    @DisplayName("GetAllOrders возвращает копию")
    void testGetAllOrdersReturnsCopy() throws Exception {
        service.createOrder(customer1);

        Map<String, Order> orders1 = service.getAllOrders();
        Map<String, Order> orders2 = service.getAllOrders();

        assertNotSame(orders1, orders2); // Разные объекты
        assertEquals(orders1.size(), orders2.size()); // Но одинаковое содержимое
    }

    @Test
    @DisplayName("Пустой список заказов при инициализации")
    void testEmptyOrdersOnInit() {
        Map<String, Order> orders = service.getAllOrders();

        assertNotNull(orders);
        assertTrue(orders.isEmpty());
    }

    @Test
    @DisplayName("Изменение статуса на все возможные значения")
    void testAllStatusTransitions() throws Exception {
        Order order = service.createOrder(customer1);

        OrderStatus[] allStatuses = {
            OrderStatus.PENDING,
            OrderStatus.PREPARING,
            OrderStatus.READY,
            OrderStatus.DELIVERING,
            OrderStatus.DELIVERED,
            OrderStatus.CANCELLED
        };

        for (OrderStatus status : allStatuses) {
            service.updateOrderStatus("ORD-1", status);
            assertEquals(status, order.getStatus());
        }
    }
}
