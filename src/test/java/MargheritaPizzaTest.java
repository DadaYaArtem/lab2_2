import com.pizzeria.enums.PizzaSize;
import com.pizzeria.exceptions.InvalidPriceException;
import com.pizzeria.exceptions.InvalidPaymentException;
import com.pizzeria.model.products.MargheritaPizza;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MargheritaPizza Tests")
class MargheritaPizzaTest {

    private MargheritaPizza smallPizza;
    private MargheritaPizza mediumPizza;
    private MargheritaPizza largePizza;

    @BeforeEach
    void setUp() throws InvalidPriceException {
        smallPizza = new MargheritaPizza(PizzaSize.SMALL);
        mediumPizza = new MargheritaPizza(PizzaSize.MEDIUM);
        largePizza = new MargheritaPizza(PizzaSize.LARGE);
    }

    @Test
    @DisplayName("Создание пиццы Маргарита")
    void testPizzaCreation() {
        assertNotNull(smallPizza);
        assertEquals("Маргарита", smallPizza.getName());
        assertEquals(PizzaSize.SMALL, smallPizza.getSize());
    }

    @Test
    @DisplayName("Проверка калорий для разных размеров")
    void testCaloriesForDifferentSizes() {
        assertEquals(800, smallPizza.getCalories());
        assertEquals(1200, mediumPizza.getCalories());
        assertEquals(1600, largePizza.getCalories());
    }

    @Test
    @DisplayName("Проверка что пицца вегетарианская")
    void testIsVegetarian() {
        assertTrue(smallPizza.isVegetarian());
        assertTrue(mediumPizza.isVegetarian());
    }

    @Test
    @DisplayName("Проверка происхождения пиццы")
    void testOrigin() {
        assertEquals("Италия, Неаполь", smallPizza.getOrigin());
    }

    @Test
    @DisplayName("Проверка базовой цены")
    void testBasePrice() {
        assertEquals(300.0, smallPizza.getBasePrice(), 0.01);
    }

    @Test
    @DisplayName("Проверка цены с учетом размера")
    void testPriceWithSize() {
        // Маленькая: 300 * 1.0 = 300
        assertEquals(300.0, smallPizza.getPrice(), 0.01);
        // Средняя: 300 * 1.5 = 450
        assertEquals(450.0, mediumPizza.getPrice(), 0.01);
        // Большая: 300 * 2.0 = 600
        assertEquals(600.0, largePizza.getPrice(), 0.01);
    }

    @Test
    @DisplayName("Применение скидки")
    void testApplyDiscount() {
        smallPizza.applyDiscount(10.0);
        // 300 * 0.9 = 270
        assertEquals(270.0, smallPizza.getFinalPrice(), 0.01);
    }

    @Test
    @DisplayName("Время приготовления")
    void testPreparationTime() {
        assertEquals(18, smallPizza.getPreparationTime());
    }

    @Test
    @DisplayName("Проверка описания")
    void testDescription() {
        assertNotNull(smallPizza.getDescription());
        assertTrue(smallPizza.getDescription().contains("моцареллой"));
    }

    @Test
    @DisplayName("Обработка платежа с достаточной суммой")
    void testProcessPaymentWithSufficientAmount() throws InvalidPaymentException {
        assertTrue(smallPizza.processPayment(300.0));
        assertTrue(smallPizza.processPayment(500.0));
    }

    @Test
    @DisplayName("Обработка платежа с недостаточной суммой")
    void testProcessPaymentWithInsufficientAmount() {
        assertThrows(InvalidPaymentException.class, () -> {
            smallPizza.processPayment(100.0);
        });
    }
}
