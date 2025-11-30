package com.pizzeria.model;

import com.pizzeria.enums.OrderStatus;
import com.pizzeria.exceptions.InvalidDeliveryAddressException;
import com.pizzeria.model.products.Product;
import com.pizzeria.model.users.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Тесты для класса Order")
class OrderTest {

    private Customer mockCustomer;
    private Product mockProduct;
    private Address mockAddress;

    @BeforeEach
    void setUp() throws InvalidDeliveryAddressException {
        mockCustomer = Mockito.mock(Customer.class);
        mockProduct = Mockito.mock(Product.class);
        mockAddress = Mockito.mock(Address.class);

        when(mockCustomer.getFullName()).thenReturn("Иван Иванов");
        when(mockProduct.getName()).thenReturn("Пицца Маргарита");
        when(mockProduct.getFinalPrice()).thenReturn(500.0);
        when(mockProduct.getCalories()).thenReturn(250);
        when(mockAddress.getLatitude()).thenReturn(2.5);
    }

    @Nested
    @DisplayName("Тесты конструктора")
    class ConstructorTests {

        @Test
        @DisplayName("Успешное создание заказа")
        void shouldCreateOrderWithValidData() {
            // given & when
            Order order = new Order("ORD-001", mockCustomer);

            // then
            assertNotNull(order);
            assertEquals("ORD-001", order.getId());
            assertEquals(mockCustomer, order.getCustomer());
            assertEquals(OrderStatus.PENDING, order.getStatus());
            assertNotNull(order.getOrderTime());
            assertNotNull(order.getItems());
            assertTrue(order.getItems().isEmpty());
            assertEquals(0.0, order.getDiscountPercentage());
            assertFalse(order.isPaid());
        }

        @Test
        @DisplayName("Время заказа устанавливается при создании")
        void shouldSetOrderTimeOnCreation() {
            // given
            LocalDateTime before = LocalDateTime.now();

            // when
            Order order = new Order("ORD-001", mockCustomer);

            // then
            LocalDateTime after = LocalDateTime.now();
            assertTrue(!order.getOrderTime().isBefore(before));
            assertTrue(!order.getOrderTime().isAfter(after));
        }

        @Test
        @DisplayName("Начальный статус PENDING")
        void shouldHavePendingStatusInitially() {
            // given & when
            Order order = new Order("ORD-001", mockCustomer);

            // then
            assertEquals(OrderStatus.PENDING, order.getStatus());
        }

        @Test
        @DisplayName("Заказ не оплачен при создании")
        void shouldBeUnpaidInitially() {
            // given & when
            Order order = new Order("ORD-001", mockCustomer);

            // then
            assertFalse(order.isPaid());
        }
    }

    @Nested
    @DisplayName("Тесты метода addItem")
    class AddItemTests {

        @Test
        @DisplayName("Добавление товара в заказ")
        void shouldAddItemToOrder() {
            // given
            Order order = new Order("ORD-001", mockCustomer);

            // when
            order.addItem(mockProduct, 2);

            // then
            assertEquals(1, order.getItems().size());
            OrderItem item = order.getItems().get(0);
            assertEquals(mockProduct, item.getProduct());
            assertEquals(2, item.getQuantity());
        }

        @Test
        @DisplayName("Добавление нескольких разных товаров")
        void shouldAddMultipleDifferentItems() {
            // given
            Order order = new Order("ORD-001", mockCustomer);
            Product product2 = Mockito.mock(Product.class);

            // when
            order.addItem(mockProduct, 2);
            order.addItem(product2, 3);

            // then
            assertEquals(2, order.getItems().size());
        }

        @Test
        @DisplayName("Добавление товара с количеством 1")
        void shouldAddItemWithQuantity1() {
            // given
            Order order = new Order("ORD-001", mockCustomer);

            // when
            order.addItem(mockProduct, 1);

            // then
            assertEquals(1, order.getItems().get(0).getQuantity());
        }

