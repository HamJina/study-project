package com.example.study_project.domain.study.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudyListResponseDTO {
    private String title;
    private int recruitedPeopleNum;
    private int totalPeopleNum;
}
