package com.pizzeria.factory;

import com.pizzeria.exceptions.InvalidPriceException;
import com.pizzeria.model.ingredients.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты для IngredientFactory")
class IngredientFactoryTest {

    private IngredientFactory factory;

    @BeforeEach
    void setUp() {
        factory = new IngredientFactory();
    }

    @Test
    @DisplayName("Создание сыра")
    void testCreateCheese_ValidType_Success() throws InvalidPriceException {
        // Act
        Cheese cheese = factory.createCheese("Моцарелла");

        // Assert
        assertNotNull(cheese);
        assertEquals("Моцарелла", cheese.getName());
        assertEquals(50.0, cheese.getPrice());
        assertEquals(100, cheese.getQuantity());
    }

    @Test
    @DisplayName("Создание разных видов сыра")
    void testCreateCheese_DifferentTypes_Success() throws InvalidPriceException {
        // Act
        Cheese mozzarella = factory.createCheese("Моцарелла");
        Cheese cheddar = factory.createCheese("Чеддер");
        Cheese parmesan = factory.createCheese("Пармезан");

        // Assert
        assertNotNull(mozzarella);
        assertNotNull(cheddar);
        assertNotNull(parmesan);
        assertEquals("Моцарелла", mozzarella.getName());
        assertEquals("Чеддер", cheddar.getName());
        assertEquals("Пармезан", parmesan.getName());
    }

    @Test
    @DisplayName("Создание мяса")
    void testCreateMeat_ValidType_Success() throws InvalidPriceException {
        // Act
        Meat meat = factory.createMeat("Пепперони");

        // Assert
        assertNotNull(meat);
        assertEquals("Пепперони", meat.getName());
        assertEquals(120.0, meat.getPrice());
        assertEquals(80, meat.getQuantity());
    }

    @Test
    @DisplayName("Создание разных видов мяса")
    void testCreateMeat_DifferentTypes_Success() throws InvalidPriceException {
        // Act
        Meat pepperoni = factory.createMeat("Пепперони");
        Meat bacon = factory.createMeat("Бекон");
        Meat sausage = factory.createMeat("Колбаса");

        // Assert
        assertNotNull(pepperoni);
        assertNotNull(bacon);
        assertNotNull(sausage);
        assertEquals("Пепперони", pepperoni.getName());
        assertEquals("Бекон", bacon.getName());
        assertEquals("Колбаса", sausage.getName());
    }

    @Test
    @DisplayName("Создание овощей")
    void testCreateVegetable_ValidName_Success() throws InvalidPriceException {
        // Act
        Vegetable vegetable = factory.createVegetable("Помидор");

        // Assert
        assertNotNull(vegetable);
        assertEquals("Помидор", vegetable.getName());
        assertEquals(30.0, vegetable.getPrice());
        assertEquals(150, vegetable.getQuantity());
    }

    @Test
    @DisplayName("Создание разных овощей")
    void testCreateVegetable_DifferentTypes_Success() throws InvalidPriceException {
        // Act
        Vegetable tomato = factory.createVegetable("Помидор");
        Vegetable onion = factory.createVegetable("Лук");
        Vegetable pepper = factory.createVegetable("Перец");

        // Assert
        assertNotNull(tomato);
        assertNotNull(onion);
        assertNotNull(pepper);
        assertEquals("Помидор", tomato.getName());
        assertEquals("Лук", onion.getName());
        assertEquals("Перец", pepper.getName());
    }

    @Test
    @DisplayName("Создание соуса")
    void testCreateSauce_ValidType_Success() throws InvalidPriceException {
        // Act
        Sauce sauce = factory.createSauce("Томатный");

        // Assert
        assertNotNull(sauce);
        assertEquals("Томатный", sauce.getName());
        assertEquals(40.0, sauce.getPrice());
        assertEquals(200, sauce.getQuantity());
    }

