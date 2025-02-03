package com.example.study_project.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//회원가입시 필요한 DTO
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinDTO {

    private String name;
    private String username;
    private String password;
    private String phoneNumber;
    private String email;
}
