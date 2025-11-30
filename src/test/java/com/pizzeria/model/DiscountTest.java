package com.pizzeria.model;

import com.pizzeria.exceptions.InvalidDiscountException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты для класса Discount")
class DiscountTest {

    @Nested
    @DisplayName("Тесты конструктора")
    class ConstructorTests {

        @Test
        @DisplayName("Успешное создание скидки с валидным процентом")
        void shouldCreateDiscountWithValidPercentage() throws InvalidDiscountException {
            // given & when
            Discount discount = new Discount("SUMMER2024", 15.0);

            // then
            assertNotNull(discount);
            assertEquals("SUMMER2024", discount.getCode());
            assertEquals(15.0, discount.getPercentage());
            assertTrue(discount.isActive());
            assertEquals(100, discount.getUsageLimit());
            assertEquals(0, discount.getTimesUsed());
        }

        @Test
        @DisplayName("Исключение при отрицательном проценте")
        void shouldThrowExceptionWithNegativePercentage() {
            // when & then
            InvalidDiscountException exception = assertThrows(
                InvalidDiscountException.class,
                () -> new Discount("INVALID", -10.0)
            );
            assertTrue(exception.getMessage().contains("-10"));
        }

        @Test
        @DisplayName("Исключение при проценте больше 100")
        void shouldThrowExceptionWithPercentageOver100() {
            // when & then
            InvalidDiscountException exception = assertThrows(
                InvalidDiscountException.class,
                () -> new Discount("INVALID", 150.0)
            );
            assertTrue(exception.getMessage().contains("150"));
        }

        @Test
        @DisplayName("Граничный случай: процент равен 0")
        void shouldCreateDiscountWithZeroPercentage() throws InvalidDiscountException {
            // given & when
            Discount discount = new Discount("ZERO", 0.0);

            // then
            assertNotNull(discount);
            assertEquals(0.0, discount.getPercentage());
        }

        @Test
        @DisplayName("Граничный случай: процент равен 100")
        void shouldCreateDiscountWith100Percentage() throws InvalidDiscountException {
            // given & when
            Discount discount = new Discount("FREE", 100.0);

            // then
            assertNotNull(discount);
            assertEquals(100.0, discount.getPercentage());
        }

        @Test
        @DisplayName("Установка даты начала и окончания при создании")
        void shouldSetStartAndEndDateOnCreation() throws InvalidDiscountException {
            // given
            LocalDate now = LocalDate.now();

            // when
            Discount discount = new Discount("TEST", 10.0);

            // then
            assertEquals(now, discount.getStartDate());
            assertEquals(now.plusMonths(1), discount.getEndDate());
        }
    }

    @Nested
    @DisplayName("Тесты метода applyDiscount")
    class ApplyDiscountTests {

        @Test
        @DisplayName("Применение валидного процента скидки")
        void shouldApplyValidDiscount() throws InvalidDiscountException {
            // given
            Discount discount = new Discount("TEST", 10.0);

            // when
            discount.applyDiscount(20.0);

            // then
            assertEquals(20.0, discount.getPercentage());
        }

        @Test
        @DisplayName("Исключение при применении отрицательного процента")
        void shouldThrowExceptionWhenApplyingNegativePercentage() throws InvalidDiscountException {
            // given
            Discount discount = new Discount("TEST", 10.0);

            // when & then
            assertThrows(InvalidDiscountException.class, () -> discount.applyDiscount(-5.0));
        }

        @Test
        @DisplayName("Исключение при применении процента больше 100")
        void shouldThrowExceptionWhenApplyingPercentageOver100() throws InvalidDiscountException {
            // given
            Discount discount = new Discount("TEST", 10.0);

            // when & then
            assertThrows(InvalidDiscountException.class, () -> discount.applyDiscount(120.0));
        }

        @Test
        @DisplayName("Применение граничных значений 0 и 100")
        void shouldApplyBoundaryValues() throws InvalidDiscountException {
            // given
            Discount discount = new Discount("TEST", 10.0);

            // when & then
            assertDoesNotThrow(() -> discount.applyDiscount(0.0));
            assertEquals(0.0, discount.getPercentage());

            assertDoesNotThrow(() -> discount.applyDiscount(100.0));
            assertEquals(100.0, discount.getPercentage());
        }
    }

