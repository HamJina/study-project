package com.example.study_project.domain.study.repository;

import com.example.study_project.domain.study.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyRepository extends JpaRepository<Study, Long> {
    Study findByPostId(Long postId);
}
