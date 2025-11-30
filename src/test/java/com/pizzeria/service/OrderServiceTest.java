package com.pizzeria.service;

import com.pizzeria.enums.OrderStatus;
import com.pizzeria.exceptions.DuplicateOrderException;
import com.pizzeria.exceptions.OrderNotFoundException;
import com.pizzeria.model.Order;
import com.pizzeria.model.users.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тесты для OrderService")
class OrderServiceTest {

    private OrderService orderService;

    @Mock
    private Customer mockCustomer;

    @BeforeEach
    void setUp() {
        orderService = new OrderService();
        when(mockCustomer.getFullName()).thenReturn("Иван Иванов");
    }

    @Test
    @DisplayName("Создание заказа с валидным клиентом")
    void testCreateOrder_ValidCustomer_Success() throws DuplicateOrderException {
        // Act
        Order order = orderService.createOrder(mockCustomer);

        // Assert
        assertNotNull(order);
        assertEquals("ORD-1", order.getId());
        assertEquals(mockCustomer, order.getCustomer());
        verify(mockCustomer, times(1)).addToOrderHistory("ORD-1");
    }

    @Test
    @DisplayName("Создание нескольких заказов увеличивает счетчик")
    void testCreateOrder_MultipleOrders_IncrementCounter() throws DuplicateOrderException {
        // Act
        Order order1 = orderService.createOrder(mockCustomer);
        Order order2 = orderService.createOrder(mockCustomer);
        Order order3 = orderService.createOrder(mockCustomer);

        // Assert
        assertEquals("ORD-1", order1.getId());
        assertEquals("ORD-2", order2.getId());
        assertEquals("ORD-3", order3.getId());
        assertEquals(3, orderService.getOrderCount());
    }

    @Test
    @DisplayName("Получение существующего заказа")
    void testGetOrder_ExistingOrder_ReturnsOrder() throws DuplicateOrderException, OrderNotFoundException {
        // Arrange
        Order createdOrder = orderService.createOrder(mockCustomer);

        // Act
        Order retrievedOrder = orderService.getOrder("ORD-1");

        // Assert
        assertNotNull(retrievedOrder);
        assertEquals(createdOrder, retrievedOrder);
    }

    @Test
    @DisplayName("Получение несуществующего заказа выбрасывает исключение")
    void testGetOrder_NonExistingOrder_ThrowsException() {
        // Act & Assert
        assertThrows(OrderNotFoundException.class, () -> {
            orderService.getOrder("ORD-999");
        });
    }

    @Test
    @DisplayName("Отмена заказа обновляет статус на CANCELLED")
    void testCancelOrder_ValidOrder_UpdatesStatus() throws DuplicateOrderException, OrderNotFoundException {
        // Arrange
        Order order = orderService.createOrder(mockCustomer);

        // Act
        orderService.cancelOrder("ORD-1");

        // Assert
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
    }

    @Test
    @DisplayName("Отмена несуществующего заказа выбрасывает исключение")
    void testCancelOrder_NonExistingOrder_ThrowsException() {
        // Act & Assert
        assertThrows(OrderNotFoundException.class, () -> {
            orderService.cancelOrder("ORD-999");
        });
    }

    @Test
    @DisplayName("Обновление статуса заказа")
    void testUpdateOrderStatus_ValidOrder_UpdatesStatus() throws DuplicateOrderException, OrderNotFoundException {
        // Arrange
        Order order = orderService.createOrder(mockCustomer);

        // Act
        orderService.updateOrderStatus("ORD-1", OrderStatus.IN_PROGRESS);

        // Assert
        assertEquals(OrderStatus.IN_PROGRESS, order.getStatus());
    }

    @Test
    @DisplayName("Обновление статуса несуществующего заказа выбрасывает исключение")
    void testUpdateOrderStatus_NonExistingOrder_ThrowsException() {
        // Act & Assert
        assertThrows(OrderNotFoundException.class, () -> {
            orderService.updateOrderStatus("ORD-999", OrderStatus.COMPLETED);
        });
    }

    @Test
    @DisplayName("Расчет общей выручки для оплаченных заказов")
    void testCalculateTotalRevenue_PaidOrders_ReturnsCorrectSum() throws DuplicateOrderException {
        // Arrange
        Order order1 = orderService.createOrder(mockCustomer);
        Order order2 = orderService.createOrder(mockCustomer);
        Order order3 = orderService.createOrder(mockCustomer);

        // Мокаем методы заказов
        Order spyOrder1 = spy(order1);
        Order spyOrder2 = spy(order2);
        Order spyOrder3 = spy(order3);

        when(spyOrder1.isPaid()).thenReturn(true);
        when(spyOrder1.getFinalPrice()).thenReturn(100.0);
        when(spyOrder2.isPaid()).thenReturn(true);
        when(spyOrder2.getFinalPrice()).thenReturn(200.0);
        when(spyOrder3.isPaid()).thenReturn(false);
        when(spyOrder3.getFinalPrice()).thenReturn(300.0);

        // Подменяем заказы в сервисе
        Map<String, Order> orders = orderService.getAllOrders();
        orders.put("ORD-1", spyOrder1);
        orders.put("ORD-2", spyOrder2);
        orders.put("ORD-3", spyOrder3);

        // Используем рефлексию чтобы обновить внутреннее состояние
        try {
            java.lang.reflect.Field field = OrderService.class.getDeclaredField("orders");
            field.setAccessible(true);
            field.set(orderService, orders);
        } catch (Exception e) {
            fail("Не удалось настроить тест: " + e.getMessage());
        }

        // Act
        double revenue = orderService.calculateTotalRevenue();

        // Assert
        assertEquals(300.0, revenue);
    }

    @Test
    @DisplayName("Расчет выручки для пустого списка заказов")
    void testCalculateTotalRevenue_NoOrders_ReturnsZero() {
        // Act
        double revenue = orderService.calculateTotalRevenue();

        // Assert
        assertEquals(0.0, revenue);
    }

    @Test
    @DisplayName("Получение количества заказов")
    void testGetOrderCount_ReturnsCorrectCount() throws DuplicateOrderException {
        // Arrange
        assertEquals(0, orderService.getOrderCount());

        // Act
        orderService.createOrder(mockCustomer);
        orderService.createOrder(mockCustomer);

        // Assert
        assertEquals(2, orderService.getOrderCount());
    }

    @Test
    @DisplayName("Получение всех заказов возвращает копию")
    void testGetAllOrders_ReturnsNewMap() throws DuplicateOrderException {
        // Arrange
        orderService.createOrder(mockCustomer);

        // Act
        Map<String, Order> orders1 = orderService.getAllOrders();
        Map<String, Order> orders2 = orderService.getAllOrders();

        // Assert
        assertNotSame(orders1, orders2);
        assertEquals(orders1.size(), orders2.size());
    }

    @Test
    @DisplayName("Изменение возвращенной карты не влияет на внутреннее состояние")
    void testGetAllOrders_ModificationDoesNotAffectInternalState() throws DuplicateOrderException {
        // Arrange
        orderService.createOrder(mockCustomer);

        // Act
        Map<String, Order> orders = orderService.getAllOrders();
        orders.clear();

        // Assert
        assertEquals(1, orderService.getOrderCount());
    }
}
