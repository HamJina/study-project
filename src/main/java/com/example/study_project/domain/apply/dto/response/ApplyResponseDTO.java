package com.example.study_project.domain.apply.dto.response;

import com.example.study_project.domain.apply.entity.Apply;
import com.example.study_project.domain.enums.ApplyStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplyResponseDTO {

    private Long id;
    private String applicant;
    private String phoneNumber;
    private String email;
    private String title;
    private String message;
    private ApplyStatus status;

    public static ApplyResponseDTO createToDTO(Apply apply) {
        ApplyResponseDTO applyResponseDTO = new ApplyResponseDTO();
        applyResponseDTO.setId(apply.getId());
        applyResponseDTO.setApplicant(apply.getUser().getName());
        applyResponseDTO.setPhoneNumber(apply.getUser().getPhoneNumber());
        applyResponseDTO.setEmail(apply.getUser().getEmail());
        applyResponseDTO.setTitle(apply.getPost().getTitle());
        applyResponseDTO.setMessage(apply.getMessage());
        applyResponseDTO.setStatus(apply.getStatus());

        return applyResponseDTO;
    }
}
