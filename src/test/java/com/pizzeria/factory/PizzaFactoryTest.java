package com.pizzeria.factory;

import com.pizzeria.enums.PizzaSize;
import com.pizzeria.exceptions.InvalidPizzaSizeException;
import com.pizzeria.exceptions.InvalidPriceException;
import com.pizzeria.model.products.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты для PizzaFactory")
class PizzaFactoryTest {

    private PizzaFactory factory;

    @BeforeEach
    void setUp() {
        factory = new PizzaFactory();
    }

    @Test
    @DisplayName("Создание пиццы Маргарита на английском")
    void testCreatePizza_Margherita_English_Success() throws InvalidPriceException, InvalidPizzaSizeException {
        // Act
        Pizza pizza = factory.createPizza("margherita", PizzaSize.MEDIUM);

        // Assert
        assertNotNull(pizza);
        assertInstanceOf(MargheritaPizza.class, pizza);
        assertEquals(PizzaSize.MEDIUM, pizza.getSize());
    }

    @Test
    @DisplayName("Создание пиццы Маргарита на русском")
    void testCreatePizza_Margherita_Russian_Success() throws InvalidPriceException, InvalidPizzaSizeException {
        // Act
        Pizza pizza = factory.createPizza("маргарита", PizzaSize.LARGE);

        // Assert
        assertNotNull(pizza);
        assertInstanceOf(MargheritaPizza.class, pizza);
        assertEquals(PizzaSize.LARGE, pizza.getSize());
    }

    @Test
    @DisplayName("Создание пиццы Пепперони на английском")
    void testCreatePizza_Pepperoni_English_Success() throws InvalidPriceException, InvalidPizzaSizeException {
        // Act
        Pizza pizza = factory.createPizza("pepperoni", PizzaSize.SMALL);

        // Assert
        assertNotNull(pizza);
        assertInstanceOf(PepperoniPizza.class, pizza);
    }

    @Test
    @DisplayName("Создание пиццы Пепперони на русском")
    void testCreatePizza_Pepperoni_Russian_Success() throws InvalidPriceException, InvalidPizzaSizeException {
        // Act
        Pizza pizza = factory.createPizza("пепперони", PizzaSize.MEDIUM);

        // Assert
        assertNotNull(pizza);
        assertInstanceOf(PepperoniPizza.class, pizza);
    }

    @Test
    @DisplayName("Создание вегетарианской пиццы на английском")
    void testCreatePizza_Veggie_English_Success() throws InvalidPriceException, InvalidPizzaSizeException {
        // Act
        Pizza pizza = factory.createPizza("veggie", PizzaSize.LARGE);

        // Assert
        assertNotNull(pizza);
        assertInstanceOf(VeggiePizza.class, pizza);
    }

    @Test
    @DisplayName("Создание вегетарианской пиццы на русском")
    void testCreatePizza_Veggie_Russian_Success() throws InvalidPriceException, InvalidPizzaSizeException {
        // Act
        Pizza pizza = factory.createPizza("вегетарианская", PizzaSize.SMALL);

        // Assert
        assertNotNull(pizza);
        assertInstanceOf(VeggiePizza.class, pizza);
    }

    @Test
    @DisplayName("Создание мясной пиццы на английском")
    void testCreatePizza_Meat_English_Success() throws InvalidPriceException, InvalidPizzaSizeException {
        // Act
        Pizza pizza = factory.createPizza("meat", PizzaSize.MEDIUM);

        // Assert
        assertNotNull(pizza);
        assertInstanceOf(MeatLoversPizza.class, pizza);
    }

    @Test
    @DisplayName("Создание мясной пиццы на русском")
    void testCreatePizza_Meat_Russian_Success() throws InvalidPriceException, InvalidPizzaSizeException {
        // Act
        Pizza pizza = factory.createPizza("мясная", PizzaSize.LARGE);

        // Assert
        assertNotNull(pizza);
        assertInstanceOf(MeatLoversPizza.class, pizza);
    }

    @Test
    @DisplayName("Создание пользовательской пиццы на английском")
    void testCreatePizza_Custom_English_Success() throws InvalidPriceException, InvalidPizzaSizeException {
        // Act
        Pizza pizza = factory.createPizza("custom", PizzaSize.SMALL);

        // Assert
        assertNotNull(pizza);
        assertInstanceOf(CustomPizza.class, pizza);
    }

    @Test
    @DisplayName("Создание пользовательской пиццы на русском")
    void testCreatePizza_Custom_Russian_Success() throws InvalidPriceException, InvalidPizzaSizeException {
        // Act
        Pizza pizza = factory.createPizza("пользовательская", PizzaSize.MEDIUM);

        // Assert
        assertNotNull(pizza);
        assertInstanceOf(CustomPizza.class, pizza);
    }

