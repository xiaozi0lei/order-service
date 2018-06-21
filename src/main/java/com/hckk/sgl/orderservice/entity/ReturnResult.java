package com.hckk.sgl.orderservice.entity;

import lombok.Data;

/**
 * @author Sun Guolei 2018/6/21 14:34
 */
@Data
public class ReturnResult<T> {
    private int code = 200;
    private String message;
    private T data;

    public ReturnResult() {}

    private ReturnResult(T data) {
        this.data = data;
    }

    private ReturnResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static <T> ReturnResult<T> success(T data) {
        ReturnResult<T> result = new ReturnResult<>(data);
        result.setCode(200);
        result.setMessage("成功");
        return result;
    }

    public static <T> ReturnResult<T> fail(int code, String message) {
        return new ReturnResult<>(code, message);
    }
}
