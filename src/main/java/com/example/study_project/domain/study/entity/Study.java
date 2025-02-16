package com.example.study_project.domain.study.entity;

import com.example.study_project.domain.posts.entity.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Study {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_id")
    private Long id;

    @JoinColumn(name = "post_id")
    @OneToOne(fetch = FetchType.LAZY)
    private Post post;

    private int score;

    private int totalPeopleNum;

    public void increaseScore(int score) {
        this.score += score;
    }
}
