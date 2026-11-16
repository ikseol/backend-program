package me.ihchoi.springdeveloper.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ArticleUpdateRequest {
    private String title;
    private String content;

    // 리퀘스트 바디에서 꺼낸 이 객체를 만들 때 내부적으로 new ArticleUpdateRequest( 꺼낸 내용 )으로 만듦.
    // 하지만 안 만들어짐 -> 이유: 생성자가 없음.  => 생성자 만들어야 함.

//    public ArticleUpdateRequest(String title, String content) {
//        this.title = title;
//        this.content = content;
//    }
//    // 이걸 안 하기 위해서 lombok 라이브러리를 이용해서 자동 생성 가능. -> @AllArgsConstructor 로 자동으로 기본 생성자 생성


}
