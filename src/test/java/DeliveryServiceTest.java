import com.pizzeria.enums.EmployeeRole;
import com.pizzeria.exceptions.InvalidDeliveryAddressException;
import com.pizzeria.model.Address;
import com.pizzeria.model.DeliveryInfo;
import com.pizzeria.model.Order;
import com.pizzeria.model.users.Customer;
import com.pizzeria.model.users.DeliveryDriver;
import com.pizzeria.service.DeliveryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DeliveryService Tests")
class DeliveryServiceTest {

    private DeliveryService service;
    private DeliveryDriver driver1;
    private DeliveryDriver driver2;
    private DeliveryDriver driver3;
    private Order order;
    private Address validAddress;

    @BeforeEach
    void setUp() {
        service = new DeliveryService();

        driver1 = new DeliveryDriver("DRV001", "Алексей", "Смирнов", 40000.0);
        driver2 = new DeliveryDriver("DRV002", "Дмитрий", "Кузнецов", 38000.0);
        driver3 = new DeliveryDriver("DRV003", "Сергей", "Попов", 42000.0);

        Customer customer = new Customer("CUST001", "Иван", "Иванов");
        order = new Order("ORD-001", customer);

        validAddress = new Address("Москва", "Ленина", "10", "123");
    }

    @Test
    @DisplayName("Создание сервиса доставки")
    void testServiceCreation() {
        assertNotNull(service);
        assertEquals(0, service.getActiveDeliveriesCount());
    }

    @Test
    @DisplayName("Добавление водителя")
    void testAddDriver() {
        service.addDriver(driver1);

        DeliveryDriver found = service.findAvailableDriver();
        assertNotNull(found);
        assertEquals(driver1, found);
    }

    @Test
    @DisplayName("Добавление нескольких водителей")
    void testAddMultipleDrivers() {
        service.addDriver(driver1);
        service.addDriver(driver2);
        service.addDriver(driver3);

        // Должен вернуть первого доступного
        DeliveryDriver found = service.findAvailableDriver();
        assertNotNull(found);
    }

    @Test
    @DisplayName("Поиск доступного водителя когда все доступны")
    void testFindAvailableDriverWhenAllAvailable() {
        service.addDriver(driver1);
        service.addDriver(driver2);

        assertTrue(driver1.isAvailable());
        assertTrue(driver2.isAvailable());

        DeliveryDriver found = service.findAvailableDriver();
        assertNotNull(found);
        assertEquals(driver1, found); // Возвращает первого
    }

    @Test
    @DisplayName("Поиск доступного водителя когда никто не доступен")
    void testFindAvailableDriverWhenNoneAvailable() {
        service.addDriver(driver1);
        driver1.startDelivery(); // Делаем недоступным

        DeliveryDriver found = service.findAvailableDriver();
        assertNull(found);
    }

    @Test
    @DisplayName("Поиск доступного водителя когда список пуст")
    void testFindAvailableDriverWhenEmpty() {
        DeliveryDriver found = service.findAvailableDriver();
        assertNull(found);
    }

    @Test
    @DisplayName("Поиск доступного водителя среди частично занятых")
    void testFindAvailableDriverPartiallyBusy() {
        service.addDriver(driver1);
        service.addDriver(driver2);
        service.addDriver(driver3);

        driver1.startDelivery();
        driver3.startDelivery();

        DeliveryDriver found = service.findAvailableDriver();
        assertNotNull(found);
        assertEquals(driver2, found); // Только driver2 доступен
    }

    @Test
    @DisplayName("Планирование доставки")
    void testScheduleDelivery() throws Exception {
        service.addDriver(driver1);
        order.setDeliveryAddress(validAddress);

        DeliveryInfo delivery = service.scheduleDelivery(order, driver1);

        assertNotNull(delivery);
        assertEquals(order, delivery.getOrder());
        assertEquals(driver1, delivery.getDriver());
        assertEquals(1, service.getActiveDeliveriesCount());
        assertFalse(driver1.isAvailable()); // Водитель занят
    }