    @Test
    @DisplayName("Создание разных соусов")
    void testCreateSauce_DifferentTypes_Success() throws InvalidPriceException {
        // Act
        Sauce tomato = factory.createSauce("Томатный");
        Sauce white = factory.createSauce("Белый");
        Sauce bbq = factory.createSauce("BBQ");

        // Assert
        assertNotNull(tomato);
        assertNotNull(white);
        assertNotNull(bbq);
        assertEquals("Томатный", tomato.getName());
        assertEquals("Белый", white.getName());
        assertEquals("BBQ", bbq.getName());
    }

    @Test
    @DisplayName("Создание теста")
    void testCreateDough_ValidType_Success() throws InvalidPriceException {
        // Act
        Dough dough = factory.createDough("Тонкое");

        // Assert
        assertNotNull(dough);
        assertEquals("Тонкое", dough.getName());
        assertEquals(25.0, dough.getPrice());
        assertEquals(120, dough.getQuantity());
    }

    @Test
    @DisplayName("Создание разных видов теста")
    void testCreateDough_DifferentTypes_Success() throws InvalidPriceException {
        // Act
        Dough thin = factory.createDough("Тонкое");
        Dough thick = factory.createDough("Толстое");
        Dough stuffed = factory.createDough("С начинкой");

        // Assert
        assertNotNull(thin);
        assertNotNull(thick);
        assertNotNull(stuffed);
        assertEquals("Тонкое", thin.getName());
        assertEquals("Толстое", thick.getName());
        assertEquals("С начинкой", stuffed.getName());
    }

    @Test
    @DisplayName("Создание ингредиента категории сыр на английском")
    void testCreateIngredient_Cheese_English_Success() throws InvalidPriceException {
        // Act
        Ingredient ingredient = factory.createIngredient("cheese", "Моцарелла");

        // Assert
        assertNotNull(ingredient);
        assertInstanceOf(Cheese.class, ingredient);
        assertEquals("Моцарелла", ingredient.getName());
    }

    @Test
    @DisplayName("Создание ингредиента категории сыр на русском")
    void testCreateIngredient_Cheese_Russian_Success() throws InvalidPriceException {
        // Act
        Ingredient ingredient = factory.createIngredient("сыр", "Чеддер");

        // Assert
        assertNotNull(ingredient);
        assertInstanceOf(Cheese.class, ingredient);
        assertEquals("Чеддер", ingredient.getName());
    }

    @Test
    @DisplayName("Создание ингредиента категории мясо на английском")
    void testCreateIngredient_Meat_English_Success() throws InvalidPriceException {
        // Act
        Ingredient ingredient = factory.createIngredient("meat", "Бекон");

        // Assert
        assertNotNull(ingredient);
        assertInstanceOf(Meat.class, ingredient);
        assertEquals("Бекон", ingredient.getName());
    }

    @Test
    @DisplayName("Создание ингредиента категории мясо на русском")
    void testCreateIngredient_Meat_Russian_Success() throws InvalidPriceException {
        // Act
        Ingredient ingredient = factory.createIngredient("мясо", "Колбаса");

        // Assert
        assertNotNull(ingredient);
        assertInstanceOf(Meat.class, ingredient);
        assertEquals("Колбаса", ingredient.getName());
    }

    @Test
    @DisplayName("Создание ингредиента категории овощи на английском")
    void testCreateIngredient_Vegetable_English_Success() throws InvalidPriceException {
        // Act
        Ingredient ingredient = factory.createIngredient("vegetable", "Лук");

        // Assert
        assertNotNull(ingredient);
        assertInstanceOf(Vegetable.class, ingredient);
        assertEquals("Лук", ingredient.getName());
    }

    @Test
    @DisplayName("Создание ингредиента категории овощи на русском")
    void testCreateIngredient_Vegetable_Russian_Success() throws InvalidPriceException {
        // Act
        Ingredient ingredient = factory.createIngredient("овощи", "Перец");

        // Assert
        assertNotNull(ingredient);
        assertInstanceOf(Vegetable.class, ingredient);
        assertEquals("Перец", ingredient.getName());
    }

