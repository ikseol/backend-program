package me.ihchoi.springdeveloper;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// 클래스 단위에서 사용해야하는 SpringBootTest, TestInstance
// SpringBootTest : 의존성 주입을 위해 스프링 bean 생성
// AutoConfigureMockMvc : 테스트 중 웹, postman 사용을 안 하기 위해서 mockMvc 필요하니까 사용. -> 사용을 하기 위해 맴버변수에 선언 필요
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class QuizControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // 자바 객체를 JSON 문자열로 변환해주는 API, POST 요청 테스트 보낼때 사용.

    // QuizController의 Quiz1 메소드 실행 결과 아래와 같이 나와야 함.
    @DisplayName("GET /quiz?code=1 => 응답코드: 201, 응답 body본문: created! 를 리턴")
    @Test
    public void getQuiz1() throws Exception {
        // 준비 given ->
        final String url = "/quiz";

        // 실행 when
        final ResultActions result = mockMvc.perform(get(url).param("code","1")); // GET /quiz?code=1
        // mockMvc.perform은 예외처리 해야함. -> try catch로 감싸거나, throws 로 던지기 처리 해야함. 던지기 = Junit(날 부른 친구에게) 던짐
        // mockMvc.perform으로 요청을 보내고 결과(상태코드와, body 내용)를 ResultActions타입 변수 result에 저장

        // 검증 then
        result.andExpect(status().isCreated()).andExpect(content().string("Created!"));
        // result에 전달된 응답 스테이터스가 created(201)인가? 또한 전달된 string이 Created! 인가? 검사.
    }

    // QuizController의 Quiz1 메소드 실행 결과 아래와 같이 나와야 함.
    @DisplayName("GET /quiz?code=2 => 응답코드: 400, 응답 body본문: Bad Request! 를 리턴")
    @Test
    public void getQuiz2() throws Exception {
        // 준비 given ->
        final String url = "/quiz";
//        final ObjectMapper obj = objectMapper.writeValueAsString(new Code(1));

        // 실행 when
        final ResultActions result = mockMvc.perform(get(url).param("code","2")); // GET /quiz?code=2
        // mockMvc.perform은 예외처리 해야함. -> try catch로 감싸거나, throws 로 던지기 처리 해야함. 던지기 = Junit(날 부른 친구에게) 던짐
        // mockMvc.perform으로 요청을 보내고 결과(상태코드와, body 내용)를 ResultActions타입 변수 result에 저장

        // 검증 then
        result.andExpect(status().isBadRequest()).andExpect(content().string("Bad Request!"));
        // result에 전달된 응답 스테이터스가 BadRequest(400)인가? 또한 전달된 string이 Bad Request! 인가? 검사.
    }

    @DisplayName("POST /quiz 요청, 바디에 {value:1}이면, 403, Forbidden!")
    @Test
    public void postQuiz1() throws Exception {
        // given 준비
        final String url = "/quiz";
//        final String body = objectMapper.writeValueAsString(new Code(1)); 개인 테스트 수업 X

        // when 실행
        final ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new Code(1))));

        // body의 데이터 를 넣으려면 contentType메소드를 체이닝,
        // 메소드 인자로는 MediaType.APPLICATION_JSON (JSON으로 보내겠다는 뜻)
        // .content() 내가 보낼 실제 내용은 이거라는 뜻.
        // 오브젝트 맵퍼로 자동으로 {"value" : 1} JSON 형태로 변환해줌

        // then 검증
        result.andExpect(status().isForbidden()).andExpect(content().string("Forbidden!"));
    }

    @DisplayName("POST /quiz 요청, 바디에 {value:2}면 200, OK!")
    @Test
    public void postQuiz2() throws Exception {
        // given 준비
        final String url = "/quiz";

        // when 실행
        final ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new Code(2))));

        // then 검증
        result.andExpect(status().isOk())
                .andExpect(content().string("OK!"));
    }
}

// ppt 용어 F.I.R.S.T  좋은 테스트의 5가지 원칙. (노션에도 있으니 참고)
// first에 대한 노션 보고 설명함. -> 시험에 나온다고 함.