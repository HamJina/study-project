package com.example.study_project.domain.plan.service;

import com.example.study_project.domain.plan.dto.PlanRequestDTO;
import com.example.study_project.domain.plan.entity.Plan;
import com.example.study_project.domain.plan.repository.PlanRepository;
import com.example.study_project.domain.study.entity.Study;
import com.example.study_project.domain.study.repository.StudyRepository;
import com.example.study_project.domain.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class PlanService {
    private final PlanRepository planRepository;
    private final StudyRepository studyRepository;

    public void createPlan(Long studyId, PlanRequestDTO planRequestDTO, User currentUser) {
        Study findStudy = studyRepository.findById(studyId).orElseThrow(() -> {
            throw new RuntimeException("존재하지 않는 스터디입니다.");
        });

        //해당 스터디에 대해 진행표 작성
        Plan plan = new Plan();
        plan.setStudy(findStudy);
        plan.setSequence(planRequestDTO.getSequence());
        plan.setDate(planRequestDTO.getDate());
        plan.setTime(planRequestDTO.getTime());
        plan.setPlace(planRequestDTO.getPlace());
        plan.setContent(planRequestDTO.getContent());
        plan.setAssignment(planRequestDTO.getAssignment());
        plan.setCreateBy(currentUser);

        planRepository.save(plan);

        //진행표를 작성하면 점수 10점 얻음
        findStudy.increaseScore(10);
    }
}
