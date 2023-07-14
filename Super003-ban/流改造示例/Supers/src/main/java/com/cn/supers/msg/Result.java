package com.cn.supers.msg;


import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * The type Result.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
@SuppressWarnings("all")
public class Result extends LinkedHashMap<String, Object> implements Serializable {


    /**
     * The constant serialVersionUID.
     *
     * @author bdth
     * @email 2074055628 @qq.om
     */
    private static final long serialVersionUID = 1L;

    /**
     * The constant CODE_SUCCESS.
     *
     * @author bdth
     * @email 2074055628 @qq.om
     */
    public static final int CODE_SUCCESS = 200;

    /**
     * The constant CODE_ERROR.
     *
     * @author bdth
     * @email 2074055628 @qq.om
     */
    public static final int CODE_ERROR = 500;


    /**
     * Instantiates a new Result.
     *
     * @author bdth
     * @email 2074055628 @qq.om
     */
    public Result() {
    }

    /**
     * Instantiates a new Result.
     *
     * @param code the code
     * @param msg  the msg
     * @param data the data
     * @author bdth
     * @email 2074055628 @qq.om
     */
    public Result(int code, String msg, Object data) {
        this.setCode(code);
        this.setMsg(msg);
        this.setData(data);
    }


    /**
     * Instantiates a new Result.
     *
     * @param map the map
     * @author bdth
     * @email 2074055628 @qq.om
     */
    public Result(Map<String, ?> map) {
        this.setMap(map);
    }

    /**
     * Gets code.
     *
     * @return the code
     * @author bdth
     * @email 2074055628 @qq.om
     */
    public Integer getCode() {
        return (Integer) this.get("code");
    }

    /**
     * Gets msg.
     *
     * @return the msg
     * @author bdth
     * @email 2074055628 @qq.om
     */
    public String getMsg() {
        return (String) this.get("msg");
    }


    /**
     * Gets data.
     *
     * @return the data
     * @author bdth
     * @email 2074055628 @qq.om
     */
    public Object getData() {
        return this.get("data");
    }


    /**
     * Sets code.
     *
     * @param code the code
     * @return the code
     * @author bdth
     * @email 2074055628 @qq.om
     */
    public Result setCode(int code) {
        this.put("code", code);
        return this;
    }


    /**
     * Sets msg.
     *
     * @param msg the msg
     * @return the msg
     * @author bdth
     * @email 2074055628 @qq.om
     */
    public Result setMsg(String msg) {
        this.put("msg", msg);
        return this;
    }


    /**
     * Sets data.
     *
     * @param data the data
     * @return the data
     * @author bdth
     * @email 2074055628 @qq.om
     */
    public Result setData(Object data) {
        this.put("data", data);
        return this;
    }

    /**
     * Set result.
     *
     * @param key  the key
     * @param data the data
     * @return the result
     * @author bdth
     * @email 2074055628 @qq.om
     */
    public Result set(String key, Object data) {
        this.put(key, data);
        return this;
    }


    /**
     * Sets map.
     *
     * @param map the map
     * @return the map
     * @author bdth
     * @email 2074055628 @qq.om
     */
    public Result setMap(Map<String, ?> map) {
        Iterator var2 = map.keySet().iterator();

        while (var2.hasNext()) {
            String key = (String) var2.next();
            this.put(key, map.get(key));
        }

        return this;
    }

    /**
     * Ok result.
     *
     * @return the result
     * @author bdth
     * @email 2074055628 @qq.om
     */
    public static Result ok() {
        return new Result(CODE_SUCCESS, "操作成功", (Object) null);
    }


    /**
     * Ok result.
     *
     * @param msg the msg
     * @return the result
     * @author bdth
     * @email 2074055628 @qq.om
     */
    public static Result ok(String msg) {
        return new Result(CODE_SUCCESS, msg, (Object) null);
    }


    /**
     * Code result.
     *
     * @param code the code
     * @return the result
     * @author bdth
     * @email 2074055628 @qq.om
     */
    public static Result code(int code) {
        return new Result(code, (String) null, (Object) null);
    }


    /**
     * Data result.
     *
     * @param data the data
     * @return the result
     * @author bdth
     * @email 2074055628 @qq.om
     */
    public static Result data(Object data) {
        return new Result(CODE_SUCCESS, "ok", data);
    }


    /**
     * Error result.
     *
     * @return the result
     * @author bdth
     * @email 2074055628 @qq.om
     */
    public static Result error() {
        return new Result(CODE_ERROR, "操作失败", (Object) null);
    }


    /**
     * Error result.
     *
     * @return the result
     * @author bdth
     * @email 2074055628 @qq.om
     */
    public static Result error(final String msg, final Integer code) {
        return new Result(code, msg, (Object) null);
    }

    /**
     * Error result.
     *
     * @param msg the msg
     * @return the result
     * @author bdth
     * @email 2074055628 @qq.om
     */
    public static Result error(String msg) {
        return new Result(CODE_ERROR, msg, (Object) null);
    }


    /**
     * Build result.
     *
     * @param code the code
     * @param msg  the msg
     * @param data the data
     * @return the result
     * @author bdth
     * @email 2074055628 @qq.om
     */
    public static Result build(int code, String msg, Object data) {
        return new Result(code, msg, data);
    }


    /**
     * To string string.
     *
     * @return the string
     * @author bdth
     * @email 2074055628 @qq.om
     */
    public String toString() {
        return "{\"code\": " + this.getCode() + ", \"msg\": " + this.transValue(this.getMsg()) + ", \"data\": " + this.transValue(this.getData()) + "}";
    }


    /**
     * Trans value string.
     *
     * @param value the value
     * @return the string
     * @author bdth
     * @email 2074055628 @qq.om
     */
    private String transValue(Object value) {
        return value instanceof String ? "\"" + value + "\"" : String.valueOf(value);
    }
}