        @Test
        @DisplayName("Добавление товара с большим количеством")
        void shouldAddItemWithLargeQuantity() {
            // given
            Order order = new Order("ORD-001", mockCustomer);

            // when
            order.addItem(mockProduct, 100);

            // then
            assertEquals(100, order.getItems().get(0).getQuantity());
        }
    }

    @Nested
    @DisplayName("Тесты метода removeItem")
    class RemoveItemTests {

        @Test
        @DisplayName("Удаление товара из заказа")
        void shouldRemoveItemFromOrder() {
            // given
            Order order = new Order("ORD-001", mockCustomer);
            order.addItem(mockProduct, 2);
            OrderItem item = order.getItems().get(0);

            // when
            order.removeItem(item);

            // then
            assertEquals(0, order.getItems().size());
        }

        @Test
        @DisplayName("Удаление одного из нескольких товаров")
        void shouldRemoveOneOfMultipleItems() {
            // given
            Order order = new Order("ORD-001", mockCustomer);
            order.addItem(mockProduct, 2);
            Product product2 = Mockito.mock(Product.class);
            order.addItem(product2, 3);
            OrderItem itemToRemove = order.getItems().get(0);

            // when
            order.removeItem(itemToRemove);

            // then
            assertEquals(1, order.getItems().size());
        }

        @Test
        @DisplayName("Удаление несуществующего товара")
        void shouldHandleRemovingNonExistentItem() {
            // given
            Order order = new Order("ORD-001", mockCustomer);
            order.addItem(mockProduct, 2);
            OrderItem nonExistentItem = new OrderItem(Mockito.mock(Product.class), 1);

            // when
            order.removeItem(nonExistentItem);

            // then
            assertEquals(1, order.getItems().size());
        }
    }

    @Nested
    @DisplayName("Тесты метода getPrice")
    class GetPriceTests {

        @Test
        @DisplayName("Цена пустого заказа равна 0")
        void shouldReturn0ForEmptyOrder() {
            // given
            Order order = new Order("ORD-001", mockCustomer);

            // when
            double price = order.getPrice();

            // then
            assertEquals(0.0, price);
        }

        @Test
        @DisplayName("Расчет цены с одним товаром")
        void shouldCalculatePriceWithOneItem() {
            // given
            Order order = new Order("ORD-001", mockCustomer);
            order.addItem(mockProduct, 2);

            // when
            double price = order.getPrice();

            // then
            assertEquals(1000.0, price); // 500 * 2
        }

        @Test
        @DisplayName("Расчет цены с несколькими товарами")
        void shouldCalculatePriceWithMultipleItems() {
            // given
            Order order = new Order("ORD-001", mockCustomer);
            Product product2 = Mockito.mock(Product.class);
            when(product2.getFinalPrice()).thenReturn(300.0);

            order.addItem(mockProduct, 2); // 1000
            order.addItem(product2, 3);     // 900

            // when
            double price = order.getPrice();

            // then
            assertEquals(1900.0, price);
        }
    }

    @Nested
    @DisplayName("Тесты метода applyDiscount")
    class ApplyDiscountTests {

        @Test
        @DisplayName("Применение скидки")
        void shouldApplyDiscount() {
            // given
            Order order = new Order("ORD-001", mockCustomer);

            // when
            order.applyDiscount(10.0);

            // then
            assertEquals(10.0, order.getDiscountPercentage());
        }

        @Test
        @DisplayName("Применение скидки 0%")
        void shouldApplyZeroDiscount() {
            // given
            Order order = new Order("ORD-001", mockCustomer);

            // when
            order.applyDiscount(0.0);

            // then
            assertEquals(0.0, order.getDiscountPercentage());
        }

        @Test
        @DisplayName("Применение скидки 100%")
        void shouldApply100PercentDiscount() {
            // given
            Order order = new Order("ORD-001", mockCustomer);

            // when
            order.applyDiscount(100.0);

            // then
            assertEquals(100.0, order.getDiscountPercentage());
        }

