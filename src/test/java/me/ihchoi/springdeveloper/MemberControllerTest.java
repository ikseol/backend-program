package me.ihchoi.springdeveloper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*; // 목 Mvc 관련
import javax.swing.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest // 컨트롤러 테스트 하려면 스프링빈을 DI 받아야 하기 때문에 해당 어노테이션이 있어야 함. = 스프링 빈 생성
@AutoConfigureMockMvc // 컨트롤러 단위 테스트를 위해서 이것 또한 필요.
// 컨트롤러는 클라이언트의 요청을 받음, 클라이언트 : 웹브라우저 혹은 포스트맨
// MockMvc = 가짜 인터넷 요청을 보내서 컨트롤러가 응답을 주면 받아서 흉내내서 처리
class MemberControllerTest {

    // 스프링에서는 객체를 new 키워드로 생성 X 어노테이션을 활용해 스프링에게서 주입, 소멸 생성 관리 등을 받아야함.
    // 스프링빈 : 컨트롤러 서비스 리포지터리 엔티티 등
    @Autowired // autowired 키워드로 DI
    private MemberRepository memberRepository; // 맴버 변수는 직접 선언.

    // MockMvc도 DI 받아야 사용 가능
    @Autowired
    private MockMvc mockMvc;

    @Test //J유닛 테스트를 위해 필요한 어노테이션
    void getAllMember() throws Exception {
        // 1. Given 준비
        // 한 명의 회원을 DB테이블에 저장함으로써 등록
        // 구현해 둔 컨트롤러로도 등록 가능하지만 "회원 리스트 요청" 테스트를 하는것이기 때문에 직접 등록.
        final String url = "/member"; // 테스트에서 쓸 url 등도 사전에 준비.
        Member savedMember = memberRepository.save(new Member("최익훈"));
        // 내부적으로 insert into ....; 또는 update 실행. 내부적으로 키가 없으면 인서트 있으면 update.
        // 객체 안에 ID값이 DB안에 있으면 update, 없으면 insert => 우리가 autoincrement 설정을 했기 때문
        // id 가 없어야 하는데 생성된 생성자에는 인자를 안 받는 생성자 or id, 이름 둘 다 받는 생성자 두가지. -> member entity에 만들면 됨
        // .save 키워드로 성공적으로 삽입되면 id와 이름을 리턴해줌. 리턴해준 것을 savedMember변수에 받아 둠.
        // 아무것도 없는 테이블에 삽입 성공했다면 id:1, name:최익훈 리턴되어 저장

        // 2. When 실행
        // 회원 리스트 요청
        final ResultActions result = mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON)); // 클라이언트의 요청 흉내 : perform(), 리턴값 : 클라이언트의 응답
        // 목mvc의 퍼폼이라는 메소드를 호출했을때 예외 발생 가능 = 예외 발생 가능한 메소드는 try-catch로 감싸거나 메소드에 throws Exception 해야함.
        // 목mvc의 기능으로

        // 웹 서비스의 규약 : HTTP 프로토콜, 내가 http 요청을 보내고, 백엔드에서 http response를 보냄.
        // http는 헤더와 바디 두 개로 구성.
        // 정상처리는 상태코드 또한 같이 감. = 200번
        // 클라이언트의 잘못된 요청 : 400번대 response
        // 클라이언트의 바른 요청 + 서버의 내부의 오류 : 500번대
        // 기본적으로 응답에는 status 코드가 같이 감. => List<member> 이것은 body에 정보로 들어라고, 200 OK 상태는 header에 들어감

        // 3. Then 검증
        // 회원 리스트 요청에 의해서 전달받은 데이터가 준비 단계에서 저장된 회원 정보와 일치하는가?
        result.andExpect(status().isOk()) // == result에 저장된 http헤더가 200 OK인가?
                .andExpect(jsonPath("$[0].id").value(savedMember.getId()))
                .andExpect(jsonPath("$[0].name").value(savedMember.getName()));


    }
    // 테스트 실행 시 기존에 DB에 있던 내용때문에 실패 처리됨. => 테스트 전에 실행될 내용 지정 필요.
    @BeforeEach
    void deleteAll() {
        memberRepository.deleteAll(); // 각각 테스트 실행 전 모든 맴버테이블 내용 삭제.
    }
}
// 컨트롤러 테스트 계속 해야함. 맴버 컨트롤러 테스트 말고 다른 컨트롤러 만들어서 테스트 진행.

// 테스트 : 단위 통합 인수 테스트
// 단위 : 기능한 구현 단위 테스트
// 통합 : 전체 시스템 통합 테스트
// 인수 : 고객에게 인수 후 테스트
// 우리가 할 것 : 단위 테스트, 자바 표준 JUnit + 검증 프레임워크 AssertJ 사용
// 단위 테스트를 위해서 : @test 어노테이션 @DisplayName("설명") 어노테이션 사용
// 각 테스트 전후 수행할 메소드 @BeforeEach, @AfterEach
// 모든 테스트 실행 전후 1회 수행할 메소드 : 반드시 static메소드 @BeforeAll, @AfterAll
// 테스트 주도 개발 방식 TDD 사용
// 서버와 postman 실행 없이 MockMVC를 이용해서 테스트.

// 여력이 되면 진도가 느려도 더 공부해서, 개별 프로젝트 진행.