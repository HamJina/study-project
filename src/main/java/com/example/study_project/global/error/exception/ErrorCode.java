package com.example.study_project.global.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    ALREADY_EXIST_ID(HttpStatus.BAD_REQUEST, "해당 ID는 이미 사용 중입니다. 다른 ID를 입력해주세요."),
    INVALID_USER_OR_PASSWORD(HttpStatus.NOT_FOUND,"존재하지 않는 회원정보입니다."),
    FAILURE_LOGIN(HttpStatus.BAD_REQUEST, "로그인에 실패하였습니다."),

    TOKEN_IS_NOT_EXIST(HttpStatus.UNAUTHORIZED, "토큰 정보가 존재하지 않습니다."),
    TOKEN_IS_EXPIRED(HttpStatus.FORBIDDEN, "만료된 토큰입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "타당하지 않은 토큰입니다.")
    ;

    private final HttpStatus status;
    private final String message;
}
