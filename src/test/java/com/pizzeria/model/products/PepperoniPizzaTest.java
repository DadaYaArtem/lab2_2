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
@DisplayName("Тесты для PepperoniPizza")
class PepperoniPizzaTest {

    private PepperoniPizza smallPizza;
    private PepperoniPizza mediumPizza;
    private PepperoniPizza largePizza;
    private PepperoniPizza extraLargePizza;

    @Mock
    private Ingredient mockIngredient;

    @BeforeEach
    void setUp() throws InvalidPriceException {
        smallPizza = new PepperoniPizza(PizzaSize.SMALL);
        mediumPizza = new PepperoniPizza(PizzaSize.MEDIUM);
        largePizza = new PepperoniPizza(PizzaSize.LARGE);
        extraLargePizza = new PepperoniPizza(PizzaSize.EXTRA_LARGE);
    }

    @Test
    @DisplayName("Конструктор должен создать пиццу Пепперони с правильным названием")
    void testConstructorSetsName() {
        assertEquals("Пепперони", smallPizza.getName());
    }

    @Test
    @DisplayName("Конструктор должен установить правильное описание")
    void testConstructorSetsDescription() {
        assertEquals("Пицца с пепперони, моцареллой и томатным соусом",
                     smallPizza.getDescription());
    }

    @Test
    @DisplayName("Конструктор должен установить время приготовления 20 минут")
    void testConstructorSetsCookingTime() {
        assertEquals(20, smallPizza.getCookingTime());
    }

    @Test
    @DisplayName("Пицца изначально не готова")
    void testPizzaInitiallyNotReady() {
        assertFalse(smallPizza.isReady());
    }

    @Test
    @DisplayName("Маленькая пицца должна иметь 1000 калорий")
    void testSmallPizzaCalories() {
        assertEquals(1000, smallPizza.getCalories());
    }

    @Test
    @DisplayName("Средняя пицца должна иметь 1500 калорий")
    void testMediumPizzaCalories() {
        assertEquals(1500, mediumPizza.getCalories());
    }

    @Test
    @DisplayName("Большая пицца должна иметь 2000 калорий")
    void testLargePizzaCalories() {
        assertEquals(2000, largePizza.getCalories());
    }

    @Test
    @DisplayName("Очень большая пицца должна иметь 2500 калорий")
    void testExtraLargePizzaCalories() {
        assertEquals(2500, extraLargePizza.getCalories());
    }

    @Test
    @DisplayName("Пепперони является острой пиццей")
    void testIsSpicy() {
        assertTrue(smallPizza.isSpicy());
    }

    @Test
    @DisplayName("Уровень остроты пепперони должен быть 2 из 5")
    void testGetSpicyLevel() {
        assertEquals(2, smallPizza.getSpicyLevel());
    }

    @Test
    @DisplayName("Цена маленькой пиццы должна быть правильной")
    void testSmallPizzaPrice() {
        assertEquals(400.0 * 1.0, smallPizza.getPrice(), 0.01);
    }

    @Test
    @DisplayName("Цена средней пиццы должна быть правильной")
    void testMediumPizzaPrice() {
        assertEquals(400.0 * 1.5, mediumPizza.getPrice(), 0.01);
    }

    @Test
    @DisplayName("Цена большой пиццы должна быть правильной")
    void testLargePizzaPrice() {
        assertEquals(400.0 * 2.0, largePizza.getPrice(), 0.01);
    }

    @Test
    @DisplayName("Цена очень большой пиццы должна быть правильной")
    void testExtraLargePizzaPrice() {
        assertEquals(400.0 * 2.5, extraLargePizza.getPrice(), 0.01);
    }

    @Test
    @DisplayName("Применение скидки должно уменьшить итоговую цену")
    void testApplyDiscount() {
        smallPizza.applyDiscount(20);
        assertEquals(400.0 * 0.8, smallPizza.getFinalPrice(), 0.01);
    }

    @Test
    @DisplayName("Время приготовления должно быть 20 минут")
    void testGetPreparationTime() {
        assertEquals(20, smallPizza.getPreparationTime());
    }

    @Test
    @DisplayName("Приготовление пиццы с доступными ингредиентами должно вернуть время готовки")
    void testCookWithAvailableIngredients() throws InsufficientIngredientsException {
        when(mockIngredient.checkAvailability(1)).thenReturn(true);
        when(mockIngredient.getName()).thenReturn("Пепперони");

        smallPizza.addIngredient(mockIngredient);
        int cookingTime = smallPizza.cook();

        assertEquals(20, cookingTime);
        assertTrue(smallPizza.isReady());
        verify(mockIngredient).removeStock(1);
    }

    @Test
    @DisplayName("Приготовление пиццы без достаточных ингредиентов должно выбросить исключение")
    void testCookWithInsufficientIngredients() {
        when(mockIngredient.checkAvailability(1)).thenReturn(false);
        when(mockIngredient.getName()).thenReturn("Пепперони");
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
        assertTrue(smallPizza.processPayment(400.0));
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
    @DisplayName("Базовая цена должна быть 400 рублей")
    void testBasePrice() {
        assertEquals(400.0, smallPizza.getBasePrice(), 0.01);
    }

    @Test
    @DisplayName("toString должен возвращать правильный формат")
    void testToString() {
        String result = smallPizza.toString();
        assertTrue(result.contains("Пепперони"));
        assertTrue(result.contains("руб."));
    }

    @Test
    @DisplayName("Приготовление пиццы без ингредиентов должно пройти успешно")
    void testCookWithoutIngredients() throws InsufficientIngredientsException {
        int cookingTime = smallPizza.cook();
        assertEquals(20, cookingTime);
        assertTrue(smallPizza.isReady());
    }
}
