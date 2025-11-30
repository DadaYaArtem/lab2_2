import com.pizzeria.exceptions.InvalidPriceException;
import com.pizzeria.factory.IngredientFactory;
import com.pizzeria.model.ingredients.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("IngredientFactory Tests")
class IngredientFactoryTest {

    private IngredientFactory factory;

    @BeforeEach
    void setUp() {
        factory = new IngredientFactory();
    }

    @Test
    @DisplayName("Создание фабрики")
    void testFactoryCreation() {
        assertNotNull(factory);
    }

    // ============= Тесты для createCheese =============

    @Test
    @DisplayName("Создание сыра Моцарелла")
    void testCreateMozzarellaCheese() throws Exception {
        Cheese cheese = factory.createCheese("Моцарелла");

        assertNotNull(cheese);
        assertTrue(cheese instanceof Cheese);
        assertEquals("Моцарелла", cheese.getName());
        assertEquals("Моцарелла", cheese.getCheeseType());
        assertEquals(50.0, cheese.getPricePerUnit(), 0.01);
        assertEquals(100, cheese.getQuantity());
        assertEquals("Сыр", cheese.getCategory());
    }

    @Test
    @DisplayName("Создание сыра Пармезан")
    void testCreateParmesanCheese() throws Exception {
        Cheese cheese = factory.createCheese("Пармезан");

        assertNotNull(cheese);
        assertEquals("Пармезан", cheese.getName());
        assertEquals("Пармезан", cheese.getCheeseType());
        assertEquals(100, cheese.getQuantity());
    }

    @Test
    @DisplayName("Создание сыра Чеддер")
    void testCreateCheddarCheese() throws Exception {
        Cheese cheese = factory.createCheese("Чеддер");

        assertNotNull(cheese);
        assertEquals("Чеддер", cheese.getName());
    }

    @Test
    @DisplayName("Все сыры вегетарианские")
    void testCheeseIsVegetarian() throws Exception {
        Cheese mozzarella = factory.createCheese("Моцарелла");
        Cheese parmesan = factory.createCheese("Пармезан");

        assertTrue(mozzarella.isVegetarian());
        assertTrue(parmesan.isVegetarian());
    }

    // ============= Тесты для createMeat =============

    @Test
    @DisplayName("Создание мяса Пепперони")
    void testCreatePepperoniMeat() throws Exception {
        Meat meat = factory.createMeat("Пепперони");

        assertNotNull(meat);
        assertTrue(meat instanceof Meat);
        assertEquals("Пепперони", meat.getName());
        assertEquals("Пепперони", meat.getMeatType());
        assertEquals(120.0, meat.getPricePerUnit(), 0.01);
        assertEquals(80, meat.getQuantity());
        assertEquals("Мясо", meat.getCategory());
    }

    @Test
    @DisplayName("Создание мяса Бекон")
    void testCreateBaconMeat() throws Exception {
        Meat meat = factory.createMeat("Бекон");

        assertNotNull(meat);
        assertEquals("Бекон", meat.getName());
        assertEquals("Бекон", meat.getMeatType());
        assertEquals(80, meat.getQuantity());
    }

    @Test
    @DisplayName("Создание мяса Ветчина")
    void testCreateHamMeat() throws Exception {
        Meat meat = factory.createMeat("Ветчина");

        assertNotNull(meat);
        assertEquals("Ветчина", meat.getName());
    }

    @Test
    @DisplayName("Мясо не вегетарианское")
    void testMeatIsNotVegetarian() throws Exception {
        Meat meat = factory.createMeat("Пепперони");
        assertFalse(meat.isVegetarian());
    }

    // ============= Тесты для createVegetable =============

    @Test
    @DisplayName("Создание овоща Помидор")
    void testCreateTomatoVegetable() throws Exception {
        Vegetable vegetable = factory.createVegetable("Помидор");

        assertNotNull(vegetable);
        assertTrue(vegetable instanceof Vegetable);
        assertEquals("Помидор", vegetable.getName());
        assertEquals(30.0, vegetable.getPricePerUnit(), 0.01);
        assertEquals(150, vegetable.getQuantity());
        assertEquals("Овощи", vegetable.getCategory());
    }

    @Test
    @DisplayName("Создание овоща Перец")
    void testCreatePepperVegetable() throws Exception {
        Vegetable vegetable = factory.createVegetable("Перец");

        assertNotNull(vegetable);
        assertEquals("Перец", vegetable.getName());
        assertEquals(150, vegetable.getQuantity());
    }

