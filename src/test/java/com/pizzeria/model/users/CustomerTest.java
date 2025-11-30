package com.pizzeria.model.users;

import com.pizzeria.model.Address;
import com.pizzeria.model.Email;
import com.pizzeria.model.LoyaltyCard;
import com.pizzeria.model.PhoneNumber;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты для класса Customer")
class CustomerTest {

    @Nested
    @DisplayName("Тесты конструктора")
    class ConstructorTests {

        @Test
        @DisplayName("Успешное создание клиента с валидными данными")
        void shouldCreateCustomerWithValidData() {
            // given & when
            Customer customer = new Customer("C-001", "Иван", "Иванов");

            // then
            assertNotNull(customer);
            assertEquals("C-001", customer.getId());
            assertEquals("Иван", customer.getFirstName());
            assertEquals("Иванов", customer.getLastName());
            assertTrue(customer.isNotificationEnabled());
            assertEquals("email", customer.getPreferredContact());
            assertNotNull(customer.getOrderHistory());
            assertTrue(customer.getOrderHistory().isEmpty());
        }

        @Test
        @DisplayName("История заказов пустая при создании")
        void shouldHaveEmptyOrderHistoryOnCreation() {
            // given & when
            Customer customer = new Customer("C-001", "Иван", "Иванов");

            // then
            assertEquals(0, customer.getTotalOrders());
        }

        @Test
        @DisplayName("Уведомления включены по умолчанию")
        void shouldHaveNotificationsEnabledByDefault() {
            // given & when
            Customer customer = new Customer("C-001", "Иван", "Иванов");

            // then
            assertTrue(customer.isNotificationEnabled());
        }

        @Test
        @DisplayName("Предпочтительный контакт - email по умолчанию")
        void shouldHaveEmailAsPreferredContactByDefault() {
            // given & when
            Customer customer = new Customer("C-001", "Иван", "Иванов");

            // then
            assertEquals("email", customer.getPreferredContact());
        }
    }

    @Nested
    @DisplayName("Тесты метода getFullName (из Person)")
    class GetFullNameTests {

        @Test
        @DisplayName("Получение полного имени")
        void shouldReturnFullName() {
            // given
            Customer customer = new Customer("C-001", "Иван", "Иванов");

            // when
            String fullName = customer.getFullName();

            // then
            assertEquals("Иван Иванов", fullName);
        }

        @Test
        @DisplayName("Полное имя с различными символами")
        void shouldHandleSpecialCharactersInName() {
            // given
            Customer customer = new Customer("C-001", "Анна-Мария", "О'Коннор");

            // when
            String fullName = customer.getFullName();

            // then
            assertEquals("Анна-Мария О'Коннор", fullName);
        }
    }

    @Nested
    @DisplayName("Тесты метода getRole")
    class GetRoleTests {

        @Test
        @DisplayName("Роль клиента")
        void shouldReturnCustomerRole() {
            // given
            Customer customer = new Customer("C-001", "Иван", "Иванов");

            // when
            String role = customer.getRole();

            // then
            assertEquals("Клиент", role);
        }
    }

    @Nested
    @DisplayName("Тесты метода sendNotification")
    class SendNotificationTests {

        @Test
        @DisplayName("Отправка уведомления когда они включены")
        void shouldSendNotificationWhenEnabled() {
            // given
            Customer customer = new Customer("C-001", "Иван", "Иванов");

            // when & then
            assertDoesNotThrow(() -> customer.sendNotification("Тестовое сообщение"));
        }

        @Test
        @DisplayName("Уведомление не отправляется когда они отключены")
        void shouldNotSendNotificationWhenDisabled() {
            // given
            Customer customer = new Customer("C-001", "Иван", "Иванов");
            customer.setNotificationEnabled(false);

            // when & then
            assertDoesNotThrow(() -> customer.sendNotification("Тестовое сообщение"));
        }