        @Test
        @DisplayName("Замена предыдущей скидки")
        void shouldReplacePreviousDiscount() {
            // given
            Order order = new Order("ORD-001", mockCustomer);
            order.applyDiscount(10.0);

            // when
            order.applyDiscount(20.0);

            // then
            assertEquals(20.0, order.getDiscountPercentage());
        }
    }

    @Nested
    @DisplayName("Тесты метода getFinalPrice")
    class GetFinalPriceTests {

        @Test
        @DisplayName("Итоговая цена без скидки и доставки")
        void shouldCalculateFinalPriceWithoutDiscountAndDelivery() {
            // given
            Order order = new Order("ORD-001", mockCustomer);
            order.addItem(mockProduct, 2);

            // when
            double finalPrice = order.getFinalPrice();

            // then
            assertEquals(1000.0, finalPrice);
        }

        @Test
        @DisplayName("Итоговая цена со скидкой")
        void shouldCalculateFinalPriceWithDiscount() {
            // given
            Order order = new Order("ORD-001", mockCustomer);
            order.addItem(mockProduct, 2);
            order.applyDiscount(10.0);

            // when
            double finalPrice = order.getFinalPrice();

            // then
            assertEquals(900.0, finalPrice); // 1000 * 0.9
        }

        @Test
        @DisplayName("Итоговая цена с доставкой")
        void shouldCalculateFinalPriceWithDelivery() throws InvalidDeliveryAddressException {
            // given
            Order order = new Order("ORD-001", mockCustomer);
            order.addItem(mockProduct, 2);
            order.setDeliveryAddress(mockAddress);

            // when
            double finalPrice = order.getFinalPrice();

            // then
            assertTrue(finalPrice > 1000.0); // базовая цена + стоимость доставки
        }

        @Test
        @DisplayName("Итоговая цена со скидкой и доставкой")
        void shouldCalculateFinalPriceWithDiscountAndDelivery() throws InvalidDeliveryAddressException {
            // given
            Order order = new Order("ORD-001", mockCustomer);
            order.addItem(mockProduct, 2);
            order.applyDiscount(10.0);
            order.setDeliveryAddress(mockAddress);

            // when
            double finalPrice = order.getFinalPrice();

            // then
            double basePrice = 1000.0;
            double deliveryCost = order.calculateDeliveryCost();
            double expected = (basePrice + deliveryCost) * 0.9;
            assertEquals(expected, finalPrice, 0.01);
        }

        @Test
        @DisplayName("Итоговая цена со 100% скидкой")
        void shouldCalculateFinalPriceWith100PercentDiscount() {
            // given
            Order order = new Order("ORD-001", mockCustomer);
            order.addItem(mockProduct, 2);
            order.applyDiscount(100.0);

            // when
            double finalPrice = order.getFinalPrice();

            // then
            assertEquals(0.0, finalPrice);
        }
    }

    @Nested
    @DisplayName("Тесты метода processPayment")
    class ProcessPaymentTests {

        @Test
        @DisplayName("Успешная оплата")
        void shouldProcessPaymentSuccessfully() {
            // given
            Order order = new Order("ORD-001", mockCustomer);
            order.addItem(mockProduct, 2);

            // when
            boolean result = order.processPayment(1000.0);

            // then
            assertTrue(result);
            assertTrue(order.isPaid());
            assertEquals(OrderStatus.CONFIRMED, order.getStatus());
        }

        @Test
        @DisplayName("Оплата с суммой больше необходимой")
        void shouldProcessPaymentWithExcessAmount() {
            // given
            Order order = new Order("ORD-001", mockCustomer);
            order.addItem(mockProduct, 2);

            // when
            boolean result = order.processPayment(1500.0);

            // then
            assertTrue(result);
            assertTrue(order.isPaid());
        }

        @Test
        @DisplayName("Неудачная оплата с недостаточной суммой")
        void shouldFailPaymentWithInsufficientAmount() {
            // given
            Order order = new Order("ORD-001", mockCustomer);
            order.addItem(mockProduct, 2);

            // when
            boolean result = order.processPayment(500.0);

            // then
            assertFalse(result);
            assertFalse(order.isPaid());
            assertEquals(OrderStatus.PENDING, order.getStatus());
        }

