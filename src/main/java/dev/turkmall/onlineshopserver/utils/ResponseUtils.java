package dev.turkmall.onlineshopserver.utils;

import dev.turkmall.onlineshopserver.payload.ApiResponse;

public class ResponseUtils {

    public static ApiResponse success(String message, Object object) {
        return new ApiResponse(message, true, object);
    }

    public static ApiResponse success(String message) {
        return new ApiResponse(message, true);
    }

    public static ApiResponse success(Object object) {
        return new ApiResponse(true, object);
    }

    public static ApiResponse error(String message) {
        return new ApiResponse(message, false);
    }

    public static ApiResponse error() {
        return new ApiResponse(false);
    }

    public static ApiResponse errorPageable() {
        int[] a = {};
        return new ApiResponse(
                a,
                0,
                0,
                0,
                0L
        );
    }
}
