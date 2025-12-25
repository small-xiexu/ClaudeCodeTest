package com.xbk.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * LegacyGreetingService 的测试类
 * 覆盖所有现有逻辑分支，作为重构的安全网
 *
 * @author xiexu
 */
class LegacyGreetingServiceTest {

    private LegacyGreetingService service;

    @BeforeEach
    void setUp() {
        service = new LegacyGreetingService();
    }

    @Nested
    @DisplayName("中文场景测试")
    class ChineseLanguageTests {

        @Nested
        @DisplayName("早上时段 (0-11)")
        class MorningTests {

            @Test
            @DisplayName("VIP男性用户 - 早上好，尊敬的 X 先生")
            void vipMaleUser() {
                LegacyGreetingService.User user = createUser("张三", 1, 1);
                String result = service.generateWelcomeMessage(user, 8, "zh");
                assertEquals("早上好，尊敬的 张三 先生", result);
            }

            @Test
            @DisplayName("VIP女性用户 - 早上好，尊贵的 X 女士")
            void vipFemaleUser() {
                LegacyGreetingService.User user = createUser("李四", 1, 2);
                String result = service.generateWelcomeMessage(user, 10, "zh");
                assertEquals("早上好，尊贵的 李四 女士", result);
            }

            @Test
            @DisplayName("VIP未知性别用户 - 早上好，尊贵的 VIP 会员")
            void vipUnknownGender() {
                LegacyGreetingService.User user = createUser("王五", 1, 0);
                String result = service.generateWelcomeMessage(user, 9, "zh");
                assertEquals("早上好，尊贵的 VIP 会员", result);
            }

            @Test
            @DisplayName("管理员用户 - 早上好，管理员，系统监控正常")
            void adminUser() {
                LegacyGreetingService.User user = createUser("Admin", 99, 1);
                String result = service.generateWelcomeMessage(user, 7, "zh");
                assertEquals("早上好，管理员，系统监控正常", result);
            }

            @Test
            @DisplayName("普通用户 - 早上好，X")
            void normalUser() {
                LegacyGreetingService.User user = createUser("赵六", 2, 1);
                String result = service.generateWelcomeMessage(user, 6, "zh");
                assertEquals("早上好，赵六", result);
            }

            @Test
            @DisplayName("游客 (null用户) - 早上好，游客")
            void guestUser() {
                String result = service.generateWelcomeMessage(null, 11, "zh");
                assertEquals("早上好，游客", result);
            }

            @Test
            @DisplayName("边界测试 - 0点属于早上")
            void boundary0Hour() {
                LegacyGreetingService.User user = createUser("测试", 2, 1);
                String result = service.generateWelcomeMessage(user, 0, "zh");
                assertEquals("早上好，测试", result);
            }

            @Test
            @DisplayName("边界测试 - 11点属于早上")
            void boundary11Hour() {
                LegacyGreetingService.User user = createUser("测试", 2, 1);
                String result = service.generateWelcomeMessage(user, 11, "zh");
                assertEquals("早上好，测试", result);
            }
        }

        @Nested
        @DisplayName("下午时段 (12-18)")
        class AfternoonTests {

            @Test
            @DisplayName("VIP用户 - 下午好，尊贵的 VIP X")
            void vipUser() {
                LegacyGreetingService.User user = createUser("张三", 1, 1);
                String result = service.generateWelcomeMessage(user, 14, "zh");
                assertEquals("下午好，尊贵的 VIP 张三", result);
            }

            @Test
            @DisplayName("管理员用户 - 下午好，管理员")
            void adminUser() {
                LegacyGreetingService.User user = createUser("Admin", 99, 1);
                String result = service.generateWelcomeMessage(user, 15, "zh");
                assertEquals("下午好，管理员", result);
            }

            @Test
            @DisplayName("普通用户 - 下午好，X")
            void normalUser() {
                LegacyGreetingService.User user = createUser("李四", 2, 1);
                String result = service.generateWelcomeMessage(user, 16, "zh");
                assertEquals("下午好，李四", result);
            }

            @Test
            @DisplayName("游客 - 下午好，游客")
            void guestUser() {
                String result = service.generateWelcomeMessage(null, 18, "zh");
                assertEquals("下午好，游客", result);
            }

            @Test
            @DisplayName("边界测试 - 12点属于下午")
            void boundary12Hour() {
                LegacyGreetingService.User user = createUser("测试", 2, 1);
                String result = service.generateWelcomeMessage(user, 12, "zh");
                assertEquals("下午好，测试", result);
            }
        }

        @Nested
        @DisplayName("晚上时段 (19-23)")
        class EveningTests {

