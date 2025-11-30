package com.pizzeria.model;

import com.pizzeria.enums.PaymentMethod;
import com.pizzeria.model.payment.Payment;
import com.pizzeria.model.products.Product;
import com.pizzeria.model.users.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Тесты для класса Receipt")
class ReceiptTest {

    private Order mockOrder;
    private Payment mockPayment;
    private Customer mockCustomer;

    @BeforeEach
    void setUp() {
        mockOrder = Mockito.mock(Order.class);
        mockPayment = Mockito.mock(Payment.class);
        mockCustomer = Mockito.mock(Customer.class);

        when(mockOrder.getCustomer()).thenReturn(mockCustomer);
        when(mockCustomer.getFullName()).thenReturn("Иван Иванов");
        when(mockPayment.getMethod()).thenReturn(PaymentMethod.CASH);
    }

    @Nested
    @DisplayName("Тесты конструктора")
    class ConstructorTests {

        @Test
        @DisplayName("Успешное создание чека")
        void shouldCreateReceiptWithValidData() {
            // given & when
            Receipt receipt = new Receipt("R-12345", mockOrder, mockPayment);

            // then
            assertNotNull(receipt);
            assertEquals("R-12345", receipt.getReceiptNumber());
            assertEquals(mockOrder, receipt.getOrder());
            assertEquals(mockPayment, receipt.getPayment());
            assertNotNull(receipt.getIssueTime());
        }

        @Test
        @DisplayName("Время выдачи устанавливается при создании")
        void shouldSetIssueTimeOnCreation() {
            // given
            LocalDateTime before = LocalDateTime.now();

            // when
            Receipt receipt = new Receipt("R-12345", mockOrder, mockPayment);

            // then
            LocalDateTime after = LocalDateTime.now();
            assertTrue(!receipt.getIssueTime().isBefore(before));
            assertTrue(!receipt.getIssueTime().isAfter(after));
        }
    }

    @Nested
    @DisplayName("Тесты метода generateReceipt")
    class GenerateReceiptTests {

        @Test
        @DisplayName("Генерация чека с базовой информацией")
        void shouldGenerateReceiptWithBasicInfo() {
            // given
            when(mockOrder.getItems()).thenReturn(List.of());
            when(mockOrder.getPrice()).thenReturn(1000.0);
            when(mockOrder.getDiscountPercentage()).thenReturn(0.0);
            when(mockOrder.calculateDeliveryCost()).thenReturn(0.0);
            when(mockOrder.getFinalPrice()).thenReturn(1000.0);

            Receipt receipt = new Receipt("R-12345", mockOrder, mockPayment);

            // when
            String receiptText = receipt.generateReceipt();

            // then
            assertNotNull(receiptText);
            assertTrue(receiptText.contains("ЧЕК №R-12345"));
            assertTrue(receiptText.contains("Клиент: Иван Иванов"));
            assertTrue(receiptText.contains("1000"));
            assertTrue(receiptText.contains("Наличные"));
        }

        @Test
        @DisplayName("Генерация чека с товарами")
        void shouldGenerateReceiptWithItems() {
            // given
            Product mockProduct1 = Mockito.mock(Product.class);
            when(mockProduct1.getName()).thenReturn("Пицца Маргарита");

            Product mockProduct2 = Mockito.mock(Product.class);
            when(mockProduct2.getName()).thenReturn("Напиток Кола");

            OrderItem item1 = new OrderItem(mockProduct1, 2);
            OrderItem item2 = new OrderItem(mockProduct2, 1);

            when(mockOrder.getItems()).thenReturn(Arrays.asList(item1, item2));
            when(mockOrder.getPrice()).thenReturn(1500.0);
            when(mockOrder.getDiscountPercentage()).thenReturn(0.0);
            when(mockOrder.calculateDeliveryCost()).thenReturn(0.0);
            when(mockOrder.getFinalPrice()).thenReturn(1500.0);

            Receipt receipt = new Receipt("R-12345", mockOrder, mockPayment);

            // when
            String receiptText = receipt.generateReceipt();

            // then
            assertTrue(receiptText.contains("Пицца Маргарита"));
            assertTrue(receiptText.contains("Напиток Кола"));
            assertTrue(receiptText.contains("x2"));
            assertTrue(receiptText.contains("x1"));
        }

