package com.example.study_project.domain.apply.controller;

import com.example.study_project.domain.apply.dto.request.ApplyDTO;
import com.example.study_project.domain.apply.dto.response.ApplyResponseDTO;
import com.example.study_project.domain.apply.service.ApplyService;
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
@RequestMapping("/apply")
@RequiredArgsConstructor
public class ApplyController {

    private final ApplyService applyService;
    private final UserService userService;

    //지원메시지 작성
    @PostMapping("/message/{postId}")
    public ResponseEntity applyPost(@PathVariable Long postId, @RequestBody ApplyDTO applyDTO) {
        User currentUser = getCurrentUser();
        ApplyResponseDTO applyResponse = applyService.applyPost(applyDTO, currentUser, postId);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("applyResponse", applyResponse);
        responseData.put("message", "지원이 완료되었습니다.");

        return ResponseEntity.ok().body(responseData);
    }

    //나의 지원 현황 목록 조회
    @GetMapping("/list")
    public ResponseEntity myApplyList() {
        User currentUser = getCurrentUser();

        List<ApplyResponseDTO> applyList = applyService.myApplyList(currentUser);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("applyList", applyList);

        return ResponseEntity.ok().body(responseData);
    }

    /*//수락 대기중인 스터디원 조회
    @GetMapping("/waiting/{postId}")
    public ResponseEntity waitingPeopleList(@PathVariable Long postId) {
        User currentUser = getCurrentUser();

    }*/

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.getUserByUserId(authentication.getName());
    }
}
