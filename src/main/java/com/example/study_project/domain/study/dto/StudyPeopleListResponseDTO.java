package com.example.study_project.domain.study.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudyPeopleListResponseDTO {
    private String name;
    private String phoneNumber;
    private String email;
}
