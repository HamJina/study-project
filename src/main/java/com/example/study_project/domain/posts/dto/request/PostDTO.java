package com.example.study_project.domain.posts.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostDTO {

    private String title;
    private String content;
    private String tags;
    private String filed;
    //총 인원
    private int totalPeopleNum;

}
