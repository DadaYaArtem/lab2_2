package com.pizzeria.model;

import com.pizzeria.model.products.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Тесты для класса Menu")
class MenuTest {

    private Product createMockProduct(String name, double price, boolean available) {
        Product product = Mockito.mock(Product.class);
        when(product.getName()).thenReturn(name);
        when(product.getPrice()).thenReturn(price);
        when(product.isAvailable()).thenReturn(available);
        return product;
    }

    @Nested
    @DisplayName("Тесты конструктора")
    class ConstructorTests {

        @Test
        @DisplayName("Успешное создание меню с названием")
        void shouldCreateMenuWithName() {
            // given & when
            Menu menu = new Menu("Основное меню");

            // then
            assertNotNull(menu);
            assertEquals("Основное меню", menu.getName());
            assertTrue(menu.isActive());
            assertNotNull(menu.getProducts());
            assertTrue(menu.getProducts().isEmpty());
        }

        @Test
        @DisplayName("Меню создается активным по умолчанию")
        void shouldCreateActiveMenuByDefault() {
            // given & when
            Menu menu = new Menu("Тестовое меню");

            // then
            assertTrue(menu.isActive());
        }

        @Test
        @DisplayName("Список продуктов пустой при создании")
        void shouldHaveEmptyProductListOnCreation() {
            // given & when
            Menu menu = new Menu("Тестовое меню");

            // then
            assertEquals(0, menu.getProducts().size());
        }
    }

    @Nested
    @DisplayName("Тесты метода addProduct")
    class AddProductTests {

        @Test
        @DisplayName("Добавление продукта в меню")
        void shouldAddProductToMenu() {
            // given
            Menu menu = new Menu("Тестовое меню");
            Product product = createMockProduct("Пицца Маргарита", 500.0, true);

            // when
            menu.addProduct(product);

            // then
            assertEquals(1, menu.getProducts().size());
            assertTrue(menu.getProducts().contains(product));
        }

        @Test
        @DisplayName("Добавление нескольких продуктов")
        void shouldAddMultipleProducts() {
            // given
            Menu menu = new Menu("Тестовое меню");
            Product product1 = createMockProduct("Пицца Маргарита", 500.0, true);
            Product product2 = createMockProduct("Пицца Пепперони", 600.0, true);
            Product product3 = createMockProduct("Напиток Кола", 100.0, true);

            // when
            menu.addProduct(product1);
            menu.addProduct(product2);
            menu.addProduct(product3);

            // then
            assertEquals(3, menu.getProducts().size());
        }

        @Test
        @DisplayName("Добавление дубликата продукта")
        void shouldAddDuplicateProduct() {
            // given
            Menu menu = new Menu("Тестовое меню");
            Product product = createMockProduct("Пицца Маргарита", 500.0, true);

            // when
            menu.addProduct(product);
            menu.addProduct(product);

            // then
            assertEquals(2, menu.getProducts().size());
        }

        @Test
        @DisplayName("Добавление недоступного продукта")
        void shouldAddUnavailableProduct() {
            // given
            Menu menu = new Menu("Тестовое меню");
            Product product = createMockProduct("Пицца Маргарита", 500.0, false);

            // when
            menu.addProduct(product);

            // then
            assertEquals(1, menu.getProducts().size());
            assertTrue(menu.getProducts().contains(product));
        }
    }

    @Nested
    @DisplayName("Тесты метода removeProduct")
    class RemoveProductTests {

        @Test
        @DisplayName("Удаление продукта из меню")
        void shouldRemoveProductFromMenu() {
            // given
            Menu menu = new Menu("Тестовое меню");
            Product product = createMockProduct("Пицца Маргарита", 500.0, true);
            menu.addProduct(product);

            // when
            menu.removeProduct(product);

            // then
            assertEquals(0, menu.getProducts().size());
            assertFalse(menu.getProducts().contains(product));
        }

        @Test
        @DisplayName("Удаление несуществующего продукта")
        void shouldHandleRemovingNonExistentProduct() {
            // given
            Menu menu = new Menu("Тестовое меню");
            Product product1 = createMockProduct("Пицца Маргарита", 500.0, true);
            Product product2 = createMockProduct("Пицца Пепперони", 600.0, true);
            menu.addProduct(product1);

            // when
            menu.removeProduct(product2);

            // then
            assertEquals(1, menu.getProducts().size());
            assertTrue(menu.getProducts().contains(product1));
        }

