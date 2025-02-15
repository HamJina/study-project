package com.example.study_project.domain.study.entity;

import com.example.study_project.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter @Setter
public class StudyPeople {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_people_id")
    private Long id;

    @JoinColumn(name = "study_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Study study;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public StudyPeople(Study study, User user) {
        this.study = study;
        this.user = user;
    }
}
