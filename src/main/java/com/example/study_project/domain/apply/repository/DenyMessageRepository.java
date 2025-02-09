package com.example.study_project.domain.apply.repository;

import com.example.study_project.domain.apply.entity.DenyMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DenyMessageRepository extends JpaRepository<DenyMessage, Long> {
}
