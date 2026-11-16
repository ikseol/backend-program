package me.ihchoi.springdeveloper.controller;

import lombok.RequiredArgsConstructor;
import me.ihchoi.springdeveloper.dao.Article;
import me.ihchoi.springdeveloper.dto.ArticleCreationRequest;
import me.ihchoi.springdeveloper.dto.ArticleResponse;
import me.ihchoi.springdeveloper.dto.ArticleUpdateRequest;
import me.ihchoi.springdeveloper.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController // REST API를 처리하기 위한 컨트롤러 // REST의 특징 : 호출 시 응답으로 JSON 데이터 반환.


// 맴버변수를 초기화 하기 위해서 맴버변수를 가지고 있는 생성자를 생성하라는 어노테이션
// 초기화에 필요하다고 인식하기 위해 맴버변수에 final 필요
// public BlogApiController(BlogService bs) {
//     this.blogService = bs;
// } Autowired도 안쓰고, 자런 과정도 자동으로 해주는게 아래의 어노테이션
@RequiredArgsConstructor
public class BlogApiController {
    // 블로그 서비스 = 스프링 빈 = 내가 생성 X 의존성 주입 해야함. blogService = new BlogService 이것 X
    // 생성자로 의존성 주입 : BlogApiController의 생성자를 만들어서 주입 가능
    private final BlogService blogService;

    // 구현해둔 서비스를 호출 해 글 저장하는 것을 구현
    @PostMapping("/api/articles") // 해당 URL에 POST 요청이 오면 아래 메소드 실행
    public ResponseEntity<Article> addArticle(@RequestBody ArticleCreationRequest articleCreationRequest) {
        // 리턴 타입 : ResponseEntity //  JSON 안에있는 본문을 추출해서 ArticleCreationRequest DTO로 바꿔서 반환

        // 서비스에게 블로그 글 등록을 의뢰
        Article savedArticle = blogService.save(articleCreationRequest);
        // 성공했다면 ResponseEntity 객체를 반환 (성공했다고 알림)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedArticle);

        // 11-02
        // HTML 웹페이지
        // <form method="post" action="api/articls">
        //      <input type="text" name='title'>
        //      <input type="text" name='content'>
        //      <button type=submit>등록
        // </form>
        // 웹에서 이렇게 요청 보내면 -> 정보가 요청 바디에 들어감.
        // 백엔드에서 요청 바디에 있는 정보를 빼내는 법 = requestBody
        // (@RequestBody ArticleCreationRequest) => 자동으로 리퀘스트 바디의 정보를 추출해서  ArticleCreationRequest
        // DTO 대로 자동으로 객체 생성해서 지정해준 대로 DTO를 만들어줌.
    }

    // 서비스와 DTO 생성 후
    @GetMapping("/api/articles")
    public ResponseEntity<List<ArticleResponse>> getAllArticles() { // 리턴 : http 응답 속에 리스트가 들어있고 그 리스트 속에 dto가 들어감.
        // 모든 글 목록을 articleList에 불러옴.
        List<Article> articleList = blogService.findAll();

//      return ResponseEntity.ok().body(articleList);
//      이렇게 할 수 없는 이유 : 반환해야할 타입이 안 맞는다, DTO로 리턴 해야함.

        // 반환하기 위한 새 배열 생성 List는 인터페이스 ArrayList 는 ?
        List<ArticleResponse> articleResponsesList = new ArrayList<>();

        for (Article article : articleList) { // forEach 문 처음에 받아온 아티클 리스트의 각 원소에 아래의 코드 실행
            // 각 원소를 articleResponses 객체로 변환해 ArrayList에 삽입.
            articleResponsesList.add(new ArticleResponse(article));
        }

        return ResponseEntity.ok().body(articleResponsesList);
        // 200번 ok를 리턴 + 바디에는 articleResponses을 삽입해 리턴

        // blogService.findAll() -> 리스트 반환 -> .straem 사용 가능
        // blogService.findAll().stream().map(ArticleResponse::new).toList()
        // stream에 map을 체이닝해서 값 변경 가능함.    map(ArticleResponse::new) == new ArticleResponse(article)

        // 아티클 객체를 이용해서 ArticleResponse객체를 만드는 것을 간단하게 할 수 있음.

        // .toList() == 리스트로 변경하라

        // for 문 돌면서 하던 작업을 한줄로 바꿀 수 있다.

//        List<ArticleResponse> articleResponse2 = blogService.findAll().stream().map(ArticleResponse::new).toList();
        // 이런 느낌으로 사용
    }

