package com.example.study_project.domain.posts.entity;

import com.example.study_project.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Table(name = "posts")
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User writer;

    private String title;
    private String content;

    private String tags;

    private boolean recruited;
    private int hits;

    private String filed;

    //현재 모집된 인원
    private int recruitedPeopleNum;

    //총 인원
    private int totalPeopleNum;

    private LocalDateTime createdDate = LocalDateTime.now();

    //조회수 증가 메소드
    public void increaseHits() {
        this.hits += 1;
    }

}