        @Test
        @DisplayName("Генерация чека со скидкой")
        void shouldGenerateReceiptWithDiscount() {
            // given
            when(mockOrder.getItems()).thenReturn(List.of());
            when(mockOrder.getPrice()).thenReturn(1000.0);
            when(mockOrder.getDiscountPercentage()).thenReturn(10.0);
            when(mockOrder.calculateDeliveryCost()).thenReturn(0.0);
            when(mockOrder.getFinalPrice()).thenReturn(900.0);

            Receipt receipt = new Receipt("R-12345", mockOrder, mockPayment);

            // when
            String receiptText = receipt.generateReceipt();

            // then
            assertTrue(receiptText.contains("Скидка: 10%"));
        }

        @Test
        @DisplayName("Генерация чека без скидки не показывает строку скидки")
        void shouldNotShowDiscountLineWhenNoDiscount() {
            // given
            when(mockOrder.getItems()).thenReturn(List.of());
            when(mockOrder.getPrice()).thenReturn(1000.0);
            when(mockOrder.getDiscountPercentage()).thenReturn(0.0);
            when(mockOrder.calculateDeliveryCost()).thenReturn(0.0);
            when(mockOrder.getFinalPrice()).thenReturn(1000.0);

            Receipt receipt = new Receipt("R-12345", mockOrder, mockPayment);

            // when
            String receiptText = receipt.generateReceipt();

            // then
            assertFalse(receiptText.contains("Скидка:"));
        }

        @Test
        @DisplayName("Генерация чека с доставкой")
        void shouldGenerateReceiptWithDelivery() {
            // given
            when(mockOrder.getItems()).thenReturn(List.of());
            when(mockOrder.getPrice()).thenReturn(1000.0);
            when(mockOrder.getDiscountPercentage()).thenReturn(0.0);
            when(mockOrder.calculateDeliveryCost()).thenReturn(150.0);
            when(mockOrder.getFinalPrice()).thenReturn(1150.0);

            Receipt receipt = new Receipt("R-12345", mockOrder, mockPayment);

            // when
            String receiptText = receipt.generateReceipt();

            // then
            assertTrue(receiptText.contains("Доставка: 150"));
        }

        @Test
        @DisplayName("Генерация чека без доставки не показывает строку доставки")
        void shouldNotShowDeliveryLineWhenNoDelivery() {
            // given
            when(mockOrder.getItems()).thenReturn(List.of());
            when(mockOrder.getPrice()).thenReturn(1000.0);
            when(mockOrder.getDiscountPercentage()).thenReturn(0.0);
            when(mockOrder.calculateDeliveryCost()).thenReturn(0.0);
            when(mockOrder.getFinalPrice()).thenReturn(1000.0);

            Receipt receipt = new Receipt("R-12345", mockOrder, mockPayment);

            // when
            String receiptText = receipt.generateReceipt();

            // then
            assertFalse(receiptText.contains("Доставка:"));
        }

        @Test
        @DisplayName("Генерация чека с различными методами оплаты")
        void shouldGenerateReceiptWithDifferentPaymentMethods() {
            // given
            when(mockOrder.getItems()).thenReturn(List.of());
            when(mockOrder.getPrice()).thenReturn(1000.0);
            when(mockOrder.getDiscountPercentage()).thenReturn(0.0);
            when(mockOrder.calculateDeliveryCost()).thenReturn(0.0);
            when(mockOrder.getFinalPrice()).thenReturn(1000.0);

            // when & then - CASH
            when(mockPayment.getMethod()).thenReturn(PaymentMethod.CASH);
            Receipt receipt1 = new Receipt("R-1", mockOrder, mockPayment);
            assertTrue(receipt1.generateReceipt().contains("Наличные"));

            // when & then - CARD
            when(mockPayment.getMethod()).thenReturn(PaymentMethod.CARD);
            Receipt receipt2 = new Receipt("R-2", mockOrder, mockPayment);
            assertTrue(receipt2.generateReceipt().contains("Банковская карта"));

            // when & then - ONLINE
            when(mockPayment.getMethod()).thenReturn(PaymentMethod.ONLINE);
            Receipt receipt3 = new Receipt("R-3", mockOrder, mockPayment);
            assertTrue(receipt3.generateReceipt().contains("Онлайн оплата"));

            // when & then - CRYPTO
            when(mockPayment.getMethod()).thenReturn(PaymentMethod.CRYPTO);
            Receipt receipt4 = new Receipt("R-4", mockOrder, mockPayment);
            assertTrue(receipt4.generateReceipt().contains("Криптовалюта"));
        }

