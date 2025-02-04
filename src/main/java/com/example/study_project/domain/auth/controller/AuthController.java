package com.example.study_project.domain.auth.controller;

import com.example.study_project.domain.auth.dto.ReissueDTO;
import com.example.study_project.domain.user.dto.request.LoginDTO;
import com.example.study_project.domain.user.service.UserService;
import com.example.study_project.global.common.response.GlobalResponse;
import com.example.study_project.global.error.exception.CustomException;
import com.example.study_project.global.error.exception.ErrorCode;
import com.example.study_project.global.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController{
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    //test
    @GetMapping("/test")
    public String test() {
        return "test";
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity loginUser(@RequestBody LoginDTO loginDTO) {
        try {
            // 1️⃣ 사용자 인증 시도 (loginDTO에서 username과 password를 추출해서 인증객체 생성하기), 해당 인증객체 authenticationManager에세 전달
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())
            );

            // 2️⃣ 인증 성공하면 JWT 생성
            String accessToken = jwtUtil.createJwt("accessToken",authentication.getName(), 60*60*10L); //10시간 유효
            String refreshToken = jwtUtil.createJwt("refreshToken",authentication.getName(), 60 * 60 * 24 * 7L); // 7일 유효

            // 3️⃣ 응답 데이터 구성
            Map<String, String> responseData = new HashMap<>();
            responseData.put("accessToken", accessToken);
            responseData.put("refreshToken", refreshToken);

            return ResponseEntity.ok().body(responseData);
        } catch (AuthenticationException e) {
            // 4️⃣ 실패 응답 데이터 구성
            throw new CustomException(ErrorCode.FAILURE_LOGIN);
        }
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@RequestBody ReissueDTO reissueDTO) {

        // get refresh token from request body
        String refresh = reissueDTO.getRefreshToken();

        if (refresh == null) {
            // refresh token이 없으면
            throw new CustomException(ErrorCode.TOKEN_IS_NOT_EXIST);
        }

        // expired check
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            // refresh token 만료 시
            throw new CustomException(ErrorCode.TOKEN_IS_EXPIRED);
        }

        // refresh 토큰이 refresh인지 확인 (발급 시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);
        if (!category.equals("refreshToken")) {
            // refresh token이 아닌 경우 400 Bad Request 반환
            throw new CustomException(ErrorCode.TOKEN_IS_NOT_EXIST);
        }

        // refresh 토큰에서 username과 role을 추출하여 새로운 access token과 refresh token 생성
        String username = jwtUtil.getUsername(refresh);

        // 새로운 JWT(access token)과 refresh token 생성
        String newAccess = jwtUtil.createJwt("accessToken", username, 60*60*10L);  // accessToken 재발급
        String newRefresh = jwtUtil.createJwt("refreshToken", username, 60 * 60 * 24 * 7L);  // refreshToken 재발급

        // 응답 데이터 구성
        Map<String, String> responseData = new HashMap<>();
        responseData.put("accessToken", newAccess);
        responseData.put("refreshToken", newRefresh);
        responseData.put("message", "새로운 토큰이 발급되었습니다.");

        // 응답 반환
        return ResponseEntity.ok().body(responseData);
    }


}


