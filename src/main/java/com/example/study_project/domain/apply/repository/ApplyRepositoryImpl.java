package com.example.study_project.domain.apply.repository;

import com.example.study_project.domain.apply.entity.Apply;
import com.example.study_project.domain.apply.entity.QApply;
import com.example.study_project.domain.enums.ApplyStatus;
import com.example.study_project.domain.user.entity.QUser;
import com.example.study_project.domain.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.example.study_project.domain.posts.entity.QPost.post;

public class ApplyRepositoryImpl implements ApplyRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public ApplyRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Apply> findByPostIdAndWaiting(Long postId) {
        QApply apply = QApply.apply;
        QUser user = QUser.user;

        return queryFactory
                .selectFrom(apply)
                .join(apply.user, user).fetchJoin()  // User를 fetch join
                .join(apply.post, post).fetchJoin()
                .where(apply.post.id.eq(postId), apply.status.eq(ApplyStatus.WAITING))
                .fetch();
    }
}
