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
    @GetMapping("/idcheck")
    public ResponseEntity<GlobalResponse<Map<String, String>>> ExistIDCheck(@RequestParam("username") String username) {
        boolean isExist = userService.ExistIDCheck(username);

        if(isExist) {
            //이미 존재하는 ID
            Map<String, String> data = new HashMap<>();
            data.put("message", "해당 ID는 이미 사용 중입니다. 다른 ID를 입력해주세요.");
            data.put("isPossible", "false");

            return ResponseEntity.ok(GlobalResponse.failure(200, data));
        } else {
            //사용 가능한 ID
            Map<String, String> data = new HashMap<>();
            data.put("message", "사용 가능한 ID입니다.");
            data.put("isPossible", "true");

            return ResponseEntity.ok(GlobalResponse.success(200, data));
        }
    }


}
