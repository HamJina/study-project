package com.example.study_project.domain.auth.service;


import com.example.study_project.domain.auth.RefreshTokenRepository;
import com.example.study_project.domain.auth.entity.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    // 리프레시 토큰 저장 (기존 토큰이 있으면 업데이트)
    @Transactional
    public void updateRefreshToken(String username, String newRefreshToken) {
        refreshTokenRepository.findByUsername(username)
                .ifPresentOrElse(
                        token -> token.updateToken(newRefreshToken), // 기존 토큰 업데이트
                        () -> refreshTokenRepository.save( // 새 토큰 저장
                                RefreshToken.builder()
                                        .username(username)
                                        .refreshToken(newRefreshToken)
                                        .build()
                        )
                );
    }

    // 리프레시 토큰 가져오기
    @Transactional(readOnly = true)
    public String getRefreshToken(String username) {
        return refreshTokenRepository.findByUsername(username)
                .map(RefreshToken::getRefreshToken)
                .orElse(null);
    }

    // 리프레시 토큰 삭제 (로그아웃 시)
    @Transactional
    public void deleteRefreshToken(String username) {
        refreshTokenRepository.deleteByUsername(username);
    }
}