        @Test
        @DisplayName("Отправка пустого уведомления")
        void shouldHandleEmptyNotification() {
            // given
            Customer customer = new Customer("C-001", "Иван", "Иванов");

            // when & then
            assertDoesNotThrow(() -> customer.sendNotification(""));
        }

        @Test
        @DisplayName("Отправка null уведомления")
        void shouldHandleNullNotification() {
            // given
            Customer customer = new Customer("C-001", "Иван", "Иванов");

            // when & then
            assertDoesNotThrow(() -> customer.sendNotification(null));
        }
    }

    @Nested
    @DisplayName("Тесты метода getContactInfo")
    class GetContactInfoTests {

        @Test
        @DisplayName("Возвращает email когда предпочтение email и email установлен")
        void shouldReturnEmailWhenPreferredAndSet() {
            // given
            Customer customer = new Customer("C-001", "Иван", "Иванов");
            customer.setEmail(new Email("ivan@example.com"));
            customer.setPreferredContact("email");

            // when
            String contactInfo = customer.getContactInfo();

            // then
            assertEquals("ivan@example.com", contactInfo);
        }

        @Test
        @DisplayName("Возвращает телефон когда предпочтение phone и телефон установлен")
        void shouldReturnPhoneWhenPreferredAndSet() {
            // given
            Customer customer = new Customer("C-001", "Иван", "Иванов");
            customer.setPhoneNumber(new PhoneNumber("+7", "495", "1234567"));
            customer.setPreferredContact("phone");

            // when
            String contactInfo = customer.getContactInfo();

            // then
            assertEquals("+74951234567", contactInfo);
        }

        @Test
        @DisplayName("Возвращает пустую строку когда email не установлен")
        void shouldReturnEmptyStringWhenEmailNotSet() {
            // given
            Customer customer = new Customer("C-001", "Иван", "Иванов");
            customer.setPreferredContact("email");

            // when
            String contactInfo = customer.getContactInfo();

            // then
            assertEquals("", contactInfo);
        }

        @Test
        @DisplayName("Возвращает email когда предпочтение phone но телефон не установлен")
        void shouldReturnEmailWhenPhonePreferredButNotSet() {
            // given
            Customer customer = new Customer("C-001", "Иван", "Иванов");
            customer.setEmail(new Email("ivan@example.com"));
            customer.setPreferredContact("phone");

            // when
            String contactInfo = customer.getContactInfo();

            // then
            assertEquals("ivan@example.com", contactInfo);
        }

        @Test
        @DisplayName("Возвращает пустую строку когда ничего не установлено")
        void shouldReturnEmptyStringWhenNothingSet() {
            // given
            Customer customer = new Customer("C-001", "Иван", "Иванов");

            // when
            String contactInfo = customer.getContactInfo();

            // then
            assertEquals("", contactInfo);
        }
    }

    @Nested
    @DisplayName("Тесты метода isNotificationEnabled")
    class IsNotificationEnabledTests {

        @Test
        @DisplayName("Уведомления включены по умолчанию")
        void shouldBeEnabledByDefault() {
            // given
            Customer customer = new Customer("C-001", "Иван", "Иванов");

            // when & then
            assertTrue(customer.isNotificationEnabled());
        }

        @Test
        @DisplayName("Уведомления отключены после изменения")
        void shouldBeDisabledAfterChange() {
            // given
            Customer customer = new Customer("C-001", "Иван", "Иванов");

            // when
            customer.setNotificationEnabled(false);

            // then
            assertFalse(customer.isNotificationEnabled());
        }

        @Test
        @DisplayName("Уведомления можно включить обратно")
        void shouldBeReenabled() {
            // given
            Customer customer = new Customer("C-001", "Иван", "Иванов");
            customer.setNotificationEnabled(false);

            // when
            customer.setNotificationEnabled(true);

            // then
            assertTrue(customer.isNotificationEnabled());
        }
    }

