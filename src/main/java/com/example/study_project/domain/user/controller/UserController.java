package com.example.study_project.domain.user.controller;

import com.example.study_project.domain.user.dto.request.JoinDTO;
import com.example.study_project.domain.user.dto.response.IdCheckResponse;
import com.example.study_project.domain.user.dto.response.UserResponseDTO;
import com.example.study_project.domain.user.entity.User;
import com.example.study_project.domain.user.service.UserService;
import com.example.study_project.global.common.response.GlobalResponse;
import com.example.study_project.global.error.exception.CustomException;
import com.example.study_project.global.error.exception.ErrorCode;
import com.example.study_project.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    //회원가입
    @PostMapping("/join")
    public ResponseEntity joinUser(@RequestBody JoinDTO joinDTO) {
        userService.joinUser(joinDTO);
        Map<String, String> data = new HashMap<>();
        data.put("message", "회원가입에 성공하였습니다.");

        return ResponseEntity.ok().body(data);
    }

    //회원정보조회
    @GetMapping()
    public ResponseEntity getUserInfo() {
        User currentUser = getCurrentUser();
        UserResponseDTO userResponse = new UserResponseDTO(currentUser.getName(), currentUser.getUsername(), currentUser.getPhoneNumber(), currentUser.getEmail());
        return ResponseEntity.ok().body(userResponse);
    }

    //아이디 중복확인
    @GetMapping("/idcheck")
    public ResponseEntity<IdCheckResponse> ExistIDCheck(@RequestParam("username") String username) {
        boolean isExist = userService.ExistIDCheck(username);

        if (isExist) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_ID);
        }

        return ResponseEntity.ok(new IdCheckResponse("사용 가능한 ID입니다.", true));
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.getUserByUserId(authentication.getName());
    }
}
