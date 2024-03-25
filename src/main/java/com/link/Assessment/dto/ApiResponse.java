package com.link.Assessment.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ApiResponse<T> {
    private String message;
    private String code;
    private T data;
    private Map<String, Object> meta = new HashMap<>();

    public ApiResponse() {
    }

    public Map<String, Object> getMeta() {
        return meta;
    }

    public ApiResponse addMeta(String key, Object value){
        meta.put(key, value);
        return this;
    }
    public ApiResponse(String message, T data) {
        this.message = message;
        this.data = data;
    }
    public ApiResponse(String message,String code, T data) {
        this.message = message;
        this.data = data;
        this.code = code;
    }


}
