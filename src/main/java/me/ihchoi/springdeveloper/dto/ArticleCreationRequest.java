package me.ihchoi.springdeveloper.dto;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ihchoi.springdeveloper.dao.Article;
// DTO는 어노테이션이 필요하지 않음. 엔터티의 무슨 정보를 담을 지만 적으면 됨.

@Getter
//@AllArgsConstructor(access = AccessLevel.PROTECTED) // 생성자 .. 11-02 test코드 작성 시 버그 발생해서 수정
@AllArgsConstructor
public class ArticleCreationRequest {
    private String title;
    private String content;

    // dto를 엔터티로 바꾸는걸 누가 구현?? → 객체지향의 캡슐화 원칙에 따라서 dto가 가지고 있어야 함.
    // 예시) BlogService안에 DTO -> Entity로 바꾸는 코드가 있으면 안됨. 이러면 캡슐화가 아님

    public Article toEntity() {
        // return new Article(this.title, this.content);
        return Article.builder().title(this.title).content(this.content).build();

        // 컨트롤러 객체에 매게변수로 적어놓기만 하면 스프링이 자동으로 dto를 만들어줌. -> 생성자를 @AllArgsConstructor로 생성
    }
}
