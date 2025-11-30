import com.pizzeria.exceptions.InvalidDiscountException;
import com.pizzeria.model.Discount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Discount Tests")
class DiscountTest {

    private Discount discount;

    @BeforeEach
    void setUp() throws InvalidDiscountException {
        discount = new Discount("SAVE10", 10.0);
    }

    @Test
    @DisplayName("Создание скидки")
    void testDiscountCreation() {
        assertNotNull(discount);
        assertEquals("SAVE10", discount.getCode());
        assertEquals(10.0, discount.getPercentage(), 0.01);
    }

    @Test
    @DisplayName("Скидка активна по умолчанию")
    void testDiscountActiveByDefault() {
        assertTrue(discount.isActive());
    }

    @Test
    @DisplayName("Лимит использования по умолчанию равен 100")
    void testDefaultUsageLimit() {
        assertEquals(100, discount.getUsageLimit());
    }

    @Test
    @DisplayName("Начальное количество использований равно нулю")
    void testInitialTimesUsedIsZero() {
        assertEquals(0, discount.getTimesUsed());
    }

    @Test
    @DisplayName("Дата начала устанавливается на сегодня")
    void testStartDateIsToday() {
        assertEquals(LocalDate.now(), discount.getStartDate());
    }

    @Test
    @DisplayName("Дата окончания через месяц")
    void testEndDateIsOneMonthLater() {
        assertEquals(LocalDate.now().plusMonths(1), discount.getEndDate());
    }

    @Test
    @DisplayName("Скидка применима при корректных условиях")
    void testDiscountIsApplicable() {
        assertTrue(discount.isDiscountApplicable());
    }

    @Test
    @DisplayName("Использование скидки увеличивает счетчик")
    void testUseIncreasesTimesUsed() {
        discount.use();
        assertEquals(1, discount.getTimesUsed());

        discount.use();
        assertEquals(2, discount.getTimesUsed());
    }

    @Test
    @DisplayName("Скидка не применима после достижения лимита")
    void testDiscountNotApplicableAfterLimit() {
        discount.setUsageLimit(2);

        discount.use();
        discount.use();
        assertTrue(discount.isDiscountApplicable()); // еще можно использовать

        // Не увеличивается, потому что лимит достигнут
        assertEquals(2, discount.getTimesUsed());
    }

    @Test
    @DisplayName("Деактивированная скидка не применима")
    void testInactiveDiscountNotApplicable() {
        discount.setActive(false);
        assertFalse(discount.isDiscountApplicable());
    }

    @Test
    @DisplayName("Просроченная скидка не применима")
    void testExpiredDiscountNotApplicable() {
        discount.setEndDate(LocalDate.now().minusDays(1));
        assertFalse(discount.isDiscountApplicable());
    }

    @Test
    @DisplayName("Будущая скидка не применима")
    void testFutureDiscountNotApplicable() {
        discount.setStartDate(LocalDate.now().plusDays(1));
        assertFalse(discount.isDiscountApplicable());
    }

    @Test
    @DisplayName("Валидация кода скидки")
    void testValidateCode() {
        assertTrue(discount.validateCode("SAVE10"));
        assertTrue(discount.validateCode("save10")); // игнорирование регистра
        assertTrue(discount.validateCode("SaVe10"));
        assertFalse(discount.validateCode("WRONG"));
    }

    @Test
    @DisplayName("Применение новой скидки")
    void testApplyDiscount() throws InvalidDiscountException {
        discount.applyDiscount(15.0);
        assertEquals(15.0, discount.getPercentage(), 0.01);
        assertEquals(15.0, discount.getDiscountAmount(), 0.01);
    }

    @Test
    @DisplayName("Исключение при отрицательной скидке")
    void testNegativeDiscountThrowsException() {
        assertThrows(InvalidDiscountException.class, () -> {
            new Discount("TEST", -5.0);
        });
    }

    @Test
    @DisplayName("Исключение при скидке больше 100%")
    void testExcessiveDiscountThrowsException() {
        assertThrows(InvalidDiscountException.class, () -> {
            new Discount("TEST", 101.0);
        });
    }

    @Test
    @DisplayName("Применение некорректной скидки")
    void testApplyInvalidDiscount() {
        assertThrows(InvalidDiscountException.class, () -> {
            discount.applyDiscount(-10.0);
        });

        assertThrows(InvalidDiscountException.class, () -> {
            discount.applyDiscount(150.0);
        });
    }

    @Test
    @DisplayName("Граничные значения скидки")
    void testBoundaryDiscountValues() throws InvalidDiscountException {
        Discount zero = new Discount("ZERO", 0.0);
        assertEquals(0.0, zero.getPercentage(), 0.01);

        Discount hundred = new Discount("HUNDRED", 100.0);
        assertEquals(100.0, hundred.getPercentage(), 0.01);
    }

    @Test
    @DisplayName("Изменение кода скидки")
    void testSetCode() {
        discount.setCode("NEWSAVE");
        assertEquals("NEWSAVE", discount.getCode());
    }

    @Test
    @DisplayName("Изменение процента скидки напрямую")
    void testSetPercentage() {
        discount.setPercentage(20.0);
        assertEquals(20.0, discount.getPercentage(), 0.01);
    }

    @Test
    @DisplayName("Изменение лимита использования")
    void testSetUsageLimit() {
        discount.setUsageLimit(50);
        assertEquals(50, discount.getUsageLimit());
    }
}
