package me.ihchoi.springdeveloper.dto;


import lombok.Getter;
import me.ihchoi.springdeveloper.dao.Article;

@Getter
public class ArticleResponse {
    // 전달용이기때문에 수정 X , final로 상수 선언 + @Getter 자동 생성
    private final String title;
    private final String content;


    // DTO => 대응되는 엔터티 객체가 있어야 함. => Article객체를 받아서 초기화하는 생성자
    public ArticleResponse(Article article) {
        this.title = article.getTitle();
        this.content = article.getContent();
    }
}
