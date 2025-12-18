package com.xbk.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * MyBatis-Plus 自动填充处理器
 * 自动填充创建时间和更新时间字段
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * 插入时自动填充
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        // 自动填充 createdAt 字段
        this.strictInsertFill(metaObject, "createdAt", Timestamp.class, now);
        // 自动填充 updatedAt 字段
        this.strictInsertFill(metaObject, "updatedAt", Timestamp.class, now);
    }

    /**
     * 更新时自动填充
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        // 自动填充 updatedAt 字段
        this.strictUpdateFill(metaObject, "updatedAt", Timestamp.class, now);
    }
}
