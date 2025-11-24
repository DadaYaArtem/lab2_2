package com.pizzeria;

import com.pizzeria.enums.*;
import com.pizzeria.exceptions.*;
import com.pizzeria.factory.*;
import com.pizzeria.model.*;
import com.pizzeria.model.ingredients.*;
import com.pizzeria.model.payment.*;
import com.pizzeria.model.products.*;
import com.pizzeria.model.users.*;
import com.pizzeria.service.*;
import com.pizzeria.strategy.*;
import com.pizzeria.util.*;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Главный класс приложения - Система управления пиццерией
 * Демонстрация работы ООП системы с применением SOLID и GRASP принципов
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("   СИСТЕМА УПРАВЛЕНИЯ ПИЦЦЕРИЕЙ - ДЕМО");
        System.out.println("=================================================\n");

        try {
            // 1. Инициализация системы
            System.out.println("1. ИНИЦИАЛИЗАЦИЯ СИСТЕМЫ");
            System.out.println("-------------------------------------------------");

            // Создание адреса пиццерии
            Address pizzeriaAddress = new Address("Пушкина", "10", "Москва", "101000");
            pizzeriaAddress.setLatitude(55.7558);
            pizzeriaAddress.setLongitude(37.6173);

            // Создание пиццерии
            Pizzeria pizzeria = new Pizzeria("Bella Italia", pizzeriaAddress);
            pizzeria.setWorkingHours("10:00 - 23:00");
            pizzeria.open();

            // Создание инвентаря
            Inventory inventory = new Inventory();

            // Создание кухни
            Kitchen kitchen = new Kitchen(inventory);
            pizzeria.setKitchen(kitchen);

            // 2. Добавление ингредиентов
            System.out.println("\n2. ДОБАВЛЕНИЕ ИНГРЕДИЕНТОВ");
            System.out.println("-------------------------------------------------");

            IngredientFactory ingredientFactory = new IngredientFactory();

            Cheese mozzarella = ingredientFactory.createCheese("Моцарелла");
            Cheese parmesan = ingredientFactory.createCheese("Пармезан");
            Meat pepperoni = ingredientFactory.createMeat("Пепперони");
            Meat chicken = ingredientFactory.createMeat("Курица");
            Vegetable tomatoes = ingredientFactory.createVegetable("Помидоры");
            Vegetable mushrooms = ingredientFactory.createVegetable("Грибы");
            Sauce tomatoSauce = ingredientFactory.createSauce("Томатный");
            Dough thinDough = ingredientFactory.createDough("Тонкое");

            inventory.addIngredient(mozzarella);
            inventory.addIngredient(parmesan);
            inventory.addIngredient(pepperoni);
            inventory.addIngredient(chicken);
            inventory.addIngredient(tomatoes);
            inventory.addIngredient(mushrooms);
            inventory.addIngredient(tomatoSauce);
            inventory.addIngredient(thinDough);

            // 3. Создание сотрудников
            System.out.println("\n3. НАЙМ СОТРУДНИКОВ");
            System.out.println("-------------------------------------------------");

            Manager manager = new Manager("MGR001", "Иван", "Петров", 80000);
            Chef chef1 = new Chef("CHF001", "Марио", "Росси", 60000);
            chef1.setSpecialty("Итальянская пицца");
            chef1.setExperienceYears(5);

            Chef chef2 = new Chef("CHF002", "Луиджи", "Бьянки", 55000);
            chef2.setExperienceYears(3);

            Waiter waiter = new Waiter("WTR001", "Анна", "Смирнова", 35000);
            DeliveryDriver driver = new DeliveryDriver("DRV001", "Петр", "Иванов", 40000);
            driver.setVehicleType("Мотоцикл");
            driver.setVehicleNumber("А123ВС");

            pizzeria.setManager(manager);
            pizzeria.hireEmployee(manager);
            pizzeria.hireEmployee(chef1);
            pizzeria.hireEmployee(chef2);
            pizzeria.hireEmployee(waiter);
            pizzeria.hireEmployee(driver);

            kitchen.addChef(chef1);
            kitchen.addChef(chef2);

            // 4. Создание меню
            System.out.println("\n4. СОЗДАНИЕ МЕНЮ");
            System.out.println("-------------------------------------------------");

            Menu menu = new Menu("Основное меню");
            PizzaFactory pizzaFactory = new PizzaFactory();

            // Создание пицц разных размеров
            Pizza margherita = pizzaFactory.createPizza("маргарита", PizzaSize.MEDIUM);
            margherita.addIngredient(mozzarella);
            margherita.addIngredient(tomatoSauce);
            margherita.addIngredient(thinDough);

            Pizza pepperoniPizza = pizzaFactory.createPizza("пепперони", PizzaSize.LARGE);
            pepperoniPizza.addIngredient(mozzarella);
            pepperoniPizza.addIngredient(pepperoni);
            pepperoniPizza.addIngredient(tomatoSauce);

            Pizza veggiePizza = pizzaFactory.createPizza("вегетарианская", PizzaSize.MEDIUM);
            veggiePizza.addIngredient(tomatoes);
            veggiePizza.addIngredient(mushrooms);

            // Добавление напитков и десертов
            Drink cola = new Drink("Кока-кола", 100.0, 500);
            cola.setCarbonated(true);
            cola.chill();

            Dessert tiramisu = new Dessert("Тирамису", 250.0, 150);

            menu.addProduct(margherita);
            menu.addProduct(pepperoniPizza);
            menu.addProduct(veggiePizza);
            menu.addProduct(cola);
            menu.addProduct(tiramisu);

            pizzeria.setMenu(menu);
            menu.displayMenu();

            // 5. Создание клиента
            System.out.println("\n5. РЕГИСТРАЦИЯ КЛИЕНТА");
            System.out.println("-------------------------------------------------");

            Customer customer = new Customer("CUST001", "Алексей", "Николаев");
            customer.setPhoneNumber(new PhoneNumber("+7", "495", "1234567"));
            customer.setEmail(new Email("alexey", "example.com"));

            Address customerAddress = new Address("Ленина", "25", "Москва", "102000");
            customerAddress.setApartmentNumber("42");
            customerAddress.setLatitude(55.7500);
            customerAddress.setLongitude(37.6200);
            customer.setAddress(customerAddress);

            // Создание карты лояльности
            LoyaltyCard loyaltyCard = new LoyaltyCard("LOYAL-001");
            loyaltyCard.addPoints(150);
            customer.setLoyaltyCard(loyaltyCard);

            System.out.println("Клиент зарегистрирован: " + customer.getFullName());
            System.out.println("Карта лояльности: " + loyaltyCard.getTier() +
                             " (Скидка: " + loyaltyCard.getDiscountAmount() + "%)");

            // 6. Создание заказа
            System.out.println("\n6. СОЗДАНИЕ ЗАКАЗА");
            System.out.println("-------------------------------------------------");

            OrderService orderService = new OrderService();
            Order order = orderService.createOrder(customer);

            // Добавление товаров в заказ
            order.addItem(pepperoniPizza, 2);
            order.addItem(margherita, 1);
            order.addItem(cola, 3);
            order.addItem(tiramisu, 2);

            // Применение скидки по карте лояльности
            if (loyaltyCard.isDiscountApplicable()) {
                order.applyDiscount(loyaltyCard.getDiscountAmount());
                System.out.println("Применена скидка: " + loyaltyCard.getDiscountAmount() + "%");
            }

            System.out.println("Заказ создан: " + order);
            System.out.println("Количество товаров: " + order.getTotalItems());
            System.out.println("Сумма заказа: " + PriceCalculator.formatPrice(order.getPrice()));
            System.out.println("Итого к оплате: " + PriceCalculator.formatPrice(order.getFinalPrice()));

            // 7. Валидация заказа
            System.out.println("\n7. ВАЛИДАЦИЯ ЗАКАЗА");
            System.out.println("-------------------------------------------------");

            if (OrderValidator.validateOrder(order)) {
                System.out.println("✓ Заказ прошел валидацию");
            }

            // Установка адреса доставки
            order.setDeliveryAddress(customerAddress);
            System.out.println("Адрес доставки: " + customerAddress);
            System.out.println("Время доставки: " + order.calculateDeliveryTime() + " мин");
            System.out.println("Стоимость доставки: " +
                             PriceCalculator.formatPrice(order.calculateDeliveryCost()));

            // 8. Обработка оплаты
            System.out.println("\n8. ОБРАБОТКА ПЛАТЕЖА");
            System.out.println("-------------------------------------------------");

            PaymentFactory paymentFactory = new PaymentFactory();
            PaymentService paymentService = new PaymentService();

            // Оплата картой
            CardPayment cardPayment = paymentFactory.createCardPayment(
                order.getFinalPrice(), "4532123456789012");
            cardPayment.setCardHolderName(customer.getFullName());
            cardPayment.setExpiryDate("12/25");

            Receipt receipt = paymentService.processPayment(order, cardPayment);

            System.out.println("Платеж успешно обработан");
            System.out.println("ID транзакции: " + cardPayment.getTransactionId());

            // 9. Приготовление заказа
            System.out.println("\n9. ПРИГОТОВЛЕНИЕ ЗАКАЗА НА КУХНЕ");
            System.out.println("-------------------------------------------------");

            order.updateStatus(OrderStatus.PREPARING);
            waiter.takeOrder(order);

            // Повар готовит пиццы
            for (OrderItem item : order.getItems()) {
                if (item.getProduct() instanceof Pizza) {
                    Pizza pizza = (Pizza) item.getProduct();
                    System.out.println("\nПриготовление: " + pizza.getName());
                    int cookingTime = chef1.cookPizza(pizza);
                    System.out.println("Время приготовления: " + cookingTime + " мин");
                    System.out.println("Калории: " + pizza.getCalories());
                }
            }

            order.updateStatus(OrderStatus.READY);
            waiter.serveOrder(order);

            // 10. Доставка
            System.out.println("\n10. ОРГАНИЗАЦИЯ ДОСТАВКИ");
            System.out.println("-------------------------------------------------");

            DeliveryService deliveryService = new DeliveryService();
            deliveryService.addDriver(driver);

            DeliveryDriver availableDriver = deliveryService.findAvailableDriver();
            if (availableDriver != null) {
                order.updateStatus(OrderStatus.IN_DELIVERY);

                DeliveryInfo delivery = deliveryService.scheduleDelivery(order, availableDriver);
                System.out.println("Трек-номер: " + delivery.getTrackingNumber());
                System.out.println("Ожидаемое время доставки: " + delivery.getEstimatedTime() + " мин");

                // Симуляция завершения доставки
                deliveryService.completeDelivery(delivery);
                order.updateStatus(OrderStatus.DELIVERED);

                // Чаевые водителю
                double tip = PriceCalculator.calculateTip(order.getFinalPrice(), 10);
                waiter.receiveTip(tip);
                System.out.println("Чаевые: " + PriceCalculator.formatPrice(tip));
            }

            // 11. Работа с промо-кодами и акциями
            System.out.println("\n11. РАБОТА С ПРОМО-КОДАМИ");
            System.out.println("-------------------------------------------------");

            Discount discount = new Discount("PIZZA20", 20.0);
            discount.setUsageLimit(10);

            if (discount.isDiscountApplicable()) {
                System.out.println("Промо-код '" + discount.getCode() + "' активен");
                System.out.println("Скидка: " + discount.getDiscountAmount() + "%");
                discount.use();
            }

            Promotion promotion = new Promotion("Счастливые часы", 15.0);
            promotion.setDescription("15% скидка с 14:00 до 16:00");

            if (promotion.isValid()) {
                System.out.println("Акция '" + promotion.getName() + "' активна");
                System.out.println(promotion.getDescription());
            }

            // 12. Расписание работы
            System.out.println("\n12. УПРАВЛЕНИЕ РАСПИСАНИЕМ");
            System.out.println("-------------------------------------------------");

            WorkSchedule schedule = new WorkSchedule();
            Shift morningShift = new Shift(LocalTime.of(9, 0), LocalTime.of(17, 0));
            Shift eveningShift = new Shift(LocalTime.of(17, 0), LocalTime.of(1, 0));

            LocalDate today = LocalDate.now();
            schedule.addShift(today, chef1, morningShift);
            schedule.addShift(today, waiter, eveningShift);

            System.out.println("Смены на сегодня запланированы");

            // 13. Использование Strategy Pattern для ценообразования
            System.out.println("\n13. СТРАТЕГИИ ЦЕНООБРАЗОВАНИЯ");
            System.out.println("-------------------------------------------------");

            PriceCalculationStrategy standardStrategy = new StandardPricingStrategy();
            PriceCalculationStrategy discountStrategy = new DiscountPricingStrategy(15.0);
            PriceCalculationStrategy premiumStrategy = new PremiumPricingStrategy(200.0);

            System.out.println("Стандартная цена: " +
                             PriceCalculator.formatPrice(standardStrategy.calculatePrice(order)));
            System.out.println("Цена со скидкой 15%: " +
                             PriceCalculator.formatPrice(discountStrategy.calculatePrice(order)));
            System.out.println("Премиум цена (+ сервисный сбор): " +
                             PriceCalculator.formatPrice(premiumStrategy.calculatePrice(order)));

            // 15. Отчеты
            System.out.println("\n15. ГЕНЕРАЦИЯ ОТЧЕТОВ");
            System.out.println("-------------------------------------------------");

            pizzeria.addOrder(order);

            String salesReport = ReportGenerator.generateDailySalesReport(pizzeria, today);
            System.out.println(salesReport);

            String employeeReport = ReportGenerator.generateEmployeeReport(chef1);
            System.out.println(employeeReport);

            // 16. Статистика пиццерии
            System.out.println("\n16. СТАТИСТИКА ПИЦЦЕРИИ");
            System.out.println("-------------------------------------------------");

            System.out.println("Название: " + pizzeria.getName());
            System.out.println("Адрес: " + pizzeria.getAddress());
            System.out.println("Часы работы: " + pizzeria.getWorkingHours());
            System.out.println("Статус: " + (pizzeria.isOpen() ? "Открыта" : "Закрыта"));
            System.out.println("Всего сотрудников: " + pizzeria.getActiveEmployees().size());
            System.out.println("Всего заказов: " + pizzeria.getTotalOrders());
            System.out.println("Выручка за день: " +
                             PriceCalculator.formatPrice(pizzeria.calculateDailyRevenue()));
            System.out.println("Стоимость инвентаря: " +
                             PriceCalculator.formatPrice(inventory.calculateTotalInventoryValue()));

            // 17. Демонстрация обработки исключений
            System.out.println("\n17. ДЕМОНСТРАЦИЯ ОБРАБОТКИ ИСКЛЮЧЕНИЙ");
            System.out.println("-------------------------------------------------");

            demonstrateExceptionHandling(
                ingredientFactory, pizzaFactory, orderService, paymentFactory);

            // Закрытие пиццерии
            System.out.println("\n=================================================");
            pizzeria.close();
            System.out.println("=================================================");
            System.out.println("   ДЕМОНСТРАЦИЯ ЗАВЕРШЕНА");
            System.out.println("=================================================");

        } catch (Exception e) {
            System.err.println("Критическая ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Демонстрация обработки персональных исключений
     */
    private static void demonstrateExceptionHandling(
            IngredientFactory ingredientFactory,
            PizzaFactory pizzaFactory,
            OrderService orderService,
            PaymentFactory paymentFactory) {

        System.out.println("Проверка обработки исключений:");

        // 1. InvalidPriceException
        try {
            new Cheese("Тестовый сыр", -10.0, "Тест");
        } catch (InvalidPriceException e) {
            System.out.println("✓ InvalidPriceException: " + e.getMessage());
        }

        // 2. InvalidPizzaSizeException
        try {
            pizzaFactory.createPizza("маргарита", null);
        } catch (Exception e) {
            System.out.println("✓ InvalidPizzaSizeException: " + e.getMessage());
        }

        // 3. InvalidDiscountException
        try {
            Discount invalidDiscount = new Discount("TEST", 150.0);
        } catch (InvalidDiscountException e) {
            System.out.println("✓ InvalidDiscountException: " + e.getMessage());
        }

        // 4. OrderNotFoundException
        try {
            orderService.getOrder("INVALID-ID");
        } catch (OrderNotFoundException e) {
            System.out.println("✓ OrderNotFoundException: " + e.getMessage());
        }

        // 5. InvalidPaymentException
        try {
            new CashPayment("TEST", -100.0);
        } catch (InvalidPaymentException e) {
            System.out.println("✓ InvalidPaymentException: " + e.getMessage());
        }

        // 6. InvalidDeliveryAddressException
        try {
            new Address("", "", "Москва", "101000");
        } catch (InvalidDeliveryAddressException e) {
            System.out.println("✓ InvalidDeliveryAddressException: " + e.getMessage());
        }

        System.out.println("\nВсе исключения обработаны корректно!");
    }
}
