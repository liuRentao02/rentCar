package com.suse.campus_rent.common.exception;

import com.suse.campus_rent.common.ResultCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessException extends RuntimeException {
    private final int code;

    public BusinessException(String message) {
        super(message);
        this.code = ResultCode.ERROR; // 默认 500
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }
}