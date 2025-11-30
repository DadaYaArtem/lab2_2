package com.pizzeria.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты для класса Email")
class EmailTest {

    @Nested
    @DisplayName("Тесты конструктора с email строкой")
    class EmailStringConstructorTests {

        @Test
        @DisplayName("Создание email с валидной строкой")
        void shouldCreateEmailWithValidString() {
            // given & when
            Email email = new Email("user@example.com");

            // then
            assertNotNull(email);
            assertEquals("user", email.getLocalPart());
            assertEquals("example.com", email.getDomain());
        }

        @Test
        @DisplayName("Создание email с подстрокой до @ как localPart")
        void shouldParseLocalPartCorrectly() {
            // given & when
            Email email = new Email("john.doe@company.org");

            // then
            assertEquals("john.doe", email.getLocalPart());
            assertEquals("company.org", email.getDomain());
        }

        @Test
        @DisplayName("Создание email с длинным доменом")
        void shouldParseLongDomain() {
            // given & when
            Email email = new Email("admin@mail.google.com");

            // then
            assertEquals("admin", email.getLocalPart());
            assertEquals("mail.google.com", email.getDomain());
        }

        @Test
        @DisplayName("Создание email без @ символа")
        void shouldHandleEmailWithoutAtSymbol() {
            // given & when
            Email email = new Email("invalidemail.com");

            // then
            assertNull(email.getLocalPart());
            assertNull(email.getDomain());
        }

        @Test
        @DisplayName("Создание email с null строкой")
        void shouldHandleNullEmail() {
            // given & when
            Email email = new Email((String) null);

            // then
            assertNull(email.getLocalPart());
            assertNull(email.getDomain());
        }

        @Test
        @DisplayName("Создание email с несколькими @ символами")
        void shouldParseEmailWithMultipleAtSymbols() {
            // given & when - split берет только первое вхождение
            Email email = new Email("user@name@domain.com");

            // then
            assertEquals("user", email.getLocalPart());
            assertEquals("name@domain.com", email.getDomain());
        }

        @Test
        @DisplayName("Создание email с пустой строкой")
        void shouldHandleEmptyString() {
            // given & when
            Email email = new Email("");

            // then
            assertNull(email.getLocalPart());
            assertNull(email.getDomain());
        }
    }

    @Nested
    @DisplayName("Тесты конструктора с двумя параметрами")
    class TwoParameterConstructorTests {

        @Test
        @DisplayName("Создание email с валидными параметрами")
        void shouldCreateEmailWithValidParameters() {
            // given & when
            Email email = new Email("user", "example.com");

            // then
            assertEquals("user", email.getLocalPart());
            assertEquals("example.com", email.getDomain());
        }

        @Test
        @DisplayName("Создание email с null параметрами")
        void shouldCreateEmailWithNullParameters() {
            // given & when
            Email email = new Email(null, null);

            // then
            assertNull(email.getLocalPart());
            assertNull(email.getDomain());
        }

        @Test
        @DisplayName("Создание email с пустыми строками")
        void shouldCreateEmailWithEmptyStrings() {
            // given & when
            Email email = new Email("", "");

            // then
            assertEquals("", email.getLocalPart());
            assertEquals("", email.getDomain());
        }
    }

    @Nested
    @DisplayName("Тесты метода getFullEmail")
    class GetFullEmailTests {

        @Test
        @DisplayName("Получение полного email")
        void shouldReturnFullEmail() {
            // given
            Email email = new Email("user", "example.com");

            // when
            String fullEmail = email.getFullEmail();

            // then
            assertEquals("user@example.com", fullEmail);
        }

        @Test
        @DisplayName("Полный email после изменения компонентов")
        void shouldReturnUpdatedFullEmail() {
            // given
            Email email = new Email("user", "example.com");
            email.setLocalPart("admin");
            email.setDomain("newdomain.org");

            // when
            String fullEmail = email.getFullEmail();

            // then
            assertEquals("admin@newdomain.org", fullEmail);
        }

