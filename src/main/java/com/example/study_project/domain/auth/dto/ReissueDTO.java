package com.example.study_project.domain.auth.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReissueDTO {
    private String refreshToken;
}
