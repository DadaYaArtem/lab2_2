package com.pizzeria.model.products;

import com.pizzeria.enums.PizzaSize;
import com.pizzeria.exceptions.InsufficientIngredientsException;
import com.pizzeria.exceptions.InvalidPriceException;
import com.pizzeria.model.ingredients.Ingredient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тесты для MargheritaPizza")
class MargheritaPizzaTest {

    private MargheritaPizza smallPizza;
    private MargheritaPizza mediumPizza;
    private MargheritaPizza largePizza;
    private MargheritaPizza extraLargePizza;

    @Mock
    private Ingredient mockIngredient;

    @BeforeEach
    void setUp() throws InvalidPriceException {
        smallPizza = new MargheritaPizza(PizzaSize.SMALL);
        mediumPizza = new MargheritaPizza(PizzaSize.MEDIUM);
        largePizza = new MargheritaPizza(PizzaSize.LARGE);
        extraLargePizza = new MargheritaPizza(PizzaSize.EXTRA_LARGE);
    }

    @Test
    @DisplayName("Конструктор должен создать пиццу Маргарита с правильным названием")
    void testConstructorSetsName() {
        assertEquals("Маргарита", smallPizza.getName());
    }

    @Test
    @DisplayName("Конструктор должен установить правильное описание")
    void testConstructorSetsDescription() {
        assertEquals("Классическая пицца с томатным соусом, моцареллой и базиликом",
                     smallPizza.getDescription());
    }

    @Test
    @DisplayName("Конструктор должен установить время приготовления 18 минут")
    void testConstructorSetsCookingTime() {
        assertEquals(18, smallPizza.getCookingTime());
    }

    @Test
    @DisplayName("Пицца изначально не готова")
    void testPizzaInitiallyNotReady() {
        assertFalse(smallPizza.isReady());
    }

    @Test
    @DisplayName("Маленькая пицца должна иметь 800 калорий")
    void testSmallPizzaCalories() {
        assertEquals(800, smallPizza.getCalories());
    }

    @Test
    @DisplayName("Средняя пицца должна иметь 1200 калорий")
    void testMediumPizzaCalories() {
        assertEquals(1200, mediumPizza.getCalories());
    }

    @Test
    @DisplayName("Большая пицца должна иметь 1600 калорий")
    void testLargePizzaCalories() {
        assertEquals(1600, largePizza.getCalories());
    }

    @Test
    @DisplayName("Очень большая пицца должна иметь 2000 калорий")
    void testExtraLargePizzaCalories() {
        assertEquals(2000, extraLargePizza.getCalories());
    }

    @Test
    @DisplayName("Маргарита является вегетарианской")
    void testIsVegetarian() {
        assertTrue(smallPizza.isVegetarian());
    }

    @Test
    @DisplayName("Маргарита имеет итальянское происхождение")
    void testGetOrigin() {
        assertEquals("Италия, Неаполь", smallPizza.getOrigin());
    }

    @Test
    @DisplayName("Цена маленькой пиццы должна быть правильной")
    void testSmallPizzaPrice() {
        assertEquals(300.0 * 1.0, smallPizza.getPrice(), 0.01);
    }

    @Test
    @DisplayName("Цена средней пиццы должна быть правильной")
    void testMediumPizzaPrice() {
        assertEquals(300.0 * 1.5, mediumPizza.getPrice(), 0.01);
    }

    @Test
    @DisplayName("Цена большой пиццы должна быть правильной")
    void testLargePizzaPrice() {
        assertEquals(300.0 * 2.0, largePizza.getPrice(), 0.01);
    }

    @Test
    @DisplayName("Цена очень большой пиццы должна быть правильной")
    void testExtraLargePizzaPrice() {
        assertEquals(300.0 * 2.5, extraLargePizza.getPrice(), 0.01);
    }