        @Test
        @DisplayName("Полный email с null компонентами")
        void shouldHandleNullComponents() {
            // given
            Email email = new Email(null, null);

            // when
            String fullEmail = email.getFullEmail();

            // then
            assertEquals("null@null", fullEmail);
        }
    }

    @Nested
    @DisplayName("Тесты метода isValid")
    class IsValidTests {

        @Test
        @DisplayName("Валидный email")
        void shouldBeValidWithCorrectFormat() {
            // given
            Email email = new Email("user@example.com");

            // when & then
            assertTrue(email.isValid());
        }

        @Test
        @DisplayName("Валидный email с поддоменом")
        void shouldBeValidWithSubdomain() {
            // given
            Email email = new Email("admin@mail.google.com");

            // when & then
            assertTrue(email.isValid());
        }

        @Test
        @DisplayName("Невалидный email с пустым localPart")
        void shouldBeInvalidWithEmptyLocalPart() {
            // given
            Email email = new Email("", "example.com");

            // when & then
            assertFalse(email.isValid());
        }

        @Test
        @DisplayName("Невалидный email с null localPart")
        void shouldBeInvalidWithNullLocalPart() {
            // given
            Email email = new Email(null, "example.com");

            // when & then
            assertFalse(email.isValid());
        }

        @Test
        @DisplayName("Невалидный email без точки в домене")
        void shouldBeInvalidWithoutDotInDomain() {
            // given
            Email email = new Email("user", "examplecom");

            // when & then
            assertFalse(email.isValid());
        }

        @Test
        @DisplayName("Невалидный email с null доменом")
        void shouldBeInvalidWithNullDomain() {
            // given
            Email email = new Email("user", null);

            // when & then
            assertFalse(email.isValid());
        }

        @Test
        @DisplayName("Невалидный email с пустым доменом")
        void shouldBeInvalidWithEmptyDomain() {
            // given
            Email email = new Email("user", "");

            // when & then
            assertFalse(email.isValid());
        }

        @Test
        @DisplayName("Валидный email с минимальной длиной")
        void shouldBeValidWithMinimalLength() {
            // given
            Email email = new Email("a", "b.c");

            // when & then
            assertTrue(email.isValid());
        }
    }

    @Nested
    @DisplayName("Тесты метода maskEmail")
    class MaskEmailTests {

        @Test
        @DisplayName("Маскировка email с длинным localPart")
        void shouldMaskEmailWithLongLocalPart() {
            // given
            Email email = new Email("john.doe@example.com");

            // when
            String masked = email.maskEmail();

            // then
            assertEquals("jo***@example.com", masked);
        }

        @Test
        @DisplayName("Маскировка email с localPart из 3 символов")
        void shouldMaskEmailWithThreeCharacterLocalPart() {
            // given
            Email email = new Email("abc@example.com");

            // when
            String masked = email.maskEmail();

            // then
            assertEquals("ab***@example.com", masked);
        }

        @Test
        @DisplayName("Маскировка email с localPart из 2 символов")
        void shouldMaskEmailWithTwoCharacterLocalPart() {
            // given
            Email email = new Email("ab@example.com");

            // when
            String masked = email.maskEmail();

            // then
            assertEquals("***@example.com", masked);
        }

        @Test
        @DisplayName("Маскировка email с localPart из 1 символа")
        void shouldMaskEmailWithOneCharacterLocalPart() {
            // given
            Email email = new Email("a@example.com");

            // when
            String masked = email.maskEmail();

            // then
            assertEquals("***@example.com", masked);
        }

        @Test
        @DisplayName("Маскировка email с пустым localPart")
        void shouldMaskEmailWithEmptyLocalPart() {
            // given
            Email email = new Email("", "example.com");

            // when
            String masked = email.maskEmail();

            // then
            assertEquals("***@example.com", masked);
        }

        @Test
        @DisplayName("Маскировка сохраняет домен")
        void shouldKeepDomainUnmasked() {
            // given
            Email email = new Email("user", "secure.mail.com");

            // when
            String masked = email.maskEmail();

            // then
            assertTrue(masked.endsWith("@secure.mail.com"));
        }
    }

