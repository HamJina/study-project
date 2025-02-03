package com.example.study_project.domain.user.controller;

import com.example.study_project.domain.user.dto.JoinDTO;
import com.example.study_project.domain.user.service.UserService;
import com.example.study_project.global.common.response.GlobalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //회원가입
    @PostMapping("/join")
    public ResponseEntity<GlobalResponse<Map<String, String>>> joinUser(@RequestBody JoinDTO joinDTO) {
        userService.joinUser(joinDTO);
        Map<String, String> data = new HashMap<>();
        data.put("message", "회원가입에 성공하였습니다.");

        return ResponseEntity.ok(GlobalResponse.success(200, data));
    }

    //로그인

    //로그아웃

    //아이디 중복확인



}
