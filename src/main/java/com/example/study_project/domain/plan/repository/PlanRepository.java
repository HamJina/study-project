package com.example.study_project.domain.plan.repository;


import com.example.study_project.domain.plan.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanRepository extends JpaRepository<Plan, Long> {
    List<Plan> findByStudyIdOrderBySequenceAsc(Long studyId);

}
