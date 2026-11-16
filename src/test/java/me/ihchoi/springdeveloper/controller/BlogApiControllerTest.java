package me.ihchoi.springdeveloper.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.ihchoi.springdeveloper.dao.Article;
import me.ihchoi.springdeveloper.dto.ArticleCreationRequest;
import me.ihchoi.springdeveloper.repository.BlogRepository;
import me.ihchoi.springdeveloper.service.BlogService;
import org.hibernate.engine.jdbc.Size;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest // 컨트롤러 테스트 시 필요
@AutoConfigureMockMvc // http 요청을 흉내내기 위해 사용
class BlogApiControllerTest {
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

    @DisplayName("글 업데이트 테스트")
    public void updateTest() {
        // 1. given

        // 2. when

        // 3. then
    }

    @DisplayName("글 삭제 테스트")
    public void deleteTest() {
        // 1. given

        // 2. when

        // 3. then
    }

}