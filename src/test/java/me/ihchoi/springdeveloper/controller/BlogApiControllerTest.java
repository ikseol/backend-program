package me.ihchoi.springdeveloper.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.ihchoi.springdeveloper.repository.BlogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

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
    public void addArticle() {
        // 1. Given 준비
        // 제목, 내용을 담은 요청 DTO를 만들고 objectMapper.writeValueAsString()으로 JSON으로 변환

        // 2. When 실행
        // POST /api/articles 요청 (contentType을 application/JSON 으로 지정)

        // 3. Then 검증
        // 1. 상태 코드가 컨트롤러가 반환하는 값과 일치하는가? (201 또는 200)

        // 2. findAll() 결과가 1건이고, 제목 내용이 요청값과 같은가?

    }

    @DisplayName("테스트 2 - findArticle - 글 조회 테스트")
    @Test
    public void findArticle() {
        // 1. Given 준비
        //  blogRepository.save()로 글 1건을 미리 저장

        // 2. When 실행
        // GET /api/articles/{id} 요청 (저장된 글의 ID 사용)

        // 3. Then 검증
        // 1. 200 OK 2. 응답 JSON의 title, content가 저장한 값과 같은가? ( jsonPath("$.title")활용)

    }

    @DisplayName("테스트 3 - updateArticle - 글 수정 테스트")
    @Test
    public void updateArticle() {
        // 1. Given 준비
        // 글 1 건을 저장하고, 새 제목과 내용을 담은 UpdateArticleRequest를 JSON으로 변환

        // 2. When 실행
        // PUT /api/articles/{id} 요청

        // 3. Then 검증
        // 1. 200 OK , 2. findById()로 다시 조회한 글의 제목과 내용이 새 값으로 바뀌었는지 확인.

    }


    @DisplayName("테스트 4 - deleteArticle - 글 삭제 테스트")
    @Test
    public void deleteArticle() {
        // 1. Given 준비
        // 글 1건을 저장

        // 2. When 실행
        // DELETE /api/articles/{id} 요청

        // 3. Then 검증
        // 1. 200 OK 2. findAll() 결과가 비어 있음 (assertThat(articles).isEmpty())

    }


    @DisplayName("테스트 5 - findAllArticle - 글 목록 조회 테스트")
    @Test
    public void findAllArticle() {
        // 1. Given 준비
        // blogRepositoy.save()로 글 2건 이상을 미리 저장

        // 2. When 실행
        // GET /api/articles 요청

        // 3. Then 검증
        // 1. 200 OK  2. 응답 배열의 길이가 저장한 글의 갯수와 같은가 (jsonPath("$.length()").value(2)
        // 3. 각 글의 title, content가 저장한 값과 같음 (jsonPath("$[0].title")) (jsonPath("$[1].title"))

    }
}