    @Test
    @DisplayName("Создание овоща Лук")
    void testCreateOnionVegetable() throws Exception {
        Vegetable vegetable = factory.createVegetable("Лук");

        assertNotNull(vegetable);
        assertEquals("Лук", vegetable.getName());
    }

    @Test
    @DisplayName("Овощи вегетарианские")
    void testVegetableIsVegetarian() throws Exception {
        Vegetable vegetable = factory.createVegetable("Помидор");
        assertTrue(vegetable.isVegetarian());
    }

    // ============= Тесты для createSauce =============

    @Test
    @DisplayName("Создание соуса Томатный")
    void testCreateTomatoSauce() throws Exception {
        Sauce sauce = factory.createSauce("Томатный");

        assertNotNull(sauce);
        assertTrue(sauce instanceof Sauce);
        assertEquals("Томатный", sauce.getName());
        assertEquals("Томатный", sauce.getSauceType());
        assertEquals(40.0, sauce.getPricePerUnit(), 0.01);
        assertEquals(200, sauce.getQuantity());
        assertEquals("Соус", sauce.getCategory());
    }

    @Test
    @DisplayName("Создание соуса Сливочный")
    void testCreateCreamSauce() throws Exception {
        Sauce sauce = factory.createSauce("Сливочный");

        assertNotNull(sauce);
        assertEquals("Сливочный", sauce.getName());
        assertEquals(200, sauce.getQuantity());
    }

    @Test
    @DisplayName("Создание соуса Барбекю")
    void testCreateBBQSauce() throws Exception {
        Sauce sauce = factory.createSauce("Барбекю");

        assertNotNull(sauce);
        assertEquals("Барбекю", sauce.getName());
    }

    // ============= Тесты для createDough =============

    @Test
    @DisplayName("Создание теста Тонкое")
    void testCreateThinDough() throws Exception {
        Dough dough = factory.createDough("Тонкое");

        assertNotNull(dough);
        assertTrue(dough instanceof Dough);
        assertEquals("Тонкое", dough.getName());
        assertEquals("Тонкое", dough.getDoughType());
        assertEquals(25.0, dough.getPricePerUnit(), 0.01);
        assertEquals(120, dough.getQuantity());
        assertEquals("Тесто", dough.getCategory());
    }

    @Test
    @DisplayName("Создание теста Толстое")
    void testCreateThickDough() throws Exception {
        Dough dough = factory.createDough("Толстое");

        assertNotNull(dough);
        assertEquals("Толстое", dough.getName());
        assertEquals(120, dough.getQuantity());
    }

    @Test
    @DisplayName("Создание теста Дрожжевое")
    void testCreateYeastDough() throws Exception {
        Dough dough = factory.createDough("Дрожжевое");

        assertNotNull(dough);
        assertEquals("Дрожжевое", dough.getName());
    }

    @Test
    @DisplayName("Тесто вегетарианское")
    void testDoughIsVegetarian() throws Exception {
        Dough dough = factory.createDough("Тонкое");
        assertTrue(dough.isVegetarian());
    }

    // ============= Тесты для createIngredient (универсальный метод) =============

    @Test
    @DisplayName("Создание ингредиента через универсальный метод - Cheese (английское)")
    void testCreateIngredientCheeseEnglish() throws Exception {
        Ingredient ingredient = factory.createIngredient("cheese", "Моцарелла");

        assertNotNull(ingredient);
        assertTrue(ingredient instanceof Cheese);
        assertEquals("Моцарелла", ingredient.getName());
    }

    @Test
    @DisplayName("Создание ингредиента через универсальный метод - Сыр (русское)")
    void testCreateIngredientCheeseRussian() throws Exception {
        Ingredient ingredient = factory.createIngredient("сыр", "Пармезан");

        assertNotNull(ingredient);
        assertTrue(ingredient instanceof Cheese);
        assertEquals("Пармезан", ingredient.getName());
    }

    @Test
    @DisplayName("Создание ингредиента через универсальный метод - Meat")
    void testCreateIngredientMeat() throws Exception {
        Ingredient ingredient = factory.createIngredient("meat", "Бекон");

        assertNotNull(ingredient);
        assertTrue(ingredient instanceof Meat);
        assertEquals("Бекон", ingredient.getName());
    }

