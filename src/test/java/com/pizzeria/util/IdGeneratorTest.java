package com.pizzeria.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты для IdGenerator")
class IdGeneratorTest {

    @BeforeEach
    void setUp() {
        // Сброс счетчиков перед каждым тестом
        IdGenerator.resetCounters();
    }

    @AfterEach
    void tearDown() {
        // Сброс счетчиков после каждого теста для изоляции
        IdGenerator.resetCounters();
    }

    @Test
    @DisplayName("Генерация ID заказа с правильным префиксом")
    void testGenerateOrderId_HasCorrectPrefix() {
        // Act
        String orderId = IdGenerator.generateOrderId();

        // Assert
        assertNotNull(orderId);
        assertTrue(orderId.startsWith("ORD-"));
    }

    @Test
    @DisplayName("Генерация ID заказа с правильным форматом")
    void testGenerateOrderId_HasCorrectFormat() {
        // Act
        String orderId = IdGenerator.generateOrderId();

        // Assert
        assertEquals("ORD-000001", orderId);
    }

    @Test
    @DisplayName("Генерация нескольких ID заказов увеличивает счетчик")
    void testGenerateOrderId_MultipleIds_Increments() {
        // Act
        String id1 = IdGenerator.generateOrderId();
        String id2 = IdGenerator.generateOrderId();
        String id3 = IdGenerator.generateOrderId();

        // Assert
        assertEquals("ORD-000001", id1);
        assertEquals("ORD-000002", id2);
        assertEquals("ORD-000003", id3);
    }

    @Test
    @DisplayName("Генерация ID заказа - все ID уникальны")
    void testGenerateOrderId_AllIdsUnique() {
        // Act
        String id1 = IdGenerator.generateOrderId();
        String id2 = IdGenerator.generateOrderId();
        String id3 = IdGenerator.generateOrderId();

        // Assert
        assertNotEquals(id1, id2);
        assertNotEquals(id2, id3);
        assertNotEquals(id1, id3);
    }

    @Test
    @DisplayName("Генерация ID клиента с правильным префиксом")
    void testGenerateCustomerId_HasCorrectPrefix() {
        // Act
        String customerId = IdGenerator.generateCustomerId();

        // Assert
        assertNotNull(customerId);
        assertTrue(customerId.startsWith("CUST-"));
    }

    @Test
    @DisplayName("Генерация ID клиента с правильным форматом")
    void testGenerateCustomerId_HasCorrectFormat() {
        // Act
        String customerId = IdGenerator.generateCustomerId();

        // Assert
        assertEquals("CUST-000001", customerId);
    }

    @Test
    @DisplayName("Генерация нескольких ID клиентов увеличивает счетчик")
    void testGenerateCustomerId_MultipleIds_Increments() {
        // Act
        String id1 = IdGenerator.generateCustomerId();
        String id2 = IdGenerator.generateCustomerId();
        String id3 = IdGenerator.generateCustomerId();

        // Assert
        assertEquals("CUST-000001", id1);
        assertEquals("CUST-000002", id2);
        assertEquals("CUST-000003", id3);
    }

    @Test
    @DisplayName("Генерация ID сотрудника с правильным префиксом")
    void testGenerateEmployeeId_HasCorrectPrefix() {
        // Act
        String employeeId = IdGenerator.generateEmployeeId();

        // Assert
        assertNotNull(employeeId);
        assertTrue(employeeId.startsWith("EMP-"));
    }

    @Test
    @DisplayName("Генерация ID сотрудника с правильным форматом")
    void testGenerateEmployeeId_HasCorrectFormat() {
        // Act
        String employeeId = IdGenerator.generateEmployeeId();

        // Assert
        assertEquals("EMP-000001", employeeId);
    }

    @Test
    @DisplayName("Генерация нескольких ID сотрудников увеличивает счетчик")
    void testGenerateEmployeeId_MultipleIds_Increments() {
        // Act
        String id1 = IdGenerator.generateEmployeeId();
        String id2 = IdGenerator.generateEmployeeId();
        String id3 = IdGenerator.generateEmployeeId();

        // Assert
        assertEquals("EMP-000001", id1);
        assertEquals("EMP-000002", id2);
        assertEquals("EMP-000003", id3);
    }

    @Test
    @DisplayName("Генерация UUID не пустая")
    void testGenerateUUID_NotEmpty() {
        // Act
        String uuid = IdGenerator.generateUUID();

        // Assert
        assertNotNull(uuid);
        assertFalse(uuid.isEmpty());
    }

    @Test
    @DisplayName("Генерация UUID - все UUID уникальны")
    void testGenerateUUID_AllUnique() {
        // Act
        String uuid1 = IdGenerator.generateUUID();
        String uuid2 = IdGenerator.generateUUID();
        String uuid3 = IdGenerator.generateUUID();

        // Assert
        assertNotEquals(uuid1, uuid2);
        assertNotEquals(uuid2, uuid3);
        assertNotEquals(uuid1, uuid3);
    }

    @Test
    @DisplayName("Генерация UUID имеет правильный формат")
    void testGenerateUUID_HasCorrectFormat() {
        // Act
        String uuid = IdGenerator.generateUUID();

        // Assert
        // UUID формат: 8-4-4-4-12 символов
        assertTrue(uuid.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}"));
    }