        @Test
        @DisplayName("Оплата точной суммы")
        void shouldProcessPaymentWithExactAmount() {
            // given
            Order order = new Order("ORD-001", mockCustomer);
            order.addItem(mockProduct, 2);

            // when
            boolean result = order.processPayment(order.getFinalPrice());

            // then
            assertTrue(result);
            assertTrue(order.isPaid());
        }

        @Test
        @DisplayName("Оплата с нулевой суммой")
        void shouldFailPaymentWithZeroAmount() {
            // given
            Order order = new Order("ORD-001", mockCustomer);
            order.addItem(mockProduct, 2);

            // when
            boolean result = order.processPayment(0.0);

            // then
            assertFalse(result);
            assertFalse(order.isPaid());
        }
    }

    @Nested
    @DisplayName("Тесты метода setDeliveryAddress")
    class SetDeliveryAddressTests {

        @Test
        @DisplayName("Установка адреса доставки")
        void shouldSetDeliveryAddress() throws InvalidDeliveryAddressException {
            // given
            Order order = new Order("ORD-001", mockCustomer);

            // when
            order.setDeliveryAddress(mockAddress);

            // then
            assertEquals(mockAddress, order.getDeliveryAddress());
        }

        @Test
        @DisplayName("Исключение при установке null адреса")
        void shouldThrowExceptionWhenSettingNullAddress() {
            // given
            Order order = new Order("ORD-001", mockCustomer);

            // when & then
            InvalidDeliveryAddressException exception = assertThrows(
                InvalidDeliveryAddressException.class,
                () -> order.setDeliveryAddress(null)
            );
            assertTrue(exception.getMessage().contains("Адрес не может быть пустым"));
        }

        @Test
        @DisplayName("Замена адреса доставки")
        void shouldReplaceDeliveryAddress() throws InvalidDeliveryAddressException {
            // given
            Order order = new Order("ORD-001", mockCustomer);
            Address newAddress = Mockito.mock(Address.class);
            order.setDeliveryAddress(mockAddress);

            // when
            order.setDeliveryAddress(newAddress);

            // then
            assertEquals(newAddress, order.getDeliveryAddress());
        }
    }

    @Nested
    @DisplayName("Тесты метода calculateDeliveryTime")
    class CalculateDeliveryTimeTests {

        @Test
        @DisplayName("Время доставки без адреса")
        void shouldReturn0WhenNoAddress() {
            // given
            Order order = new Order("ORD-001", mockCustomer);

            // when
            int deliveryTime = order.calculateDeliveryTime();

            // then
            assertEquals(0, deliveryTime);
        }

        @Test
        @DisplayName("Расчет времени доставки с адресом")
        void shouldCalculateDeliveryTimeWithAddress() throws InvalidDeliveryAddressException {
            // given
            Order order = new Order("ORD-001", mockCustomer);
            order.setDeliveryAddress(mockAddress);

            // when
            int deliveryTime = order.calculateDeliveryTime();

            // then
            assertTrue(deliveryTime > 0);
            // 30 минут базовое + 5 минут на км (2.5 км = 12.5 минут)
            assertEquals(30 + (int)(2.5 * 5), deliveryTime);
        }

        @Test
        @DisplayName("Расчет времени с разными расстояниями")
        void shouldCalculateTimeForDifferentDistances() throws InvalidDeliveryAddressException {
            // given
            Order order = new Order("ORD-001", mockCustomer);
            when(mockAddress.getLatitude()).thenReturn(5.0);
            order.setDeliveryAddress(mockAddress);

            // when
            int deliveryTime = order.calculateDeliveryTime();

            // then
            assertEquals(30 + 25, deliveryTime); // 30 + 5*5
        }
    }

