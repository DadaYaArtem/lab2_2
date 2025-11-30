package com.pizzeria.model;

import com.pizzeria.enums.OrderStatus;
import com.pizzeria.model.users.DeliveryDriver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.mockito.Mockito;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Тесты для класса DeliveryInfo")
class DeliveryInfoTest {

    private Order mockOrder;
    private DeliveryDriver mockDriver;

    @BeforeEach
    void setUp() {
        mockOrder = Mockito.mock(Order.class);
        mockDriver = Mockito.mock(DeliveryDriver.class);

        when(mockOrder.calculateDeliveryTime()).thenReturn(45);
        when(mockDriver.getFullName()).thenReturn("Петр Петров");
    }

    @Nested
    @DisplayName("Тесты конструктора")
    class ConstructorTests {

        @Test
        @DisplayName("Успешное создание информации о доставке")
        void shouldCreateDeliveryInfoWithValidData() {
            // given & when
            DeliveryInfo deliveryInfo = new DeliveryInfo(mockOrder, mockDriver);

            // then
            assertNotNull(deliveryInfo);
            assertEquals(mockOrder, deliveryInfo.getOrder());
            assertEquals(mockDriver, deliveryInfo.getDriver());
            assertNotNull(deliveryInfo.getDispatchTime());
            assertNull(deliveryInfo.getDeliveryTime());
            assertEquals(45, deliveryInfo.getEstimatedTime());
            assertNotNull(deliveryInfo.getTrackingNumber());
        }

        @Test
        @DisplayName("Время отправки устанавливается при создании")
        void shouldSetDispatchTimeOnCreation() {
            // given
            LocalDateTime before = LocalDateTime.now();

            // when
            DeliveryInfo deliveryInfo = new DeliveryInfo(mockOrder, mockDriver);

            // then
            LocalDateTime after = LocalDateTime.now();
            assertTrue(!deliveryInfo.getDispatchTime().isBefore(before));
            assertTrue(!deliveryInfo.getDispatchTime().isAfter(after));
        }

        @Test
        @DisplayName("Расчетное время устанавливается из заказа")
        void shouldSetEstimatedTimeFromOrder() {
            // given
            when(mockOrder.calculateDeliveryTime()).thenReturn(60);

            // when
            DeliveryInfo deliveryInfo = new DeliveryInfo(mockOrder, mockDriver);

            // then
            assertEquals(60, deliveryInfo.getEstimatedTime());
        }

        @Test
        @DisplayName("Генерация номера отслеживания")
        void shouldGenerateTrackingNumber() {
            // given & when
            DeliveryInfo deliveryInfo = new DeliveryInfo(mockOrder, mockDriver);

            // then
            assertNotNull(deliveryInfo.getTrackingNumber());
            assertTrue(deliveryInfo.getTrackingNumber().startsWith("TRK-"));
        }

        @Test
        @DisplayName("Уникальные номера отслеживания для разных доставок")
        void shouldGenerateUniqueTrackingNumbers() throws InterruptedException {
            // given & when
            DeliveryInfo deliveryInfo1 = new DeliveryInfo(mockOrder, mockDriver);
            Thread.sleep(1); // гарантируем разные временные метки
            DeliveryInfo deliveryInfo2 = new DeliveryInfo(mockOrder, mockDriver);

            // then
            assertNotEquals(deliveryInfo1.getTrackingNumber(), deliveryInfo2.getTrackingNumber());
        }
    }

    @Nested
    @DisplayName("Тесты метода complete")
    class CompleteTests {

        @Test
        @DisplayName("Завершение доставки устанавливает время доставки")
        void shouldSetDeliveryTimeWhenCompleted() {
            // given
            DeliveryInfo deliveryInfo = new DeliveryInfo(mockOrder, mockDriver);
            assertNull(deliveryInfo.getDeliveryTime());

            // when
            deliveryInfo.complete();

            // then
            assertNotNull(deliveryInfo.getDeliveryTime());
        }

