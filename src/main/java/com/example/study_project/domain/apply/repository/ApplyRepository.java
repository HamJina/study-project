package com.example.study_project.domain.apply.repository;

import com.example.study_project.domain.apply.entity.Apply;
import com.example.study_project.domain.enums.ApplyStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplyRepository extends JpaRepository<Apply, Long>, ApplyRepositoryCustom{
    Apply findByPostIdAndUserId(Long postId, Long userId);
    List<Apply> findByUserId(Long userId);
    List<Apply> findByPostIdAndStatus(Long postId, ApplyStatus status);
    List<Apply> findByUserIdAndStatus(Long userId, ApplyStatus status);

}
