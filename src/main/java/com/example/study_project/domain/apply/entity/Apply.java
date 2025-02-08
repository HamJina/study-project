package com.example.study_project.domain.apply.entity;

import com.example.study_project.domain.enums.ApplyStatus;
import com.example.study_project.domain.posts.entity.Post;
import com.example.study_project.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Apply {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String message;

    @Enumerated(value = EnumType.STRING)
    private ApplyStatus status;
}
