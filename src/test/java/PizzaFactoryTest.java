import com.pizzeria.enums.PizzaSize;
import com.pizzeria.exceptions.InvalidPizzaSizeException;
import com.pizzeria.exceptions.InvalidPriceException;
import com.pizzeria.factory.PizzaFactory;
import com.pizzeria.model.products.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PizzaFactory Tests")
class PizzaFactoryTest {

    private PizzaFactory factory;

    @BeforeEach
    void setUp() {
        factory = new PizzaFactory();
    }

    @Test
    @DisplayName("Создание фабрики")
    void testFactoryCreation() {
        assertNotNull(factory);
    }

    @Test
    @DisplayName("Создание пиццы Маргарита (английское название)")
    void testCreateMargheritaEnglish() throws Exception {
        Pizza pizza = factory.createPizza("margherita", PizzaSize.MEDIUM);
        assertNotNull(pizza);
        assertTrue(pizza instanceof MargheritaPizza);
        assertEquals("Маргарита", pizza.getName());
    }

    @Test
    @DisplayName("Создание пиццы Маргарита (русское название)")
    void testCreateMargheritaRussian() throws Exception {
        Pizza pizza = factory.createPizza("маргарита", PizzaSize.SMALL);
        assertNotNull(pizza);
        assertTrue(pizza instanceof MargheritaPizza);
    }

    @Test
    @DisplayName("Создание пиццы Пепперони")
    void testCreatePepperoni() throws Exception {
        Pizza pizza = factory.createPizza("pepperoni", PizzaSize.LARGE);
        assertNotNull(pizza);
        assertTrue(pizza instanceof PepperoniPizza);
        assertEquals("Пепперони", pizza.getName());
    }

    @Test
    @DisplayName("Создание вегетарианской пиццы")
    void testCreateVeggiePizza() throws Exception {
        Pizza pizza = factory.createPizza("veggie", PizzaSize.MEDIUM);
        assertNotNull(pizza);
        assertTrue(pizza instanceof VeggiePizza);
    }

    @Test
    @DisplayName("Создание мясной пиццы")
    void testCreateMeatPizza() throws Exception {
        Pizza pizza = factory.createPizza("meat", PizzaSize.EXTRA_LARGE);
        assertNotNull(pizza);
        assertTrue(pizza instanceof MeatLoversPizza);
    }

    @Test
    @DisplayName("Создание пользовательской пиццы")
    void testCreateCustomPizza() throws Exception {
        Pizza pizza = factory.createPizza("custom", PizzaSize.MEDIUM);
        assertNotNull(pizza);
        assertTrue(pizza instanceof CustomPizza);
    }

    @Test
    @DisplayName("Создание пиццы с разными размерами")
    void testCreatePizzaWithDifferentSizes() throws Exception {
        Pizza small = factory.createPizza("margherita", PizzaSize.SMALL);
        Pizza medium = factory.createPizza("margherita", PizzaSize.MEDIUM);
        Pizza large = factory.createPizza("margherita", PizzaSize.LARGE);
        Pizza extraLarge = factory.createPizza("margherita", PizzaSize.EXTRA_LARGE);

        assertEquals(PizzaSize.SMALL, small.getSize());
        assertEquals(PizzaSize.MEDIUM, medium.getSize());
        assertEquals(PizzaSize.LARGE, large.getSize());
        assertEquals(PizzaSize.EXTRA_LARGE, extraLarge.getSize());
    }

    @Test
    @DisplayName("Создание пиццы по умолчанию")
    void testCreateDefaultPizza() throws Exception {
        Pizza pizza = factory.createDefaultPizza();
        assertNotNull(pizza);
        assertTrue(pizza instanceof MargheritaPizza);
        assertEquals(PizzaSize.MEDIUM, pizza.getSize());
    }

    @Test
    @DisplayName("Создание большой пиццы")
    void testCreateLargePizza() throws Exception {
        Pizza pizza = factory.createLargePizza("pepperoni");
        assertNotNull(pizza);
        assertTrue(pizza instanceof PepperoniPizza);
        assertEquals(PizzaSize.LARGE, pizza.getSize());
    }

    @Test
    @DisplayName("Исключение при неизвестном типе пиццы")
    void testUnknownPizzaType() {
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createPizza("unknown", PizzaSize.MEDIUM);
        });
    }

    @Test
    @DisplayName("Исключение при null размере")
    void testNullSize() {
        assertThrows(InvalidPizzaSizeException.class, () -> {
            factory.createPizza("margherita", null);
        });
    }

    @Test
    @DisplayName("Игнорирование регистра названия")
    void testCaseInsensitivePizzaType() throws Exception {
        Pizza p1 = factory.createPizza("MARGHERITA", PizzaSize.MEDIUM);
        Pizza p2 = factory.createPizza("Margherita", PizzaSize.MEDIUM);
        Pizza p3 = factory.createPizza("margherita", PizzaSize.MEDIUM);

        assertNotNull(p1);
        assertNotNull(p2);
        assertNotNull(p3);
    }
}