    @Nested
    @DisplayName("Тесты метода getDiscountAmount")
    class GetDiscountAmountTests {

        @Test
        @DisplayName("Получение размера скидки")
        void shouldReturnDiscountAmount() throws InvalidDiscountException {
            // given
            Discount discount = new Discount("TEST", 25.5);

            // when
            double amount = discount.getDiscountAmount();

            // then
            assertEquals(25.5, amount);
        }

        @Test
        @DisplayName("Размер скидки после изменения")
        void shouldReturnUpdatedDiscountAmount() throws InvalidDiscountException {
            // given
            Discount discount = new Discount("TEST", 10.0);
            discount.applyDiscount(30.0);

            // when
            double amount = discount.getDiscountAmount();

            // then
            assertEquals(30.0, amount);
        }
    }

    @Nested
    @DisplayName("Тесты метода isDiscountApplicable")
    class IsDiscountApplicableTests {

        @Test
        @DisplayName("Скидка применима когда активна и в пределах дат")
        void shouldBeApplicableWhenActiveAndWithinDates() throws InvalidDiscountException {
            // given
            Discount discount = new Discount("TEST", 10.0);

            // when & then
            assertTrue(discount.isDiscountApplicable());
        }

        @Test
        @DisplayName("Скидка не применима когда неактивна")
        void shouldNotBeApplicableWhenInactive() throws InvalidDiscountException {
            // given
            Discount discount = new Discount("TEST", 10.0);
            discount.setActive(false);

            // when & then
            assertFalse(discount.isDiscountApplicable());
        }

        @Test
        @DisplayName("Скидка не применима когда дата начала в будущем")
        void shouldNotBeApplicableWhenStartDateInFuture() throws InvalidDiscountException {
            // given
            Discount discount = new Discount("TEST", 10.0);
            discount.setStartDate(LocalDate.now().plusDays(1));

            // when & then
            assertFalse(discount.isDiscountApplicable());
        }

        @Test
        @DisplayName("Скидка не применима когда дата окончания в прошлом")
        void shouldNotBeApplicableWhenEndDateInPast() throws InvalidDiscountException {
            // given
            Discount discount = new Discount("TEST", 10.0);
            discount.setEndDate(LocalDate.now().minusDays(1));

            // when & then
            assertFalse(discount.isDiscountApplicable());
        }

        @Test
        @DisplayName("Скидка не применима когда достигнут лимит использования")
        void shouldNotBeApplicableWhenUsageLimitReached() throws InvalidDiscountException {
            // given
            Discount discount = new Discount("TEST", 10.0);
            discount.setUsageLimit(2);

            // when
            discount.use();
            discount.use();

            // then
            assertFalse(discount.isDiscountApplicable());
        }

        @Test
        @DisplayName("Скидка применима в последний день действия")
        void shouldBeApplicableOnLastDay() throws InvalidDiscountException {
            // given
            Discount discount = new Discount("TEST", 10.0);
            discount.setEndDate(LocalDate.now());

            // when & then
            assertTrue(discount.isDiscountApplicable());
        }

        @Test
        @DisplayName("Скидка применима в первый день действия")
        void shouldBeApplicableOnFirstDay() throws InvalidDiscountException {
            // given
            Discount discount = new Discount("TEST", 10.0);
            discount.setStartDate(LocalDate.now());

            // when & then
            assertTrue(discount.isDiscountApplicable());
        }
    }

    @Nested
    @DisplayName("Тесты метода use")
    class UseTests {

        @Test
        @DisplayName("Использование скидки увеличивает счетчик")
        void shouldIncrementTimesUsedWhenUsed() throws InvalidDiscountException {
            // given
            Discount discount = new Discount("TEST", 10.0);
            assertEquals(0, discount.getTimesUsed());

            // when
            discount.use();

            // then
            assertEquals(1, discount.getTimesUsed());
        }

