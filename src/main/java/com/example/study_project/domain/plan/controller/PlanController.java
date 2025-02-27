package com.example.study_project.domain.plan.controller;

import com.example.study_project.domain.plan.dto.request.PlanRequestDTO;
import com.example.study_project.domain.plan.dto.response.PlanResponseDTO;
import com.example.study_project.domain.plan.service.PlanService;
import com.example.study_project.domain.user.entity.User;
import com.example.study_project.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/plan")
@RequiredArgsConstructor
public class PlanController {
    private final PlanService planService;
    private final UserService userService;

    //계획표 작성하기
    @PostMapping("/create/{studyId}")
    public ResponseEntity createPlan(@PathVariable Long studyId, @RequestBody PlanRequestDTO planRequestDTO){
        User currentUser = getCurrentUser();
        planService.createPlan(studyId, planRequestDTO, currentUser);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("message", "진행표가 작성되었습니다.");

        return ResponseEntity.ok().body(responseData);
    }

    //현재 스터디의 계획표 조회하기 studyId로 list조회하기
    @GetMapping("/list/{studyId}")
    public ResponseEntity getPlanList(@PathVariable Long studyId) {
        List<PlanResponseDTO> planList = planService.getPlanList(studyId);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("planList", planList);

        return ResponseEntity.ok().body(responseData);
    }

    //계획표 수정하기
    @PutMapping("/update/{planId}")
    public ResponseEntity updatePlan(@PathVariable Long planId, @RequestBody PlanRequestDTO planRequestDTO) {
        PlanResponseDTO updatedPlan = planService.updatePlan(planId, planRequestDTO);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("updatedPlan", updatedPlan);

        return ResponseEntity.ok().body(responseData);
    }

    //계획표 삭제가ㅣ
    @DeleteMapping("/delete/{planId}")
    public ResponseEntity deletePlan(@PathVariable Long planId) {
        planService.deletePlan(planId)
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.getUserByUserId(authentication.getName());
    }

}