    @Nested
    @DisplayName("Тесты геттеров и сеттеров")
    class GetterSetterTests {

        @Test
        @DisplayName("Установка и получение localPart")
        void shouldSetAndGetLocalPart() {
            // given
            Email email = new Email("user@example.com");

            // when
            email.setLocalPart("admin");

            // then
            assertEquals("admin", email.getLocalPart());
        }

        @Test
        @DisplayName("Установка и получение domain")
        void shouldSetAndGetDomain() {
            // given
            Email email = new Email("user@example.com");

            // when
            email.setDomain("newdomain.org");

            // then
            assertEquals("newdomain.org", email.getDomain());
        }

        @Test
        @DisplayName("Установка null значений")
        void shouldSetNullValues() {
            // given
            Email email = new Email("user@example.com");

            // when
            email.setLocalPart(null);
            email.setDomain(null);

            // then
            assertNull(email.getLocalPart());
            assertNull(email.getDomain());
        }
    }

    @Nested
    @DisplayName("Тесты toString")
    class ToStringTests {

        @Test
        @DisplayName("toString возвращает полный email")
        void shouldReturnFullEmailFromToString() {
            // given
            Email email = new Email("user@example.com");

            // when
            String result = email.toString();

            // then
            assertEquals("user@example.com", result);
        }

        @Test
        @DisplayName("toString после изменения компонентов")
        void shouldReturnUpdatedEmail() {
            // given
            Email email = new Email("user@example.com");
            email.setLocalPart("newuser");
            email.setDomain("newdomain.com");

            // when
            String result = email.toString();

            // then
            assertEquals("newuser@newdomain.com", result);
        }
    }

    @Nested
    @DisplayName("Граничные случаи")
    class EdgeCaseTests {

        @Test
        @DisplayName("Email с цифрами")
        void shouldHandleEmailWithNumbers() {
            // given & when
            Email email = new Email("user123@domain456.com");

            // then
            assertEquals("user123", email.getLocalPart());
            assertEquals("domain456.com", email.getDomain());
            assertTrue(email.isValid());
        }

        @Test
        @DisplayName("Email с специальными символами")
        void shouldHandleEmailWithSpecialCharacters() {
            // given & when
            Email email = new Email("user.name+tag@example.co.uk");

            // then
            assertEquals("user.name+tag", email.getLocalPart());
            assertEquals("example.co.uk", email.getDomain());
            assertTrue(email.isValid());
        }

        @Test
        @DisplayName("Email с очень длинным localPart")
        void shouldHandleVeryLongLocalPart() {
            // given
            String longLocal = "a".repeat(100);
            Email email = new Email(longLocal, "example.com");

            // when
            String masked = email.maskEmail();

            // then
            assertTrue(masked.startsWith("aa***"));
            assertTrue(email.isValid());
        }

        @Test
        @DisplayName("Email начинается с @")
        void shouldHandleEmailStartingWithAt() {
            // given & when
            Email email = new Email("@example.com");

            // then
            assertEquals("", email.getLocalPart());
            assertEquals("example.com", email.getDomain());
            assertFalse(email.isValid());
        }

        @Test
        @DisplayName("Email заканчивается на @")
        void shouldHandleEmailEndingWithAt() {
            // given & when
            Email email = new Email("user@");

            // then
            assertEquals("user", email.getLocalPart());
            assertEquals("", email.getDomain());
            assertFalse(email.isValid());
        }

        @Test
        @DisplayName("Email состоит только из @")
        void shouldHandleEmailWithOnlyAt() {
            // given & when
            Email email = new Email("@");

            // then
            assertEquals("", email.getLocalPart());
            assertEquals("", email.getDomain());
            assertFalse(email.isValid());
        }

        @Test
        @DisplayName("Email с пробелами")
        void shouldHandleEmailWithSpaces() {
            // given & when
            Email email = new Email("user name@example.com");

            // then
            assertEquals("user name", email.getLocalPart());
            assertEquals("example.com", email.getDomain());
        }
    }
}