    @Test
    @DisplayName("Генерация ID транзакции с префиксом")
    void testGenerateTransactionId_WithPrefix_HasCorrectFormat() {
        // Act
        String transactionId = IdGenerator.generateTransactionId("TRANS");

        // Assert
        assertNotNull(transactionId);
        assertTrue(transactionId.startsWith("TRANS-"));
    }

    @Test
    @DisplayName("Генерация ID транзакции содержит timestamp")
    void testGenerateTransactionId_ContainsTimestamp() {
        // Act
        String transactionId = IdGenerator.generateTransactionId("PAY");

        // Assert
        assertTrue(transactionId.startsWith("PAY-"));
        String timestampPart = transactionId.substring(4);
        assertTrue(timestampPart.matches("\\d+"));
    }

    @Test
    @DisplayName("Генерация нескольких ID транзакций - все уникальны")
    void testGenerateTransactionId_MultipleIds_AllUnique() throws InterruptedException {
        // Act
        String id1 = IdGenerator.generateTransactionId("TX");
        Thread.sleep(2); // Небольшая задержка для разных timestamp
        String id2 = IdGenerator.generateTransactionId("TX");
        Thread.sleep(2);
        String id3 = IdGenerator.generateTransactionId("TX");

        // Assert
        assertNotEquals(id1, id2);
        assertNotEquals(id2, id3);
        assertNotEquals(id1, id3);
    }

    @Test
    @DisplayName("Генерация ID транзакции с пустым префиксом")
    void testGenerateTransactionId_EmptyPrefix_Success() {
        // Act
        String transactionId = IdGenerator.generateTransactionId("");

        // Assert
        assertNotNull(transactionId);
        assertTrue(transactionId.startsWith("-"));
    }

    @Test
    @DisplayName("Сброс счетчиков возвращает их к начальному значению")
    void testResetCounters_ResetsAllCounters() {
        // Arrange - генерируем несколько ID
        IdGenerator.generateOrderId();
        IdGenerator.generateCustomerId();
        IdGenerator.generateEmployeeId();

        // Act
        IdGenerator.resetCounters();

        // Assert
        assertEquals("ORD-000001", IdGenerator.generateOrderId());
        assertEquals("CUST-000001", IdGenerator.generateCustomerId());
        assertEquals("EMP-000001", IdGenerator.generateEmployeeId());
    }

    @Test
    @DisplayName("Счетчики заказов независимы от счетчиков клиентов")
    void testCounters_OrderAndCustomer_Independent() {
        // Act
        String orderId1 = IdGenerator.generateOrderId();
        String customerId1 = IdGenerator.generateCustomerId();
        String orderId2 = IdGenerator.generateOrderId();
        String customerId2 = IdGenerator.generateCustomerId();

        // Assert
        assertEquals("ORD-000001", orderId1);
        assertEquals("CUST-000001", customerId1);
        assertEquals("ORD-000002", orderId2);
        assertEquals("CUST-000002", customerId2);
    }

    @Test
    @DisplayName("Счетчики сотрудников независимы от других счетчиков")
    void testCounters_Employee_Independent() {
        // Act
        String employeeId1 = IdGenerator.generateEmployeeId();
        String orderId = IdGenerator.generateOrderId();
        String employeeId2 = IdGenerator.generateEmployeeId();

        // Assert
        assertEquals("EMP-000001", employeeId1);
        assertEquals("ORD-000001", orderId);
        assertEquals("EMP-000002", employeeId2);
    }

    @Test
    @DisplayName("Генерация большого количества ID заказов")
    void testGenerateOrderId_LargeNumber_CorrectFormat() {
        // Arrange
        for (int i = 0; i < 999; i++) {
            IdGenerator.generateOrderId();
        }

        // Act
        String orderId = IdGenerator.generateOrderId();

        // Assert
        assertEquals("ORD-001000", orderId);
    }

    @Test
    @DisplayName("Форматирование ID с ведущими нулями")
    void testIdFormat_LeadingZeros_Correct() {
        // Act
        String id1 = IdGenerator.generateOrderId();
        String id10 = IdGenerator.generateOrderId();

        for (int i = 0; i < 8; i++) {
            IdGenerator.generateOrderId();
        }
        String id10th = IdGenerator.generateOrderId();

        // Assert
        assertTrue(id1.endsWith("000001"));
        assertTrue(id10.endsWith("000002"));
        assertTrue(id10th.endsWith("000010"));
    }

    @Test
    @DisplayName("Все типы ID можно генерировать одновременно")
    void testGenerateAllTypes_Simultaneously_Success() {
        // Act
        String orderId = IdGenerator.generateOrderId();
        String customerId = IdGenerator.generateCustomerId();
        String employeeId = IdGenerator.generateEmployeeId();
        String uuid = IdGenerator.generateUUID();
        String transactionId = IdGenerator.generateTransactionId("TX");

        // Assert
        assertNotNull(orderId);
        assertNotNull(customerId);
        assertNotNull(employeeId);
        assertNotNull(uuid);
        assertNotNull(transactionId);

        assertTrue(orderId.startsWith("ORD-"));
        assertTrue(customerId.startsWith("CUST-"));
        assertTrue(employeeId.startsWith("EMP-"));
        assertTrue(transactionId.startsWith("TX-"));
    }
}
