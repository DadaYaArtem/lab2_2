import com.pizzeria.enums.PizzaSize;
import com.pizzeria.exceptions.InvalidPriceException;
import com.pizzeria.exceptions.InvalidPaymentException;
import com.pizzeria.model.products.PepperoniPizza;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PepperoniPizza Tests")
class PepperoniPizzaTest {

    private PepperoniPizza smallPizza;
    private PepperoniPizza mediumPizza;
    private PepperoniPizza largePizza;
    private PepperoniPizza extraLargePizza;

    @BeforeEach
    void setUp() throws InvalidPriceException {
        smallPizza = new PepperoniPizza(PizzaSize.SMALL);
        mediumPizza = new PepperoniPizza(PizzaSize.MEDIUM);
        largePizza = new PepperoniPizza(PizzaSize.LARGE);
        extraLargePizza = new PepperoniPizza(PizzaSize.EXTRA_LARGE);
    }

    @Test
    @DisplayName("Создание пиццы Пепперони")
    void testPizzaCreation() {
        assertNotNull(smallPizza);
        assertEquals("Пепперони", smallPizza.getName());
        assertEquals(PizzaSize.SMALL, smallPizza.getSize());
    }

    @Test
    @DisplayName("Проверка калорий для всех размеров")
    void testCaloriesForAllSizes() {
        assertEquals(1000, smallPizza.getCalories());
        assertEquals(1500, mediumPizza.getCalories());
        assertEquals(2000, largePizza.getCalories());
        assertEquals(2500, extraLargePizza.getCalories());
    }

    @Test
    @DisplayName("Проверка что пицца острая")
    void testIsSpicy() {
        assertTrue(smallPizza.isSpicy());
        assertTrue(mediumPizza.isSpicy());
    }

    @Test
    @DisplayName("Проверка уровня остроты")
    void testSpicyLevel() {
        assertEquals(2, smallPizza.getSpicyLevel());
    }

    @Test
    @DisplayName("Проверка базовой цены")
    void testBasePrice() {
        assertEquals(400.0, smallPizza.getBasePrice(), 0.01);
    }

    @Test
    @DisplayName("Проверка цены с учетом размера")
    void testPriceWithSize() {
        // Маленькая: 400 * 1.0 = 400
        assertEquals(400.0, smallPizza.getPrice(), 0.01);
        // Средняя: 400 * 1.5 = 600
        assertEquals(600.0, mediumPizza.getPrice(), 0.01);
        // Большая: 400 * 2.0 = 800
        assertEquals(800.0, largePizza.getPrice(), 0.01);
        // Очень большая: 400 * 2.5 = 1000
        assertEquals(1000.0, extraLargePizza.getPrice(), 0.01);
    }

    @Test
    @DisplayName("Время приготовления")
    void testPreparationTime() {
        assertEquals(20, smallPizza.getPreparationTime());
    }

    @Test
    @DisplayName("Проверка описания")
    void testDescription() {
        assertNotNull(smallPizza.getDescription());
        assertTrue(smallPizza.getDescription().contains("пепперони"));
    }

    @Test
    @DisplayName("Обработка платежа")
    void testProcessPayment() throws InvalidPaymentException {
        assertTrue(smallPizza.processPayment(400.0));
        assertTrue(largePizza.processPayment(800.0));
    }
}