    @Nested
    @DisplayName("Тесты метода calculateDeliveryCost")
    class CalculateDeliveryCostTests {

        @Test
        @DisplayName("Стоимость доставки без адреса")
        void shouldReturn0WhenNoAddress() {
            // given
            Order order = new Order("ORD-001", mockCustomer);

            // when
            double cost = order.calculateDeliveryCost();

            // then
            assertEquals(0.0, cost);
        }

        @Test
        @DisplayName("Стоимость доставки < 3 км")
        void shouldCalculateCostForDistanceLessThan3Km() throws InvalidDeliveryAddressException {
            // given
            Order order = new Order("ORD-001", mockCustomer);
            when(mockAddress.getLatitude()).thenReturn(2.0);
            order.setDeliveryAddress(mockAddress);

            // when
            double cost = order.calculateDeliveryCost();

            // then
            assertEquals(100.0, cost);
        }

        @Test
        @DisplayName("Стоимость доставки >= 3 км и < 5 км")
        void shouldCalculateCostForDistanceBetween3And5Km() throws InvalidDeliveryAddressException {
            // given
            Order order = new Order("ORD-001", mockCustomer);
            when(mockAddress.getLatitude()).thenReturn(4.0);
            order.setDeliveryAddress(mockAddress);

            // when
            double cost = order.calculateDeliveryCost();

            // then
            assertEquals(150.0, cost);
        }

        @Test
        @DisplayName("Стоимость доставки >= 5 км")
        void shouldCalculateCostForDistanceGreaterOrEqual5Km() throws InvalidDeliveryAddressException {
            // given
            Order order = new Order("ORD-001", mockCustomer);
            when(mockAddress.getLatitude()).thenReturn(6.0);
            order.setDeliveryAddress(mockAddress);

            // when
            double cost = order.calculateDeliveryCost();

            // then
            assertEquals(200.0, cost);
        }

        @Test
        @DisplayName("Граничные случаи расстояний")
        void shouldHandleBoundaryDistances() throws InvalidDeliveryAddressException {
            // given
            Order order1 = new Order("ORD-001", mockCustomer);
            Order order2 = new Order("ORD-002", mockCustomer);

            // when & then - ровно 3 км
            when(mockAddress.getLatitude()).thenReturn(3.0);
            order1.setDeliveryAddress(mockAddress);
            assertEquals(150.0, order1.calculateDeliveryCost());

            // when & then - ровно 5 км
            when(mockAddress.getLatitude()).thenReturn(5.0);
            order2.setDeliveryAddress(mockAddress);
            assertEquals(200.0, order2.calculateDeliveryCost());
        }
    }

    @Nested
    @DisplayName("Тесты метода updateStatus")
    class UpdateStatusTests {

        @Test
        @DisplayName("Обновление статуса заказа")
        void shouldUpdateOrderStatus() {
            // given
            Order order = new Order("ORD-001", mockCustomer);

            // when
            order.updateStatus(OrderStatus.PREPARING);

            // then
            assertEquals(OrderStatus.PREPARING, order.getStatus());
        }

        @Test
        @DisplayName("Обновление статуса на DELIVERED")
        void shouldUpdateToDelivered() {
            // given
            Order order = new Order("ORD-001", mockCustomer);

            // when
            order.updateStatus(OrderStatus.DELIVERED);

            // then
            assertEquals(OrderStatus.DELIVERED, order.getStatus());
        }

        @Test
        @DisplayName("Множественное обновление статуса")
        void shouldUpdateStatusMultipleTimes() {
            // given
            Order order = new Order("ORD-001", mockCustomer);

            // when
            order.updateStatus(OrderStatus.CONFIRMED);
            order.updateStatus(OrderStatus.PREPARING);
            order.updateStatus(OrderStatus.READY);

            // then
            assertEquals(OrderStatus.READY, order.getStatus());
        }
    }

    @Nested
    @DisplayName("Тесты метода getTotalItems")
    class GetTotalItemsTests {