        @Test
        @DisplayName("Завершение доставки обновляет статус заказа")
        void shouldUpdateOrderStatusWhenCompleted() {
            // given
            DeliveryInfo deliveryInfo = new DeliveryInfo(mockOrder, mockDriver);

            // when
            deliveryInfo.complete();

            // then
            verify(mockOrder, times(1)).updateStatus(OrderStatus.DELIVERED);
        }

        @Test
        @DisplayName("Время доставки после времени отправки")
        void shouldSetDeliveryTimeAfterDispatchTime() {
            // given
            DeliveryInfo deliveryInfo = new DeliveryInfo(mockOrder, mockDriver);
            LocalDateTime dispatchTime = deliveryInfo.getDispatchTime();

            // when
            deliveryInfo.complete();

            // then
            LocalDateTime deliveryTime = deliveryInfo.getDeliveryTime();
            assertTrue(!deliveryTime.isBefore(dispatchTime));
        }

        @Test
        @DisplayName("Повторное завершение обновляет время доставки")
        void shouldUpdateDeliveryTimeOnRepeatedComplete() throws InterruptedException {
            // given
            DeliveryInfo deliveryInfo = new DeliveryInfo(mockOrder, mockDriver);
            deliveryInfo.complete();
            LocalDateTime firstDeliveryTime = deliveryInfo.getDeliveryTime();

            // when
            Thread.sleep(1);
            deliveryInfo.complete();

            // then
            LocalDateTime secondDeliveryTime = deliveryInfo.getDeliveryTime();
            assertTrue(secondDeliveryTime.isAfter(firstDeliveryTime) ||
                      secondDeliveryTime.isEqual(firstDeliveryTime));
        }
    }

    @Nested
    @DisplayName("Тесты метода getActualDeliveryTime")
    class GetActualDeliveryTimeTests {

        @Test
        @DisplayName("Возвращает 0 если доставка не завершена")
        void shouldReturn0WhenDeliveryNotCompleted() {
            // given
            DeliveryInfo deliveryInfo = new DeliveryInfo(mockOrder, mockDriver);

            // when
            int actualTime = deliveryInfo.getActualDeliveryTime();

            // then
            assertEquals(0, actualTime);
        }

        @Test
        @DisplayName("Возвращает фактическое время после завершения")
        void shouldReturnActualTimeWhenCompleted() {
            // given
            DeliveryInfo deliveryInfo = new DeliveryInfo(mockOrder, mockDriver);

            // when
            deliveryInfo.complete();
            int actualTime = deliveryInfo.getActualDeliveryTime();

            // then
            assertTrue(actualTime >= 0);
        }

        @Test
        @DisplayName("Фактическое время рассчитывается в минутах")
        void shouldCalculateTimeInMinutes() throws InterruptedException {
            // given
            DeliveryInfo deliveryInfo = new DeliveryInfo(mockOrder, mockDriver);
            LocalDateTime customDispatch = LocalDateTime.now().minusMinutes(30);
            deliveryInfo.setDispatchTime(customDispatch);

            // when
            deliveryInfo.complete();
            int actualTime = deliveryInfo.getActualDeliveryTime();

            // then
            assertTrue(actualTime >= 30);
        }
    }

    @Nested
    @DisplayName("Тесты метода isOnTime")
    class IsOnTimeTests {

        @Test
        @DisplayName("Доставка вовремя если фактическое время меньше расчетного")
        void shouldBeOnTimeWhenActualLessThanEstimated() {
            // given
            DeliveryInfo deliveryInfo = new DeliveryInfo(mockOrder, mockDriver);
            deliveryInfo.setEstimatedTime(60);
            deliveryInfo.setDispatchTime(LocalDateTime.now().minusMinutes(30));

            // when
            deliveryInfo.complete();
            boolean onTime = deliveryInfo.isOnTime();

            // then
            assertTrue(onTime);
        }

        @Test
        @DisplayName("Доставка вовремя если фактическое время равно расчетному")
        void shouldBeOnTimeWhenActualEqualsEstimated() {
            // given
            DeliveryInfo deliveryInfo = new DeliveryInfo(mockOrder, mockDriver);
            deliveryInfo.setEstimatedTime(30);
            deliveryInfo.setDispatchTime(LocalDateTime.now().minusMinutes(30));

            // when
            deliveryInfo.complete();
            boolean onTime = deliveryInfo.isOnTime();

            // then
            assertTrue(onTime);
        }

