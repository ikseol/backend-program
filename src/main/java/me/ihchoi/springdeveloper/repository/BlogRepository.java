package me.ihchoi.springdeveloper.repository;

import me.ihchoi.springdeveloper.dao.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// 어노테이션이 없어도 Jpa리포지토리를 상속받으면 스프링이 자동으로 처리.
@Repository              // JpaRepository 상속 + 연결될 엔티티와 그 엔티티의 ID를 <> 안에 적어줘야 함
public interface BlogRepository extends JpaRepository<Article, Long> {
    // findById(), findAll(), save(), saveAll(), deleteById(), deleteAll() 기본 사용 가능
    // 추가 필요한 메서드는 직접 선언 해야함.

}