    // 정보가 Article객체로 반환된 경우 ( optional로 반환된걸 서비스에서 .get() 해서 article이 됨)
    // /api/articles/{id} 주소에 get 요청을 보내면
    @GetMapping("/api/articles/{id}")   // { id } -> @PathVariable을 사용하는 법 path상의 값을 뽑아내기 위해 사용
    public ResponseEntity<ArticleResponse> getById(@PathVariable("id") Long id) { // path상의 값을 빼서 Long id에 저장
        // blogService에 구현된 findById(id) 메소드 호출해서 Article 객체에 저장
        // id는 PathValue임.
        Article article = blogService.findById(id);

        // 생성된 Article 객체를 ArticleResponse객체로 변환
        ArticleResponse response = new ArticleResponse(article);

        // Article에서 ArticleResponse로 변환된 객체를 ResponseEntity의 request body에 넣어서 전송
        return ResponseEntity.ok().body(response);

        // 혹은 위 두 과정을 합쳐서 이렇게도 사용 가능함.
        // return ResponseEntity.ok().body(new ArticleResponse(article));

    }

    // Optional<Articles>로 반환된 객체의 경우
    @GetMapping("/api/articles2/{id}")
    public ResponseEntity<ArticleResponse> getById2(@PathVariable("id") Long id) {
        Optional<Article> article = blogService.findById2(id);

        // Optional에 들어있는 Articles을 꺼내서 Article a 객체에 저장
        Article a = article.get();

        ArticleResponse response = new ArticleResponse(a);

        return ResponseEntity.ok().body(response);
    }

    // /api/articles/{id}에 delete요청을 보내면
    @DeleteMapping("/api/articles/{id}")
    // URL의 {id}에 위치하는 값을 매개변수로 가져옴. 리턴 타입은 ResponseEntity<Void>
    public ResponseEntity<Void> deleteById(@PathVariable("id") Long id) {
        // blogService에 deleteById메소드를 실행.
        blogService.deleteById(id);
        // ResponseEntity 객체를 리턴하고 ResponseEntity의 제네릭이 <Void>이기 때문에 내용 없이 그대로 리턴.
        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/articles/{id}")
    @PatchMapping("/api/articles/{id}") // 이런식으로 어노테이션을 두개 달면 풋, 패치 모두 처리 가능
    // 컨트롤러에서 항상 ResponseEntity로 리턴  내부에는 ArticleResponse (응답 줄떄 사용하는 DTO) 전달
    // PathVariable이 여러개 올 수도 있음 그럴때는 @PathVariable("id")이런식으로 적고 하나면 생략 가능.
    // @RequestBody로 body에 있는 JSON을 뽑아와  ArticleUpdateRequest타입으로 변환해 request 변수에 받아옴.
    public ResponseEntity<ArticleResponse> updateArticle(@PathVariable("id") Long id, @RequestBody ArticleUpdateRequest request) {
        // ArticleResponse에는 변경된 내용을 그대로 넣어서 리턴
        // 서비스에서 Article로 받으면 ArticleResponse로 변환해야하고
        // 서비스에서 ArticleResponse로 받으면 변환 안해도 됨.

        // 컨트롤러에서는 해당하는 비즈니스 로직을 호출.
        Article article = blogService.updateArticle(id, request);
        ArticleResponse articleResponse = new ArticleResponse(article);
        // 서비스에서 Article로 리턴받았기 때문에 변환 과정 추가.

        return ResponseEntity.ok().body(articleResponse);
    }

}

