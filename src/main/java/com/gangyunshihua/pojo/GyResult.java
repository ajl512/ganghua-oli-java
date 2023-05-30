package com.gangyunshihua.pojo;

public class GyResult<T> {

    private int code = 1;
    private String msg = "";
    private T data;

    public static GyResult success() {
        return new GyResult();
    }

    public static <T> GyResult success(T data) {
        return new GyResult().data(data);
    }

    public static GyResult fail(String msg) {
        return new GyResult().code(2).msg(msg);
    }

    public GyResult code(int code) {
        this.code = code;
        if (code == 0) this.msg = "登陆状态已过期，请重新登陆";
        return this;
    }

    public GyResult data(T data) {
        this.data = data;
        return this;
    }

    public GyResult msg(String msg) {
        this.msg = msg;
        return this;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }
}
