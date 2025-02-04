package com.example.study_project.domain.auth.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username; // 사용자 아이디

    @Column(nullable = false)
    private String refreshToken; // 리프레시 토큰

    // 토큰 업데이트 메서드
    public void updateToken(String newRefreshToken) {
        this.refreshToken = newRefreshToken;
    }
}

