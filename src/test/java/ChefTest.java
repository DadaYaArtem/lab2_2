import com.pizzeria.enums.EmployeeRole;
import com.pizzeria.enums.PizzaSize;
import com.pizzeria.exceptions.InvalidPriceException;
import com.pizzeria.exceptions.InsufficientIngredientsException;
import com.pizzeria.model.products.MargheritaPizza;
import com.pizzeria.model.products.PepperoniPizza;
import com.pizzeria.model.products.Pizza;
import com.pizzeria.model.users.Chef;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Chef Tests")
class ChefTest {

    private Chef chef;
    private Pizza pizza;

    @BeforeEach
    void setUp() throws InvalidPriceException {
        chef = new Chef("CHEF001", "Антон", "Петров", 50000.0);
        pizza = new MargheritaPizza(PizzaSize.MEDIUM);
    }

    @Test
    @DisplayName("Создание повара")
    void testChefCreation() {
        assertNotNull(chef);
        assertEquals("CHEF001", chef.getId());
        assertEquals("Антон", chef.getFirstName());
        assertEquals("Петров", chef.getLastName());
        assertEquals(50000.0, chef.getSalary(), 0.01);
    }

    @Test
    @DisplayName("Получение полного имени")
    void testGetFullName() {
        assertEquals("Антон Петров", chef.getFullName());
    }

    @Test
    @DisplayName("Получение роли")
    void testGetRole() {
        assertEquals("Повар", chef.getRole());
    }

    @Test
    @DisplayName("Получение роли из enum")
    void testGetEmployeeRole() {
        assertEquals(EmployeeRole.CHEF, chef.getEmployeeRole());
    }

    @Test
    @DisplayName("Начальное количество приготовленных пицц равно нулю")
    void testInitialPizzasCookedIsZero() {
        assertEquals(0, chef.getPizzasCooked());
    }

    @Test
    @DisplayName("Приготовление пиццы увеличивает счетчик")
    void testCookPizzaIncreasesCount() throws InsufficientIngredientsException {
        chef.cookPizza(pizza);
        assertEquals(1, chef.getPizzasCooked());

        chef.cookPizza(pizza);
        assertEquals(2, chef.getPizzasCooked());
    }

    @Test
    @DisplayName("Приготовление пиццы возвращает время")
    void testCookPizzaReturnsTime() throws InsufficientIngredientsException {
        int cookingTime = chef.cookPizza(pizza);
        assertEquals(pizza.getPreparationTime(), cookingTime);
    }

    @Test
    @DisplayName("Начальный опыт равен нулю")
    void testInitialExperienceIsZero() {
        assertEquals(0, chef.getExperienceYears());
    }

    @Test
    @DisplayName("Установка опыта работы")
    void testSetExperience() {
        chef.setExperienceYears(5);
        assertEquals(5, chef.getExperienceYears());
    }

    @Test
    @DisplayName("Уровень навыка новичка")
    void testNoviceSkillLevel() {
        chef.setExperienceYears(0);
        assertEquals("Новичок", chef.getSkillLevel());

        chef.setExperienceYears(1);
        assertEquals("Новичок", chef.getSkillLevel());
    }

    @Test
    @DisplayName("Уровень навыка опытного повара")
    void testExperiencedSkillLevel() {
        chef.setExperienceYears(2);
        assertEquals("Опытный", chef.getSkillLevel());

        chef.setExperienceYears(4);
        assertEquals("Опытный", chef.getSkillLevel());
    }

    @Test
    @DisplayName("Уровень навыка профессионала")
    void testProfessionalSkillLevel() {
        chef.setExperienceYears(5);
        assertEquals("Профессионал", chef.getSkillLevel());

        chef.setExperienceYears(9);
        assertEquals("Профессионал", chef.getSkillLevel());
    }

    @Test
    @DisplayName("Уровень навыка мастера")
    void testMasterSkillLevel() {
        chef.setExperienceYears(10);
        assertEquals("Мастер", chef.getSkillLevel());

        chef.setExperienceYears(15);
        assertEquals("Мастер", chef.getSkillLevel());
    }

    @Test
    @DisplayName("Установка специализации")
    void testSetSpecialty() {
        chef.setSpecialty("Маргарита");
        assertEquals("Маргарита", chef.getSpecialty());
    }

    @Test
    @DisplayName("Повар может готовить свою специализацию")
    void testCanCookSpecialtyPizza() {
        chef.setSpecialty("Маргарита");
        assertTrue(chef.canCookPizza("Маргарита"));
        assertTrue(chef.canCookPizza("маргарита"));
    }

    @Test
    @DisplayName("Новичок не может готовить пиццу вне специализации")
    void testNoviceCannotCookNonSpecialtyPizza() {
        chef.setSpecialty("Маргарита");
        chef.setExperienceYears(1);
        assertFalse(chef.canCookPizza("Пепперони"));
    }

    @Test
    @DisplayName("Опытный повар может готовить любую пиццу")
    void testExperiencedChefCanCookAnyPizza() {
        chef.setExperienceYears(3);
        assertTrue(chef.canCookPizza("Маргарита"));
        assertTrue(chef.canCookPizza("Пепперони"));
        assertTrue(chef.canCookPizza("Карбонара"));
    }

    @Test
    @DisplayName("Инкремент счетчика приготовленных пицц")
    void testIncrementPizzasCooked() {
        chef.incrementPizzasCooked();
        assertEquals(1, chef.getPizzasCooked());

        chef.incrementPizzasCooked();
        assertEquals(2, chef.getPizzasCooked());
    }
}
