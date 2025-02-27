package com.example.study_project.domain.plan.dto.response;

import com.example.study_project.domain.plan.entity.Plan;
import com.example.study_project.domain.posts.dto.response.PostResponseDTO;
import com.example.study_project.domain.posts.entity.Post;
import lombok.Data;

@Data
public class PlanResponseDTO {
    private Long id;
    private int sequence;
    private String date;
    private String time;
    private String place;
    private String content;
    private String assignment;
    private String createBy;

    public static PlanResponseDTO createToDTO(Plan plan) {
        PlanResponseDTO planResponseDTO = new PlanResponseDTO();
        planResponseDTO.setId(plan.getId());
        planResponseDTO.setSequence(plan.getSequence());
        planResponseDTO.setDate(plan.getDate());
        planResponseDTO.setTime(plan.getTime());
        planResponseDTO.setPlace(plan.getPlace());
        planResponseDTO.setContent(plan.getContent());
        planResponseDTO.setAssignment(plan.getAssignment());
        planResponseDTO.setCreateBy(plan.getCreateBy().getName());
        return planResponseDTO;
    }
}
