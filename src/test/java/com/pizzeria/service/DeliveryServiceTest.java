package com.pizzeria.service;

import com.pizzeria.exceptions.InvalidDeliveryAddressException;
import com.pizzeria.model.Address;
import com.pizzeria.model.DeliveryInfo;
import com.pizzeria.model.Order;
import com.pizzeria.model.users.DeliveryDriver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тесты для DeliveryService")
class DeliveryServiceTest {

    private DeliveryService deliveryService;

    @Mock
    private DeliveryDriver mockDriver;

    @Mock
    private Order mockOrder;

    @Mock
    private Address mockAddress;

    @Mock
    private DeliveryInfo mockDeliveryInfo;

    @BeforeEach
    void setUp() {
        deliveryService = new DeliveryService();
    }

    @Test
    @DisplayName("Добавление водителя в сервис")
    void testAddDriver_ValidDriver_Success() {
        // Arrange
        when(mockDriver.getFullName()).thenReturn("Петр Петров");

        // Act
        deliveryService.addDriver(mockDriver);

        // Assert
        DeliveryDriver foundDriver = deliveryService.findAvailableDriver();
        // Водитель будет найден если он доступен
        verify(mockDriver, times(1)).getFullName();
    }

    @Test
    @DisplayName("Добавление нескольких водителей")
    void testAddDriver_MultipleDrivers_Success() {
        // Arrange
        DeliveryDriver driver1 = mock(DeliveryDriver.class);
        DeliveryDriver driver2 = mock(DeliveryDriver.class);
        when(driver1.getFullName()).thenReturn("Водитель 1");
        when(driver2.getFullName()).thenReturn("Водитель 2");

        // Act
        deliveryService.addDriver(driver1);
        deliveryService.addDriver(driver2);

        // Assert
        verify(driver1, times(1)).getFullName();
        verify(driver2, times(1)).getFullName();
    }

    @Test
    @DisplayName("Поиск доступного водителя когда водитель доступен")
    void testFindAvailableDriver_DriverAvailable_ReturnsDriver() {
        // Arrange
        when(mockDriver.isAvailable()).thenReturn(true);
        when(mockDriver.getFullName()).thenReturn("Иван Иванов");
        deliveryService.addDriver(mockDriver);

        // Act
        DeliveryDriver foundDriver = deliveryService.findAvailableDriver();

        // Assert
        assertNotNull(foundDriver);
        assertEquals(mockDriver, foundDriver);
    }

    @Test
    @DisplayName("Поиск доступного водителя когда все водители заняты")
    void testFindAvailableDriver_NoDriversAvailable_ReturnsNull() {
        // Arrange
        when(mockDriver.isAvailable()).thenReturn(false);
        when(mockDriver.getFullName()).thenReturn("Иван Иванов");
        deliveryService.addDriver(mockDriver);

        // Act
        DeliveryDriver foundDriver = deliveryService.findAvailableDriver();

        // Assert
        assertNull(foundDriver);
    }

    @Test
    @DisplayName("Поиск доступного водителя когда нет водителей")
    void testFindAvailableDriver_NoDrivers_ReturnsNull() {
        // Act
        DeliveryDriver foundDriver = deliveryService.findAvailableDriver();

        // Assert
        assertNull(foundDriver);
    }

    @Test
    @DisplayName("Поиск доступного водителя среди нескольких")
    void testFindAvailableDriver_MultipleDrivers_ReturnsFirstAvailable() {
        // Arrange
        DeliveryDriver driver1 = mock(DeliveryDriver.class);
        DeliveryDriver driver2 = mock(DeliveryDriver.class);
        DeliveryDriver driver3 = mock(DeliveryDriver.class);

        when(driver1.isAvailable()).thenReturn(false);
        when(driver2.isAvailable()).thenReturn(true);
        when(driver3.isAvailable()).thenReturn(true);

        when(driver1.getFullName()).thenReturn("Водитель 1");
        when(driver2.getFullName()).thenReturn("Водитель 2");
        when(driver3.getFullName()).thenReturn("Водитель 3");

        deliveryService.addDriver(driver1);
        deliveryService.addDriver(driver2);
        deliveryService.addDriver(driver3);

        // Act
        DeliveryDriver foundDriver = deliveryService.findAvailableDriver();

        // Assert
        assertNotNull(foundDriver);
        assertEquals(driver2, foundDriver);
    }

    @Test
    @DisplayName("Успешное планирование доставки")
    void testScheduleDelivery_ValidOrder_Success() throws InvalidDeliveryAddressException {
        // Arrange
        when(mockOrder.getDeliveryAddress()).thenReturn(mockAddress);
        when(mockOrder.getId()).thenReturn("ORD-123");
        when(mockDriver.getFullName()).thenReturn("Петр Петров");

        // Act
        DeliveryInfo delivery = deliveryService.scheduleDelivery(mockOrder, mockDriver);

        // Assert
        assertNotNull(delivery);
        assertEquals(1, deliveryService.getActiveDeliveriesCount());
        verify(mockDriver, times(1)).startDelivery();
    }