    @ParameterizedTest
    @ValueSource(strings = {"MARGHERITA", "PEPPERONI", "VEGGIE", "MEAT", "CUSTOM"})
    @DisplayName("Создание пиццы с различными регистрами")
    void testCreatePizza_DifferentCases_Success(String type) throws InvalidPriceException, InvalidPizzaSizeException {
        // Act
        Pizza pizza = factory.createPizza(type, PizzaSize.MEDIUM);

        // Assert
        assertNotNull(pizza);
        assertTrue(pizza instanceof Pizza);
    }

    @Test
    @DisplayName("Создание пиццы с неизвестным типом выбрасывает исключение")
    void testCreatePizza_UnknownType_ThrowsException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            factory.createPizza("неизвестная", PizzaSize.MEDIUM);
        });

        assertTrue(exception.getMessage().contains("Неизвестный тип пиццы"));
    }

    @Test
    @DisplayName("Создание пиццы с пустым типом выбрасывает исключение")
    void testCreatePizza_EmptyType_ThrowsException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createPizza("", PizzaSize.MEDIUM);
        });
    }

    @Test
    @DisplayName("Создание пиццы с null размером выбрасывает исключение")
    void testCreatePizza_NullSize_ThrowsException() {
        // Act & Assert
        InvalidPizzaSizeException exception = assertThrows(InvalidPizzaSizeException.class, () -> {
            factory.createPizza("margherita", null);
        });

        assertTrue(exception.getMessage().contains("null"));
    }

    @Test
    @DisplayName("Создание пиццы по умолчанию")
    void testCreateDefaultPizza_Success() throws InvalidPriceException {
        // Act
        Pizza pizza = factory.createDefaultPizza();

        // Assert
        assertNotNull(pizza);
        assertInstanceOf(MargheritaPizza.class, pizza);
        assertEquals(PizzaSize.MEDIUM, pizza.getSize());
    }

    @Test
    @DisplayName("Создание большой пиццы Маргарита")
    void testCreateLargePizza_Margherita_Success() throws InvalidPriceException, InvalidPizzaSizeException {
        // Act
        Pizza pizza = factory.createLargePizza("margherita");

        // Assert
        assertNotNull(pizza);
        assertInstanceOf(MargheritaPizza.class, pizza);
        assertEquals(PizzaSize.LARGE, pizza.getSize());
    }

    @Test
    @DisplayName("Создание большой пиццы Пепперони")
    void testCreateLargePizza_Pepperoni_Success() throws InvalidPriceException, InvalidPizzaSizeException {
        // Act
        Pizza pizza = factory.createLargePizza("pepperoni");

        // Assert
        assertNotNull(pizza);
        assertInstanceOf(PepperoniPizza.class, pizza);
        assertEquals(PizzaSize.LARGE, pizza.getSize());
    }

    @Test
    @DisplayName("Создание большой пиццы неизвестного типа выбрасывает исключение")
    void testCreateLargePizza_UnknownType_ThrowsException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createLargePizza("неизвестная");
        });
    }

    @Test
    @DisplayName("Создание всех размеров пиццы Маргарита")
    void testCreatePizza_AllSizes_Success() throws InvalidPriceException, InvalidPizzaSizeException {
        // Act
        Pizza small = factory.createPizza("margherita", PizzaSize.SMALL);
        Pizza medium = factory.createPizza("margherita", PizzaSize.MEDIUM);
        Pizza large = factory.createPizza("margherita", PizzaSize.LARGE);

        // Assert
        assertNotNull(small);
        assertNotNull(medium);
        assertNotNull(large);
        assertEquals(PizzaSize.SMALL, small.getSize());
        assertEquals(PizzaSize.MEDIUM, medium.getSize());
        assertEquals(PizzaSize.LARGE, large.getSize());
    }

    @Test
    @DisplayName("Создание всех типов пицц")
    void testCreatePizza_AllTypes_Success() throws InvalidPriceException, InvalidPizzaSizeException {
        // Act
        Pizza margherita = factory.createPizza("margherita", PizzaSize.MEDIUM);
        Pizza pepperoni = factory.createPizza("pepperoni", PizzaSize.MEDIUM);
        Pizza veggie = factory.createPizza("veggie", PizzaSize.MEDIUM);
        Pizza meat = factory.createPizza("meat", PizzaSize.MEDIUM);
        Pizza custom = factory.createPizza("custom", PizzaSize.MEDIUM);

        // Assert
        assertInstanceOf(MargheritaPizza.class, margherita);
        assertInstanceOf(PepperoniPizza.class, pepperoni);
        assertInstanceOf(VeggiePizza.class, veggie);
        assertInstanceOf(MeatLoversPizza.class, meat);
        assertInstanceOf(CustomPizza.class, custom);
    }
}
