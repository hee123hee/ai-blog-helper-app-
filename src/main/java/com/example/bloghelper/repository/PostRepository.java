package com.example.bloghelper.repository;

import com.example.bloghelper.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * PostRepository 인터페이스는 데이터베이스에서 Post 엔티티를 조회, 생성, 수정, 삭제하는 방법을 정의합니다.
 * Spring Data JPA의 JpaRepository를 상속받아 기본적인 CRUD 메서드를 바로 사용할 수 있습니다.
 * 추가적으로 커스텀한 쿼리 메서드나 쿼리 어노테이션을 사용한 메서드를 정의할 수 있습니다.
 */
public interface PostRepository extends JpaRepository<Post, Long> {

    /**
     * 생성된 시간을 기준으로 내림차순 정렬하여 모든 Post 엔티티를 조회하는 메서드입니다.
     * 메서드 이름을 기반으로 JPA가 자동으로 쿼리를 생성합니다.
     *
     * @return 생성일(createdAt) 기준으로 최신 글부터 조회된 Post 리스트
     */
    List<Post> findAllByOrderByCreatedAtDesc();

}
