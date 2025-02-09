package com.example.study_project.domain.apply.repository;

import com.example.study_project.domain.apply.entity.Apply;
import com.example.study_project.domain.apply.entity.QApply;
import com.example.study_project.domain.enums.ApplyStatus;
import com.example.study_project.domain.posts.entity.QPost;
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
        QPost post = QPost.post;

        return queryFactory
                .selectFrom(apply)
                .join(apply.user, user).fetchJoin()  // User fetch join
                .join(apply.post, post).fetchJoin()  // Post fetch join
                .where(
                        apply.post.id.eq(postId),
                        apply.status.in(ApplyStatus.WAITING, ApplyStatus.ACCEPTED) // 상태 필터 수정
                )
                .fetch();    }
}
