package me.ihchoi.springdeveloper.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.ihchoi.springdeveloper.dao.Article;
import me.ihchoi.springdeveloper.dto.ArticleCreationRequest;
import me.ihchoi.springdeveloper.repository.BlogRepository;
import me.ihchoi.springdeveloper.service.BlogService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// 과제 이외 (수업시간, 야자시간 등) 에 작성한 테스트 코드를 저장하기 위해 분리함.

@SpringBootTest // 컨트롤러 테스트 시 필요
@AutoConfigureMockMvc // http 요청을 흉내내기 위해 사용
public class blogApiControllerTestClass {
    @Autowired
    private MockMvc mockMvc;

    // 생성 요청 보내면 {"title":"1", "content":"A"}이런 JSON이 만들어져야함.
    // -> DTO 객체 등 자바 객체를 JSON으로 바꾸거나 Json을 자바 객체로 바꾸는 라이브러라가 있음 = ObjectMapper => Jackson 이미 포함되어있음.

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private BlogService blogService;

    @Test
    @DisplayName("addArticle : 블로그 글 추가 성공한다")
    public void addArticle() throws Exception {
        // 준비 : 저장할 데이터, 요청 url 준비
        // 저장할 데이터는 JSON으로 준비, DTO를 통해 만들면 됨
        String title = "Junit 테스트를 통한 블로그 글 생성";
        String content = "11월02일 화요일 등록한 테스트임.";
        String url = "/api/articles";

        ArticleCreationRequest req = new ArticleCreationRequest(title, content);
        String articleStr = objectMapper.writeValueAsString(req); // 예외처리 해야함. 자바 객체를 JSON으로 바꿔줌

        // 실행
        ResultActions result =
                mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(articleStr));

        // 검증
        List<Article> articles = blogRepository.findAll();
        assertThat(articles.size()).isEqualTo(1);
        Article article = articles.get(0);
        assertThat(article.getTitle()).isEqualTo(title);
        assertThat(article.getContent()).isEqualTo(content);
    }

    @DisplayName("모든 블로그 글 조회 성공")
    @Test
    public void getAllArticle() throws Exception {
        // 준비 : url과 데이터를 저장
        String url = "/api/articles";

        String title = "11월05일 목요일 등록한 테스트";
        String content = "모든 블로그 글 조회 테스트 코드 작성";

        // 빌더 패턴을 사용해 DB에 레코드 저장
        blogRepository.save(Article.builder()
                .content(content)
                .title(title)
                .build());

        // 실행
        ResultActions resultActions = mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON));

        //검증
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value(title))
                .andExpect(jsonPath("$[0].content").value(content));
        // JSONPath 스태틱 매소드로 특정한 값 추출 가능
        // jsonPath("$[0].title").value() => 0번째 원소값의 제목
    }
    // addArticle 이 버그가 발생 -> 클래스 레벨에서 DataJPA테스트 어노테이션을 안 줬기 때문에
    // 트랜잭셔널 어노테이션이 자동으로 미포함 => 그 전 테스트가 롤백 안 됨
    // 이럴 때는 정리 코드를 작성해줘야 함.

    @AfterEach
    public void cleanUp() {
        blogRepository.deleteAll();
    }
    // 이럼 하나의 메소드 실행 후 모든 정보 삭제 -> 깨긋한 상태에서 테스트 -> 오류발생 안함.

    @DisplayName("특정 id를 가진 글 조회")
    @Test
    public void getArticleById() throws Exception {
        // 1. 준비 Given
        String url = "/api/articles";

        // 등록할 제목과 내용을 저장
        String title = "11월 19일 목요일 등록한 테스트";
        String content = "특정 블로그 글 조회 테스트 코드 작성";

        // 빌더 패턴을 사용해 DB에 레코드 저장
        Article savedArticle = blogRepository.save(Article.builder()
                .content(content)
                .title(title)
                .build());

        // 2. 실행 when

        int id = savedArticle.getId();  // 저장된 글의 ID를 id 변수에 담음 이 경우 id가 1이 될 예정

        //  mockMvc를 활용해 /api/articles/1 url에 get요청을 보내서 응답을 result에 저장
        // 응답( .accept() )은 JSON(MediaType.APPLICATION_JSON)으로 받겠다.
        ResultActions result = mockMvc.perform(get(url+"/{id}", id).accept(MediaType.APPLICATION_JSON));

        // get(url+"/{id}", id) 의 의미
        // url =  "/api/articles"
        // {id}에는 위에서 저장한 id의 값이 들어감. = /1
        // 두가지를 + 연산으로 합치면 /api/articles/1 url에 요청이 감.

        // 3. 검증 then
        // 응답의 HTTP Status가 OK인지 검증
        result.andExpect(status().isOk());
        // result (응답) 의 jsonPath(응답으로 온 json)의 title의 값이 위에서 작성한 title변수의 값과 같은가?
        // $.title 이란? 응답으로 온 JSON의 최상위에 있는 title을 의미함.
        result.andExpect(jsonPath("$.title").value(title));
        // 위와 동일하게 content 내용을 검사하는 코드
        result.andExpect(jsonPath("$.content").value(content));
    }

    @DisplayName("글 업데이트 테스트")
    @Test
    public void updateTest() throws Exception {
        // 1. given
        String url = "/api/articles";

        // 새 글 등록  ID=1
        Article article = new Article("수정 전 제목", "수정 전 내용");
        blogRepository.save(article);

        // 글 수정을 위한 정보 작성
        String newTitle = "수정 후 제목";
        String newContent = "수정 후 내용";

        // 수정할 정보를 담은 객체를 만들고
        Article newArticle = new Article(newTitle, newContent);
        // 그 객체를 objectMapper를 통해 Json으로 변환
        String JsonNewArticle = objectMapper.writeValueAsString(newArticle);

        // 2. when
        ResultActions result =
                mockMvc.perform(put(url+"/{id}", article.getId())  // 등록한 Article의 ID는 1 -> /api/articles/1 경로로 PUT 요청 보냄
                        .contentType(MediaType.APPLICATION_JSON)  // 콘텐츠의 타입은 JSON
                        .content(JsonNewArticle)); // 바디에는 아까 수정할 정보를  JSON으로 변환한것을 넣음
        // 3. then
        result.andExpect(status().isOk()); // HTTP요청이 OK?

        // 수정된 id1번인 글을 가져와서
        Article updatedArticle = blogRepository.findById(1L).get();
        assertThat(updatedArticle.getTitle()).isEqualTo(newTitle); // 아까 등록해둔 새 제목과 같은지?
        assertThat(updatedArticle.getContent()).isEqualTo(newContent); // 아까 등록해둔 새 내용과 같은지? 검증
    }

    @DisplayName("글 삭제 테스트")
    @Test
    public void deleteTest() throws Exception {
        // 1. given
        String url = "/api/articles";

        // 새 글 등록  ID=1
        Article article = new Article("등록할 제목", "등록할 내용");
        blogRepository.save(article);

        // 2. when
        ResultActions result = mockMvc.perform(delete(url+"/{id}", article.getId()));

        // 3. then

        // API 응답 상태 코드가 200 OK인지 확인
        result.andExpect(status().isOk());

        // 방금 지운 ID로 글 조회
        Optional<Article> deletedArticle = blogRepository.findById( (long) article.getId());

        // 조회 결과 옵셔널 배열이 비어있는가? 체크해서 비어있으면 삭제 완료
        assertThat(deletedArticle).isEmpty();
    }

}