package com.suse.campus_rent.common.enums;

import lombok.Getter;

@Getter
public enum CarStatusEnum {
    AVAILABLE("available", 0, "可租"),
    RENTED("rented", 1, "已租"),
    MAINTENANCE("maintenance", 2, "维护中"),
    UNAVAILABLE("unavailable", 3, "停用");

    private final String code;
    private final int dbValue;
    private final String text;

    CarStatusEnum(String code, int dbValue, String text) {
        this.code = code;
        this.dbValue = dbValue;
        this.text = text;
    }

}