    @Test
    @DisplayName("Создание ингредиента категории соус на английском")
    void testCreateIngredient_Sauce_English_Success() throws InvalidPriceException {
        // Act
        Ingredient ingredient = factory.createIngredient("sauce", "Томатный");

        // Assert
        assertNotNull(ingredient);
        assertInstanceOf(Sauce.class, ingredient);
        assertEquals("Томатный", ingredient.getName());
    }

    @Test
    @DisplayName("Создание ингредиента категории соус на русском")
    void testCreateIngredient_Sauce_Russian_Success() throws InvalidPriceException {
        // Act
        Ingredient ingredient = factory.createIngredient("соус", "Белый");

        // Assert
        assertNotNull(ingredient);
        assertInstanceOf(Sauce.class, ingredient);
        assertEquals("Белый", ingredient.getName());
    }

    @Test
    @DisplayName("Создание ингредиента категории тесто на английском")
    void testCreateIngredient_Dough_English_Success() throws InvalidPriceException {
        // Act
        Ingredient ingredient = factory.createIngredient("dough", "Тонкое");

        // Assert
        assertNotNull(ingredient);
        assertInstanceOf(Dough.class, ingredient);
        assertEquals("Тонкое", ingredient.getName());
    }

    @Test
    @DisplayName("Создание ингредиента категории тесто на русском")
    void testCreateIngredient_Dough_Russian_Success() throws InvalidPriceException {
        // Act
        Ingredient ingredient = factory.createIngredient("тесто", "Толстое");

        // Assert
        assertNotNull(ingredient);
        assertInstanceOf(Dough.class, ingredient);
        assertEquals("Толстое", ingredient.getName());
    }

    @ParameterizedTest
    @ValueSource(strings = {"CHEESE", "MEAT", "VEGETABLE", "SAUCE", "DOUGH"})
    @DisplayName("Создание ингредиентов с различными регистрами категорий")
    void testCreateIngredient_DifferentCases_Success(String category) throws InvalidPriceException {
        // Act
        Ingredient ingredient = factory.createIngredient(category, "Тестовый");

        // Assert
        assertNotNull(ingredient);
        assertTrue(ingredient instanceof Ingredient);
    }

    @Test
    @DisplayName("Создание ингредиента с неизвестной категорией выбрасывает исключение")
    void testCreateIngredient_UnknownCategory_ThrowsException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            factory.createIngredient("неизвестная", "Тест");
        });

        assertTrue(exception.getMessage().contains("Неизвестная категория"));
    }

    @Test
    @DisplayName("Создание ингредиента с пустой категорией выбрасывает исключение")
    void testCreateIngredient_EmptyCategory_ThrowsException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createIngredient("", "Тест");
        });
    }

    @Test
    @DisplayName("Все ингредиенты имеют корректные базовые цены")
    void testIngredientPrices_AllTypes_CorrectPrices() throws InvalidPriceException {
        // Act
        Cheese cheese = factory.createCheese("Тест");
        Meat meat = factory.createMeat("Тест");
        Vegetable vegetable = factory.createVegetable("Тест");
        Sauce sauce = factory.createSauce("Тест");
        Dough dough = factory.createDough("Тест");

        // Assert
        assertEquals(50.0, cheese.getPrice());
        assertEquals(120.0, meat.getPrice());
        assertEquals(30.0, vegetable.getPrice());
        assertEquals(40.0, sauce.getPrice());
        assertEquals(25.0, dough.getPrice());
    }

    @Test
    @DisplayName("Все ингредиенты имеют корректные количества")
    void testIngredientQuantities_AllTypes_CorrectQuantities() throws InvalidPriceException {
        // Act
        Cheese cheese = factory.createCheese("Тест");
        Meat meat = factory.createMeat("Тест");
        Vegetable vegetable = factory.createVegetable("Тест");
        Sauce sauce = factory.createSauce("Тест");
        Dough dough = factory.createDough("Тест");

        // Assert
        assertEquals(100, cheese.getQuantity());
        assertEquals(80, meat.getQuantity());
        assertEquals(150, vegetable.getQuantity());
        assertEquals(200, sauce.getQuantity());
        assertEquals(120, dough.getQuantity());
    }
}