    @Test
    @DisplayName("Планирование доставки без адреса выбрасывает исключение")
    void testScheduleDelivery_NullAddress_ThrowsException() {
        // Arrange
        when(mockOrder.getDeliveryAddress()).thenReturn(null);

        // Act & Assert
        InvalidDeliveryAddressException exception = assertThrows(InvalidDeliveryAddressException.class, () -> {
            deliveryService.scheduleDelivery(mockOrder, mockDriver);
        });

        assertTrue(exception.getMessage().contains("Адрес доставки не указан"));
        verify(mockDriver, never()).startDelivery();
    }

    @Test
    @DisplayName("Завершение доставки")
    void testCompleteDelivery_ValidDelivery_Success() throws InvalidDeliveryAddressException {
        // Arrange
        when(mockOrder.getDeliveryAddress()).thenReturn(mockAddress);
        when(mockOrder.getId()).thenReturn("ORD-123");
        when(mockDriver.getFullName()).thenReturn("Петр Петров");

        DeliveryInfo delivery = deliveryService.scheduleDelivery(mockOrder, mockDriver);
        assertEquals(1, deliveryService.getActiveDeliveriesCount());

        // Act
        deliveryService.completeDelivery(delivery);

        // Assert
        assertEquals(0, deliveryService.getActiveDeliveriesCount());
        verify(mockDriver, times(1)).completeDelivery();
    }

    @Test
    @DisplayName("Завершение доставки с моком DeliveryInfo")
    void testCompleteDelivery_MockedDeliveryInfo_Success() {
        // Arrange
        when(mockDeliveryInfo.getDriver()).thenReturn(mockDriver);
        when(mockDeliveryInfo.getOrder()).thenReturn(mockOrder);
        when(mockOrder.getId()).thenReturn("ORD-456");

        // Act
        deliveryService.completeDelivery(mockDeliveryInfo);

        // Assert
        verify(mockDeliveryInfo, times(1)).complete();
        verify(mockDriver, times(1)).completeDelivery();
    }

    @Test
    @DisplayName("Валидация корректного адреса")
    void testValidateAddress_ValidAddress_ReturnsTrue() {
        // Arrange
        when(mockAddress.getStreet()).thenReturn("Ленина");
        when(mockAddress.getHouseNumber()).thenReturn("10");
        when(mockAddress.getCity()).thenReturn("Москва");

        // Act
        boolean isValid = deliveryService.validateAddress(mockAddress);

        // Assert
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Валидация null адреса")
    void testValidateAddress_NullAddress_ReturnsFalse() {
        // Act
        boolean isValid = deliveryService.validateAddress(null);

        // Assert
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Валидация адреса без улицы")
    void testValidateAddress_NullStreet_ReturnsFalse() {
        // Arrange
        when(mockAddress.getStreet()).thenReturn(null);
        when(mockAddress.getHouseNumber()).thenReturn("10");
        when(mockAddress.getCity()).thenReturn("Москва");

        // Act
        boolean isValid = deliveryService.validateAddress(mockAddress);

        // Assert
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Валидация адреса без номера дома")
    void testValidateAddress_NullHouseNumber_ReturnsFalse() {
        // Arrange
        when(mockAddress.getStreet()).thenReturn("Ленина");
        when(mockAddress.getHouseNumber()).thenReturn(null);
        when(mockAddress.getCity()).thenReturn("Москва");

        // Act
        boolean isValid = deliveryService.validateAddress(mockAddress);

        // Assert
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Валидация адреса без города")
    void testValidateAddress_NullCity_ReturnsFalse() {
        // Arrange
        when(mockAddress.getStreet()).thenReturn("Ленина");
        when(mockAddress.getHouseNumber()).thenReturn("10");
        when(mockAddress.getCity()).thenReturn(null);

        // Act
        boolean isValid = deliveryService.validateAddress(mockAddress);

        // Assert
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Получение количества активных доставок")
    void testGetActiveDeliveriesCount_NoDeliveries_ReturnsZero() {
        // Act
        int count = deliveryService.getActiveDeliveriesCount();

        // Assert
        assertEquals(0, count);
    }

    @Test
    @DisplayName("Получение количества активных доставок после добавления")
    void testGetActiveDeliveriesCount_WithDeliveries_ReturnsCorrectCount() throws InvalidDeliveryAddressException {
        // Arrange
        when(mockOrder.getDeliveryAddress()).thenReturn(mockAddress);
        when(mockOrder.getId()).thenReturn("ORD-123");
        when(mockDriver.getFullName()).thenReturn("Петр Петров");

        // Act
        deliveryService.scheduleDelivery(mockOrder, mockDriver);
        deliveryService.scheduleDelivery(mockOrder, mockDriver);

        // Assert
        assertEquals(2, deliveryService.getActiveDeliveriesCount());
    }
}
