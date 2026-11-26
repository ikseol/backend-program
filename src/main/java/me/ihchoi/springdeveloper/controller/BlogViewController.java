package me.ihchoi.springdeveloper.controller;


import lombok.RequiredArgsConstructor;
import me.ihchoi.springdeveloper.dao.Article;
import me.ihchoi.springdeveloper.service.BlogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller  // 이 클래스가 컨트롤러라고 스프링부트에 명시, REST컨트롤러가 아닌 HTML을 응답으로 주는 일반 controller
@RequiredArgsConstructor // 필요한 인자를 가진 생성자 만드는 어노테이션 ( private final 이 붙은 맴버변수를 초기화 하기 위해 사용)
// AllArgs => 모든 맴버변수 초기화  noArgs = 기본 생성자 제작
public class BlogViewController {
    private final BlogService blogService; // 블로그 서비스 DI 받아야 함. 보통 @AutoWired로 받았음. // DI 다른 방법으로 받는법 : Final로 상수 선언 하고, AllArgsConstructor

    // api/articles 는 데이터    /articles 는 HTML
    @GetMapping("/articles")
    public String getArticles(Model model) {
        // 모델에 넣어줘야 할 정보는? -> 서비스에서 가져온 article 테이블에 있는 모든 레코드의 리스트

        // 모델에 넣어줄 데이터를 갖고 옴 (blogService 이용)
        List<Article> articles = blogService.findAll();

        // 모델에 가지고 온 데이터를 넣어 줌.
        model.addAttribute("articles", articles);

        // resources/templates/articleList.HTML 생성
        return "articleList";
    }

}