    @Test
    @DisplayName("Исключение при планировании доставки без адреса")
    void testScheduleDeliveryWithoutAddress() {
        service.addDriver(driver1);
        // order.deliveryAddress == null

        InvalidDeliveryAddressException exception = assertThrows(
            InvalidDeliveryAddressException.class,
            () -> service.scheduleDelivery(order, driver1)
        );

        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("не указан"));
        assertEquals(0, service.getActiveDeliveriesCount());
    }

    @Test
    @DisplayName("Планирование нескольких доставок")
    void testScheduleMultipleDeliveries() throws Exception {
        service.addDriver(driver1);
        service.addDriver(driver2);

        Customer customer2 = new Customer("CUST002", "Петр", "Петров");
        Order order2 = new Order("ORD-002", customer2);

        order.setDeliveryAddress(validAddress);
        order2.setDeliveryAddress(validAddress);

        service.scheduleDelivery(order, driver1);
        service.scheduleDelivery(order2, driver2);

        assertEquals(2, service.getActiveDeliveriesCount());
    }

    @Test
    @DisplayName("Завершение доставки")
    void testCompleteDelivery() throws Exception {
        service.addDriver(driver1);
        order.setDeliveryAddress(validAddress);

        DeliveryInfo delivery = service.scheduleDelivery(order, driver1);
        assertEquals(1, service.getActiveDeliveriesCount());
        assertFalse(driver1.isAvailable());

        service.completeDelivery(delivery);

        assertEquals(0, service.getActiveDeliveriesCount());
        assertTrue(driver1.isAvailable()); // Водитель снова доступен
        assertTrue(delivery.isCompleted());
    }

    @Test
    @DisplayName("Завершение одной из нескольких доставок")
    void testCompleteOneOfMultipleDeliveries() throws Exception {
        service.addDriver(driver1);
        service.addDriver(driver2);

        Customer customer2 = new Customer("CUST002", "Петр", "Петров");
        Order order2 = new Order("ORD-002", customer2);

        order.setDeliveryAddress(validAddress);
        order2.setDeliveryAddress(validAddress);

        DeliveryInfo delivery1 = service.scheduleDelivery(order, driver1);
        DeliveryInfo delivery2 = service.scheduleDelivery(order2, driver2);

        assertEquals(2, service.getActiveDeliveriesCount());

        service.completeDelivery(delivery1);

        assertEquals(1, service.getActiveDeliveriesCount());
        assertTrue(driver1.isAvailable());
        assertFalse(driver2.isAvailable());
    }

    @Test
    @DisplayName("Валидация корректного адреса")
    void testValidateCorrectAddress() {
        Address address = new Address("Москва", "Ленина", "10", "123");
        assertTrue(service.validateAddress(address));
    }

    @Test
    @DisplayName("Валидация null адреса")
    void testValidateNullAddress() {
        assertFalse(service.validateAddress(null));
    }

    @Test
    @DisplayName("Валидация адреса без улицы")
    void testValidateAddressWithoutStreet() {
        Address address = new Address("Москва", null, "10", "123");
        assertFalse(service.validateAddress(address));
    }

    @Test
    @DisplayName("Валидация адреса без номера дома")
    void testValidateAddressWithoutHouseNumber() {
        Address address = new Address("Москва", "Ленина", null, "123");
        assertFalse(service.validateAddress(address));
    }

    @Test
    @DisplayName("Валидация адреса без города")
    void testValidateAddressWithoutCity() {
        Address address = new Address(null, "Ленина", "10", "123");
        assertFalse(service.validateAddress(address));
    }

    @Test
    @DisplayName("Валидация адреса без квартиры (квартира опциональна)")
    void testValidateAddressWithoutApartment() {
        Address address = new Address("Москва", "Ленина", "10", null);
        assertTrue(service.validateAddress(address)); // Квартира не обязательна
    }

    @Test
    @DisplayName("Количество активных доставок изначально ноль")
    void testInitialActiveDeliveriesCount() {
        assertEquals(0, service.getActiveDeliveriesCount());
    }

    @Test
    @DisplayName("Количество активных доставок увеличивается")
    void testActiveDeliveriesCountIncreases() throws Exception {
        service.addDriver(driver1);
        service.addDriver(driver2);
        service.addDriver(driver3);

        Customer c1 = new Customer("C1", "A", "B");
        Customer c2 = new Customer("C2", "C", "D");
        Customer c3 = new Customer("C3", "E", "F");

        Order o1 = new Order("O1", c1);
        Order o2 = new Order("O2", c2);
        Order o3 = new Order("O3", c3);

        o1.setDeliveryAddress(validAddress);
        o2.setDeliveryAddress(validAddress);
        o3.setDeliveryAddress(validAddress);

        service.scheduleDelivery(o1, driver1);
        assertEquals(1, service.getActiveDeliveriesCount());

        service.scheduleDelivery(o2, driver2);
        assertEquals(2, service.getActiveDeliveriesCount());

        service.scheduleDelivery(o3, driver3);
        assertEquals(3, service.getActiveDeliveriesCount());
    }

    @Test
    @DisplayName("Полный цикл: планирование и завершение доставки")
    void testFullDeliveryCycle() throws Exception {
        service.addDriver(driver1);
        order.setDeliveryAddress(validAddress);

        // Начальное состояние
        assertTrue(driver1.isAvailable());
        assertEquals(0, service.getActiveDeliveriesCount());

        // Планирование
        DeliveryInfo delivery = service.scheduleDelivery(order, driver1);
        assertFalse(driver1.isAvailable());
        assertEquals(1, service.getActiveDeliveriesCount());

        // Завершение
        service.completeDelivery(delivery);
        assertTrue(driver1.isAvailable());
        assertEquals(0, service.getActiveDeliveriesCount());
        assertTrue(delivery.isCompleted());
    }

    @Test
    @DisplayName("Водитель может взять новую доставку после завершения предыдущей")
    void testDriverCanTakeNewDeliveryAfterCompletion() throws Exception {
        service.addDriver(driver1);

        Customer customer2 = new Customer("CUST002", "Петр", "Петров");
        Order order2 = new Order("ORD-002", customer2);

        order.setDeliveryAddress(validAddress);
        order2.setDeliveryAddress(validAddress);

        // Первая доставка
        DeliveryInfo delivery1 = service.scheduleDelivery(order, driver1);
        assertFalse(driver1.isAvailable());

        service.completeDelivery(delivery1);
        assertTrue(driver1.isAvailable());

        // Вторая доставка тем же водителем
        DeliveryInfo delivery2 = service.scheduleDelivery(order2, driver1);
        assertFalse(driver1.isAvailable());
        assertEquals(1, service.getActiveDeliveriesCount());
    }
}
