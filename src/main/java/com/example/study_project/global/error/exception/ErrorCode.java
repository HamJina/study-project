package com.example.study_project.global.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    ALREADY_EXIST_ID(HttpStatus.BAD_REQUEST, "해당 ID는 이미 사용 중입니다. 다른 ID를 입력해주세요."),
    INVALID_USER_OR_PASSWORD(HttpStatus.NOT_FOUND,"존재하지 않는 회원정보입니다. 다시 입력하세요"),
    FAILURE_LOGIN(HttpStatus.BAD_REQUEST, "로그인에 실패하였습니다.")
    ;

    private final HttpStatus status;
    private final String message;
}