        @Test
        @DisplayName("Удаление из пустого меню")
        void shouldHandleRemovingFromEmptyMenu() {
            // given
            Menu menu = new Menu("Тестовое меню");
            Product product = createMockProduct("Пицца Маргарита", 500.0, true);

            // when
            menu.removeProduct(product);

            // then
            assertEquals(0, menu.getProducts().size());
        }

        @Test
        @DisplayName("Удаление одного экземпляра дублированного продукта")
        void shouldRemoveOneInstanceOfDuplicateProduct() {
            // given
            Menu menu = new Menu("Тестовое меню");
            Product product = createMockProduct("Пицца Маргарита", 500.0, true);
            menu.addProduct(product);
            menu.addProduct(product);

            // when
            menu.removeProduct(product);

            // then
            assertEquals(1, menu.getProducts().size());
        }
    }

    @Nested
    @DisplayName("Тесты метода getAvailableProducts")
    class GetAvailableProductsTests {

        @Test
        @DisplayName("Получение только доступных продуктов")
        void shouldReturnOnlyAvailableProducts() {
            // given
            Menu menu = new Menu("Тестовое меню");
            Product product1 = createMockProduct("Пицца Маргарита", 500.0, true);
            Product product2 = createMockProduct("Пицца Пепперони", 600.0, false);
            Product product3 = createMockProduct("Напиток Кола", 100.0, true);
            menu.addProduct(product1);
            menu.addProduct(product2);
            menu.addProduct(product3);

            // when
            List<Product> availableProducts = menu.getAvailableProducts();

            // then
            assertEquals(2, availableProducts.size());
            assertTrue(availableProducts.contains(product1));
            assertTrue(availableProducts.contains(product3));
            assertFalse(availableProducts.contains(product2));
        }

        @Test
        @DisplayName("Пустой список когда нет доступных продуктов")
        void shouldReturnEmptyListWhenNoAvailableProducts() {
            // given
            Menu menu = new Menu("Тестовое меню");
            Product product1 = createMockProduct("Пицца Маргарита", 500.0, false);
            Product product2 = createMockProduct("Пицца Пепперони", 600.0, false);
            menu.addProduct(product1);
            menu.addProduct(product2);

            // when
            List<Product> availableProducts = menu.getAvailableProducts();

            // then
            assertEquals(0, availableProducts.size());
        }

        @Test
        @DisplayName("Все продукты доступны")
        void shouldReturnAllProductsWhenAllAvailable() {
            // given
            Menu menu = new Menu("Тестовое меню");
            Product product1 = createMockProduct("Пицца Маргарита", 500.0, true);
            Product product2 = createMockProduct("Пицца Пепперони", 600.0, true);
            menu.addProduct(product1);
            menu.addProduct(product2);

            // when
            List<Product> availableProducts = menu.getAvailableProducts();

            // then
            assertEquals(2, availableProducts.size());
        }
    }

    @Nested
    @DisplayName("Тесты метода findProductByName")
    class FindProductByNameTests {

        @Test
        @DisplayName("Поиск продукта по точному имени")
        void shouldFindProductByExactName() {
            // given
            Menu menu = new Menu("Тестовое меню");
            Product product = createMockProduct("Пицца Маргарита", 500.0, true);
            menu.addProduct(product);

            // when
            Product found = menu.findProductByName("Пицца Маргарита");

            // then
            assertNotNull(found);
            assertEquals(product, found);
        }

        @Test
        @DisplayName("Поиск продукта без учета регистра")
        void shouldFindProductCaseInsensitive() {
            // given
            Menu menu = new Menu("Тестовое меню");
            Product product = createMockProduct("Пицца Маргарита", 500.0, true);
            menu.addProduct(product);

            // when
            Product found1 = menu.findProductByName("пицца маргарита");
            Product found2 = menu.findProductByName("ПИЦЦА МАРГАРИТА");
            Product found3 = menu.findProductByName("ПиЦцА МаРгАрИтА");

            // then
            assertNotNull(found1);
            assertNotNull(found2);
            assertNotNull(found3);
            assertEquals(product, found1);
            assertEquals(product, found2);
            assertEquals(product, found3);
        }