    @Test
    @DisplayName("Применение скидки должно уменьшить итоговую цену")
    void testApplyDiscount() {
        smallPizza.applyDiscount(10);
        assertEquals(300.0 * 0.9, smallPizza.getFinalPrice(), 0.01);
    }

    @Test
    @DisplayName("Время приготовления должно быть 18 минут")
    void testGetPreparationTime() {
        assertEquals(18, smallPizza.getPreparationTime());
    }

    @Test
    @DisplayName("Приготовление пиццы с доступными ингредиентами должно вернуть время готовки")
    void testCookWithAvailableIngredients() throws InsufficientIngredientsException {
        when(mockIngredient.checkAvailability(1)).thenReturn(true);
        when(mockIngredient.getName()).thenReturn("Моцарелла");

        smallPizza.addIngredient(mockIngredient);
        int cookingTime = smallPizza.cook();

        assertEquals(18, cookingTime);
        assertTrue(smallPizza.isReady());
        verify(mockIngredient).removeStock(1);
    }

    @Test
    @DisplayName("Приготовление пиццы без достаточных ингредиентов должно выбросить исключение")
    void testCookWithInsufficientIngredients() {
        when(mockIngredient.checkAvailability(1)).thenReturn(false);
        when(mockIngredient.getName()).thenReturn("Моцарелла");
        when(mockIngredient.getQuantity()).thenReturn(0);

        smallPizza.addIngredient(mockIngredient);

        assertThrows(InsufficientIngredientsException.class, () -> smallPizza.cook());
        assertFalse(smallPizza.isReady());
    }

    @Test
    @DisplayName("Добавление ингредиента должно увеличить количество ингредиентов")
    void testAddIngredient() {
        assertEquals(0, smallPizza.getIngredients().size());
        smallPizza.addIngredient(mockIngredient);
        assertEquals(1, smallPizza.getIngredients().size());
    }

    @Test
    @DisplayName("Удаление ингредиента должно уменьшить количество ингредиентов")
    void testRemoveIngredient() {
        smallPizza.addIngredient(mockIngredient);
        assertEquals(1, smallPizza.getIngredients().size());
        smallPizza.removeIngredient(mockIngredient);
        assertEquals(0, smallPizza.getIngredients().size());
    }

    @Test
    @DisplayName("Обработка платежа с достаточной суммой должна вернуть true")
    void testProcessPaymentWithSufficientAmount() {
        assertTrue(smallPizza.processPayment(300.0));
    }

    @Test
    @DisplayName("Обработка платежа с недостаточной суммой должна вернуть false")
    void testProcessPaymentWithInsufficientAmount() {
        assertFalse(smallPizza.processPayment(100.0));
    }

    @Test
    @DisplayName("Пицца изначально доступна")
    void testPizzaInitiallyAvailable() {
        assertTrue(smallPizza.isAvailable());
    }

    @Test
    @DisplayName("Размер пиццы должен быть установлен правильно")
    void testPizzaSize() {
        assertEquals(PizzaSize.SMALL, smallPizza.getSize());
        assertEquals(PizzaSize.MEDIUM, mediumPizza.getSize());
        assertEquals(PizzaSize.LARGE, largePizza.getSize());
        assertEquals(PizzaSize.EXTRA_LARGE, extraLargePizza.getSize());
    }

    @Test
    @DisplayName("Изменение размера пиццы должно работать корректно")
    void testSetSize() {
        smallPizza.setSize(PizzaSize.LARGE);
        assertEquals(PizzaSize.LARGE, smallPizza.getSize());
    }

    @Test
    @DisplayName("Базовая цена должна быть 300 рублей")
    void testBasePrice() {
        assertEquals(300.0, smallPizza.getBasePrice(), 0.01);
    }

    @Test
    @DisplayName("toString должен возвращать правильный формат")
    void testToString() {
        String result = smallPizza.toString();
        assertTrue(result.contains("Маргарита"));
        assertTrue(result.contains("руб."));
    }
}
