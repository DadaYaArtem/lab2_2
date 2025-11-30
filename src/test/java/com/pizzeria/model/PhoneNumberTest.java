package com.pizzeria.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты для класса PhoneNumber")
class PhoneNumberTest {

    @Nested
    @DisplayName("Тесты конструктора с тремя параметрами")
    class ThreeParameterConstructorTests {

        @Test
        @DisplayName("Создание номера телефона с валидными данными")
        void shouldCreatePhoneNumberWithValidData() {
            // given & when
            PhoneNumber phone = new PhoneNumber("+7", "495", "1234567");

            // then
            assertNotNull(phone);
            assertEquals("+7", phone.getCountryCode());
            assertEquals("495", phone.getAreaCode());
            assertEquals("1234567", phone.getNumber());
        }

        @Test
        @DisplayName("Создание номера с различными кодами стран")
        void shouldCreatePhoneNumberWithDifferentCountryCodes() {
            // given & when
            PhoneNumber phone1 = new PhoneNumber("+1", "555", "1234567");
            PhoneNumber phone2 = new PhoneNumber("+44", "20", "12345678");
            PhoneNumber phone3 = new PhoneNumber("+86", "10", "12345678");

            // then
            assertEquals("+1", phone1.getCountryCode());
            assertEquals("+44", phone2.getCountryCode());
            assertEquals("+86", phone3.getCountryCode());
        }
    }

    @Nested
    @DisplayName("Тесты конструктора с полным номером")
    class FullNumberConstructorTests {

        @Test
        @DisplayName("Парсинг номера, начинающегося с +")
        void shouldParseNumberStartingWithPlus() {
            // given & when
            PhoneNumber phone = new PhoneNumber("+74951234567");

            // then
            assertEquals("+7", phone.getCountryCode());
            assertEquals("495", phone.getAreaCode());
            assertEquals("1234567", phone.getNumber());
        }

        @Test
        @DisplayName("Парсинг номера без + устанавливает значения по умолчанию")
        void shouldSetDefaultValuesForNumberWithoutPlus() {
            // given & when
            PhoneNumber phone = new PhoneNumber("1234567890");

            // then
            assertEquals("+1", phone.getCountryCode());
            assertEquals("000", phone.getAreaCode());
            assertEquals("1234567890", phone.getNumber());
        }

        @Test
        @DisplayName("Парсинг короткого номера с +")
        void shouldParseShortNumberWithPlus() {
            // given & when
            PhoneNumber phone = new PhoneNumber("+71234");

            // then
            assertEquals("+7", phone.getCountryCode());
            assertEquals("123", phone.getAreaCode());
            assertEquals("4", phone.getNumber());
        }

        @Test
        @DisplayName("Парсинг длинного номера с +")
        void shouldParseLongNumberWithPlus() {
            // given & when
            PhoneNumber phone = new PhoneNumber("+712345678901234567890");

            // then
            assertEquals("+7", phone.getCountryCode());
            assertEquals("123", phone.getAreaCode());
            assertEquals("45678901234567890", phone.getNumber());
        }
    }

    @Nested
    @DisplayName("Тесты метода getFullNumber")
    class GetFullNumberTests {

        @Test
        @DisplayName("Получение полного номера")
        void shouldReturnFullNumber() {
            // given
            PhoneNumber phone = new PhoneNumber("+7", "495", "1234567");

            // when
            String fullNumber = phone.getFullNumber();

            // then
            assertEquals("+74951234567", fullNumber);
        }

        @Test
        @DisplayName("Полный номер после изменения компонентов")
        void shouldReturnUpdatedFullNumber() {
            // given
            PhoneNumber phone = new PhoneNumber("+7", "495", "1234567");
            phone.setCountryCode("+1");
            phone.setAreaCode("555");
            phone.setNumber("9876543");

            // when
            String fullNumber = phone.getFullNumber();

            // then
            assertEquals("+15559876543", fullNumber);
        }
    }

    @Nested
    @DisplayName("Тесты метода isValid")
    class IsValidTests {

        @Test
        @DisplayName("Валидный номер с длиной 7 символов")
        void shouldBeValidWithSevenDigits() {
            // given
            PhoneNumber phone = new PhoneNumber("+7", "495", "1234567");

            // when & then
            assertTrue(phone.isValid());
        }

        @Test
        @DisplayName("Валидный номер с длиной больше 7 символов")
        void shouldBeValidWithMoreThanSevenDigits() {
            // given
            PhoneNumber phone = new PhoneNumber("+7", "495", "12345678901");

            // when & then
            assertTrue(phone.isValid());
        }

        @Test
        @DisplayName("Невалидный номер с длиной меньше 7 символов")
        void shouldBeInvalidWithLessThanSevenDigits() {
            // given
            PhoneNumber phone = new PhoneNumber("+7", "495", "123456");

            // when & then
            assertFalse(phone.isValid());
        }

        @Test
        @DisplayName("Невалидный номер с null значением")
        void shouldBeInvalidWithNullNumber() {
            // given
            PhoneNumber phone = new PhoneNumber("+7", "495", null);

            // when & then
            assertFalse(phone.isValid());
        }

        @Test
        @DisplayName("Невалидный номер с пустой строкой")
        void shouldBeInvalidWithEmptyString() {
            // given
            PhoneNumber phone = new PhoneNumber("+7", "495", "");

            // when & then
            assertFalse(phone.isValid());
        }

        @Test
        @DisplayName("Граничный случай: номер ровно 7 символов")
        void shouldBeValidWithExactlySevenDigits() {
            // given
            PhoneNumber phone = new PhoneNumber("+7", "495", "1234567");

            // when & then
            assertTrue(phone.isValid());
            assertEquals(7, phone.getNumber().length());
        }
    }