        @Test
        @DisplayName("Возвращает null если продукт не найден")
        void shouldReturnNullWhenProductNotFound() {
            // given
            Menu menu = new Menu("Тестовое меню");
            Product product = createMockProduct("Пицца Маргарита", 500.0, true);
            menu.addProduct(product);

            // when
            Product found = menu.findProductByName("Пицца Пепперони");

            // then
            assertNull(found);
        }

        @Test
        @DisplayName("Поиск в пустом меню")
        void shouldReturnNullInEmptyMenu() {
            // given
            Menu menu = new Menu("Тестовое меню");

            // when
            Product found = menu.findProductByName("Пицца Маргарита");

            // then
            assertNull(found);
        }

        @Test
        @DisplayName("Поиск возвращает первое совпадение")
        void shouldReturnFirstMatchWhenMultipleProductsWithSameName() {
            // given
            Menu menu = new Menu("Тестовое меню");
            Product product1 = createMockProduct("Пицца Маргарита", 500.0, true);
            Product product2 = createMockProduct("Пицца Маргарита", 600.0, true);
            menu.addProduct(product1);
            menu.addProduct(product2);

            // when
            Product found = menu.findProductByName("Пицца Маргарита");

            // then
            assertNotNull(found);
            assertEquals(product1, found); // возвращает первый найденный
        }
    }

    @Nested
    @DisplayName("Тесты метода getProductsByPriceRange")
    class GetProductsByPriceRangeTests {

        @Test
        @DisplayName("Поиск продуктов в ценовом диапазоне")
        void shouldFindProductsInPriceRange() {
            // given
            Menu menu = new Menu("Тестовое меню");
            Product product1 = createMockProduct("Продукт 1", 100.0, true);
            Product product2 = createMockProduct("Продукт 2", 200.0, true);
            Product product3 = createMockProduct("Продукт 3", 300.0, true);
            Product product4 = createMockProduct("Продукт 4", 400.0, true);
            menu.addProduct(product1);
            menu.addProduct(product2);
            menu.addProduct(product3);
            menu.addProduct(product4);

            // when
            List<Product> products = menu.getProductsByPriceRange(150.0, 350.0);

            // then
            assertEquals(2, products.size());
            assertTrue(products.contains(product2));
            assertTrue(products.contains(product3));
        }

        @Test
        @DisplayName("Включает продукты с ценой на границах диапазона")
        void shouldIncludeProductsAtBoundaries() {
            // given
            Menu menu = new Menu("Тестовое меню");
            Product product1 = createMockProduct("Продукт 1", 100.0, true);
            Product product2 = createMockProduct("Продукт 2", 200.0, true);
            Product product3 = createMockProduct("Продукт 3", 300.0, true);
            menu.addProduct(product1);
            menu.addProduct(product2);
            menu.addProduct(product3);

            // when
            List<Product> products = menu.getProductsByPriceRange(100.0, 300.0);

            // then
            assertEquals(3, products.size());
            assertTrue(products.contains(product1));
            assertTrue(products.contains(product2));
            assertTrue(products.contains(product3));
        }

        @Test
        @DisplayName("Пустой список если нет продуктов в диапазоне")
        void shouldReturnEmptyListWhenNoProductsInRange() {
            // given
            Menu menu = new Menu("Тестовое меню");
            Product product1 = createMockProduct("Продукт 1", 100.0, true);
            Product product2 = createMockProduct("Продукт 2", 500.0, true);
            menu.addProduct(product1);
            menu.addProduct(product2);

            // when
            List<Product> products = menu.getProductsByPriceRange(200.0, 400.0);

            // then
            assertEquals(0, products.size());
        }

        @Test
        @DisplayName("Включает недоступные продукты в результаты")
        void shouldIncludeUnavailableProductsInResults() {
            // given
            Menu menu = new Menu("Тестовое меню");
            Product product1 = createMockProduct("Продукт 1", 200.0, true);
            Product product2 = createMockProduct("Продукт 2", 250.0, false);
            menu.addProduct(product1);
            menu.addProduct(product2);

            // when
            List<Product> products = menu.getProductsByPriceRange(150.0, 300.0);

            // then
            assertEquals(2, products.size());
        }

