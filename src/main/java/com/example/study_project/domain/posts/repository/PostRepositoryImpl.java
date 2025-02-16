package com.example.study_project.domain.posts.repository;

import com.example.study_project.domain.posts.entity.Post;
import com.example.study_project.domain.posts.entity.QPost;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

import static com.example.study_project.domain.posts.entity.QPost.post;
import static io.micrometer.common.util.StringUtils.isEmpty;


public class PostRepositoryImpl implements PostRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public PostRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public Slice<Post> findPostsByPage(Long lastPostId, Pageable pageable) {
        QPost post = QPost.post;

        List<Post> posts = queryFactory
                .selectFrom(post)
                .where(lastPostId == null ? null : post.id.lt(lastPostId)) // lastPostId보다 작은 데이터 가져오기
                .orderBy(post.id.desc()) // 최신순 정렬
                .limit(pageable.getPageSize() + 1) // 다음 페이지 여부 확인을 위해 pageSize + 1개 조회
                .fetch();

        boolean hasNext = posts.size() > pageable.getPageSize();

        return new SliceImpl<>(hasNext ? posts.subList(0, pageable.getPageSize()) : posts, pageable, hasNext);
    }

    @Override
    public List<Post> findLatestRecruitmentPosts(int size) {
        QPost post = QPost.post;

        return queryFactory
                .selectFrom(post)
                .where(post.recruited.isTrue()) // 모집중인 글만 필터링
                .orderBy(post.createdDate.desc()) // 최신순 정렬
                .limit(size) // 요청한 개수만큼 제한
                .fetch();
    }

    @Override
    public List<Post> findHotRecruitmentPosts(int size) {
        QPost post = QPost.post;
        return queryFactory
                .selectFrom(post)
                .where(post.recruited.isTrue())
                .orderBy(post.hits.desc(), post.createdDate.desc())
                .limit(size)
                .fetch();
    }

    @Override
    public Slice<Post> findKeywordPostsByPage(Long lastPostId, Pageable pageable, String keyword) {
        QPost post = QPost.post;

        List<Post> posts = queryFactory
                .selectFrom(post)
                .where(lastPostId == null ? null : post.id.lt(lastPostId),keywordEq(keyword))// lastPostId보다 작은 데이터 가져오기
                .orderBy(post.id.desc()) // 최신순 정렬
                .limit(pageable.getPageSize() + 1) // 다음 페이지 여부 확인을 위해 pageSize + 1개 조회
                .fetch();

        boolean hasNext = posts.size() > pageable.getPageSize();

        return new SliceImpl<>(hasNext ? posts.subList(0, pageable.getPageSize()) : posts, pageable, hasNext);
    }

    @Override
    public Slice<Post> findKeywordFilterPostsAndRecruiting(Long lastPostId, Pageable pageable, String keyword) {
        QPost post = QPost.post;

        List<Post> posts = queryFactory
                .selectFrom(post)
                .where(lastPostId == null ? null : post.id.lt(lastPostId),keywordEq(keyword), post.recruited.eq(true))// lastPostId보다 작은 데이터 가져오기
                .orderBy(post.id.desc()) // 최신순 정렬
                .limit(pageable.getPageSize() + 1) // 다음 페이지 여부 확인을 위해 pageSize + 1개 조회
                .fetch();

        boolean hasNext = posts.size() > pageable.getPageSize();

        return new SliceImpl<>(hasNext ? posts.subList(0, pageable.getPageSize()) : posts, pageable, hasNext);
    }

    private BooleanExpression keywordEq(String keyword) {
        return isEmpty(keyword) ? null : post.title.contains(keyword);
    }
}
