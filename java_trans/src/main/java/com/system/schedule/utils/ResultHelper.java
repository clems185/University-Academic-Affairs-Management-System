package com.system.schedule.utils;

import java.util.HashMap;
import java.util.Map;

public class ResultHelper {

    public static Map<String, Object> success(Object data) {
        Map<String, Object> result = new HashMap<>();
        result.put("IsSuccess", true);
        result.put("Result", data);
        return result;
    }

    public static Map<String, Object> error(String message) {
        Map<String, Object> result = new HashMap<>();
        result.put("IsSuccess", false);
        result.put("Message", message);
        return result;
    }
}