            @Test
            @DisplayName("管理员用户 - 晚上好，管理员，请注意休息")
            void adminUser() {
                LegacyGreetingService.User user = createUser("Admin", 99, 1);
                String result = service.generateWelcomeMessage(user, 20, "zh");
                assertEquals("晚上好，管理员，请注意休息", result);
            }

            @Test
            @DisplayName("VIP用户 - 晚上好")
            void vipUser() {
                LegacyGreetingService.User user = createUser("张三", 1, 1);
                String result = service.generateWelcomeMessage(user, 22, "zh");
                assertEquals("晚上好", result);
            }

            @Test
            @DisplayName("普通用户 - 晚上好")
            void normalUser() {
                LegacyGreetingService.User user = createUser("李四", 2, 1);
                String result = service.generateWelcomeMessage(user, 23, "zh");
                assertEquals("晚上好", result);
            }

            @Test
            @DisplayName("游客 - 晚上好")
            void guestUser() {
                String result = service.generateWelcomeMessage(null, 19, "zh");
                assertEquals("晚上好", result);
            }
        }
    }

    @Nested
    @DisplayName("英文场景测试")
    class EnglishLanguageTests {

        @Nested
        @DisplayName("早上时段 (<12)")
        class MorningTests {

            @Test
            @DisplayName("VIP用户 - Good morning, Sir X")
            void vipUser() {
                LegacyGreetingService.User user = createUser("John", 1, 1);
                String result = service.generateWelcomeMessage(user, 8, "en");
                assertEquals("Good morning, Sir John", result);
            }

            @Test
            @DisplayName("普通用户 - Good morning, X")
            void normalUser() {
                LegacyGreetingService.User user = createUser("Alice", 2, 1);
                String result = service.generateWelcomeMessage(user, 10, "en");
                assertEquals("Good morning, Alice", result);
            }

            @Test
            @DisplayName("游客 - Good morning, Guest")
            void guestUser() {
                String result = service.generateWelcomeMessage(null, 11, "en");
                assertEquals("Good morning, Guest", result);
            }

            @Test
            @DisplayName("边界测试 - 11点属于早上")
            void boundary11Hour() {
                LegacyGreetingService.User user = createUser("Bob", 2, 1);
                String result = service.generateWelcomeMessage(user, 11, "en");
                assertEquals("Good morning, Bob", result);
            }
        }

        @Nested
        @DisplayName("其他时段 (>=12)")
        class OtherTimeTests {

            @Test
            @DisplayName("有用户 - Hello X")
            void withUser() {
                LegacyGreetingService.User user = createUser("Tom", 1, 1);
                String result = service.generateWelcomeMessage(user, 15, "en");
                assertEquals("Hello Tom", result);
            }

            @Test
            @DisplayName("游客 - Hello Guest")
            void guestUser() {
                String result = service.generateWelcomeMessage(null, 18, "en");
                assertEquals("Hello Guest", result);
            }

            @Test
            @DisplayName("边界测试 - 12点不属于早上")
            void boundary12Hour() {
                LegacyGreetingService.User user = createUser("Jerry", 2, 1);
                String result = service.generateWelcomeMessage(user, 12, "en");
                assertEquals("Hello Jerry", result);
            }
        }
    }

    @Nested
    @DisplayName("边界和异常场景测试")
    class EdgeCaseTests {

        @Test
        @DisplayName("null语言 - 返回 Hi")
        void nullLanguage() {
            LegacyGreetingService.User user = createUser("Test", 2, 1);
            String result = service.generateWelcomeMessage(user, 10, null);
            assertEquals("Hi", result);
        }

        @Test
        @DisplayName("未知语言 - 返回 Hi")
        void unknownLanguage() {
            LegacyGreetingService.User user = createUser("Test", 2, 1);
            String result = service.generateWelcomeMessage(user, 10, "fr");
            assertEquals("Hi", result);
        }

        @Test
        @DisplayName("负数小时 - 应处理为晚上")
        void negativeHour() {
            LegacyGreetingService.User user = createUser("测试", 2, 1);
            String result = service.generateWelcomeMessage(user, -1, "zh");
            // 根据代码逻辑，-1 不满足 0-11 和 12-18，会进入 else (晚上)
            assertEquals("晚上好", result);
        }

        @Test
        @DisplayName("超大小时 - 应处理为晚上")
        void largeHour() {
            LegacyGreetingService.User user = createUser("测试", 2, 1);
            String result = service.generateWelcomeMessage(user, 25, "zh");
            assertEquals("晚上好", result);
        }
    }

    // ==================== Helper Methods ====================

    /**
     * 创建测试用的 User 对象
     */
    private LegacyGreetingService.User createUser(String name, int type, int gender) {
        return new LegacyGreetingService.User(name, type, gender);
    }
}
