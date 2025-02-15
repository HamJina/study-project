package com.example.study_project.domain.study.repository;

import com.example.study_project.domain.study.entity.StudyPeople;
import com.example.study_project.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyPeopleRepository extends JpaRepository<StudyPeople, Long> , StudyPeopleRepositoryCustom{
    List<User> findByUserId(Long userId);
}
