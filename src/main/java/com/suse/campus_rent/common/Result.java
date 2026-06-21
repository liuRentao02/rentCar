package com.suse.campus_rent.common;

import lombok.Data;

@Data
public class Result<T> {
    private Integer code;
    private String message;
    private T data;

    // 成功
    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.SUCCESS);
        result.setMessage("success");
        return result;
    }

    // 成功，并返回数据
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.SUCCESS);
        result.setMessage("success");
        result.setData(data);
        return result;
    }

    // 失败
    public static <T> Result<T> error(String message) {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.ERROR);
        result.setMessage(message);
        return result;
    }

    // 参数错误
    public static <T> Result<T> badRequest(String message) {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.BAD_REQUEST);
        result.setMessage(message);
        return result;
    }

    // 未授权
    public static <T> Result<T> unauthorized(String message) {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.UNAUTHORIZED);
        result.setMessage(message);
        return result;
    }

    // 在 Result 类中添加
    public static <T> Result<T> error(int code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    public static <T> Result<T> forbidden(String message) {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.FORBIDDEN);
        result.setMessage(message);
        return result;
    }
}
