package com.hongs.skyserver.handler;


import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.hongs.skycommon.constant.AutoFillConstant;
import com.hongs.skycommon.context.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("开始插入填充...");

        // 1. 填充 createTime (User实体有这个字段，会执行)
        if (metaObject.hasSetter(AutoFillConstant.CREATE_TIME)) {
            this.strictInsertFill(metaObject, AutoFillConstant.CREATE_TIME, LocalDateTime.class, LocalDateTime.now());
        }

        // 2. 填充 createUser (User实体没有这个字段，hasSetter返回false，跳过，不会报错)
        if (metaObject.hasSetter(AutoFillConstant.CREATE_USER)) {
            this.strictInsertFill(metaObject, AutoFillConstant.CREATE_USER, Long.class, BaseContext.getCurrentId());
        }

        // 3. 填充 updateTime
        if (metaObject.hasSetter(AutoFillConstant.UPDATE_TIME)) {
            this.strictInsertFill(metaObject, AutoFillConstant.UPDATE_TIME, LocalDateTime.class, LocalDateTime.now());
        }

        // 4. 填充 updateUser
        if (metaObject.hasSetter(AutoFillConstant.UPDATE_USER)) {
            this.strictInsertFill(metaObject, AutoFillConstant.UPDATE_USER, Long.class, BaseContext.getCurrentId());
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("开始更新填充...");

        if (metaObject.hasSetter(AutoFillConstant.UPDATE_TIME)) {
            this.strictUpdateFill(metaObject, AutoFillConstant.UPDATE_TIME, LocalDateTime.class, LocalDateTime.now());
        }

        if (metaObject.hasSetter(AutoFillConstant.UPDATE_USER)) {
            this.strictUpdateFill(metaObject, AutoFillConstant.UPDATE_USER, Long.class, BaseContext.getCurrentId());
        }
    }
}