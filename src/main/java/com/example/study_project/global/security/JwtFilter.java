package com.example.study_project.global.security;


import com.example.study_project.domain.user.dto.CustomUserDetails;
import com.example.study_project.domain.user.entity.User;
import com.example.study_project.domain.user.repository.UserRepository;
import com.example.study_project.global.error.exception.CustomException;
import com.example.study_project.global.error.exception.ErrorCode;
import com.example.study_project.global.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

// jwt검증 필터
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // request에서 Authorization 헤더를 찾음
        String authorization = request.getHeader("Authorization");

        // Authorization 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Bearer 부분 제거 후 순수 토큰만 획득
        String token = authorization.split(" ")[1];

        // 토큰 소멸 시간 검증
        try {
            jwtUtil.isExpired(token); // 토큰 만료 확인
        } catch (ExpiredJwtException e) {
            // 토큰 만료시 응답 처리
            throw new CustomException(ErrorCode.TOKEN_IS_EXPIRED);
        }

        // 토큰에서 category가 accessToken인지 확인 (페이로드에 명시되어 있다고 가정)
        String category = jwtUtil.getCategory(token);
        if (!category.equals("accessToken")) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        // 토큰에서 username과 role을 획득
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        // DB에서 사용자 정보 조회 (username)
        User userEntity = userRepository.findByUsername(username);
        if (userEntity == null) {
            throw new CustomException(ErrorCode.INVALID_USER_OR_PASSWORD);
        }

        // ✅ UserDetails 생성 후 SecurityContext 업데이트
        CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);  // 🔥 새 토큰으로 인증 업데이트

        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }
}