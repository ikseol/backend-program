package me.ihchoi.springdeveloper.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.ihchoi.springdeveloper.dao.Article;
import me.ihchoi.springdeveloper.dto.ArticleCreationRequest;
import me.ihchoi.springdeveloper.dto.ArticleResponse;
import me.ihchoi.springdeveloper.repository.BlogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatStream;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;


// 과제를 작성하기 위해 새 파일 생성

@SpringBootTest // 스프링부트 테스트코드 작성 시 필요
@AutoConfigureMockMvc // HTTP 요청을 흉내내는 mockMvc를 사용하기 위해 필요
public class BlogApiControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper; // 객체를 JSON으로 변환하는 기능을 제공한다.

    @Autowired
    BlogRepository blogRepository;

    @BeforeEach
    public void setup() {
        blogRepository.deleteAll(); // 매 테스트 전에 DB를 초기화하는 역할을 한다.
    }

    // 아래에 테스트 메서드 작성

    @DisplayName("테스트 1 - addArticle - 글 생성 테스트")
    @Test
    public void addArticle() throws Exception { // perform 사용 시 예외처리
        // 1. Given 준비
        // 제목, 내용을 담은 요청 DTO를 만들고 objectMapper.writeValueAsString()으로 JSON으로 변환

        // 제목 내용을 담은 요청 DTO 제작
        String title = "글 제목";
        String content = "글 내용";
        ArticleCreationRequest article = new ArticleCreationRequest(title, content); // 위에 변수에 저장한 제목과 내용으로 ArticleCreationRequest 객체 생성

        // 요청 DTO를 objectMapper.writeValueString()으로 JSON으로 변환
        String jsonArticle = objectMapper.writeValueAsString(article);  // Json으로 변환해 변수에 담아둠

        // 2. When 실행
        // POST /api/articles 요청 (contentType을 application/JSON 으로 지정)

        // "/api/articles"로 post 요청    콘텐츠 타입은 JSON    요청 body에 들어갈 내용(새로 등록할 내용)은 jsonArticle
        ResultActions result = mockMvc.perform(post("/api/articles")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(jsonArticle));

        // 3. Then 검증
        // 1. 상태 코드가 컨트롤러가 반환하는 값과 일치하는가? (201 또는 200)
        result.andExpect(status().isCreated());  // POSTMAN 테스트 결과 201 Create 로 응답이 오는것을 확인했기때문에 isCreate()로 검증 진행

        // 2. findAll() 결과가 1건이고, 제목 내용이 요청값과 같은가?
        List<Article> articleList = blogRepository.findAll(); // 결과가 1개 이상이 나올 수 있기 때문에 LIST<> 사용

        // findAll로 조회한 글의 갯수가 1인가? = findAll()의 결과가 1건인가?
        assertThat(articleList.size()).isEqualTo(1);

        // 제목과 내용이 요청값과 같은가?
        Article savedArticle = articleList.get(0);  // 위에서  findAll 해서 가져온 리스트의 첫 번째 article을 따로 저장
        assertThat(savedArticle.getTitle()).isEqualTo(title); // 따로 저장한 article의 제목이 위에서 지정한 제목과 같은가?
        assertThat(savedArticle.getContent()).isEqualTo(content); // 따로 저장한 article의 내용이 위에서 지정한 내용과 같은가?

    }

    @DisplayName("테스트 2 - findArticle - 글 조회 테스트")
    @Test
    public void findArticle() throws Exception {
        // 1. Given 준비
        //  blogRepository.save()로 글 1건을 미리 저장

        // 제목과 내용을 변수에 저장해둠.
        String title = "글 제목";
        String content = "글 내용";

        // 글을 1건 미리 저장하면서 저장될 글의 ID를 알아내기 위해서 savedArticle에 저장될 글을 넣어둠.
        Article savedArticle = blogRepository.save(new Article(title, content));

        // 저장해둔 글의 id를 가져옴
        int id = savedArticle.getId();

        // 2. When 실행
        // GET /api/articles/{id} 요청 (저장된 글의 ID 사용)
        ResultActions result = mockMvc.perform(get("/api/articles/"+id));

        // 3. Then 검증
        // 1. 200 OK 2. 응답 JSON의 title, content가 저장한 값과 같은가? ( jsonPath("$.title")활용)

        // 1. OK 요청이 왔는가?
        result.andExpect(status().isOk());

        // 2. 응답 JSON의 title, content가 저장한 값과 같은가?
        result.andExpect(jsonPath("$.title").value(title));
        result.andExpect(jsonPath("$.content").value(content));
        // jsonPath("$.title") 의 의미 : json 최상위($.) 아래에 있는 title을 의미.
        // 즉 json 최상위 아래에 있는 title의 값(.value)이 위에서 저장한 title의 값과 같은가? content 도 동일한 검증 진행
    }

    @DisplayName("테스트 3 - updateArticle - 글 수정 테스트")
    @Test
    public void updateArticle() throws Exception {
        // 1. Given 준비
        // 글 1 건을 저장하고, 새 제목과 내용을 담은 UpdateArticleRequest를 JSON으로 변환

        // 글 1건을 미리 저장
        String title = "글 제목";
        String content = "글 내용";
        Article savedArticle = blogRepository.save(new Article(title, content));
        // 미리 저장한 글을 수정할 것이기 떄문에 savedArticle의 id를 가져와서 미리 저장
        long id = savedArticle.getId();

        // 새 제목과 내용을 담은 updateArticleRequest를 JSON으로 변환
        String newTitle = "수정할 글 제목";
        String newContent = "수정할 글 내용";
        ArticleCreationRequest article = new ArticleCreationRequest(newTitle, newContent);
        // 위에 변수에 저장한 제목과 내용으로 ArticleCreationRequest 객체 생성

        // 요청 DTO를 objectMapper.writeValueString()으로 JSON으로 변환
        String jsonArticle = objectMapper.writeValueAsString(article);  // Json으로 변환해 변수에 담아둠

        // 2. When 실행
        // PUT /api/articles/{id} 요청

        // "/api/articles/{id}"로 put 요청  {id}는 아까 저장한 글의 id가 됨.
        //                       콘텐츠 타입은 JSON    요청 body에 들어갈 내용(수정될 내용)은 jsonArticle
        ResultActions result = mockMvc.perform(put("/api/articles/"+id)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(jsonArticle));
        // 3. Then 검증
        // 1. 200 OK , 2. findById()로 다시 조회한 글의 제목과 내용이 새 값으로 바뀌었는지 확인.
        // 1. HTTP 상태 코드가 200 ok 인지 검증
        result.andExpect(status().isOk());

        // 2. findById()로 다시 조회한 글의 제목과 내용이 새 값으로 바뀌었는지 확인.
        Optional<Article> articleOptional = blogRepository.findById(id); // id 조회는 값이 0개 혹은 1개이기 때문에 Optional<> 에 넣어서 반환
        Article findArticle = articleOptional.orElse(null); // Optional<article> 에 값이 있으면 꺼내서 저장 없으면 null 반환

        // findArticle에 저장된 글의 제목과 내용을 조회해서 새로 등록될 제목과 내용(newTitle, newContent)과 일치한지 비교
        assertThat(findArticle.getTitle()).isEqualTo(newTitle);
        assertThat(findArticle.getContent()).isEqualTo(newContent);
    }


    @DisplayName("테스트 4 - deleteArticle - 글 삭제 테스트")
    @Test
    public void deleteArticle() throws Exception {
        // 1. Given 준비
        // 글 1건을 저장
        // 글 1건을 미리 저장
        String title = "글 제목";
        String content = "글 내용";
        Article savedArticle = blogRepository.save(new Article(title, content));
        // 미리 저장한 글을 수정할 것이기 떄문에 savedArticle의 id를 가져와서 미리 저장
        long id = savedArticle.getId();


        // 2. When 실행
        // DELETE /api/articles/{id} 요청
        ResultActions result = mockMvc.perform(delete("/api/articles/"+id));

        // 3. Then 검증
        // 1. 200 OK 2. findAll() 결과가 비어 있음 (assertThat(articles).isEmpty())
        // 1. 200 OK 인가?
        result.andExpect(status().isOk());

        // 2. findAll() 결과가 비어있는가?
        List<Article> articles = blogRepository.findAll(); // 모든 글을 조회
        assertThat(articles).isEmpty(); // 조회한 List가 비어있는가?
    }


    @DisplayName("테스트 5 - findAllArticle - 글 목록 조회 테스트")
    @Test
    public void findAllArticle() throws Exception {
        // 1. Given 준비
        // blogRepositoy.save()로 글 2건 이상을 미리 저장
        // 글 1건 저장
        String title1 = "1번 글 제목";
        String content1 = "1번 글 내용";
        Article savedArticle1 = blogRepository.save(new Article(title1, content1));

        // 글 2건 저장
        String title2 = "2번 글 제목";
        String content2 = "2번 글 내용";
        Article savedArticle2 = blogRepository.save(new Article(title2, content2));

        // 2. When 실행
        // GET /api/articles 요청

        // /api/articles에 get요청
        ResultActions result = mockMvc.perform(get("/api/articles"));

        // 3. Then 검증
        // 1. 200 OK  2. 응답 배열의 길이가 저장한 글의 갯수와 같은가 (jsonPath("$.length()").value(2)
        // 3. 각 글의 title, content가 저장한 값과 같음 (jsonPath("$[0].title")) (jsonPath("$[1].title"))

        // http 응답 코드가 200인가?
        result.andExpect(status().isOk());

        // 저장한 글이 2개이기 때문에 => 응답으로 온 배열의 길이가 2개인가?
        result.andExpect(jsonPath("$.length()").value(2));

        // json 배열의 0번에 위치한 json의 제목과 내용을 비교
        result.andExpect((jsonPath("$[0].title").value(title1)));
        result.andExpect((jsonPath("$[0].content").value(content1)));

        // json 배열의 1번에 위치한 json의 제목과 내용을 비교
        result.andExpect((jsonPath("$[1].title").value(title2)));
        result.andExpect((jsonPath("$[1].content").value(content2)));
    }
}