        @Test
        @DisplayName("Множественное использование скидки")
        void shouldIncrementMultipleTimes() throws InvalidDiscountException {
            // given
            Discount discount = new Discount("TEST", 10.0);

            // when
            discount.use();
            discount.use();
            discount.use();

            // then
            assertEquals(3, discount.getTimesUsed());
        }

        @Test
        @DisplayName("Использование неприменимой скидки не увеличивает счетчик")
        void shouldNotIncrementWhenNotApplicable() throws InvalidDiscountException {
            // given
            Discount discount = new Discount("TEST", 10.0);
            discount.setActive(false);

            // when
            discount.use();

            // then
            assertEquals(0, discount.getTimesUsed());
        }

        @Test
        @DisplayName("Использование скидки после достижения лимита")
        void shouldNotIncrementAfterLimitReached() throws InvalidDiscountException {
            // given
            Discount discount = new Discount("TEST", 10.0);
            discount.setUsageLimit(2);

            // when
            discount.use();
            discount.use();
            discount.use(); // should not increment

            // then
            assertEquals(2, discount.getTimesUsed());
        }
    }

    @Nested
    @DisplayName("Тесты метода validateCode")
    class ValidateCodeTests {

        @Test
        @DisplayName("Валидация корректного кода")
        void shouldValidateCorrectCode() throws InvalidDiscountException {
            // given
            Discount discount = new Discount("SUMMER2024", 10.0);

            // when & then
            assertTrue(discount.validateCode("SUMMER2024"));
        }

        @Test
        @DisplayName("Валидация игнорирует регистр")
        void shouldValidateCodeCaseInsensitive() throws InvalidDiscountException {
            // given
            Discount discount = new Discount("SUMMER2024", 10.0);

            // when & then
            assertTrue(discount.validateCode("summer2024"));
            assertTrue(discount.validateCode("SuMmEr2024"));
            assertTrue(discount.validateCode("SUMMER2024"));
        }

        @Test
        @DisplayName("Неверный код не проходит валидацию")
        void shouldNotValidateIncorrectCode() throws InvalidDiscountException {
            // given
            Discount discount = new Discount("SUMMER2024", 10.0);

            // when & then
            assertFalse(discount.validateCode("WINTER2024"));
            assertFalse(discount.validateCode("SUMMER"));
            assertFalse(discount.validateCode("2024"));
        }

        @Test
        @DisplayName("Валидация пустого кода")
        void shouldNotValidateEmptyCode() throws InvalidDiscountException {
            // given
            Discount discount = new Discount("TEST", 10.0);

            // when & then
            assertFalse(discount.validateCode(""));
        }

        @Test
        @DisplayName("Валидация null кода")
        void shouldNotValidateNullCode() throws InvalidDiscountException {
            // given
            Discount discount = new Discount("TEST", 10.0);

            // when & then
            assertFalse(discount.validateCode(null));
        }
    }

    @Nested
    @DisplayName("Тесты геттеров и сеттеров")
    class GetterSetterTests {

        @Test
        @DisplayName("Установка и получение кода")
        void shouldSetAndGetCode() throws InvalidDiscountException {
            // given
            Discount discount = new Discount("TEST", 10.0);

            // when
            discount.setCode("NEWCODE");

            // then
            assertEquals("NEWCODE", discount.getCode());
        }

        @Test
        @DisplayName("Установка и получение процента")
        void shouldSetAndGetPercentage() throws InvalidDiscountException {
            // given
            Discount discount = new Discount("TEST", 10.0);

            // when
            discount.setPercentage(25.5);

            // then
            assertEquals(25.5, discount.getPercentage());
        }

        @Test
        @DisplayName("Установка и получение даты начала")
        void shouldSetAndGetStartDate() throws InvalidDiscountException {
            // given
            Discount discount = new Discount("TEST", 10.0);
            LocalDate newDate = LocalDate.of(2024, 6, 1);

            // when
            discount.setStartDate(newDate);

            // then
            assertEquals(newDate, discount.getStartDate());
        }

