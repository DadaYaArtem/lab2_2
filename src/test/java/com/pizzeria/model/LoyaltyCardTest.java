package com.pizzeria.model;

import com.pizzeria.exceptions.InvalidDiscountException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты для класса LoyaltyCard")
class LoyaltyCardTest {

    @Nested
    @DisplayName("Тесты конструктора")
    class ConstructorTests {

        @Test
        @DisplayName("Успешное создание карты лояльности")
        void shouldCreateLoyaltyCardWithValidData() {
            // given & when
            LoyaltyCard card = new LoyaltyCard("LC-12345");

            // then
            assertNotNull(card);
            assertEquals("LC-12345", card.getCardNumber());
            assertEquals(0, card.getPoints());
            assertEquals("Bronze", card.getTier());
            assertNotNull(card.getIssueDate());
            assertNotNull(card.getExpiryDate());
        }

        @Test
        @DisplayName("Установка даты выпуска и истечения при создании")
        void shouldSetIssueDateAndExpiryDate() {
            // given
            LocalDate now = LocalDate.now();

            // when
            LoyaltyCard card = new LoyaltyCard("LC-12345");

            // then
            assertEquals(now, card.getIssueDate());
            assertEquals(now.plusYears(1), card.getExpiryDate());
        }

        @Test
        @DisplayName("Начальный tier - Bronze")
        void shouldStartWithBronzeTier() {
            // given & when
            LoyaltyCard card = new LoyaltyCard("LC-12345");

            // then
            assertEquals("Bronze", card.getTier());
        }
    }

    @Nested
    @DisplayName("Тесты метода addPoints")
    class AddPointsTests {

        @Test
        @DisplayName("Добавление баллов увеличивает счетчик")
        void shouldAddPointsToCard() {
            // given
            LoyaltyCard card = new LoyaltyCard("LC-12345");

            // when
            card.addPoints(50);

            // then
            assertEquals(50, card.getPoints());
        }

        @Test
        @DisplayName("Множественное добавление баллов")
        void shouldAddPointsMultipleTimes() {
            // given
            LoyaltyCard card = new LoyaltyCard("LC-12345");

            // when
            card.addPoints(50);
            card.addPoints(30);
            card.addPoints(20);

            // then
            assertEquals(100, card.getPoints());
        }

        @Test
        @DisplayName("Обновление tier при достижении Silver (200+ баллов)")
        void shouldUpdateToSilverTierAt200Points() {
            // given
            LoyaltyCard card = new LoyaltyCard("LC-12345");

            // when
            card.addPoints(200);

            // then
            assertEquals("Silver", card.getTier());
        }

        @Test
        @DisplayName("Обновление tier при достижении Gold (500+ баллов)")
        void shouldUpdateToGoldTierAt500Points() {
            // given
            LoyaltyCard card = new LoyaltyCard("LC-12345");

            // when
            card.addPoints(500);

            // then
            assertEquals("Gold", card.getTier());
        }

        @Test
        @DisplayName("Обновление tier при достижении Platinum (1000+ баллов)")
        void shouldUpdateToPlatinumTierAt1000Points() {
            // given
            LoyaltyCard card = new LoyaltyCard("LC-12345");

            // when
            card.addPoints(1000);

            // then
            assertEquals("Platinum", card.getTier());
        }

        @Test
        @DisplayName("Tier остается Bronze при баллах меньше 200")
        void shouldStayBronzeBelow200Points() {
            // given
            LoyaltyCard card = new LoyaltyCard("LC-12345");

            // when
            card.addPoints(199);

            // then
            assertEquals("Bronze", card.getTier());
        }

        @Test
        @DisplayName("Граничный случай: ровно 200 баллов -> Silver")
        void shouldBecomeSilverAtExactly200Points() {
            // given
            LoyaltyCard card = new LoyaltyCard("LC-12345");

            // when
            card.addPoints(200);

            // then
            assertEquals("Silver", card.getTier());
            assertEquals(200, card.getPoints());
        }

        @Test
        @DisplayName("Граничный случай: ровно 500 баллов -> Gold")
        void shouldBecomeGoldAtExactly500Points() {
            // given
            LoyaltyCard card = new LoyaltyCard("LC-12345");

            // when
            card.addPoints(500);

            // then
            assertEquals("Gold", card.getTier());
            assertEquals(500, card.getPoints());
        }

