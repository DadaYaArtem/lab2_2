package com.pizzeria.util;

import com.pizzeria.model.Address;
import com.pizzeria.model.Order;
import com.pizzeria.model.OrderItem;
import com.pizzeria.model.products.Product;
import com.pizzeria.model.users.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тесты для OrderValidator")
class OrderValidatorTest {

    @Mock
    private Order mockOrder;

    @Mock
    private Customer mockCustomer;

    @Mock
    private OrderItem mockOrderItem;

    @Mock
    private Product mockProduct;

    @Mock
    private Address mockAddress;

    private List<OrderItem> orderItems;

    @BeforeEach
    void setUp() {
        orderItems = new ArrayList<>();
    }

    @Test
    @DisplayName("Валидация null заказа возвращает false")
    void testValidateOrder_NullOrder_ReturnsFalse() {
        // Act
        boolean result = OrderValidator.validateOrder(null);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Валидация заказа без клиента возвращает false")
    void testValidateOrder_NullCustomer_ReturnsFalse() {
        // Arrange
        when(mockOrder.getCustomer()).thenReturn(null);

        // Act
        boolean result = OrderValidator.validateOrder(mockOrder);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Валидация заказа без товаров возвращает false")
    void testValidateOrder_EmptyItems_ReturnsFalse() {
        // Arrange
        when(mockOrder.getCustomer()).thenReturn(mockCustomer);
        when(mockOrder.getItems()).thenReturn(orderItems);

        // Act
        boolean result = OrderValidator.validateOrder(mockOrder);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Валидация заказа с null продуктом возвращает false")
    void testValidateOrder_NullProduct_ReturnsFalse() {
        // Arrange
        when(mockOrder.getCustomer()).thenReturn(mockCustomer);
        when(mockOrderItem.getProduct()).thenReturn(null);
        orderItems.add(mockOrderItem);
        when(mockOrder.getItems()).thenReturn(orderItems);

        // Act
        boolean result = OrderValidator.validateOrder(mockOrder);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Валидация заказа с нулевым количеством возвращает false")
    void testValidateOrder_ZeroQuantity_ReturnsFalse() {
        // Arrange
        when(mockOrder.getCustomer()).thenReturn(mockCustomer);
        when(mockOrderItem.getProduct()).thenReturn(mockProduct);
        when(mockOrderItem.getQuantity()).thenReturn(0);
        orderItems.add(mockOrderItem);
        when(mockOrder.getItems()).thenReturn(orderItems);

        // Act
        boolean result = OrderValidator.validateOrder(mockOrder);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Валидация заказа с отрицательным количеством возвращает false")
    void testValidateOrder_NegativeQuantity_ReturnsFalse() {
        // Arrange
        when(mockOrder.getCustomer()).thenReturn(mockCustomer);
        when(mockOrderItem.getProduct()).thenReturn(mockProduct);
        when(mockOrderItem.getQuantity()).thenReturn(-1);
        orderItems.add(mockOrderItem);
        when(mockOrder.getItems()).thenReturn(orderItems);

        // Act
        boolean result = OrderValidator.validateOrder(mockOrder);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Валидация заказа с недоступным продуктом возвращает false")
    void testValidateOrder_UnavailableProduct_ReturnsFalse() {
        // Arrange
        when(mockOrder.getCustomer()).thenReturn(mockCustomer);
        when(mockOrderItem.getProduct()).thenReturn(mockProduct);
        when(mockOrderItem.getQuantity()).thenReturn(1);
        when(mockProduct.isAvailable()).thenReturn(false);
        when(mockProduct.getName()).thenReturn("Пицца Маргарита");
        orderItems.add(mockOrderItem);
        when(mockOrder.getItems()).thenReturn(orderItems);

        // Act
        boolean result = OrderValidator.validateOrder(mockOrder);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Валидация корректного заказа возвращает true")
    void testValidateOrder_ValidOrder_ReturnsTrue() {
        // Arrange
        when(mockOrder.getCustomer()).thenReturn(mockCustomer);
        when(mockOrderItem.getProduct()).thenReturn(mockProduct);
        when(mockOrderItem.getQuantity()).thenReturn(1);
        when(mockProduct.isAvailable()).thenReturn(true);
        orderItems.add(mockOrderItem);
        when(mockOrder.getItems()).thenReturn(orderItems);

        // Act
        boolean result = OrderValidator.validateOrder(mockOrder);

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("Валидация заказа с несколькими товарами")
    void testValidateOrder_MultipleValidItems_ReturnsTrue() {
        // Arrange
        OrderItem item1 = mock(OrderItem.class);
        OrderItem item2 = mock(OrderItem.class);
        Product product1 = mock(Product.class);
        Product product2 = mock(Product.class);

        when(mockOrder.getCustomer()).thenReturn(mockCustomer);

        when(item1.getProduct()).thenReturn(product1);
        when(item1.getQuantity()).thenReturn(2);
        when(product1.isAvailable()).thenReturn(true);

        when(item2.getProduct()).thenReturn(product2);
        when(item2.getQuantity()).thenReturn(3);
        when(product2.isAvailable()).thenReturn(true);

        orderItems.add(item1);
        orderItems.add(item2);
        when(mockOrder.getItems()).thenReturn(orderItems);

        // Act
        boolean result = OrderValidator.validateOrder(mockOrder);

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("Валидация заказа если один из нескольких товаров недоступен")
    void testValidateOrder_MultipleItemsOneUnavailable_ReturnsFalse() {
        // Arrange
        OrderItem item1 = mock(OrderItem.class);
        OrderItem item2 = mock(OrderItem.class);
        Product product1 = mock(Product.class);
        Product product2 = mock(Product.class);

        when(mockOrder.getCustomer()).thenReturn(mockCustomer);

        when(item1.getProduct()).thenReturn(product1);
        when(item1.getQuantity()).thenReturn(2);
        when(product1.isAvailable()).thenReturn(true);

        when(item2.getProduct()).thenReturn(product2);
        when(item2.getQuantity()).thenReturn(3);
        when(product2.isAvailable()).thenReturn(false);
        when(product2.getName()).thenReturn("Недоступный продукт");

        orderItems.add(item1);
        orderItems.add(item2);
        when(mockOrder.getItems()).thenReturn(orderItems);

        // Act
        boolean result = OrderValidator.validateOrder(mockOrder);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Валидация минимальной суммы заказа - сумма больше минимума")
    void testValidateMinimumOrderAmount_AmountAboveMinimum_ReturnsTrue() {
        // Arrange
        when(mockOrder.getPrice()).thenReturn(500.0);
        double minimumAmount = 300.0;

        // Act
        boolean result = OrderValidator.validateMinimumOrderAmount(mockOrder, minimumAmount);

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("Валидация минимальной суммы заказа - сумма равна минимуму")
    void testValidateMinimumOrderAmount_AmountEqualsMinimum_ReturnsTrue() {
        // Arrange
        when(mockOrder.getPrice()).thenReturn(300.0);
        double minimumAmount = 300.0;

        // Act
        boolean result = OrderValidator.validateMinimumOrderAmount(mockOrder, minimumAmount);

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("Валидация минимальной суммы заказа - сумма меньше минимума")
    void testValidateMinimumOrderAmount_AmountBelowMinimum_ReturnsFalse() {
        // Arrange
        when(mockOrder.getPrice()).thenReturn(200.0);
        double minimumAmount = 300.0;

        // Act
        boolean result = OrderValidator.validateMinimumOrderAmount(mockOrder, minimumAmount);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Валидация минимальной суммы заказа - нулевой минимум")
    void testValidateMinimumOrderAmount_ZeroMinimum_ReturnsTrue() {
        // Arrange
        when(mockOrder.getPrice()).thenReturn(100.0);
        double minimumAmount = 0.0;

        // Act
        boolean result = OrderValidator.validateMinimumOrderAmount(mockOrder, minimumAmount);

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("Валидация адреса доставки - адрес указан")
    void testValidateDeliveryAddress_AddressPresent_ReturnsTrue() {
        // Arrange
        when(mockOrder.getDeliveryAddress()).thenReturn(mockAddress);

        // Act
        boolean result = OrderValidator.validateDeliveryAddress(mockOrder);

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("Валидация адреса доставки - адрес не указан")
    void testValidateDeliveryAddress_AddressNull_ReturnsFalse() {
        // Arrange
        when(mockOrder.getDeliveryAddress()).thenReturn(null);

        // Act
        boolean result = OrderValidator.validateDeliveryAddress(mockOrder);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Валидация заказа с большим количеством товаров")
    void testValidateOrder_LargeQuantity_ReturnsTrue() {
        // Arrange
        when(mockOrder.getCustomer()).thenReturn(mockCustomer);
        when(mockOrderItem.getProduct()).thenReturn(mockProduct);
        when(mockOrderItem.getQuantity()).thenReturn(100);
        when(mockProduct.isAvailable()).thenReturn(true);
        orderItems.add(mockOrderItem);
        when(mockOrder.getItems()).thenReturn(orderItems);

        // Act
        boolean result = OrderValidator.validateOrder(mockOrder);

        // Assert
        assertTrue(result);
    }
}
