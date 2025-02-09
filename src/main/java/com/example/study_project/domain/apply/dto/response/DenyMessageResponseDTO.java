package com.example.study_project.domain.apply.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DenyMessageResponseDTO {
    private Long id;
    private String message;
}