    @Nested
    @DisplayName("Тесты метода addToOrderHistory")
    class AddToOrderHistoryTests {

        @Test
        @DisplayName("Добавление заказа в историю")
        void shouldAddOrderToHistory() {
            // given
            Customer customer = new Customer("C-001", "Иван", "Иванов");

            // when
            customer.addToOrderHistory("ORDER-001");

            // then
            assertEquals(1, customer.getTotalOrders());
            assertTrue(customer.getOrderHistory().contains("ORDER-001"));
        }

        @Test
        @DisplayName("Добавление нескольких заказов")
        void shouldAddMultipleOrders() {
            // given
            Customer customer = new Customer("C-001", "Иван", "Иванов");

            // when
            customer.addToOrderHistory("ORDER-001");
            customer.addToOrderHistory("ORDER-002");
            customer.addToOrderHistory("ORDER-003");

            // then
            assertEquals(3, customer.getTotalOrders());
        }

        @Test
        @DisplayName("Дублирующиеся заказы добавляются отдельно")
        void shouldAddDuplicateOrdersSeparately() {
            // given
            Customer customer = new Customer("C-001", "Иван", "Иванов");

            // when
            customer.addToOrderHistory("ORDER-001");
            customer.addToOrderHistory("ORDER-001");

            // then
            assertEquals(2, customer.getTotalOrders());
        }

        @Test
        @DisplayName("Добавление null заказа")
        void shouldAddNullOrder() {
            // given
            Customer customer = new Customer("C-001", "Иван", "Иванов");

            // when
            customer.addToOrderHistory(null);

            // then
            assertEquals(1, customer.getTotalOrders());
            assertTrue(customer.getOrderHistory().contains(null));
        }
    }

    @Nested
    @DisplayName("Тесты метода getTotalOrders")
    class GetTotalOrdersTests {

        @Test
        @DisplayName("Начальное количество заказов - 0")
        void shouldReturn0Initially() {
            // given
            Customer customer = new Customer("C-001", "Иван", "Иванов");

            // when
            int total = customer.getTotalOrders();

            // then
            assertEquals(0, total);
        }

        @Test
        @DisplayName("Подсчет общего количества заказов")
        void shouldCountTotalOrders() {
            // given
            Customer customer = new Customer("C-001", "Иван", "Иванов");
            customer.addToOrderHistory("ORDER-001");
            customer.addToOrderHistory("ORDER-002");

            // when
            int total = customer.getTotalOrders();

            // then
            assertEquals(2, total);
        }
    }

    @Nested
    @DisplayName("Тесты метода isVIP")
    class IsVIPTests {

        @Test
        @DisplayName("Не VIP с 10 или менее заказами")
        void shouldNotBeVIPWith10OrLessOrders() {
            // given
            Customer customer = new Customer("C-001", "Иван", "Иванов");
            for (int i = 0; i < 10; i++) {
                customer.addToOrderHistory("ORDER-" + i);
            }

            // when & then
            assertFalse(customer.isVIP());
        }

        @Test
        @DisplayName("VIP с более чем 10 заказами")
        void shouldBeVIPWithMoreThan10Orders() {
            // given
            Customer customer = new Customer("C-001", "Иван", "Иванов");
            for (int i = 0; i < 11; i++) {
                customer.addToOrderHistory("ORDER-" + i);
            }

            // when & then
            assertTrue(customer.isVIP());
        }

        @Test
        @DisplayName("Граничный случай: ровно 10 заказов")
        void shouldNotBeVIPWithExactly10Orders() {
            // given
            Customer customer = new Customer("C-001", "Иван", "Иванов");
            for (int i = 0; i < 10; i++) {
                customer.addToOrderHistory("ORDER-" + i);
            }

            // when & then
            assertFalse(customer.isVIP());
            assertEquals(10, customer.getTotalOrders());
        }

