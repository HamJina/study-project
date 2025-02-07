package com.example.study_project.domain.keyword.dto;

import com.example.study_project.domain.user.entity.User;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class KeywordDTO {
    private Long id;
    private String content;
    private LocalDateTime searchedDate;
}
