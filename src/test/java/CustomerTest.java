import com.pizzeria.model.users.Customer;
import com.pizzeria.model.LoyaltyCard;
import com.pizzeria.model.PhoneNumber;
import com.pizzeria.model.Email;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Customer Tests")
class CustomerTest {

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer("CUST001", "Иван", "Иванов");
    }

    @Test
    @DisplayName("Создание клиента")
    void testCustomerCreation() {
        assertNotNull(customer);
        assertEquals("CUST001", customer.getId());
        assertEquals("Иван", customer.getFirstName());
        assertEquals("Иванов", customer.getLastName());
    }

    @Test
    @DisplayName("Получение полного имени")
    void testGetFullName() {
        assertEquals("Иван Иванов", customer.getFullName());
    }

    @Test
    @DisplayName("Получение роли")
    void testGetRole() {
        assertEquals("Клиент", customer.getRole());
    }

    @Test
    @DisplayName("Уведомления включены по умолчанию")
    void testNotificationsEnabledByDefault() {
        assertTrue(customer.isNotificationEnabled());
    }

    @Test
    @DisplayName("Отключение уведомлений")
    void testDisableNotifications() {
        customer.setNotificationEnabled(false);
        assertFalse(customer.isNotificationEnabled());
    }

    @Test
    @DisplayName("Добавление заказа в историю")
    void testAddToOrderHistory() {
        customer.addToOrderHistory("ORD001");
        customer.addToOrderHistory("ORD002");

        assertEquals(2, customer.getTotalOrders());
        assertTrue(customer.getOrderHistory().contains("ORD001"));
        assertTrue(customer.getOrderHistory().contains("ORD002"));
    }

    @Test
    @DisplayName("Начальное количество заказов равно нулю")
    void testInitialOrderCountIsZero() {
        assertEquals(0, customer.getTotalOrders());
    }

    @Test
    @DisplayName("Клиент не VIP при малом количестве заказов")
    void testNotVIPWithFewOrders() {
        for (int i = 0; i < 10; i++) {
            customer.addToOrderHistory("ORD" + i);
        }
        assertFalse(customer.isVIP());
    }

    @Test
    @DisplayName("Клиент становится VIP после 11 заказов")
    void testBecomesVIPAfter11Orders() {
        for (int i = 0; i < 11; i++) {
            customer.addToOrderHistory("ORD" + i);
        }
        assertTrue(customer.isVIP());
    }

    @Test
    @DisplayName("Установка карты лояльности")
    void testSetLoyaltyCard() throws Exception {
        LoyaltyCard card = new LoyaltyCard("LC001");
        customer.setLoyaltyCard(card);
        assertNotNull(customer.getLoyaltyCard());
        assertEquals(card, customer.getLoyaltyCard());
    }

    @Test
    @DisplayName("Предпочтительный контакт по умолчанию - email")
    void testDefaultPreferredContact() {
        assertEquals("email", customer.getPreferredContact());
    }

    @Test
    @DisplayName("Изменение предпочтительного контакта")
    void testSetPreferredContact() {
        customer.setPreferredContact("phone");
        assertEquals("phone", customer.getPreferredContact());
    }

    @Test
    @DisplayName("Получение контактной информации через email")
    void testGetContactInfoViaEmail() throws Exception {
        Email email = new Email("ivan@example.com");
        customer.setEmail(email);

        String contactInfo = customer.getContactInfo();
        assertEquals("ivan@example.com", contactInfo);
    }

    @Test
    @DisplayName("Получение контактной информации через телефон")
    void testGetContactInfoViaPhone() throws Exception {
        PhoneNumber phone = new PhoneNumber("+7", "9001234567");
        customer.setPhoneNumber(phone);
        customer.setPreferredContact("phone");

        String contactInfo = customer.getContactInfo();
        assertEquals("+7 9001234567", contactInfo);
    }

    @Test
    @DisplayName("Получение пустого контакта если не установлен")
    void testGetContactInfoWhenNotSet() {
        assertEquals("", customer.getContactInfo());
    }
}
