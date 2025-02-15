package com.example.study_project.domain.study.repository;

import com.example.study_project.domain.study.dto.StudyListResponseDTO;

import java.util.List;

public interface StudyPeopleRepositoryCustom {
    List<StudyListResponseDTO> findMyStudyList(Long userId);
}
