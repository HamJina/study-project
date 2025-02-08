package com.example.study_project.domain.apply.repository;

import com.example.study_project.domain.apply.entity.Apply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApplyRepository extends JpaRepository<Apply, Long> {
    public Apply findByPostIdAndUserId(Long postId, Long userId);
}
