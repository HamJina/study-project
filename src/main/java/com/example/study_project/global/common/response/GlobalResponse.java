package com.example.study_project.global.common.response;

import com.example.study_project.global.error.ErrorResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class GlobalResponse<T> {
    private int status;
    private LocalDateTime timestamp;
    private boolean success;
    private Object data;

    public static GlobalResponse success(int status, Object data) {
        return GlobalResponse.builder()
                .success(true)
                .status(status)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static GlobalResponse failure(int status, ErrorResponse errorResponse) {
        return GlobalResponse.builder()
                .success(false)
                .status(status)
                .data(errorResponse)
                .timestamp(LocalDateTime.now())
                .build();
    }
}