    @Nested
    @DisplayName("Тесты метода getFormattedNumber")
    class GetFormattedNumberTests {

        @Test
        @DisplayName("Получение форматированного номера")
        void shouldReturnFormattedNumber() {
            // given
            PhoneNumber phone = new PhoneNumber("+7", "495", "1234567");

            // when
            String formatted = phone.getFormattedNumber();

            // then
            assertEquals("+7 (495) 1234567", formatted);
        }

        @Test
        @DisplayName("Форматированный номер с разными кодами")
        void shouldReturnFormattedNumberWithDifferentCodes() {
            // given
            PhoneNumber phone = new PhoneNumber("+1", "555", "9876543");

            // when
            String formatted = phone.getFormattedNumber();

            // then
            assertEquals("+1 (555) 9876543", formatted);
        }

        @Test
        @DisplayName("Форматированный номер с null компонентами")
        void shouldHandleNullComponentsInFormatting() {
            // given
            PhoneNumber phone = new PhoneNumber(null, null, null);

            // when
            String formatted = phone.getFormattedNumber();

            // then
            assertEquals("null (null) null", formatted);
        }
    }

    @Nested
    @DisplayName("Тесты геттеров и сеттеров")
    class GetterSetterTests {

        @Test
        @DisplayName("Установка и получение кода страны")
        void shouldSetAndGetCountryCode() {
            // given
            PhoneNumber phone = new PhoneNumber("+7", "495", "1234567");

            // when
            phone.setCountryCode("+1");

            // then
            assertEquals("+1", phone.getCountryCode());
        }

        @Test
        @DisplayName("Установка и получение кода региона")
        void shouldSetAndGetAreaCode() {
            // given
            PhoneNumber phone = new PhoneNumber("+7", "495", "1234567");

            // when
            phone.setAreaCode("499");

            // then
            assertEquals("499", phone.getAreaCode());
        }

        @Test
        @DisplayName("Установка и получение номера")
        void shouldSetAndGetNumber() {
            // given
            PhoneNumber phone = new PhoneNumber("+7", "495", "1234567");

            // when
            phone.setNumber("7654321");

            // then
            assertEquals("7654321", phone.getNumber());
        }

        @Test
        @DisplayName("Установка null значений")
        void shouldSetNullValues() {
            // given
            PhoneNumber phone = new PhoneNumber("+7", "495", "1234567");

            // when
            phone.setCountryCode(null);
            phone.setAreaCode(null);
            phone.setNumber(null);

            // then
            assertNull(phone.getCountryCode());
            assertNull(phone.getAreaCode());
            assertNull(phone.getNumber());
        }
    }

    @Nested
    @DisplayName("Тесты toString")
    class ToStringTests {

        @Test
        @DisplayName("toString возвращает форматированный номер")
        void shouldReturnFormattedNumberFromToString() {
            // given
            PhoneNumber phone = new PhoneNumber("+7", "495", "1234567");

            // when
            String result = phone.toString();

            // then
            assertEquals("+7 (495) 1234567", result);
        }

        @Test
        @DisplayName("toString после изменения компонентов")
        void shouldReturnUpdatedFormattedNumber() {
            // given
            PhoneNumber phone = new PhoneNumber("+7", "495", "1234567");
            phone.setCountryCode("+44");
            phone.setAreaCode("20");
            phone.setNumber("12345678");

            // when
            String result = phone.toString();

            // then
            assertEquals("+44 (20) 12345678", result);
        }
    }

    @Nested
    @DisplayName("Граничные случаи")
    class EdgeCaseTests {

        @Test
        @DisplayName("Создание номера с пустыми строками")
        void shouldCreatePhoneNumberWithEmptyStrings() {
            // given & when
            PhoneNumber phone = new PhoneNumber("", "", "");

            // then
            assertNotNull(phone);
            assertEquals("", phone.getCountryCode());
            assertEquals("", phone.getAreaCode());
            assertEquals("", phone.getNumber());
        }

        @Test
        @DisplayName("Создание номера с очень длинными значениями")
        void shouldCreatePhoneNumberWithVeryLongValues() {
            // given
            String longNumber = "1".repeat(100);

            // when
            PhoneNumber phone = new PhoneNumber("+123", "456", longNumber);

            // then
            assertEquals(longNumber, phone.getNumber());
            assertTrue(phone.isValid());
        }

        @Test
        @DisplayName("Парсинг номера с символами помимо цифр")
        void shouldParseNumberWithNonDigitCharacters() {
            // given & when
            PhoneNumber phone = new PhoneNumber("+7-495-123-45-67");

            // then
            assertEquals("+7", phone.getCountryCode());
            assertEquals("-49", phone.getAreaCode());
            assertEquals("5-123-45-67", phone.getNumber());
        }

        @Test
        @DisplayName("Создание номера с пробелами")
        void shouldCreatePhoneNumberWithSpaces() {
            // given & when
            PhoneNumber phone = new PhoneNumber("+7", "495", "123 45 67");

            // then
            assertEquals("123 45 67", phone.getNumber());
        }

        @Test
        @DisplayName("Парсинг номера минимальной длины")
        void shouldParseMinimumLengthNumber() {
            // given & when - номер должен быть минимум 6 символов (2 для кода страны, 3 для кода области, 1+ для номера)
            PhoneNumber phone = new PhoneNumber("+12345");

            // then
            assertEquals("+1", phone.getCountryCode());
            assertEquals("234", phone.getAreaCode());
            assertEquals("5", phone.getNumber());
            assertFalse(phone.isValid()); // номер слишком короткий
        }
    }
}
