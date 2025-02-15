package com.example.study_project.domain.study.repository;

import com.example.study_project.domain.study.entity.StudyPeople;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyPeopleRepository extends JpaRepository<StudyPeople, Long> {
}