    @Test
    @DisplayName("Создание ингредиента через универсальный метод - Мясо")
    void testCreateIngredientMeatRussian() throws Exception {
        Ingredient ingredient = factory.createIngredient("мясо", "Пепперони");

        assertNotNull(ingredient);
        assertTrue(ingredient instanceof Meat);
    }

    @Test
    @DisplayName("Создание ингредиента через универсальный метод - Vegetable")
    void testCreateIngredientVegetable() throws Exception {
        Ingredient ingredient = factory.createIngredient("vegetable", "Помидор");

        assertNotNull(ingredient);
        assertTrue(ingredient instanceof Vegetable);
        assertEquals("Помидор", ingredient.getName());
    }

    @Test
    @DisplayName("Создание ингредиента через универсальный метод - Овощи")
    void testCreateIngredientVegetableRussian() throws Exception {
        Ingredient ingredient = factory.createIngredient("овощи", "Перец");

        assertNotNull(ingredient);
        assertTrue(ingredient instanceof Vegetable);
    }

    @Test
    @DisplayName("Создание ингредиента через универсальный метод - Sauce")
    void testCreateIngredientSauce() throws Exception {
        Ingredient ingredient = factory.createIngredient("sauce", "Томатный");

        assertNotNull(ingredient);
        assertTrue(ingredient instanceof Sauce);
        assertEquals("Томатный", ingredient.getName());
    }

    @Test
    @DisplayName("Создание ингредиента через универсальный метод - Соус")
    void testCreateIngredientSauceRussian() throws Exception {
        Ingredient ingredient = factory.createIngredient("соус", "Барбекю");

        assertNotNull(ingredient);
        assertTrue(ingredient instanceof Sauce);
    }

    @Test
    @DisplayName("Создание ингредиента через универсальный метод - Dough")
    void testCreateIngredientDough() throws Exception {
        Ingredient ingredient = factory.createIngredient("dough", "Тонкое");

        assertNotNull(ingredient);
        assertTrue(ingredient instanceof Dough);
        assertEquals("Тонкое", ingredient.getName());
    }

    @Test
    @DisplayName("Создание ингредиента через универсальный метод - Тесто")
    void testCreateIngredientDoughRussian() throws Exception {
        Ingredient ingredient = factory.createIngredient("тесто", "Толстое");

        assertNotNull(ingredient);
        assertTrue(ingredient instanceof Dough);
    }

    @Test
    @DisplayName("Исключение при неизвестной категории")
    void testCreateIngredientUnknownCategory() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> factory.createIngredient("unknown", "Test")
        );

        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("Неизвестная категория"));
        assertTrue(exception.getMessage().contains("unknown"));
    }

    @Test
    @DisplayName("Игнорирование регистра категории")
    void testCreateIngredientCaseInsensitive() throws Exception {
        Ingredient cheese1 = factory.createIngredient("CHEESE", "Test1");
        Ingredient cheese2 = factory.createIngredient("Cheese", "Test2");
        Ingredient cheese3 = factory.createIngredient("cheese", "Test3");

        assertTrue(cheese1 instanceof Cheese);
        assertTrue(cheese2 instanceof Cheese);
        assertTrue(cheese3 instanceof Cheese);
    }

    @Test
    @DisplayName("Проверка всех категорий через универсальный метод")
    void testAllCategoriesThroughUniversalMethod() throws Exception {
        Ingredient cheese = factory.createIngredient("cheese", "Тест1");
        Ingredient meat = factory.createIngredient("meat", "Тест2");
        Ingredient vegetable = factory.createIngredient("vegetable", "Тест3");
        Ingredient sauce = factory.createIngredient("sauce", "Тест4");
        Ingredient dough = factory.createIngredient("dough", "Тест5");

        assertTrue(cheese instanceof Cheese);
        assertTrue(meat instanceof Meat);
        assertTrue(vegetable instanceof Vegetable);
        assertTrue(sauce instanceof Sauce);
        assertTrue(dough instanceof Dough);
    }

    @Test
    @DisplayName("Создание нескольких ингредиентов одного типа")
    void testCreateMultipleIngredientsOfSameType() throws Exception {
        Cheese cheese1 = factory.createCheese("Моцарелла");
        Cheese cheese2 = factory.createCheese("Пармезан");
        Cheese cheese3 = factory.createCheese("Чеддер");

        assertNotNull(cheese1);
        assertNotNull(cheese2);
        assertNotNull(cheese3);

        assertNotSame(cheese1, cheese2);
        assertNotSame(cheese2, cheese3);
    }
}
