package com.resumematch.common;

import lombok.Data;

@Data
public class R<T> {

    private int code;
    private String message;
    private T data;

    private R() {}

    public static <T> R<T> ok() {
        R<T> r = new R<T>();
        r.code = 200;
        r.message = "操作成功";
        return r;
    }

    public static <T> R<T> ok(T data) {
        R<T> r = ok();
        r.data = data;
        return r;
    }

    public static <T> R<T> ok(String message, T data) {
        R<T> r = ok(data);
        r.message = message;
        return r;
    }

    public static <T> R<T> fail(String message) {
        R<T> r = new R<T>();
        r.code = 500;
        r.message = message;
        return r;
    }

    public static <T> R<T> fail(int code, String message) {
        R<T> r = new R<T>();
        r.code = code;
        r.message = message;
        return r;
    }

    public static <T> R<T> unauthorized(String message) {
        return fail(401, message);
    }

    public static <T> R<T> forbidden(String message) {
        return fail(403, message);
    }
}