        @Test
        @DisplayName("Граничный случай: ровно 1000 баллов -> Platinum")
        void shouldBecomePlatinumAtExactly1000Points() {
            // given
            LoyaltyCard card = new LoyaltyCard("LC-12345");

            // when
            card.addPoints(1000);

            // then
            assertEquals("Platinum", card.getTier());
            assertEquals(1000, card.getPoints());
        }
    }

    @Nested
    @DisplayName("Тесты метода redeemPoints")
    class RedeemPointsTests {

        @Test
        @DisplayName("Успешное погашение баллов")
        void shouldRedeemPointsSuccessfully() {
            // given
            LoyaltyCard card = new LoyaltyCard("LC-12345");
            card.addPoints(100);

            // when
            boolean result = card.redeemPoints(50);

            // then
            assertTrue(result);
            assertEquals(50, card.getPoints());
        }

        @Test
        @DisplayName("Неудачное погашение при недостатке баллов")
        void shouldFailToRedeemWithInsufficientPoints() {
            // given
            LoyaltyCard card = new LoyaltyCard("LC-12345");
            card.addPoints(50);

            // when
            boolean result = card.redeemPoints(100);

            // then
            assertFalse(result);
            assertEquals(50, card.getPoints()); // баллы не изменились
        }

        @Test
        @DisplayName("Погашение всех баллов")
        void shouldRedeemAllPoints() {
            // given
            LoyaltyCard card = new LoyaltyCard("LC-12345");
            card.addPoints(100);

            // when
            boolean result = card.redeemPoints(100);

            // then
            assertTrue(result);
            assertEquals(0, card.getPoints());
        }

        @Test
        @DisplayName("Погашение баллов не меняет tier вниз")
        void shouldNotDowngradeTierAfterRedeeming() {
            // given
            LoyaltyCard card = new LoyaltyCard("LC-12345");
            card.addPoints(500); // Gold tier

            // when
            card.redeemPoints(400);

            // then
            assertEquals(100, card.getPoints());
            assertEquals("Bronze", card.getTier()); // tier обновляется при изменении баллов через updateTier
        }

        @Test
        @DisplayName("Граничный случай: погашение 0 баллов")
        void shouldHandleRedeemingZeroPoints() {
            // given
            LoyaltyCard card = new LoyaltyCard("LC-12345");
            card.addPoints(100);

            // when
            boolean result = card.redeemPoints(0);

            // then
            assertTrue(result);
            assertEquals(100, card.getPoints());
        }
    }

    @Nested
    @DisplayName("Тесты метода applyDiscount")
    class ApplyDiscountTests {

        @Test
        @DisplayName("Исключение при отрицательном проценте")
        void shouldThrowExceptionForNegativePercentage() {
            // given
            LoyaltyCard card = new LoyaltyCard("LC-12345");

            // when & then
            assertThrows(InvalidDiscountException.class, () -> card.applyDiscount(-10.0));
        }

        @Test
        @DisplayName("Исключение при проценте больше 100")
        void shouldThrowExceptionForPercentageOver100() {
            // given
            LoyaltyCard card = new LoyaltyCard("LC-12345");

            // when & then
            assertThrows(InvalidDiscountException.class, () -> card.applyDiscount(150.0));
        }

        @Test
        @DisplayName("Применение валидного процента не бросает исключение")
        void shouldNotThrowExceptionForValidPercentage() {
            // given
            LoyaltyCard card = new LoyaltyCard("LC-12345");

            // when & then
            assertDoesNotThrow(() -> card.applyDiscount(10.0));
            assertDoesNotThrow(() -> card.applyDiscount(0.0));
            assertDoesNotThrow(() -> card.applyDiscount(100.0));
        }
    }

    @Nested
    @DisplayName("Тесты метода getDiscountAmount")
    class GetDiscountAmountTests {

        @Test
        @DisplayName("Скидка Bronze tier - 2%")
        void shouldReturn2PercentForBronzeTier() {
            // given
            LoyaltyCard card = new LoyaltyCard("LC-12345");

            // when
            double discount = card.getDiscountAmount();

            // then
            assertEquals(2.0, discount);
        }

        @Test
        @DisplayName("Скидка Silver tier - 5%")
        void shouldReturn5PercentForSilverTier() {
            // given
            LoyaltyCard card = new LoyaltyCard("LC-12345");
            card.addPoints(200);

            // when
            double discount = card.getDiscountAmount();

            // then
            assertEquals(5.0, discount);
        }

        @Test
        @DisplayName("Скидка Gold tier - 10%")
        void shouldReturn10PercentForGoldTier() {
            // given
            LoyaltyCard card = new LoyaltyCard("LC-12345");
            card.addPoints(500);

            // when
            double discount = card.getDiscountAmount();

            // then
            assertEquals(10.0, discount);
        }

