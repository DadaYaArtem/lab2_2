package com.pizzeria.model;

import com.pizzeria.exceptions.InvalidDeliveryAddressException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты для класса Address")
class AddressTest {

    @Nested
    @DisplayName("Тесты конструктора")
    class ConstructorTests {

        @Test
        @DisplayName("Успешное создание адреса с валидными данными")
        void shouldCreateAddressWithValidData() throws InvalidDeliveryAddressException {
            // given & when
            Address address = new Address("Ленина", "15", "Москва", "123456");

            // then
            assertNotNull(address);
            assertEquals("Ленина", address.getStreet());
            assertEquals("15", address.getHouseNumber());
            assertEquals("Москва", address.getCity());
            assertEquals("123456", address.getPostalCode());
        }

        @Test
        @DisplayName("Исключение при пустой улице")
        void shouldThrowExceptionWhenStreetIsEmpty() {
            // when & then
            InvalidDeliveryAddressException exception = assertThrows(
                InvalidDeliveryAddressException.class,
                () -> new Address("", "15", "Москва", "123456")
            );
            assertTrue(exception.getMessage().contains("Улица не может быть пустой"));
        }

        @Test
        @DisplayName("Исключение при null улице")
        void shouldThrowExceptionWhenStreetIsNull() {
            // when & then
            InvalidDeliveryAddressException exception = assertThrows(
                InvalidDeliveryAddressException.class,
                () -> new Address(null, "15", "Москва", "123456")
            );
            assertTrue(exception.getMessage().contains("Улица не может быть пустой"));
        }

        @Test
        @DisplayName("Исключение при улице из пробелов")
        void shouldThrowExceptionWhenStreetIsWhitespace() {
            // when & then
            assertThrows(
                InvalidDeliveryAddressException.class,
                () -> new Address("   ", "15", "Москва", "123456")
            );
        }

        @Test
        @DisplayName("Исключение при пустом номере дома")
        void shouldThrowExceptionWhenHouseNumberIsEmpty() {
            // when & then
            InvalidDeliveryAddressException exception = assertThrows(
                InvalidDeliveryAddressException.class,
                () -> new Address("Ленина", "", "Москва", "123456")
            );
            assertTrue(exception.getMessage().contains("Номер дома не может быть пустым"));
        }

        @Test
        @DisplayName("Исключение при null номере дома")
        void shouldThrowExceptionWhenHouseNumberIsNull() {
            // when & then
            InvalidDeliveryAddressException exception = assertThrows(
                InvalidDeliveryAddressException.class,
                () -> new Address("Ленина", null, "Москва", "123456")
            );
            assertTrue(exception.getMessage().contains("Номер дома не может быть пустым"));
        }

        @Test
        @DisplayName("Исключение при пустом городе")
        void shouldThrowExceptionWhenCityIsEmpty() {
            // when & then
            InvalidDeliveryAddressException exception = assertThrows(
                InvalidDeliveryAddressException.class,
                () -> new Address("Ленина", "15", "", "123456")
            );
            assertTrue(exception.getMessage().contains("Город не может быть пустым"));
        }

        @Test
        @DisplayName("Исключение при null городе")
        void shouldThrowExceptionWhenCityIsNull() {
            // when & then
            InvalidDeliveryAddressException exception = assertThrows(
                InvalidDeliveryAddressException.class,
                () -> new Address("Ленина", "15", null, "123456")
            );
            assertTrue(exception.getMessage().contains("Город не может быть пустым"));
        }

        @Test
        @DisplayName("Создание адреса с null почтовым индексом")
        void shouldCreateAddressWithNullPostalCode() throws InvalidDeliveryAddressException {
            // given & when
            Address address = new Address("Ленина", "15", "Москва", null);

            // then
            assertNotNull(address);
            assertNull(address.getPostalCode());
        }
    }

    @Nested
    @DisplayName("Тесты геттеров и сеттеров")
    class GetterSetterTests {

        @Test
        @DisplayName("Установка и получение улицы")
        void shouldSetAndGetStreet() throws InvalidDeliveryAddressException {
            // given
            Address address = new Address("Ленина", "15", "Москва", "123456");

            // when
            address.setStreet("Пушкина");

            // then
            assertEquals("Пушкина", address.getStreet());
        }

        @Test
        @DisplayName("Установка и получение номера дома")
        void shouldSetAndGetHouseNumber() throws InvalidDeliveryAddressException {
            // given
            Address address = new Address("Ленина", "15", "Москва", "123456");

            // when
            address.setHouseNumber("20A");

            // then
            assertEquals("20A", address.getHouseNumber());
        }

        @Test
        @DisplayName("Установка и получение номера квартиры")
        void shouldSetAndGetApartmentNumber() throws InvalidDeliveryAddressException {
            // given
            Address address = new Address("Ленина", "15", "Москва", "123456");

            // when
            address.setApartmentNumber("42");

            // then
            assertEquals("42", address.getApartmentNumber());
        }

        @Test
        @DisplayName("Установка и получение города")
        void shouldSetAndGetCity() throws InvalidDeliveryAddressException {
            // given
            Address address = new Address("Ленина", "15", "Москва", "123456");

            // when
            address.setCity("Санкт-Петербург");

            // then
            assertEquals("Санкт-Петербург", address.getCity());
        }

        @Test
        @DisplayName("Установка и получение почтового индекса")
        void shouldSetAndGetPostalCode() throws InvalidDeliveryAddressException {
            // given
            Address address = new Address("Ленина", "15", "Москва", "123456");

            // when
            address.setPostalCode("654321");

            // then
            assertEquals("654321", address.getPostalCode());
        }

