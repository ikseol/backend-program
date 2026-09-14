package me.ihchoi.springdeveloper;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloWorldController {

    // 퍼블릭 메소드 hello 생성, 이 메소드를 실행하면 String을 반환
    @GetMapping("/hello") // 어노테이션 매핑.
    public String hello() {
        return "Hello World";
    }
    // 얘를 누가 호출함? -> SpringBoot가 해줌.
    // 얘를 언제 호출함? -> 클라이언트가 '/Hello' 라는 요청을 보내면 hello() 메소드를 호출
    // localhost:8080/hello 요청을 받으면 이 메소드를 실행해라. -> @GetMapping 어노테이션 문장으로 위에서 지정 필수
    // 이 클래스가 컨트롤러 라고 하는거도 미리 선언 해줘야함. 어디에 선언하는가? 퍼블릭 클래스 위에 @로
    // 컨트롤러는 2종류 있는데, 이거는 일단 Rest컨트롤러로 지정.

    // http://localhost:8080/test 에 접속했을때 -? Hello Everyone 리턴하는 코드
    // 컨트롤러를 추가로 만들어도 되는데 지금은 메소드만 추가

//    @GetMapping("/test")  // url과 메소드의 맵핑, 기본적으로 get방식
//    public String test() {
//        return "Hello Everyone!!!!!!!!!";
//    }
//
//    @PostMapping("/test") // post 방식 처리용 맵핑.  localhost:8080/test 에 post 요청이 오면 해당 메소드 실행
//    public String postTest() {
//        return "Post Test Response!";
//    }
//
//    @DeleteMapping("/test") // delete 방식 처리용 맵핑.  localhost:8080/test 에 delete 요청이 오면 해당 메소드 실행
//    public String deleteTest() {
//        return "delete Test Response!";
//    }
//
//    @PutMapping("/test") // put 방식 처리용 맵핑.  localhost:8080/test 에 put 요청이 오면 해당 메소드 실행
//    public String putTest() {
//        return "Put Test Response!";
//    }

    // 이런 식으로 요청별 처리용 맵핑 -> 웹 브라우저에서 테스트 하려면 별도로 요청을 보낼 컨트롤러(프론트를 만들어야 함)
    // 그래서 Postman을 사용해서 요청을 보내고, Response를 받을 수 있음.
}
