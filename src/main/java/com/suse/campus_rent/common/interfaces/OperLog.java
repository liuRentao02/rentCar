package com.suse.campus_rent.common.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义系统日志注解
 */
@Target(ElementType.METHOD) // 作用于方法上
@Retention(RetentionPolicy.RUNTIME) // 运行时保留
public @interface OperLog {
    /**
     * 日志标题（如：用户登录、删除订单）
     */
    String title() default "";

    /**
     * 日志分类（如：AUTH, ORDER, USER）
     */
    String category() default "SYSTEM";

    /**
     * 日志级别（默认 INFO）
     */
    String level() default "INFO";
}