        @Test
        @DisplayName("Установка и получение даты окончания")
        void shouldSetAndGetEndDate() throws InvalidDiscountException {
            // given
            Discount discount = new Discount("TEST", 10.0);
            LocalDate newDate = LocalDate.of(2024, 12, 31);

            // when
            discount.setEndDate(newDate);

            // then
            assertEquals(newDate, discount.getEndDate());
        }

        @Test
        @DisplayName("Установка и получение статуса активности")
        void shouldSetAndGetActive() throws InvalidDiscountException {
            // given
            Discount discount = new Discount("TEST", 10.0);

            // when
            discount.setActive(false);

            // then
            assertFalse(discount.isActive());
        }

        @Test
        @DisplayName("Установка и получение лимита использования")
        void shouldSetAndGetUsageLimit() throws InvalidDiscountException {
            // given
            Discount discount = new Discount("TEST", 10.0);

            // when
            discount.setUsageLimit(50);

            // then
            assertEquals(50, discount.getUsageLimit());
        }

        @Test
        @DisplayName("Получение количества использований")
        void shouldGetTimesUsed() throws InvalidDiscountException {
            // given
            Discount discount = new Discount("TEST", 10.0);

            // when
            discount.use();
            discount.use();

            // then
            assertEquals(2, discount.getTimesUsed());
        }
    }

    @Nested
    @DisplayName("Граничные случаи")
    class EdgeCaseTests {

        @Test
        @DisplayName("Скидка с дробным процентом")
        void shouldHandleFractionalPercentage() throws InvalidDiscountException {
            // given & when
            Discount discount = new Discount("FRACTION", 12.75);

            // then
            assertEquals(12.75, discount.getPercentage());
            assertEquals(12.75, discount.getDiscountAmount());
        }

        @Test
        @DisplayName("Скидка с очень длинным кодом")
        void shouldHandleVeryLongCode() throws InvalidDiscountException {
            // given
            String longCode = "A".repeat(1000);

            // when
            Discount discount = new Discount(longCode, 10.0);

            // then
            assertEquals(longCode, discount.getCode());
            assertTrue(discount.validateCode(longCode));
        }

        @Test
        @DisplayName("Скидка с лимитом использования 0")
        void shouldHandleZeroUsageLimit() throws InvalidDiscountException {
            // given
            Discount discount = new Discount("TEST", 10.0);
            discount.setUsageLimit(0);

            // when & then
            assertFalse(discount.isDiscountApplicable());
        }

        @Test
        @DisplayName("Скидка с отрицательным лимитом использования")
        void shouldHandleNegativeUsageLimit() throws InvalidDiscountException {
            // given
            Discount discount = new Discount("TEST", 10.0);
            discount.setUsageLimit(-1);

            // when & then
            assertFalse(discount.isDiscountApplicable());
        }

        @Test
        @DisplayName("Скидка с очень большим лимитом использования")
        void shouldHandleVeryLargeUsageLimit() throws InvalidDiscountException {
            // given
            Discount discount = new Discount("TEST", 10.0);

            // when
            discount.setUsageLimit(Integer.MAX_VALUE);

            // then
            assertEquals(Integer.MAX_VALUE, discount.getUsageLimit());
            assertTrue(discount.isDiscountApplicable());
        }

        @Test
        @DisplayName("Скидка с датами в одном дне")
        void shouldHandleSameDayStartAndEnd() throws InvalidDiscountException {
            // given
            Discount discount = new Discount("TEST", 10.0);
            LocalDate today = LocalDate.now();

            // when
            discount.setStartDate(today);
            discount.setEndDate(today);

            // then
            assertTrue(discount.isDiscountApplicable());
        }

        @Test
        @DisplayName("Скидка с перевернутыми датами")
        void shouldHandleReversedDates() throws InvalidDiscountException {
            // given
            Discount discount = new Discount("TEST", 10.0);

            // when
            discount.setStartDate(LocalDate.now().plusDays(10));
            discount.setEndDate(LocalDate.now().minusDays(10));

            // then
            assertFalse(discount.isDiscountApplicable());
        }
    }
}