        @Test
        @DisplayName("Работа с отрицательным диапазоном")
        void shouldHandleNegativePriceRange() {
            // given
            Menu menu = new Menu("Тестовое меню");
            Product product = createMockProduct("Продукт 1", 100.0, true);
            menu.addProduct(product);

            // when
            List<Product> products = menu.getProductsByPriceRange(-100.0, -50.0);

            // then
            assertEquals(0, products.size());
        }
    }

    @Nested
    @DisplayName("Тесты геттеров и сеттеров")
    class GetterSetterTests {

        @Test
        @DisplayName("Установка и получение названия меню")
        void shouldSetAndGetName() {
            // given
            Menu menu = new Menu("Старое название");

            // when
            menu.setName("Новое название");

            // then
            assertEquals("Новое название", menu.getName());
        }

        @Test
        @DisplayName("Установка и получение статуса активности")
        void shouldSetAndGetActive() {
            // given
            Menu menu = new Menu("Тестовое меню");

            // when
            menu.setActive(false);

            // then
            assertFalse(menu.isActive());
        }

        @Test
        @DisplayName("Установка списка продуктов")
        void shouldSetProducts() {
            // given
            Menu menu = new Menu("Тестовое меню");
            Product product1 = createMockProduct("Продукт 1", 100.0, true);
            Product product2 = createMockProduct("Продукт 2", 200.0, true);
            List<Product> products = List.of(product1, product2);

            // when
            menu.setProducts(products);

            // then
            assertEquals(2, menu.getProducts().size());
        }

        @Test
        @DisplayName("Получение списка продуктов")
        void shouldGetProducts() {
            // given
            Menu menu = new Menu("Тестовое меню");
            Product product = createMockProduct("Продукт 1", 100.0, true);
            menu.addProduct(product);

            // when
            List<Product> products = menu.getProducts();

            // then
            assertNotNull(products);
            assertEquals(1, products.size());
            assertTrue(products.contains(product));
        }
    }

    @Nested
    @DisplayName("Граничные случаи")
    class EdgeCaseTests {

        @Test
        @DisplayName("Создание меню с пустым названием")
        void shouldCreateMenuWithEmptyName() {
            // given & when
            Menu menu = new Menu("");

            // then
            assertNotNull(menu);
            assertEquals("", menu.getName());
        }

        @Test
        @DisplayName("Создание меню с null названием")
        void shouldCreateMenuWithNullName() {
            // given & when
            Menu menu = new Menu(null);

            // then
            assertNotNull(menu);
            assertNull(menu.getName());
        }

        @Test
        @DisplayName("Добавление null продукта")
        void shouldHandleAddingNullProduct() {
            // given
            Menu menu = new Menu("Тестовое меню");

            // when
            menu.addProduct(null);

            // then
            assertEquals(1, menu.getProducts().size());
            assertTrue(menu.getProducts().contains(null));
        }

        @Test
        @DisplayName("Поиск продукта с null именем")
        void shouldHandleFindingProductWithNullName() {
            // given
            Menu menu = new Menu("Тестовое меню");
            Product product = createMockProduct("Продукт 1", 100.0, true);
            menu.addProduct(product);

            // when
            Product found = menu.findProductByName(null);

            // then
            assertNull(found);
        }

        @Test
        @DisplayName("Поиск в ценовом диапазоне с minPrice > maxPrice")
        void shouldHandleInvertedPriceRange() {
            // given
            Menu menu = new Menu("Тестовое меню");
            Product product = createMockProduct("Продукт 1", 100.0, true);
            menu.addProduct(product);

            // when
            List<Product> products = menu.getProductsByPriceRange(300.0, 200.0);

            // then
            assertEquals(0, products.size());
        }

        @Test
        @DisplayName("Поиск с одинаковыми minPrice и maxPrice")
        void shouldHandleSameMinAndMaxPrice() {
            // given
            Menu menu = new Menu("Тестовое меню");
            Product product1 = createMockProduct("Продукт 1", 100.0, true);
            Product product2 = createMockProduct("Продукт 2", 200.0, true);
            menu.addProduct(product1);
            menu.addProduct(product2);

            // when
            List<Product> products = menu.getProductsByPriceRange(200.0, 200.0);

            // then
            assertEquals(1, products.size());
            assertTrue(products.contains(product2));
        }
    }
}
