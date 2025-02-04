package com.example.study_project.domain.user.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponseDTO {

    private String name;
    private String username;
    private String phoneNumber;
    private String email;
}