        @Test
        @DisplayName("Доставка не вовремя если фактическое время больше расчетного")
        void shouldNotBeOnTimeWhenActualGreaterThanEstimated() {
            // given
            DeliveryInfo deliveryInfo = new DeliveryInfo(mockOrder, mockDriver);
            deliveryInfo.setEstimatedTime(30);
            deliveryInfo.setDispatchTime(LocalDateTime.now().minusMinutes(60));

            // when
            deliveryInfo.complete();
            boolean onTime = deliveryInfo.isOnTime();

            // then
            assertFalse(onTime);
        }

        @Test
        @DisplayName("Возвращает true если доставка не завершена")
        void shouldReturnTrueWhenDeliveryNotCompleted() {
            // given
            DeliveryInfo deliveryInfo = new DeliveryInfo(mockOrder, mockDriver);

            // when
            boolean onTime = deliveryInfo.isOnTime();

            // then
            assertTrue(onTime); // getActualDeliveryTime() возвращает 0, что <= estimatedTime
        }
    }

    @Nested
    @DisplayName("Тесты геттеров и сеттеров")
    class GetterSetterTests {

        @Test
        @DisplayName("Установка и получение заказа")
        void shouldSetAndGetOrder() {
            // given
            DeliveryInfo deliveryInfo = new DeliveryInfo(mockOrder, mockDriver);
            Order newOrder = Mockito.mock(Order.class);

            // when
            deliveryInfo.setOrder(newOrder);

            // then
            assertEquals(newOrder, deliveryInfo.getOrder());
        }

        @Test
        @DisplayName("Установка и получение водителя")
        void shouldSetAndGetDriver() {
            // given
            DeliveryInfo deliveryInfo = new DeliveryInfo(mockOrder, mockDriver);
            DeliveryDriver newDriver = Mockito.mock(DeliveryDriver.class);

            // when
            deliveryInfo.setDriver(newDriver);

            // then
            assertEquals(newDriver, deliveryInfo.getDriver());
        }

        @Test
        @DisplayName("Установка и получение времени отправки")
        void shouldSetAndGetDispatchTime() {
            // given
            DeliveryInfo deliveryInfo = new DeliveryInfo(mockOrder, mockDriver);
            LocalDateTime newTime = LocalDateTime.of(2024, 6, 15, 10, 0);

            // when
            deliveryInfo.setDispatchTime(newTime);

            // then
            assertEquals(newTime, deliveryInfo.getDispatchTime());
        }

        @Test
        @DisplayName("Установка и получение времени доставки")
        void shouldSetAndGetDeliveryTime() {
            // given
            DeliveryInfo deliveryInfo = new DeliveryInfo(mockOrder, mockDriver);
            LocalDateTime newTime = LocalDateTime.of(2024, 6, 15, 11, 0);

            // when
            deliveryInfo.setDeliveryTime(newTime);

            // then
            assertEquals(newTime, deliveryInfo.getDeliveryTime());
        }

        @Test
        @DisplayName("Установка и получение расчетного времени")
        void shouldSetAndGetEstimatedTime() {
            // given
            DeliveryInfo deliveryInfo = new DeliveryInfo(mockOrder, mockDriver);

            // when
            deliveryInfo.setEstimatedTime(90);

            // then
            assertEquals(90, deliveryInfo.getEstimatedTime());
        }

        @Test
        @DisplayName("Установка и получение номера отслеживания")
        void shouldSetAndGetTrackingNumber() {
            // given
            DeliveryInfo deliveryInfo = new DeliveryInfo(mockOrder, mockDriver);

            // when
            deliveryInfo.setTrackingNumber("CUSTOM-TRACK-123");

            // then
            assertEquals("CUSTOM-TRACK-123", deliveryInfo.getTrackingNumber());
        }
    }

    @Nested
    @DisplayName("Граничные случаи")
    class EdgeCaseTests {

