package com.example.study_project.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class IdCheckResponse {
    private String message;
    private boolean isPossible;
}

