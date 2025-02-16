package com.example.study_project.domain.plan.controller;

import com.example.study_project.domain.plan.dto.PlanRequestDTO;
import com.example.study_project.domain.plan.service.PlanService;
import com.example.study_project.domain.user.entity.User;
import com.example.study_project.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/plan")
@RequiredArgsConstructor
public class PlanController {
    private final PlanService planService;
    private final UserService userService;

    @PostMapping("/create/{studyId}")
    public ResponseEntity createPlan(@PathVariable Long studyId, @RequestBody PlanRequestDTO planRequestDTO){
        User currentUser = getCurrentUser();
        planService.createPlan(studyId, planRequestDTO, currentUser);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("message", "진행표가 작성되었습니다.");

        return ResponseEntity.ok().body(responseData);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.getUserByUserId(authentication.getName());
    }
}