        @Test
        @DisplayName("Генерация полного чека со всеми элементами")
        void shouldGenerateCompleteReceipt() {
            // given
            Product mockProduct = Mockito.mock(Product.class);
            when(mockProduct.getName()).thenReturn("Пицца Маргарита");
            OrderItem item = new OrderItem(mockProduct, 2);

            when(mockOrder.getItems()).thenReturn(List.of(item));
            when(mockOrder.getPrice()).thenReturn(1000.0);
            when(mockOrder.getDiscountPercentage()).thenReturn(10.0);
            when(mockOrder.calculateDeliveryCost()).thenReturn(150.0);
            when(mockOrder.getFinalPrice()).thenReturn(1035.0); // (1000 + 150) * 0.9

            Receipt receipt = new Receipt("R-FULL", mockOrder, mockPayment);

            // when
            String receiptText = receipt.generateReceipt();

            // then
            assertTrue(receiptText.contains("ЧЕК №R-FULL"));
            assertTrue(receiptText.contains("Иван Иванов"));
            assertTrue(receiptText.contains("Пицца Маргарита"));
            assertTrue(receiptText.contains("Скидка: 10%"));
            assertTrue(receiptText.contains("Доставка: 150"));
            assertTrue(receiptText.contains("ИТОГО"));
            assertTrue(receiptText.contains("Спасибо за ваш заказ!"));
        }
    }

    @Nested
    @DisplayName("Тесты метода print")
    class PrintTests {

        @Test
        @DisplayName("Метод print не выбрасывает исключений")
        void shouldNotThrowExceptionWhenPrinting() {
            // given
            when(mockOrder.getItems()).thenReturn(List.of());
            when(mockOrder.getPrice()).thenReturn(1000.0);
            when(mockOrder.getDiscountPercentage()).thenReturn(0.0);
            when(mockOrder.calculateDeliveryCost()).thenReturn(0.0);
            when(mockOrder.getFinalPrice()).thenReturn(1000.0);

            Receipt receipt = new Receipt("R-12345", mockOrder, mockPayment);

            // when & then
            assertDoesNotThrow(() -> receipt.print());
        }
    }

    @Nested
    @DisplayName("Тесты геттеров и сеттеров")
    class GetterSetterTests {

        @Test
        @DisplayName("Установка и получение номера чека")
        void shouldSetAndGetReceiptNumber() {
            // given
            Receipt receipt = new Receipt("R-12345", mockOrder, mockPayment);

            // when
            receipt.setReceiptNumber("R-99999");

            // then
            assertEquals("R-99999", receipt.getReceiptNumber());
        }

        @Test
        @DisplayName("Установка и получение заказа")
        void shouldSetAndGetOrder() {
            // given
            Receipt receipt = new Receipt("R-12345", mockOrder, mockPayment);
            Order newOrder = Mockito.mock(Order.class);

            // when
            receipt.setOrder(newOrder);

            // then
            assertEquals(newOrder, receipt.getOrder());
        }

        @Test
        @DisplayName("Установка и получение платежа")
        void shouldSetAndGetPayment() {
            // given
            Receipt receipt = new Receipt("R-12345", mockOrder, mockPayment);
            Payment newPayment = Mockito.mock(Payment.class);

            // when
            receipt.setPayment(newPayment);

            // then
            assertEquals(newPayment, receipt.getPayment());
        }

        @Test
        @DisplayName("Установка и получение времени выдачи")
        void shouldSetAndGetIssueTime() {
            // given
            Receipt receipt = new Receipt("R-12345", mockOrder, mockPayment);
            LocalDateTime newTime = LocalDateTime.of(2024, 6, 15, 10, 30);

            // when
            receipt.setIssueTime(newTime);

            // then
            assertEquals(newTime, receipt.getIssueTime());
        }
    }

    @Nested
    @DisplayName("Граничные случаи")
    class EdgeCaseTests {

        @Test
        @DisplayName("Создание чека с пустым номером")
        void shouldCreateReceiptWithEmptyNumber() {
            // given & when
            Receipt receipt = new Receipt("", mockOrder, mockPayment);

            // then
            assertNotNull(receipt);
            assertEquals("", receipt.getReceiptNumber());
        }