        @Test
        @DisplayName("Общее количество товаров в пустом заказе")
        void shouldReturn0ForEmptyOrder() {
            // given
            Order order = new Order("ORD-001", mockCustomer);

            // when
            int total = order.getTotalItems();

            // then
            assertEquals(0, total);
        }

        @Test
        @DisplayName("Подсчет общего количества товаров")
        void shouldCountTotalItems() {
            // given
            Order order = new Order("ORD-001", mockCustomer);
            order.addItem(mockProduct, 2);
            Product product2 = Mockito.mock(Product.class);
            order.addItem(product2, 3);

            // when
            int total = order.getTotalItems();

            // then
            assertEquals(5, total);
        }

        @Test
        @DisplayName("Подсчет после удаления товара")
        void shouldCountAfterRemovingItem() {
            // given
            Order order = new Order("ORD-001", mockCustomer);
            order.addItem(mockProduct, 2);
            Product product2 = Mockito.mock(Product.class);
            order.addItem(product2, 3);
            OrderItem itemToRemove = order.getItems().get(0);

            // when
            order.removeItem(itemToRemove);
            int total = order.getTotalItems();

            // then
            assertEquals(3, total);
        }
    }

    @Nested
    @DisplayName("Тесты геттеров и сеттеров")
    class GetterSetterTests {

        @Test
        @DisplayName("Установка и получение ID")
        void shouldSetAndGetId() {
            // given
            Order order = new Order("ORD-001", mockCustomer);

            // when
            order.setId("ORD-999");

            // then
            assertEquals("ORD-999", order.getId());
        }

        @Test
        @DisplayName("Установка и получение клиента")
        void shouldSetAndGetCustomer() {
            // given
            Order order = new Order("ORD-001", mockCustomer);
            Customer newCustomer = Mockito.mock(Customer.class);

            // when
            order.setCustomer(newCustomer);

            // then
            assertEquals(newCustomer, order.getCustomer());
        }

        @Test
        @DisplayName("Установка и получение списка товаров")
        void shouldSetAndGetItems() {
            // given
            Order order = new Order("ORD-001", mockCustomer);
            OrderItem item = new OrderItem(mockProduct, 2);
            List<OrderItem> items = List.of(item);

            // when
            order.setItems(items);

            // then
            assertEquals(1, order.getItems().size());
        }

        @Test
        @DisplayName("Установка и получение статуса")
        void shouldSetAndGetStatus() {
            // given
            Order order = new Order("ORD-001", mockCustomer);

            // when
            order.setStatus(OrderStatus.COMPLETED);

            // then
            assertEquals(OrderStatus.COMPLETED, order.getStatus());
        }

        @Test
        @DisplayName("Установка и получение времени заказа")
        void shouldSetAndGetOrderTime() {
            // given
            Order order = new Order("ORD-001", mockCustomer);
            LocalDateTime newTime = LocalDateTime.of(2024, 6, 15, 10, 0);

            // when
            order.setOrderTime(newTime);

            // then
            assertEquals(newTime, order.getOrderTime());
        }

        @Test
        @DisplayName("Установка и получение времени доставки")
        void shouldSetAndGetDeliveryTime() {
            // given
            Order order = new Order("ORD-001", mockCustomer);
            LocalDateTime newTime = LocalDateTime.of(2024, 6, 15, 12, 0);

            // when
            order.setDeliveryTime(newTime);

            // then
            assertEquals(newTime, order.getDeliveryTime());
        }

        @Test
        @DisplayName("Установка и получение статуса оплаты")
        void shouldSetAndGetPaid() {
            // given
            Order order = new Order("ORD-001", mockCustomer);

            // when
            order.setPaid(true);

            // then
            assertTrue(order.isPaid());
        }
    }

    @Nested
    @DisplayName("Тесты toString")
    class ToStringTests {

        @Test
        @DisplayName("toString возвращает информацию о заказе")
        void shouldReturnOrderInfo() {
            // given
            Order order = new Order("ORD-001", mockCustomer);
            order.addItem(mockProduct, 2);

            // when
            String result = order.toString();

            // then
            assertTrue(result.contains("ORD-001"));
            assertTrue(result.contains("Ожидает обработки"));
            assertTrue(result.contains("1000"));
        }

