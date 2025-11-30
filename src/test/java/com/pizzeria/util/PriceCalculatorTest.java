package com.pizzeria.util;

import com.pizzeria.model.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тесты для PriceCalculator")
class PriceCalculatorTest {

    @Mock
    private Order mockOrder;

    @Test
    @DisplayName("Расчет цены с налогом")
    void testCalculateWithTax_StandardRate_ReturnsCorrectPrice() {
        // Arrange
        double price = 1000.0;
        double taxRate = 20.0;

        // Act
        double result = PriceCalculator.calculateWithTax(price, taxRate);

        // Assert
        assertEquals(1200.0, result, 0.01);
    }

    @Test
    @DisplayName("Расчет цены с нулевым налогом")
    void testCalculateWithTax_ZeroTax_ReturnsSamePrice() {
        // Arrange
        double price = 1000.0;
        double taxRate = 0.0;

        // Act
        double result = PriceCalculator.calculateWithTax(price, taxRate);

        // Assert
        assertEquals(1000.0, result, 0.01);
    }

    @Test
    @DisplayName("Расчет цены с малым налогом")
    void testCalculateWithTax_SmallTax_ReturnsCorrectPrice() {
        // Arrange
        double price = 100.0;
        double taxRate = 5.0;

        // Act
        double result = PriceCalculator.calculateWithTax(price, taxRate);

        // Assert
        assertEquals(105.0, result, 0.01);
    }

    @Test
    @DisplayName("Расчет цены с налогом для большой суммы")
    void testCalculateWithTax_LargeAmount_ReturnsCorrectPrice() {
        // Arrange
        double price = 10000.0;
        double taxRate = 13.0;

        // Act
        double result = PriceCalculator.calculateWithTax(price, taxRate);

        // Assert
        assertEquals(11300.0, result, 0.01);
    }

    @Test
    @DisplayName("Расчет цены со скидкой")
    void testCalculateWithDiscount_StandardDiscount_ReturnsCorrectPrice() {
        // Arrange
        double price = 1000.0;
        double discountPercentage = 20.0;

        // Act
        double result = PriceCalculator.calculateWithDiscount(price, discountPercentage);

        // Assert
        assertEquals(800.0, result, 0.01);
    }

    @Test
    @DisplayName("Расчет цены с нулевой скидкой")
    void testCalculateWithDiscount_ZeroDiscount_ReturnsSamePrice() {
        // Arrange
        double price = 1000.0;
        double discountPercentage = 0.0;

        // Act
        double result = PriceCalculator.calculateWithDiscount(price, discountPercentage);

        // Assert
        assertEquals(1000.0, result, 0.01);
    }

    @Test
    @DisplayName("Расчет цены со 100% скидкой")
    void testCalculateWithDiscount_FullDiscount_ReturnsZero() {
        // Arrange
        double price = 1000.0;
        double discountPercentage = 100.0;

        // Act
        double result = PriceCalculator.calculateWithDiscount(price, discountPercentage);

        // Assert
        assertEquals(0.0, result, 0.01);
    }

    @Test
    @DisplayName("Расчет цены со скидкой 50%")
    void testCalculateWithDiscount_HalfDiscount_ReturnsHalfPrice() {
        // Arrange
        double price = 500.0;
        double discountPercentage = 50.0;

        // Act
        double result = PriceCalculator.calculateWithDiscount(price, discountPercentage);

        // Assert
        assertEquals(250.0, result, 0.01);
    }

    @Test
    @DisplayName("Расчет чаевых")
    void testCalculateTip_StandardTip_ReturnsCorrectAmount() {
        // Arrange
        double amount = 1000.0;
        double tipPercentage = 10.0;

        // Act
        double result = PriceCalculator.calculateTip(amount, tipPercentage);

        // Assert
        assertEquals(100.0, result, 0.01);
    }

    @Test
    @DisplayName("Расчет чаевых 15%")
    void testCalculateTip_FifteenPercent_ReturnsCorrectAmount() {
        // Arrange
        double amount = 200.0;
        double tipPercentage = 15.0;

        // Act
        double result = PriceCalculator.calculateTip(amount, tipPercentage);

        // Assert
        assertEquals(30.0, result, 0.01);
    }

    @Test
    @DisplayName("Расчет чаевых с нулевым процентом")
    void testCalculateTip_ZeroTip_ReturnsZero() {
        // Arrange
        double amount = 1000.0;
        double tipPercentage = 0.0;

        // Act
        double result = PriceCalculator.calculateTip(amount, tipPercentage);

        // Assert
        assertEquals(0.0, result, 0.01);
    }

    @Test
    @DisplayName("Округление до двух знаков после запятой")
    void testRoundToTwoDecimals_StandardValue_ReturnsRounded() {
        // Arrange
        double value = 123.456;

        // Act
        double result = PriceCalculator.roundToTwoDecimals(value);

        // Assert
        assertEquals(123.46, result, 0.001);
    }

    @Test
    @DisplayName("Округление значения с одним знаком")
    void testRoundToTwoDecimals_OneDecimal_ReturnsRounded() {
        // Arrange
        double value = 100.1;

        // Act
        double result = PriceCalculator.roundToTwoDecimals(value);

        // Assert
        assertEquals(100.1, result, 0.001);
    }

