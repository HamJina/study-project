package com.example.study_project.domain.plan.service;

import com.example.study_project.domain.plan.dto.request.PlanRequestDTO;
import com.example.study_project.domain.plan.dto.response.PlanResponseDTO;
import com.example.study_project.domain.plan.entity.Plan;
import com.example.study_project.domain.plan.repository.PlanRepository;
import com.example.study_project.domain.study.entity.Study;
import com.example.study_project.domain.study.repository.StudyRepository;
import com.example.study_project.domain.user.entity.User;
import com.example.study_project.global.error.exception.CustomException;
import com.example.study_project.global.error.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.web.csrf.CsrfException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Cacheable(cacheNames = "getPlanList", key = "'planList:studyId' + #studyId", cacheManager = "cacheManager")
    public List<PlanResponseDTO> getPlanList(Long studyId) {
        Optional<Study> findStudy = studyRepository.findById(studyId);
        if(findStudy == null) {
            throw new CustomException(ErrorCode.NOT_EXIST_STUDY);
        }

        List<Plan> findPlanList = planRepository.findByStudyIdOrderBySequenceAsc(studyId);

        if(findPlanList.isEmpty()) {
            //빈 배열이면 예외 발생
            throw new CustomException(ErrorCode.NOT_EXIST_PLANS);
        }

        //배열이 존재하면 entity -> dto로 변환 후 반환
        return findPlanList.stream().map(PlanResponseDTO::createToDTO).collect(Collectors.toList());
    }

    public PlanResponseDTO updatePlan(Long planId, PlanRequestDTO planRequestDTO) {
        Plan findPlan = planRepository.findById(planId).orElseThrow(() -> {
            throw new CustomException(ErrorCode.NOT_EXIST_PLANS);
        });

        findPlan.setSequence(planRequestDTO.getSequence());
        findPlan.setDate(planRequestDTO.getDate());
        findPlan.setTime(planRequestDTO.getTime());
        findPlan.setPlace(planRequestDTO.getPlace());
        findPlan.setContent(planRequestDTO.getContent());
        findPlan.setAssignment(planRequestDTO.getAssignment());

        return PlanResponseDTO.createToDTO(findPlan);
    }
}