        @Test
        @DisplayName("toString отражает изменения")
        void shouldReflectChanges() {
            // given
            Order order = new Order("ORD-001", mockCustomer);
            order.addItem(mockProduct, 2);
            order.updateStatus(OrderStatus.CONFIRMED);

            // when
            String result = order.toString();

            // then
            assertTrue(result.contains("Подтвержден"));
        }
    }

    @Nested
    @DisplayName("Граничные случаи")
    class EdgeCaseTests {

        @Test
        @DisplayName("Создание заказа с пустым ID")
        void shouldCreateOrderWithEmptyId() {
            // given & when
            Order order = new Order("", mockCustomer);

            // then
            assertNotNull(order);
            assertEquals("", order.getId());
        }

        @Test
        @DisplayName("Создание заказа с null ID")
        void shouldCreateOrderWithNullId() {
            // given & when
            Order order = new Order(null, mockCustomer);

            // then
            assertNotNull(order);
            assertNull(order.getId());
        }

        @Test
        @DisplayName("Создание заказа с null клиентом")
        void shouldCreateOrderWithNullCustomer() {
            // given & when
            Order order = new Order("ORD-001", null);

            // then
            assertNotNull(order);
            assertNull(order.getCustomer());
        }

        @Test
        @DisplayName("Добавление товара с отрицательным количеством")
        void shouldAddItemWithNegativeQuantity() {
            // given
            Order order = new Order("ORD-001", mockCustomer);

            // when
            order.addItem(mockProduct, -5);

            // then
            assertEquals(1, order.getItems().size());
            assertEquals(-5, order.getItems().get(0).getQuantity());
        }

        @Test
        @DisplayName("Добавление товара с нулевым количеством")
        void shouldAddItemWithZeroQuantity() {
            // given
            Order order = new Order("ORD-001", mockCustomer);

            // when
            order.addItem(mockProduct, 0);

            // then
            assertEquals(1, order.getItems().size());
            assertEquals(0, order.getItems().get(0).getQuantity());
        }

        @Test
        @DisplayName("Применение отрицательной скидки")
        void shouldApplyNegativeDiscount() {
            // given
            Order order = new Order("ORD-001", mockCustomer);

            // when
            order.applyDiscount(-10.0);

            // then
            assertEquals(-10.0, order.getDiscountPercentage());
        }

        @Test
        @DisplayName("Применение скидки больше 100%")
        void shouldApplyDiscountOver100() {
            // given
            Order order = new Order("ORD-001", mockCustomer);

            // when
            order.applyDiscount(150.0);

            // then
            assertEquals(150.0, order.getDiscountPercentage());
        }

        @Test
        @DisplayName("Оплата с отрицательной суммой")
        void shouldHandleNegativePaymentAmount() {
            // given
            Order order = new Order("ORD-001", mockCustomer);
            order.addItem(mockProduct, 2);

            // when
            boolean result = order.processPayment(-100.0);

            // then
            assertFalse(result);
            assertFalse(order.isPaid());
        }

        @Test
        @DisplayName("Расчет доставки с отрицательным расстоянием")
        void shouldHandleNegativeDistance() throws InvalidDeliveryAddressException {
            // given
            Order order = new Order("ORD-001", mockCustomer);
            when(mockAddress.getLatitude()).thenReturn(-2.0);
            order.setDeliveryAddress(mockAddress);

            // when
            double cost = order.calculateDeliveryCost();

            // then
            assertEquals(100.0, cost); // -2 < 3
        }

        @Test
        @DisplayName("Добавление null товара")
        void shouldHandleNullProduct() {
            // given
            Order order = new Order("ORD-001", mockCustomer);

            // when
            order.addItem(null, 1);

            // then
            assertEquals(1, order.getItems().size());
        }
    }
}