    @Test
    @DisplayName("Округление целого числа")
    void testRoundToTwoDecimals_WholeNumber_ReturnsSame() {
        // Arrange
        double value = 100.0;

        // Act
        double result = PriceCalculator.roundToTwoDecimals(value);

        // Assert
        assertEquals(100.0, result, 0.001);
    }

    @Test
    @DisplayName("Округление малого значения")
    void testRoundToTwoDecimals_SmallValue_ReturnsRounded() {
        // Arrange
        double value = 0.123;

        // Act
        double result = PriceCalculator.roundToTwoDecimals(value);

        // Assert
        assertEquals(0.12, result, 0.001);
    }

    @Test
    @DisplayName("Округление значения с округлением вверх")
    void testRoundToTwoDecimals_RoundUp_ReturnsRounded() {
        // Arrange
        double value = 99.995;

        // Act
        double result = PriceCalculator.roundToTwoDecimals(value);

        // Assert
        assertEquals(100.0, result, 0.001);
    }

    @Test
    @DisplayName("Расчет общей стоимости заказа")
    void testCalculateOrderTotal_WithTaxAndTip_ReturnsCorrectTotal() {
        // Arrange
        when(mockOrder.getPrice()).thenReturn(1000.0);
        double taxRate = 10.0;
        double tipPercentage = 15.0;

        // Act
        double result = PriceCalculator.calculateOrderTotal(mockOrder, taxRate, tipPercentage);

        // Assert
        // 1000 + 10% налог = 1100
        // 1100 + 15% чаевых = 1100 + 165 = 1265
        assertEquals(1265.0, result, 0.01);
    }

    @Test
    @DisplayName("Расчет общей стоимости заказа без налога и чаевых")
    void testCalculateOrderTotal_NoTaxNoTip_ReturnsBasePrice() {
        // Arrange
        when(mockOrder.getPrice()).thenReturn(500.0);
        double taxRate = 0.0;
        double tipPercentage = 0.0;

        // Act
        double result = PriceCalculator.calculateOrderTotal(mockOrder, taxRate, tipPercentage);

        // Assert
        assertEquals(500.0, result, 0.01);
    }

    @Test
    @DisplayName("Расчет общей стоимости заказа только с налогом")
    void testCalculateOrderTotal_OnlyTax_ReturnsCorrectTotal() {
        // Arrange
        when(mockOrder.getPrice()).thenReturn(1000.0);
        double taxRate = 20.0;
        double tipPercentage = 0.0;

        // Act
        double result = PriceCalculator.calculateOrderTotal(mockOrder, taxRate, tipPercentage);

        // Assert
        assertEquals(1200.0, result, 0.01);
    }

    @Test
    @DisplayName("Расчет общей стоимости заказа только с чаевыми")
    void testCalculateOrderTotal_OnlyTip_ReturnsCorrectTotal() {
        // Arrange
        when(mockOrder.getPrice()).thenReturn(1000.0);
        double taxRate = 0.0;
        double tipPercentage = 10.0;

        // Act
        double result = PriceCalculator.calculateOrderTotal(mockOrder, taxRate, tipPercentage);

        // Assert
        // 1000 + 0% налог = 1000
        // 1000 + 10% чаевых = 1100
        assertEquals(1100.0, result, 0.01);
    }

    @Test
    @DisplayName("Форматирование цены")
    void testFormatPrice_StandardPrice_ReturnsFormattedString() {
        // Arrange
        double price = 1234.56;

        // Act
        String result = PriceCalculator.formatPrice(price);

        // Assert
        assertEquals("1234,56 руб.", result);
    }

    @Test
    @DisplayName("Форматирование целой цены")
    void testFormatPrice_WholeNumber_ReturnsFormattedString() {
        // Arrange
        double price = 1000.0;

        // Act
        String result = PriceCalculator.formatPrice(price);

        // Assert
        assertEquals("1000,00 руб.", result);
    }

    @Test
    @DisplayName("Форматирование малой цены")
    void testFormatPrice_SmallPrice_ReturnsFormattedString() {
        // Arrange
        double price = 9.99;

        // Act
        String result = PriceCalculator.formatPrice(price);

        // Assert
        assertEquals("9,99 руб.", result);
    }

    @Test
    @DisplayName("Форматирование нулевой цены")
    void testFormatPrice_ZeroPrice_ReturnsFormattedString() {
        // Arrange
        double price = 0.0;

        // Act
        String result = PriceCalculator.formatPrice(price);

        // Assert
        assertEquals("0,00 руб.", result);
    }

    @Test
    @DisplayName("Расчет с налогом для нулевой цены")
    void testCalculateWithTax_ZeroPrice_ReturnsZero() {
        // Arrange
        double price = 0.0;
        double taxRate = 20.0;

        // Act
        double result = PriceCalculator.calculateWithTax(price, taxRate);

        // Assert
        assertEquals(0.0, result, 0.01);
    }

    @Test
    @DisplayName("Расчет со скидкой для малой цены")
    void testCalculateWithDiscount_SmallPrice_ReturnsCorrectPrice() {
        // Arrange
        double price = 10.0;
        double discountPercentage = 10.0;

        // Act
        double result = PriceCalculator.calculateWithDiscount(price, discountPercentage);

        // Assert
        assertEquals(9.0, result, 0.01);
    }
}