        @Test
        @DisplayName("Граничный случай: ровно 11 заказов")
        void shouldBeVIPWithExactly11Orders() {
            // given
            Customer customer = new Customer("C-001", "Иван", "Иванов");
            for (int i = 0; i < 11; i++) {
                customer.addToOrderHistory("ORDER-" + i);
            }

            // when & then
            assertTrue(customer.isVIP());
            assertEquals(11, customer.getTotalOrders());
        }

        @Test
        @DisplayName("VIP с очень большим количеством заказов")
        void shouldBeVIPWithVeryManyOrders() {
            // given
            Customer customer = new Customer("C-001", "Иван", "Иванов");
            for (int i = 0; i < 100; i++) {
                customer.addToOrderHistory("ORDER-" + i);
            }

            // when & then
            assertTrue(customer.isVIP());
        }
    }

    @Nested
    @DisplayName("Тесты геттеров и сеттеров")
    class GetterSetterTests {

        @Test
        @DisplayName("Установка и получение карты лояльности")
        void shouldSetAndGetLoyaltyCard() {
            // given
            Customer customer = new Customer("C-001", "Иван", "Иванов");
            LoyaltyCard card = new LoyaltyCard("LC-12345");

            // when
            customer.setLoyaltyCard(card);

            // then
            assertEquals(card, customer.getLoyaltyCard());
        }

        @Test
        @DisplayName("Установка и получение предпочтительного контакта")
        void shouldSetAndGetPreferredContact() {
            // given
            Customer customer = new Customer("C-001", "Иван", "Иванов");

            // when
            customer.setPreferredContact("phone");

            // then
            assertEquals("phone", customer.getPreferredContact());
        }

        @Test
        @DisplayName("Установка и получение статуса уведомлений")
        void shouldSetAndGetNotificationEnabled() {
            // given
            Customer customer = new Customer("C-001", "Иван", "Иванов");

            // when
            customer.setNotificationEnabled(false);

            // then
            assertFalse(customer.isNotificationEnabled());
        }

        @Test
        @DisplayName("Получение истории заказов")
        void shouldGetOrderHistory() {
            // given
            Customer customer = new Customer("C-001", "Иван", "Иванов");
            customer.addToOrderHistory("ORDER-001");
            customer.addToOrderHistory("ORDER-002");

            // when
            var history = customer.getOrderHistory();

            // then
            assertNotNull(history);
            assertEquals(2, history.size());
            assertTrue(history.contains("ORDER-001"));
            assertTrue(history.contains("ORDER-002"));
        }

        @Test
        @DisplayName("Установка и получение ID (из Person)")
        void shouldSetAndGetId() {
            // given
            Customer customer = new Customer("C-001", "Иван", "Иванов");

            // when
            customer.setId("C-999");

            // then
            assertEquals("C-999", customer.getId());
        }

        @Test
        @DisplayName("Установка и получение имени (из Person)")
        void shouldSetAndGetFirstName() {
            // given
            Customer customer = new Customer("C-001", "Иван", "Иванов");

            // when
            customer.setFirstName("Петр");

            // then
            assertEquals("Петр", customer.getFirstName());
        }

        @Test
        @DisplayName("Установка и получение фамилии (из Person)")
        void shouldSetAndGetLastName() {
            // given
            Customer customer = new Customer("C-001", "Иван", "Иванов");

            // when
            customer.setLastName("Петров");

            // then
            assertEquals("Петров", customer.getLastName());
        }

        @Test
        @DisplayName("Установка и получение телефона (из Person)")
        void shouldSetAndGetPhoneNumber() {
            // given
            Customer customer = new Customer("C-001", "Иван", "Иванов");
            PhoneNumber phone = new PhoneNumber("+7", "495", "1234567");

            // when
            customer.setPhoneNumber(phone);

            // then
            assertEquals(phone, customer.getPhoneNumber());
        }

