import com.pizzeria.exceptions.InvalidPriceException;
import com.pizzeria.model.OrderItem;
import com.pizzeria.model.products.Product;
import com.pizzeria.exceptions.InvalidPaymentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("OrderItem Tests")
class OrderItemTest {

    private Product testProduct;
    private OrderItem orderItem;

    // Создаем тестовую реализацию Product для тестов
    private static class TestProduct extends Product {
        public TestProduct(String name, double price, int calories) throws InvalidPriceException {
            super(name, price);
        }

        @Override
        public double getPrice() {
            return getBasePrice();
        }

        @Override
        public void applyDiscount(double discountPercentage) {
            setDiscountPercentage(discountPercentage);
        }

        @Override
        public double getFinalPrice() {
            return getBasePrice() * (1 - getDiscountPercentage() / 100);
        }

        @Override
        public int getPreparationTime() {
            return 0;
        }

        @Override
        public int getCalories() {
            return 0;
        }

        @Override
        public boolean processPayment(double amount) throws InvalidPaymentException {
            double finalPrice = getFinalPrice();
            if (amount < finalPrice) {
                throw new InvalidPaymentException(amount, "Недостаточная сумма оплаты");
            }
            return true;
        }
    }

    @BeforeEach
    void setUp() throws InvalidPriceException {
        // Создаем тестовый продукт
        testProduct = new TestProduct("Test Pizza", 500.0, 800);
        orderItem = new OrderItem(testProduct, 2);
    }

    @Test
    @DisplayName("Создание OrderItem с корректными данными")
    void testOrderItemCreation() {
        assertNotNull(orderItem);
        assertEquals(testProduct, orderItem.getProduct());
        assertEquals(2, orderItem.getQuantity());
    }

    @Test
    @DisplayName("Расчет общей стоимости")
    void testGetTotalPrice() {
        // 500.0 * 2 = 1000.0
        assertEquals(1000.0, orderItem.getTotalPrice(), 0.01);
    }

    @Test
    @DisplayName("Увеличение количества")
    void testIncreaseQuantity() {
        orderItem.increaseQuantity(3);
        assertEquals(5, orderItem.getQuantity());
    }

    @Test
    @DisplayName("Уменьшение количества")
    void testDecreaseQuantity() {
        orderItem.decreaseQuantity(1);
        assertEquals(1, orderItem.getQuantity());
    }

    @Test
    @DisplayName("Уменьшение количества не делает его отрицательным")
    void testDecreaseQuantityNotNegative() {
        orderItem.decreaseQuantity(5); // Пытаемся уменьшить больше, чем есть
        assertEquals(2, orderItem.getQuantity()); // Должно остаться 2
    }

    @Test
    @DisplayName("Установка специальных инструкций")
    void testSetSpecialInstructions() {
        String instructions = "Без лука";
        orderItem.setSpecialInstructions(instructions);
        assertEquals(instructions, orderItem.getSpecialInstructions());
    }

    @Test
    @DisplayName("Изменение количества через setter")
    void testSetQuantity() {
        orderItem.setQuantity(5);
        assertEquals(5, orderItem.getQuantity());
    }

    @Test
    @DisplayName("Проверка toString")
    void testToString() {
        String result = orderItem.toString();
        assertTrue(result.contains("Test Pizza"));
        assertTrue(result.contains("x2"));
        assertTrue(result.contains("1000"));
    }

    @Test
    @DisplayName("Изменение продукта")
    void testSetProduct() throws InvalidPriceException {
        Product newProduct = new TestProduct("New Pizza", 600.0, 900);
        orderItem.setProduct(newProduct);
        assertEquals(newProduct, orderItem.getProduct());
        assertEquals(1200.0, orderItem.getTotalPrice(), 0.01); // 600 * 2
    }

    @Test
    @DisplayName("Расчет общей стоимости с учетом скидки на продукт")
    void testGetTotalPriceWithDiscount() {
        testProduct.applyDiscount(10); // 10% скидка
        // 500 * 0.9 * 2 = 900
        assertEquals(900.0, orderItem.getTotalPrice(), 0.01);
    }
}