        @Test
        @DisplayName("Установка и получение широты")
        void shouldSetAndGetLatitude() throws InvalidDeliveryAddressException {
            // given
            Address address = new Address("Ленина", "15", "Москва", "123456");

            // when
            address.setLatitude(55.7558);

            // then
            assertEquals(55.7558, address.getLatitude(), 0.0001);
        }

        @Test
        @DisplayName("Установка и получение долготы")
        void shouldSetAndGetLongitude() throws InvalidDeliveryAddressException {
            // given
            Address address = new Address("Ленина", "15", "Москва", "123456");

            // when
            address.setLongitude(37.6173);

            // then
            assertEquals(37.6173, address.getLongitude(), 0.0001);
        }
    }

    @Nested
    @DisplayName("Тесты расчета расстояния")
    class CalculateDistanceTests {

        @Test
        @DisplayName("Расчет расстояния между двумя адресами")
        void shouldCalculateDistanceBetweenAddresses() throws InvalidDeliveryAddressException {
            // given
            Address address1 = new Address("Ленина", "15", "Москва", "123456");
            address1.setLatitude(55.7558);
            address1.setLongitude(37.6173);

            Address address2 = new Address("Пушкина", "20", "Москва", "123457");
            address2.setLatitude(55.7600);
            address2.setLongitude(37.6200);

            // when
            double distance = address1.calculateDistance(address2);

            // then
            assertTrue(distance > 0);
        }

        @Test
        @DisplayName("Расстояние между одинаковыми координатами равно нулю")
        void shouldReturnZeroDistanceForSameCoordinates() throws InvalidDeliveryAddressException {
            // given
            Address address1 = new Address("Ленина", "15", "Москва", "123456");
            address1.setLatitude(55.7558);
            address1.setLongitude(37.6173);

            Address address2 = new Address("Пушкина", "20", "Москва", "123457");
            address2.setLatitude(55.7558);
            address2.setLongitude(37.6173);

            // when
            double distance = address1.calculateDistance(address2);

            // then
            assertEquals(0.0, distance, 0.0001);
        }

        @Test
        @DisplayName("Расчет расстояния с отрицательными координатами")
        void shouldCalculateDistanceWithNegativeCoordinates() throws InvalidDeliveryAddressException {
            // given
            Address address1 = new Address("Ленина", "15", "Москва", "123456");
            address1.setLatitude(-33.8688);
            address1.setLongitude(151.2093);

            Address address2 = new Address("Пушкина", "20", "Москва", "123457");
            address2.setLatitude(-33.8700);
            address2.setLongitude(151.2100);

            // when
            double distance = address1.calculateDistance(address2);

            // then
            assertTrue(distance > 0);
        }

        @Test
        @DisplayName("Расчет расстояния с нулевыми координатами")
        void shouldCalculateDistanceWithZeroCoordinates() throws InvalidDeliveryAddressException {
            // given
            Address address1 = new Address("Ленина", "15", "Москва", "123456");
            address1.setLatitude(0.0);
            address1.setLongitude(0.0);

            Address address2 = new Address("Пушкина", "20", "Москва", "123457");
            address2.setLatitude(1.0);
            address2.setLongitude(1.0);

            // when
            double distance = address1.calculateDistance(address2);

            // then
            assertTrue(distance > 0);
        }
    }

    @Nested
    @DisplayName("Тесты toString")
    class ToStringTests {

        @Test
        @DisplayName("toString без номера квартиры")
        void shouldReturnCorrectStringWithoutApartment() throws InvalidDeliveryAddressException {
            // given
            Address address = new Address("Ленина", "15", "Москва", "123456");

            // when
            String result = address.toString();

            // then
            assertEquals("Ленина, д. 15, Москва, 123456", result);
        }

        @Test
        @DisplayName("toString с номером квартиры")
        void shouldReturnCorrectStringWithApartment() throws InvalidDeliveryAddressException {
            // given
            Address address = new Address("Ленина", "15", "Москва", "123456");
            address.setApartmentNumber("42");

            // when
            String result = address.toString();

            // then
            assertEquals("Ленина, д. 15, кв. 42, Москва, 123456", result);
        }

        @Test
        @DisplayName("toString с null почтовым индексом")
        void shouldReturnCorrectStringWithNullPostalCode() throws InvalidDeliveryAddressException {
            // given
            Address address = new Address("Ленина", "15", "Москва", null);

            // when
            String result = address.toString();

            // then
            assertEquals("Ленина, д. 15, Москва, null", result);
        }
    }

    @Nested
    @DisplayName("Граничные случаи")
    class EdgeCaseTests {

        @Test
        @DisplayName("Создание адреса с очень длинной улицей")
        void shouldCreateAddressWithVeryLongStreet() throws InvalidDeliveryAddressException {
            // given
            String longStreet = "А".repeat(1000);

            // when
            Address address = new Address(longStreet, "15", "Москва", "123456");

            // then
            assertNotNull(address);
            assertEquals(longStreet, address.getStreet());
        }

        @Test
        @DisplayName("Создание адреса с специальными символами")
        void shouldCreateAddressWithSpecialCharacters() throws InvalidDeliveryAddressException {
            // given & when
            Address address = new Address("Ул. 50-лет Октября", "15А/2", "Санкт-Петербург", "123-456");

            // then
            assertNotNull(address);
            assertEquals("Ул. 50-лет Октября", address.getStreet());
            assertEquals("15А/2", address.getHouseNumber());
        }

        @Test
        @DisplayName("Создание адреса с числовыми значениями в строках")
        void shouldCreateAddressWithNumericStrings() throws InvalidDeliveryAddressException {
            // given & when
            Address address = new Address("123", "456", "789", "000");

            // then
            assertNotNull(address);
            assertEquals("123", address.getStreet());
            assertEquals("456", address.getHouseNumber());
            assertEquals("789", address.getCity());
        }
    }
}