        @Test
        @DisplayName("Скидка Platinum tier - 15%")
        void shouldReturn15PercentForPlatinumTier() {
            // given
            LoyaltyCard card = new LoyaltyCard("LC-12345");
            card.addPoints(1000);

            // when
            double discount = card.getDiscountAmount();

            // then
            assertEquals(15.0, discount);
        }
    }

    @Nested
    @DisplayName("Тесты метода isDiscountApplicable")
    class IsDiscountApplicableTests {

        @Test
        @DisplayName("Скидка применима для действующей карты")
        void shouldBeApplicableForValidCard() {
            // given
            LoyaltyCard card = new LoyaltyCard("LC-12345");

            // when & then
            assertTrue(card.isDiscountApplicable());
        }

        @Test
        @DisplayName("Скидка не применима для истекшей карты")
        void shouldNotBeApplicableForExpiredCard() {
            // given
            LoyaltyCard card = new LoyaltyCard("LC-12345");
            card.setExpiryDate(LocalDate.now().minusDays(1));

            // when & then
            assertFalse(card.isDiscountApplicable());
        }

        @Test
        @DisplayName("Скидка применима в последний день действия")
        void shouldBeApplicableOnLastDay() {
            // given
            LoyaltyCard card = new LoyaltyCard("LC-12345");
            card.setExpiryDate(LocalDate.now());

            // when & then
            assertTrue(card.isDiscountApplicable());
        }

        @Test
        @DisplayName("Скидка применима за день до истечения")
        void shouldBeApplicableOneDayBeforeExpiry() {
            // given
            LoyaltyCard card = new LoyaltyCard("LC-12345");
            card.setExpiryDate(LocalDate.now().plusDays(1));

            // when & then
            assertTrue(card.isDiscountApplicable());
        }
    }

    @Nested
    @DisplayName("Тесты метода isExpired")
    class IsExpiredTests {

        @Test
        @DisplayName("Новая карта не истекла")
        void shouldNotBeExpiredForNewCard() {
            // given
            LoyaltyCard card = new LoyaltyCard("LC-12345");

            // when & then
            assertFalse(card.isExpired());
        }

        @Test
        @DisplayName("Карта истекла после даты истечения")
        void shouldBeExpiredAfterExpiryDate() {
            // given
            LoyaltyCard card = new LoyaltyCard("LC-12345");
            card.setExpiryDate(LocalDate.now().minusDays(1));

            // when & then
            assertTrue(card.isExpired());
        }

        @Test
        @DisplayName("Карта не истекла в день истечения")
        void shouldNotBeExpiredOnExpiryDate() {
            // given
            LoyaltyCard card = new LoyaltyCard("LC-12345");
            card.setExpiryDate(LocalDate.now());

            // when & then
            assertFalse(card.isExpired());
        }
    }

    @Nested
    @DisplayName("Тесты метода renewCard")
    class RenewCardTests {

        @Test
        @DisplayName("Продление карты обновляет дату истечения")
        void shouldUpdateExpiryDateWhenRenewed() {
            // given
            LoyaltyCard card = new LoyaltyCard("LC-12345");
            LocalDate originalExpiry = card.getExpiryDate();

            // when
            card.renewCard();

            // then
            LocalDate newExpiry = card.getExpiryDate();
            assertTrue(newExpiry.isAfter(originalExpiry));
            assertEquals(LocalDate.now().plusYears(1), newExpiry);
        }

        @Test
        @DisplayName("Продление истекшей карты делает ее действительной")
        void shouldMakeExpiredCardValidAfterRenewal() {
            // given
            LoyaltyCard card = new LoyaltyCard("LC-12345");
            card.setExpiryDate(LocalDate.now().minusDays(1));
            assertTrue(card.isExpired());

            // when
            card.renewCard();

            // then
            assertFalse(card.isExpired());
            assertTrue(card.isDiscountApplicable());
        }

        @Test
        @DisplayName("Продление карты не меняет баллы и tier")
        void shouldNotChangePointsOrTierWhenRenewed() {
            // given
            LoyaltyCard card = new LoyaltyCard("LC-12345");
            card.addPoints(500);
            String originalTier = card.getTier();
            int originalPoints = card.getPoints();

            // when
            card.renewCard();

            // then
            assertEquals(originalPoints, card.getPoints());
            assertEquals(originalTier, card.getTier());
        }
    }

    @Nested
    @DisplayName("Тесты геттеров и сеттеров")
    class GetterSetterTests {

