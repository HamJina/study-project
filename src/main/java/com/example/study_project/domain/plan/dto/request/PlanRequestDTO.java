package com.example.study_project.domain.plan.dto.request;

import lombok.Data;

@Data
public class PlanRequestDTO {
    private int sequence;
    //스터디 날짜
    private String date;
    //스터디 시간
    private String time;
    //스터디 장소
    private String place;
    //스터디 진행 내용
    private String content;
    //다음 과제
    private String assignment;
}
