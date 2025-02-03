package com.example.study_project.global.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class GlobalResponse<T> {
    private int status;
    private LocalDateTime timestamp;
    private boolean success;
    private T data;

    public static <T> GlobalResponse<T> success(int status, T data) {
        return new GlobalResponse<>(status, LocalDateTime.now(), true, data);
    }

    public static <T> GlobalResponse<T> failure(int status, T data) {
        return new GlobalResponse<>(status, LocalDateTime.now(), false, data);
    }
}