        @Test
        @DisplayName("Установка и получение email (из Person)")
        void shouldSetAndGetEmail() {
            // given
            Customer customer = new Customer("C-001", "Иван", "Иванов");
            Email email = new Email("ivan@example.com");

            // when
            customer.setEmail(email);

            // then
            assertEquals(email, customer.getEmail());
        }

        @Test
        @DisplayName("Установка и получение адреса (из Person)")
        void shouldSetAndGetAddress() throws Exception {
            // given
            Customer customer = new Customer("C-001", "Иван", "Иванов");
            Address address = new Address("Ленина", "15", "Москва", "123456");

            // when
            customer.setAddress(address);

            // then
            assertEquals(address, customer.getAddress());
        }
    }

    @Nested
    @DisplayName("Тесты toString (из Person)")
    class ToStringTests {

        @Test
        @DisplayName("toString возвращает имя и ID")
        void shouldReturnNameAndId() {
            // given
            Customer customer = new Customer("C-001", "Иван", "Иванов");

            // when
            String result = customer.toString();

            // then
            assertTrue(result.contains("Иван Иванов"));
            assertTrue(result.contains("C-001"));
        }
    }

    @Nested
    @DisplayName("Граничные случаи")
    class EdgeCaseTests {

        @Test
        @DisplayName("Создание клиента с пустыми строками")
        void shouldCreateCustomerWithEmptyStrings() {
            // given & when
            Customer customer = new Customer("", "", "");

            // then
            assertNotNull(customer);
            assertEquals("", customer.getId());
            assertEquals("", customer.getFirstName());
            assertEquals("", customer.getLastName());
        }

        @Test
        @DisplayName("Создание клиента с null значениями")
        void shouldCreateCustomerWithNullValues() {
            // given & when
            Customer customer = new Customer(null, null, null);

            // then
            assertNotNull(customer);
            assertNull(customer.getId());
            assertNull(customer.getFirstName());
            assertNull(customer.getLastName());
        }

        @Test
        @DisplayName("Установка null карты лояльности")
        void shouldAllowNullLoyaltyCard() {
            // given
            Customer customer = new Customer("C-001", "Иван", "Иванов");
            customer.setLoyaltyCard(new LoyaltyCard("LC-123"));

            // when
            customer.setLoyaltyCard(null);

            // then
            assertNull(customer.getLoyaltyCard());
        }

        @Test
        @DisplayName("Установка пустого предпочтительного контакта")
        void shouldAllowEmptyPreferredContact() {
            // given
            Customer customer = new Customer("C-001", "Иван", "Иванов");

            // when
            customer.setPreferredContact("");

            // then
            assertEquals("", customer.getPreferredContact());
        }

        @Test
        @DisplayName("Установка null предпочтительного контакта")
        void shouldAllowNullPreferredContact() {
            // given
            Customer customer = new Customer("C-001", "Иван", "Иванов");

            // when
            customer.setPreferredContact(null);

            // then
            assertNull(customer.getPreferredContact());
        }

        @Test
        @DisplayName("Добавление пустой строки в историю заказов")
        void shouldAddEmptyStringToOrderHistory() {
            // given
            Customer customer = new Customer("C-001", "Иван", "Иванов");

            // when
            customer.addToOrderHistory("");

            // then
            assertEquals(1, customer.getTotalOrders());
            assertTrue(customer.getOrderHistory().contains(""));
        }

        @Test
        @DisplayName("Очень длинное имя клиента")
        void shouldHandleVeryLongName() {
            // given
            String longName = "А".repeat(1000);

            // when
            Customer customer = new Customer("C-001", longName, longName);

            // then
            assertEquals(longName, customer.getFirstName());
            assertEquals(longName, customer.getLastName());
        }

        @Test
        @DisplayName("Специальные символы в ID")
        void shouldHandleSpecialCharactersInId() {
            // given & when
            Customer customer = new Customer("C-001-@#$%", "Иван", "Иванов");

            // then
            assertEquals("C-001-@#$%", customer.getId());
        }
    }
}
