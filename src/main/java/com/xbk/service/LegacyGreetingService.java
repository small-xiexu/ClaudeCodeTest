package com.xbk.service;

import java.util.Optional;

/**
 * 问候语生成服务（重构版）
 * 使用枚举和策略方法消除深度嵌套
 *
 * @author xiexu
 */
public class LegacyGreetingService {

    /**
     * 根据用户、时间、语言生成问候语
     */
    public String generateWelcomeMessage(User user, int hour, String lang) {
        Language language = Language.fromCode(lang);
        TimeOfDay timeOfDay = TimeOfDay.fromHour(hour);
        Optional<UserType> userType = Optional.ofNullable(user).map(u -> UserType.fromCode(u.getType()));

        return buildGreeting(user, timeOfDay, language, userType);
    }

    /**
     * 组装问候语
     */
    private String buildGreeting(User user, TimeOfDay timeOfDay, Language language, Optional<UserType> userType) {
        if (language == Language.UNKNOWN) {
            return "Hi";
        }

        String timePart = getTimeGreeting(timeOfDay, language);
        String userPart = getUserTitle(user, timeOfDay, language, userType);

        return timePart + userPart;
    }

    /**
     * 获取时间段问候语
     */
    private String getTimeGreeting(TimeOfDay timeOfDay, Language language) {
        if (language == Language.ZH) {
            switch (timeOfDay) {
                case MORNING:
                    return "早上好，";
                case AFTERNOON:
                    return "下午好，";
                case EVENING:
                    return "晚上好";
                default:
                    return "你好，";
            }
        } else if (language == Language.EN) {
            if (timeOfDay == TimeOfDay.MORNING) {
                return "Good morning, ";
            }
            return "Hello ";
        }
        return "";
    }

    /**
     * 获取用户称谓部分
     */
    private String getUserTitle(User user, TimeOfDay timeOfDay, Language language, Optional<UserType> userType) {
        // 管理员特殊处理
        if (user != null && userType.isPresent() && userType.get() == UserType.ADMIN) {
            return getAdminTitle(timeOfDay, language);
        }

        // 晚上时段（中文）非管理员统一返回空
        if (language == Language.ZH && timeOfDay == TimeOfDay.EVENING) {
            return "";
        }

        // 游客场景（非晚上）
        if (user == null) {
            return getGuestTitle(language);
        }

        // VIP 用户特殊称谓
        if (userType.isPresent() && userType.get() == UserType.VIP) {
            return getVipTitle(user, timeOfDay, language);
        }

        // 普通用户
        return user.getName();
    }

    /**
     * 游客称谓
     */
    private String getGuestTitle(Language language) {
        return language == Language.ZH ? "游客" : "Guest";
    }

    /**
     * 管理员称谓
     */
    private String getAdminTitle(TimeOfDay timeOfDay, Language language) {
        if (language == Language.ZH) {
            if (timeOfDay == TimeOfDay.MORNING) {
                return "管理员，系统监控正常";
            } else if (timeOfDay == TimeOfDay.AFTERNOON) {
                return "管理员";
            } else if (timeOfDay == TimeOfDay.EVENING) {
                return "，管理员，请注意休息";
            }
        }
        return "Admin";
    }

    /**
     * VIP 用户称谓
     */
    private String getVipTitle(User user, TimeOfDay timeOfDay, Language language) {
        if (language == Language.ZH) {
            if (timeOfDay == TimeOfDay.MORNING) {
                // 早上区分性别
                Gender gender = Gender.fromCode(user.getGender());
                switch (gender) {
                    case MALE:
                        return "尊敬的 " + user.getName() + " 先生";
                    case FEMALE:
                        return "尊贵的 " + user.getName() + " 女士";
                    default:
                        return "尊贵的 VIP 会员";
                }
            } else if (timeOfDay == TimeOfDay.AFTERNOON) {
                return "尊贵的 VIP " + user.getName();
            }
        } else if (language == Language.EN && timeOfDay == TimeOfDay.MORNING) {
            return "Sir " + user.getName();
        }
        return user.getName();
    }

    // ==================== 枚举定义 ====================

    /**
     * 用户类型枚举
     */
    enum UserType {
        GUEST(0),
        VIP(1),
        NORMAL(2),
        ADMIN(99);

        private final int code;

        UserType(int code) {
            this.code = code;
        }

        static UserType fromCode(int code) {
            for (UserType type : values()) {
                if (type.code == code) {
                    return type;
                }
            }
            return GUEST;
        }
    }

    /**
     * 性别枚举
     */
    enum Gender {
        MALE(1),
        FEMALE(2),
        UNKNOWN(0);

        private final int code;

        Gender(int code) {
            this.code = code;
        }

        static Gender fromCode(int code) {
            for (Gender gender : values()) {
                if (gender.code == code) {
                    return gender;
                }
            }
            return UNKNOWN;
        }
    }

    /**
     * 时间段枚举
     */
    enum TimeOfDay {
        MORNING(0, 11),
        AFTERNOON(12, 18),
        EVENING(19, 23);

        private final int startHour;
        private final int endHour;

        TimeOfDay(int startHour, int endHour) {
            this.startHour = startHour;
            this.endHour = endHour;
        }

        static TimeOfDay fromHour(int hour) {
            for (TimeOfDay time : values()) {
                if (hour >= time.startHour && hour <= time.endHour) {
                    return time;
                }
            }
            // 默认为晚上
            return EVENING;
        }
    }

    /**
     * 语言枚举
     */
    enum Language {
        ZH("zh"),
        EN("en"),
        UNKNOWN("");

        private final String code;

        Language(String code) {
            this.code = code;
        }

        static Language fromCode(String code) {
            if (code == null) {
                return UNKNOWN;
            }
            for (Language lang : values()) {
                if (lang.code.equals(code)) {
                    return lang;
                }
            }
            return UNKNOWN;
        }
    }

    // 为了让代码能编译通过的模拟类
    public static class User {
        private String name;
        private int type; // 0=Guest, 1=VIP, 2=Normal, 99=Admin
        private int gender; // 1=Male, 2=Female

        // Constructor
        public User(String name, int type, int gender) {
            this.name = name;
            this.type = type;
            this.gender = gender;
        }

        // Getters ...
        public String getName() { return name; }
        public int getType() { return type; }
        public int getGender() { return gender; }
    }
}