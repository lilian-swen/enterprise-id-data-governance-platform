package com.idgovern.common;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * Standardized JSON response wrapper (Result Envelope) for API responses.
 *
 * <p>
 * This class provides a consistent structure for all responses returned by the backend.
 * Instead of returning raw data (e.g., List of Users), all responses are wrapped in
 * this object so that the frontend can uniformly handle success/failure, messages, and payloads.
 * </p>
 *
 * <p>
 * Business Rules:
 * <ul>
 *     <li>{@code ret} indicates whether the business logic succeeded (true) or failed (false).</li>
 *     <li>{@code msg} provides a human-readable message describing the result.</li>
 *     <li>{@code data} holds the payload (any object type), e.g., User, List, Map.</li>
 *     <li>Static factory methods are provided to simplify controller response creation.</li>
 * </ul>
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author     | Description                     |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-03-06 | Lilian S.  | Initial creation                |
 * ------------------------------------------------------------------------
 *
 * @author Lilian
 * @version 1.0
 * @since 1.0
 */

@Getter
@Setter
public class JsonData {
    // The "Result" flag. true means the business logic succeeded; false means something went wrong (e.g., validation failed, record not found).
    private boolean ret;

    // A message for the user or developer (e.g., "Save successful" or "Invalid password").
    private String msg;

    // The actual payload. Since it is of type Object, it can hold anything: a single User, a List of Departments, or a Map.
    private Object data;

    public JsonData(boolean ret) {
        this.ret = ret;
    }

    // Static Factory Methods. This makes the code more readable in Controllers. Instead of using new JsonData(...), you call

    /**
     * success(Object, String): Used when you want to return data AND a custom message.
     * @param object
     * @param msg
     * @return object
     */
    public static JsonData success(Object object, String msg) {
        JsonData jsonData = new JsonData(true);
        jsonData.data = object;
        jsonData.msg = msg;
        return jsonData;
    }

    /**
     * The most common way to return successful data.
     * @param object
     * @return
     */
    public static JsonData success(Object object) {
        JsonData jsonData = new JsonData(true);
        jsonData.data = object;
        return jsonData;
    }

    /**
     * success(): Used for operations like "Delete" where you just need to confirm it worked but have no data to return.
     * @return
     */
    public static JsonData success() {
        return new JsonData(true);
    }

    /**
     * fail(String): Creates a response where ret is false. This is typically used in your global exception handler or validation logic.
     * @param msg
     * @return
     */
    public static JsonData fail(String msg) {
        JsonData jsonData = new JsonData(false);
        jsonData.msg = msg;
        return jsonData;
    }

    /**
     * This method converts the object into a standard HashMap. This is useful if:
     * 1. You are using an older JSON library that works better with Maps.
     * 2. You want to manually manipulate the fields before sending them to the View layer (like a JSP).
     * @return
     */
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("ret", ret);
        result.put("msg", msg);
        result.put("data", data);
        return result;
    }
}
