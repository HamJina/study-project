package com.example.study_project.domain.apply.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class DenyMessage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deny_message_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apply_id")
    private Apply apply;

    private String message;
}