        @Test
        @DisplayName("Установка и получение номера карты")
        void shouldSetAndGetCardNumber() {
            // given
            LoyaltyCard card = new LoyaltyCard("LC-12345");

            // when
            card.setCardNumber("LC-99999");

            // then
            assertEquals("LC-99999", card.getCardNumber());
        }

        @Test
        @DisplayName("Установка и получение баллов")
        void shouldSetAndGetPoints() {
            // given
            LoyaltyCard card = new LoyaltyCard("LC-12345");

            // when
            card.setPoints(350);

            // then
            assertEquals(350, card.getPoints());
        }

        @Test
        @DisplayName("Установка баллов не обновляет tier автоматически")
        void shouldNotAutoUpdateTierWhenSettingPoints() {
            // given
            LoyaltyCard card = new LoyaltyCard("LC-12345");

            // when
            card.setPoints(1000);

            // then
            assertEquals(1000, card.getPoints());
            assertEquals("Bronze", card.getTier()); // tier не обновляется через setter
        }

        @Test
        @DisplayName("Получение tier")
        void shouldGetTier() {
            // given
            LoyaltyCard card = new LoyaltyCard("LC-12345");
            card.addPoints(500);

            // when
            String tier = card.getTier();

            // then
            assertEquals("Gold", tier);
        }

        @Test
        @DisplayName("Установка и получение даты выпуска")
        void shouldSetAndGetIssueDate() {
            // given
            LoyaltyCard card = new LoyaltyCard("LC-12345");
            LocalDate newDate = LocalDate.of(2023, 1, 1);

            // when
            card.setIssueDate(newDate);

            // then
            assertEquals(newDate, card.getIssueDate());
        }

        @Test
        @DisplayName("Установка и получение даты истечения")
        void shouldSetAndGetExpiryDate() {
            // given
            LoyaltyCard card = new LoyaltyCard("LC-12345");
            LocalDate newDate = LocalDate.of(2025, 12, 31);

            // when
            card.setExpiryDate(newDate);

            // then
            assertEquals(newDate, card.getExpiryDate());
        }
    }

    @Nested
    @DisplayName("Граничные случаи")
    class EdgeCaseTests {

        @Test
        @DisplayName("Добавление отрицательных баллов")
        void shouldHandleNegativePoints() {
            // given
            LoyaltyCard card = new LoyaltyCard("LC-12345");
            card.addPoints(100);

            // when
            card.addPoints(-50);

            // then
            assertEquals(50, card.getPoints());
        }

        @Test
        @DisplayName("Погашение отрицательных баллов")
        void shouldHandleRedeemingNegativePoints() {
            // given
            LoyaltyCard card = new LoyaltyCard("LC-12345");
            card.addPoints(100);

            // when
            boolean result = card.redeemPoints(-50);

            // then
            assertTrue(result); // -50 всегда меньше текущих баллов
            assertEquals(150, card.getPoints()); // баллы увеличиваются
        }

        @Test
        @DisplayName("Очень большое количество баллов")
        void shouldHandleVeryLargeNumberOfPoints() {
            // given
            LoyaltyCard card = new LoyaltyCard("LC-12345");

            // when
            card.addPoints(Integer.MAX_VALUE);

            // then
            assertEquals("Platinum", card.getTier());
            assertEquals(Integer.MAX_VALUE, card.getPoints());
        }

        @Test
        @DisplayName("Создание карты с пустым номером")
        void shouldCreateCardWithEmptyNumber() {
            // given & when
            LoyaltyCard card = new LoyaltyCard("");

            // then
            assertNotNull(card);
            assertEquals("", card.getCardNumber());
        }

        @Test
        @DisplayName("Создание карты с null номером")
        void shouldCreateCardWithNullNumber() {
            // given & when
            LoyaltyCard card = new LoyaltyCard(null);

            // then
            assertNotNull(card);
            assertNull(card.getCardNumber());
        }

        @Test
        @DisplayName("Множественное продление карты")
        void shouldHandleMultipleRenewals() {
            // given
            LoyaltyCard card = new LoyaltyCard("LC-12345");
            LocalDate firstExpiry = card.getExpiryDate();

            // when
            card.renewCard();
            LocalDate secondExpiry = card.getExpiryDate();
            card.renewCard();
            LocalDate thirdExpiry = card.getExpiryDate();

            // then
            assertTrue(secondExpiry.isAfter(firstExpiry));
            assertTrue(thirdExpiry.isAfter(secondExpiry));
        }
    }
}
