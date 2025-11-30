import com.pizzeria.exceptions.InvalidPriceException;
import com.pizzeria.model.ingredients.Cheese;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Cheese Tests")
class CheeseTest {

    private Cheese mozzarella;
    private Cheese parmesan;
    private Cheese cheddar;

    @BeforeEach
    void setUp() throws InvalidPriceException {
        mozzarella = new Cheese("Моцарелла", 50.0, "Моцарелла");
        parmesan = new Cheese("Пармезан", 80.0, "Пармезан");
        cheddar = new Cheese("Чеддер", 60.0, "Чеддер");
    }

    @Test
    @DisplayName("Создание сыра")
    void testCheeseCreation() {
        assertNotNull(mozzarella);
        assertEquals("Моцарелла", mozzarella.getName());
        assertEquals(50.0, mozzarella.getPricePerUnit(), 0.01);
        assertEquals("Моцарелла", mozzarella.getCheeseType());
    }

    @Test
    @DisplayName("Проверка категории")
    void testCategory() {
        assertEquals("Сыр", mozzarella.getCategory());
        assertEquals("Сыр", parmesan.getCategory());
    }

    @Test
    @DisplayName("Сыр вегетарианский")
    void testIsVegetarian() {
        assertTrue(mozzarella.isVegetarian());
        assertTrue(parmesan.isVegetarian());
    }

    @Test
    @DisplayName("Сыр не подходит для веганов")
    void testNotSuitableForVegans() {
        assertFalse(mozzarella.isSuitableForVegans());
        assertFalse(parmesan.isSuitableForVegans());
    }

    @Test
    @DisplayName("Качество плавления моцареллы")
    void testMozzarellaMeltingQuality() {
        assertEquals("Отличное", mozzarella.getMeltingQuality());
    }

    @Test
    @DisplayName("Качество плавления пармезана")
    void testParmesanMeltingQuality() {
        assertEquals("Среднее", parmesan.getMeltingQuality());
    }

    @Test
    @DisplayName("Качество плавления других сыров")
    void testOtherCheeseMeltingQuality() {
        assertEquals("Хорошее", cheddar.getMeltingQuality());
    }

    @Test
    @DisplayName("Калорийность сыра")
    void testCalories() {
        assertEquals(350, mozzarella.getCalories());
    }

    @Test
    @DisplayName("Единица измерения")
    void testUnit() {
        assertEquals("грамм", mozzarella.getUnit());
    }

    @Test
    @DisplayName("Установка жирности")
    void testSetFatContent() {
        mozzarella.setFatContent(45.0);
        assertEquals(45.0, mozzarella.getFatContent(), 0.01);
    }

    @Test
    @DisplayName("Установка выдержки")
    void testSetAged() {
        parmesan.setAged(true);
        assertTrue(parmesan.isAged());

        mozzarella.setAged(false);
        assertFalse(mozzarella.isAged());
    }

    @Test
    @DisplayName("Изменение типа сыра")
    void testSetCheeseType() {
        cheddar.setCheeseType("Острый Чеддер");
        assertEquals("Острый Чеддер", cheddar.getCheeseType());
    }

    @Test
    @DisplayName("Создание сыра с отрицательной ценой")
    void testCreateCheeseWithNegativePrice() {
        assertThrows(InvalidPriceException.class, () -> {
            new Cheese("Тестовый", -10.0, "Тест");
        });
    }

    @Test
    @DisplayName("Создание сыра с нулевой ценой")
    void testCreateCheeseWithZeroPrice() {
        assertThrows(InvalidPriceException.class, () -> {
            new Cheese("Тестовый", 0.0, "Тест");
        });
    }
}
