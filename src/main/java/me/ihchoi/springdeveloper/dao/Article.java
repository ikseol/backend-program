package me.ihchoi.springdeveloper.dao;

import jakarta.persistence.*;
import lombok.*;
import me.ihchoi.springdeveloper.dto.ArticleUpdateRequest;
import org.antlr.v4.runtime.misc.NotNull;

@Entity // 엔티티 어노테이션
@Getter// private인 필드에 접근하기 위한 getter 메소드 자동 생성 lombok이 지원하는 기능
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// 기본 생성자 자동 생성, 같은 패키지 내의 클래스에서만 생성하게 지정 = access = AccessLevel.PROTECTED

//@Table(name = "MyArticle") 이런식으로 하면 테이블 이름 임의 지정 가능

// 정보 은닉 -> 특별한 이유가 없으면 필드를 private로 지정. 객체 지향의 원칙 추상화 상속 정보은닉 캡슐화.
public class Article{
    @Id // 해당 필드가 PK임을 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 자동 생성 + 증가 처리
    @Column(updatable = false) // 업데이트 방지
    private int id; // PK

    @Column(nullable = false)
    private String title; // Not Null

    @Column(nullable = false)
    private String content; // Not Null

    @Builder
    public Article(String title, String content){
        this.title = title;
        this.content = content;
    }
    // 기존 : new Article(”A”,”B”)로 객체 생성 했는데
    // 빌더 : Article.builder().title(”A”).content(”B”) 이런 빌더 패턴을 가독성이 좋아서 현업에서 많이 적용.
    // → 빌더 어노테이션 사용하면 자동으로 생성 가능. 두 방법 모두 사용 가능

    // 11-12 crud의 u를 구현하기 위해 Update메소드를 추가.
    public void update(String title, String content){
        this.title = title;
        this.content = content;
    }
}

