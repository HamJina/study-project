package com.example.study_project.domain.posts.dto.response;

import com.example.study_project.domain.posts.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDTO {

    private Long id;
    private String writer;
    private String title;
    private String content;
    private String tags;
    private boolean recruited;
    private int hits;
    private String filed;
    private int recruitedPeopleNum;
    private int totalPeopleNum;
    private LocalDateTime createdDate;

    private boolean status;

    //entity -> DTO
    public static PostResponseDTO createToDTO(Post post) {
        PostResponseDTO postResponseDTO = new PostResponseDTO();
        postResponseDTO.setId(post.getId());
        postResponseDTO.setWriter(post.getWriter().getUsername());
        postResponseDTO.setTitle(post.getTitle());
        postResponseDTO.setContent(post.getContent());
        postResponseDTO.setTags(post.getTags());
        postResponseDTO.setRecruited(post.isRecruited());
        postResponseDTO.setHits(post.getHits());
        postResponseDTO.setFiled(post.getFiled());
        postResponseDTO.setRecruitedPeopleNum(post.getRecruitedPeopleNum());
        postResponseDTO.setTotalPeopleNum(post.getTotalPeopleNum());
        postResponseDTO.setCreatedDate(post.getCreatedDate());

        return postResponseDTO;
    }

}
