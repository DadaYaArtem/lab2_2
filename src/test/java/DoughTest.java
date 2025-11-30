import com.pizzeria.exceptions.InvalidPriceException;
import com.pizzeria.model.ingredients.Dough;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Dough Tests")
class DoughTest {

    private Dough yeastDough;
    private Dough thinDough;
    private Dough thickDough;

    @BeforeEach
    void setUp() throws InvalidPriceException {
        yeastDough = new Dough("Дрожжевое тесто", 30.0, "Дрожжевое");
        thinDough = new Dough("Тонкое тесто", 25.0, "Тонкое");
        thickDough = new Dough("Толстое тесто", 35.0, "Толстое");
    }

    @Test
    @DisplayName("Создание теста")
    void testDoughCreation() {
        assertNotNull(yeastDough);
        assertEquals("Дрожжевое тесто", yeastDough.getName());
        assertEquals(30.0, yeastDough.getPricePerUnit(), 0.01);
        assertEquals("Дрожжевое", yeastDough.getDoughType());
    }

    @Test
    @DisplayName("Проверка категории")
    void testCategory() {
        assertEquals("Тесто", yeastDough.getCategory());
        assertEquals("Тесто", thinDough.getCategory());
    }

    @Test
    @DisplayName("Тесто вегетарианское")
    void testIsVegetarian() {
        assertTrue(yeastDough.isVegetarian());
        assertTrue(thinDough.isVegetarian());
    }

    @Test
    @DisplayName("Время приготовления дрожжевого теста")
    void testYeastDoughPreparationTime() {
        assertEquals(60, yeastDough.getPreparationTime());
    }

    @Test
    @DisplayName("Время приготовления обычного теста")
    void testRegularDoughPreparationTime() {
        assertEquals(30, thinDough.getPreparationTime());
        assertEquals(30, thickDough.getPreparationTime());
    }

    @Test
    @DisplayName("Проверка хрустящести тонкого теста")
    void testThinDoughIsCrispy() {
        thinDough.setThickness(2);
        assertTrue(thinDough.isCrispy());
    }

    @Test
    @DisplayName("Проверка хрустящести толстого теста")
    void testThickDoughIsNotCrispy() {
        thickDough.setThickness(5);
        assertFalse(thickDough.isCrispy());
    }

    @Test
    @DisplayName("Граничное значение толщины для хрустящести")
    void testCrispyBorderCase() {
        thinDough.setThickness(3);
        assertFalse(thinDough.isCrispy());

        thinDough.setThickness(2);
        assertTrue(thinDough.isCrispy());
    }

    @Test
    @DisplayName("Калорийность теста")
    void testCalories() {
        assertEquals(270, yeastDough.getCalories());
    }

    @Test
    @DisplayName("Единица измерения")
    void testUnit() {
        assertEquals("грамм", yeastDough.getUnit());
    }

    @Test
    @DisplayName("Установка безглютеновости")
    void testSetGlutenFree() {
        yeastDough.setGlutenFree(true);
        assertTrue(yeastDough.isGlutenFree());

        thinDough.setGlutenFree(false);
        assertFalse(thinDough.isGlutenFree());
    }

    @Test
    @DisplayName("Изменение типа теста")
    void testSetDoughType() {
        yeastDough.setDoughType("Сдобное");
        assertEquals("Сдобное", yeastDough.getDoughType());
    }

    @Test
    @DisplayName("Установка толщины")
    void testSetThickness() {
        yeastDough.setThickness(4);
        assertEquals(4, yeastDough.getThickness());
    }

    @Test
    @DisplayName("Создание теста с отрицательной ценой")
    void testCreateDoughWithNegativePrice() {
        assertThrows(InvalidPriceException.class, () -> {
            new Dough("Тестовое", -5.0, "Тест");
        });
    }

    @Test
    @DisplayName("Создание теста с нулевой ценой")
    void testCreateDoughWithZeroPrice() {
        assertThrows(InvalidPriceException.class, () -> {
            new Dough("Тестовое", 0.0, "Тест");
        });
    }
}
