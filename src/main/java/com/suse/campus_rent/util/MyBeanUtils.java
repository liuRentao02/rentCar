package com.suse.campus_rent.util;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

public class MyBeanUtils {

    /**
     * 获取源对象中值为 null 或 空字符串 的属性名数组
     */
    private static String[] getNullAndEmptyPropertyNames(Object source) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(source);
        PropertyDescriptor[] propertyDescriptors = beanWrapper.getPropertyDescriptors();

        Set<String> ignoreNames = new HashSet<>();
        for (PropertyDescriptor pd : propertyDescriptors) {
            Object value = beanWrapper.getPropertyValue(pd.getName());
            // 条件1: 值为 null
            if (value == null) {
                ignoreNames.add(pd.getName());
            }
            // 条件2: 值为空字符串 (仅对 String 类型处理，其他类型不考虑)
            else if (value instanceof String && ((String) value).isEmpty()) {
                ignoreNames.add(pd.getName());
            }
        }
        return ignoreNames.toArray(new String[0]);
    }

    /**
     * 拷贝属性，忽略源对象中值为 null 或 空字符串 的属性
     *
     * @param src    源对象
     * @param target 目标对象
     */
    public static void copyPropertiesIgnoreNullAndEmpty(Object src, Object target) {
        BeanUtils.copyProperties(src, target, getNullAndEmptyPropertyNames(src));
    }
}