package com.example.study_project.domain.study.repository;

import com.example.study_project.domain.study.dto.StudyListResponseDTO;
import com.example.study_project.domain.study.dto.StudyPeopleListResponseDTO;

import java.util.List;

public interface StudyPeopleRepositoryCustom {
    List<StudyListResponseDTO> findMyStudyList(Long userId);
    List<StudyPeopleListResponseDTO> findStudyPeopleList(Long studyId);
}