        @Test
        @DisplayName("Создание чека с null номером")
        void shouldCreateReceiptWithNullNumber() {
            // given & when
            Receipt receipt = new Receipt(null, mockOrder, mockPayment);

            // then
            assertNotNull(receipt);
            assertNull(receipt.getReceiptNumber());
        }

        @Test
        @DisplayName("Генерация чека с пустым списком товаров")
        void shouldGenerateReceiptWithEmptyItemsList() {
            // given
            when(mockOrder.getItems()).thenReturn(List.of());
            when(mockOrder.getPrice()).thenReturn(0.0);
            when(mockOrder.getDiscountPercentage()).thenReturn(0.0);
            when(mockOrder.calculateDeliveryCost()).thenReturn(0.0);
            when(mockOrder.getFinalPrice()).thenReturn(0.0);

            Receipt receipt = new Receipt("R-12345", mockOrder, mockPayment);

            // when
            String receiptText = receipt.generateReceipt();

            // then
            assertNotNull(receiptText);
            assertTrue(receiptText.contains("ЧЕК №R-12345"));
        }

        @Test
        @DisplayName("Генерация чека с очень большими суммами")
        void shouldGenerateReceiptWithLargeAmounts() {
            // given
            when(mockOrder.getItems()).thenReturn(List.of());
            when(mockOrder.getPrice()).thenReturn(999999.99);
            when(mockOrder.getDiscountPercentage()).thenReturn(50.0);
            when(mockOrder.calculateDeliveryCost()).thenReturn(10000.0);
            when(mockOrder.getFinalPrice()).thenReturn(509999.995);

            Receipt receipt = new Receipt("R-12345", mockOrder, mockPayment);

            // when
            String receiptText = receipt.generateReceipt();

            // then
            assertNotNull(receiptText);
            assertTrue(receiptText.contains("999999"));
            assertTrue(receiptText.contains("10000"));
        }

        @Test
        @DisplayName("Генерация чека с дробными скидками")
        void shouldGenerateReceiptWithFractionalDiscount() {
            // given
            when(mockOrder.getItems()).thenReturn(List.of());
            when(mockOrder.getPrice()).thenReturn(1000.0);
            when(mockOrder.getDiscountPercentage()).thenReturn(12.5);
            when(mockOrder.calculateDeliveryCost()).thenReturn(0.0);
            when(mockOrder.getFinalPrice()).thenReturn(875.0);

            Receipt receipt = new Receipt("R-12345", mockOrder, mockPayment);

            // when
            String receiptText = receipt.generateReceipt();

            // then
            assertTrue(receiptText.contains("Скидка: 12%") || receiptText.contains("Скидка: 13%"));
        }

        @Test
        @DisplayName("Генерация чека с максимальной скидкой 100%")
        void shouldGenerateReceiptWith100PercentDiscount() {
            // given
            when(mockOrder.getItems()).thenReturn(List.of());
            when(mockOrder.getPrice()).thenReturn(1000.0);
            when(mockOrder.getDiscountPercentage()).thenReturn(100.0);
            when(mockOrder.calculateDeliveryCost()).thenReturn(0.0);
            when(mockOrder.getFinalPrice()).thenReturn(0.0);

            Receipt receipt = new Receipt("R-12345", mockOrder, mockPayment);

            // when
            String receiptText = receipt.generateReceipt();

            // then
            assertTrue(receiptText.contains("Скидка: 100%"));
            assertTrue(receiptText.contains("ИТОГО: 0"));
        }

        @Test
        @DisplayName("Генерация чека с null клиентом")
        void shouldHandleNullCustomer() {
            // given
            when(mockOrder.getCustomer()).thenReturn(null);
            when(mockOrder.getItems()).thenReturn(List.of());
            when(mockOrder.getPrice()).thenReturn(1000.0);
            when(mockOrder.getDiscountPercentage()).thenReturn(0.0);
            when(mockOrder.calculateDeliveryCost()).thenReturn(0.0);
            when(mockOrder.getFinalPrice()).thenReturn(1000.0);

            Receipt receipt = new Receipt("R-12345", mockOrder, mockPayment);

            // when & then
            assertThrows(NullPointerException.class, () -> receipt.generateReceipt());
        }
    }
}
