package com.example.study_project.domain.keyword.controller;

import com.example.study_project.domain.keyword.dto.KeywordDTO;
import com.example.study_project.domain.keyword.service.KeywordService;
import com.example.study_project.domain.user.entity.User;
import com.example.study_project.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/keyword")
public class KeywordController {

    private final KeywordService keywordService;
    private final UserService userService;

    @GetMapping("/list")
    public ResponseEntity getLatestKeyword() {
        User currentUser = getCurrentUser();
        List<KeywordDTO> latestKeywords = keywordService.getLatestKeyword(currentUser);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("latestKeywords", latestKeywords);

        return ResponseEntity.ok().body(responseData);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.getUserByUserId(authentication.getName());
    }
}
