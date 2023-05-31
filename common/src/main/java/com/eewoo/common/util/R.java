package com.eewoo.common.util;

/**
 * 负责处理所有的返回信息，利用R的泛型对于成功或者失败的信息统一封装，减轻Controller层开发难度
 * @param <T>
 */
public class R<T> {
    private String code;
    private String msg;
    private T data;

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }
    public R() {
    }
    public R(T data) {
        this.data = data;
    }

    public static R ok() {
        R result = new R<>();
        result.setCode(Constant.success);
        result.setMsg("success!");
        return result;
    }

    public static <T> R<T> ok(T data) {
        R<T> result = new R<>(data);
        result.setCode(Constant.success);
        result.setMsg("success!");
        return result;
    }

    public static R err(String code, String msg) {
        R result = new R();
        result.setCode(Constant.fail);
        result.setMsg(msg);
        return result;
    }

    public static R err(String msg) {
        R result = new R();
        result.setCode(Constant.fail);
        result.setMsg(msg);
        return result;
    }

    public static R err() {
        R result = new R();
        result.setCode(Constant.fail);
        result.setMsg("fail!");
        return result;
    }
}