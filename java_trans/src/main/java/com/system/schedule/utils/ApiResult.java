package com.system.schedule.utils;

import lombok.Data;

@Data
public class ApiResult {
    private Boolean isSuccess;
    private Object result;
    private String msg;

    public static ApiResult success(Object data) {
        ApiResult result = new ApiResult();
        result.setIsSuccess(true);
        result.setResult(data);
        result.setMsg("");
        return result;
    }

    public static ApiResult success(String message) {
        ApiResult result = new ApiResult();
        result.setIsSuccess(true);
        result.setResult(null);
        result.setMsg(message);
        return result;
    }

    public static ApiResult error(String msg) {
        ApiResult result = new ApiResult();
        result.setIsSuccess(false);
        result.setResult(null);
        result.setMsg(msg);
        return result;
    }
}