        @Test
        @DisplayName("Создание с нулевым расчетным временем")
        void shouldHandleZeroEstimatedTime() {
            // given
            when(mockOrder.calculateDeliveryTime()).thenReturn(0);

            // when
            DeliveryInfo deliveryInfo = new DeliveryInfo(mockOrder, mockDriver);

            // then
            assertEquals(0, deliveryInfo.getEstimatedTime());
        }

        @Test
        @DisplayName("Создание с отрицательным расчетным временем")
        void shouldHandleNegativeEstimatedTime() {
            // given
            when(mockOrder.calculateDeliveryTime()).thenReturn(-10);

            // when
            DeliveryInfo deliveryInfo = new DeliveryInfo(mockOrder, mockDriver);

            // then
            assertEquals(-10, deliveryInfo.getEstimatedTime());
        }

        @Test
        @DisplayName("Создание с очень большим расчетным временем")
        void shouldHandleVeryLargeEstimatedTime() {
            // given
            when(mockOrder.calculateDeliveryTime()).thenReturn(Integer.MAX_VALUE);

            // when
            DeliveryInfo deliveryInfo = new DeliveryInfo(mockOrder, mockDriver);

            // then
            assertEquals(Integer.MAX_VALUE, deliveryInfo.getEstimatedTime());
        }

        @Test
        @DisplayName("Установка времени доставки в прошлом относительно отправки")
        void shouldHandleDeliveryTimeBeforeDispatchTime() {
            // given
            DeliveryInfo deliveryInfo = new DeliveryInfo(mockOrder, mockDriver);
            LocalDateTime dispatchTime = LocalDateTime.now();
            deliveryInfo.setDispatchTime(dispatchTime);

            // when
            deliveryInfo.setDeliveryTime(dispatchTime.minusHours(1));

            // then
            assertTrue(deliveryInfo.getDeliveryTime().isBefore(deliveryInfo.getDispatchTime()));
            // getActualDeliveryTime будет отрицательным
        }

        @Test
        @DisplayName("Создание с null заказом")
        void shouldHandleNullOrder() {
            // given & when
            DeliveryInfo deliveryInfo = new DeliveryInfo(null, mockDriver);

            // then
            assertNull(deliveryInfo.getOrder());
        }

        @Test
        @DisplayName("Создание с null водителем")
        void shouldHandleNullDriver() {
            // given & when
            DeliveryInfo deliveryInfo = new DeliveryInfo(mockOrder, null);

            // then
            assertNull(deliveryInfo.getDriver());
        }

        @Test
        @DisplayName("Установка отрицательного расчетного времени")
        void shouldAllowNegativeEstimatedTime() {
            // given
            DeliveryInfo deliveryInfo = new DeliveryInfo(mockOrder, mockDriver);

            // when
            deliveryInfo.setEstimatedTime(-30);

            // then
            assertEquals(-30, deliveryInfo.getEstimatedTime());
        }

        @Test
        @DisplayName("Несколько завершений доставки")
        void shouldHandleMultipleCompletions() {
            // given
            DeliveryInfo deliveryInfo = new DeliveryInfo(mockOrder, mockDriver);

            // when
            deliveryInfo.complete();
            deliveryInfo.complete();
            deliveryInfo.complete();

            // then
            verify(mockOrder, times(3)).updateStatus(OrderStatus.DELIVERED);
            assertNotNull(deliveryInfo.getDeliveryTime());
        }

        @Test
        @DisplayName("Пустой номер отслеживания")
        void shouldAllowEmptyTrackingNumber() {
            // given
            DeliveryInfo deliveryInfo = new DeliveryInfo(mockOrder, mockDriver);

            // when
            deliveryInfo.setTrackingNumber("");

            // then
            assertEquals("", deliveryInfo.getTrackingNumber());
        }

        @Test
        @DisplayName("Null номер отслеживания")
        void shouldAllowNullTrackingNumber() {
            // given
            DeliveryInfo deliveryInfo = new DeliveryInfo(mockOrder, mockDriver);

            // when
            deliveryInfo.setTrackingNumber(null);

            // then
            assertNull(deliveryInfo.getTrackingNumber());
        }
    }
}
