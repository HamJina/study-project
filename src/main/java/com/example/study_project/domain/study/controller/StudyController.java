package com.example.study_project.domain.study.controller;

import com.example.study_project.domain.study.dto.StudyListResponseDTO;
import com.example.study_project.domain.study.service.StudyService;
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
@RequestMapping("/study")
@RequiredArgsConstructor
public class StudyController {

    private final StudyService studyService;
    private final UserService userService;

    @PostMapping("/create/{postId}")
    public ResponseEntity createStudy(@PathVariable Long postId) {
        User currentUser = getCurrentUser();
        studyService.createStudy(postId, currentUser);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("message", "모집글이 마감되어 스터디로 전환됩니다.");

        return ResponseEntity.ok().body(responseData);
    }

    //내가 진행하고 있는 스터디 리스트 반환하기
    @GetMapping("/mylist")
    public ResponseEntity studyMyList() {
        User currentUser = getCurrentUser();
        List<StudyListResponseDTO> myStudyList = studyService.studyMyList(currentUser);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("myStudyList", myStudyList);

        return ResponseEntity.ok().body(responseData);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.getUserByUserId(authentication.getName());
    }
}
