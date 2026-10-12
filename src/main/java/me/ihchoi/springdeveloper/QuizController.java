package me.ihchoi.springdeveloper;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // 스프링 부트의 컨트롤러로 만들기 위해 RestController 어노테이션
public class QuizController {
    // 요청 URL과 요청 방식(GET, POST, PUT, DELETE)에 따라 요청을 실행할 메서드를 매핑

    @GetMapping("/quiz")
    public ResponseEntity<String> quiz(@RequestParam("code") int code) { // GET : 정보 요청
        // 값을 responseEntity에 감싸서 리턴 = http응답 헤더 + 바디 값도 주지만,
        // 회원 정보를 요청했을떄 json 형식으로 body에 넣어주거나 상태 코드 또한 이 메소드로 지정가능. = 개발자가 header와 body 값을 직접 지정하고싶을 때
        // <String> 내부적으론 body에 String을 포함한다는 뜻

        // http://localhost:8080/quiz?code=18&name=gd&age=19 이런식으로 하는게 @requestParam
        // 요청에 포함된 param을 url에 위 방식으로 표시? 한다는 뜻 xx 아님.
        // 클라이언트가 URL 쿼리 스트링(?code=1&name=gd)으로 보낸 값을 스프링이 읽어서 메서드의 매개변수(int code)에 바인딩(할당)해 주는 어노테이션입니다.
        // code 가 1이면 create, code가 2면 bad request 등 표시.
        switch (code) {
            case 1:
                return  ResponseEntity.created(null).body("Created!");  // 201, "Created!" 를 ResponseEntity 객체에 넣어서 리턴.
            case 2:
                return  ResponseEntity.badRequest().body("Bad Request!"); //// 400, "Bad Request!" 를 ResponseEntity 객체에 넣어서 리턴.
            default:
                return  ResponseEntity.ok().body("OK!"); // 200, "OK!"를 ResponseEntity 객체에 넣어서 리턴.
        }
    }

    @PostMapping("/quiz") // 글 등록 등
    public ResponseEntity<String> quiz2(@RequestBody Code code) {
        // post로 데이터를 줘야할 때는 body에 데이터를 넣어서 전달 해줘야 함.
        // body에 있는 데이터를 뽑아내는 방법 : requestBody
//        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"+code); 코드에 무슨 값이 온지 테스트
        switch (code.value()) {
            case 1 :
                return  ResponseEntity.status(403).body("Forbidden!"); // status로 코드를 직접 줄 수도 있음
            default:
                return  ResponseEntity.ok().body("OK!");
        }
    }
}

record Code(int value) { // 생성자, getter, setter 등을 자동으로 바꿔줌 클래스 자동생성 느낌.
    // 메소드 정의
}
// 두 개를 postman으로 비교. 검증
// get http://localhost:8080/quiz?code=1
// get http://localhost:8080/quiz?code=2
// get http://localhost:8080/quiz?code=3
// 브라우저로 body 내용은 검증 됨.
// postman으로 param탭과, http 코드 또한 확인 완료

// post방식을 테스트
// http://localhost:8080/quiz 에
// post 방식으로
// body에 json
// {
//    "code": 1
// }
// 이렇게 보내면
//  [nio-8080-exec-1] .w.s.m.s.DefaultHandlerExceptionResolver : Resolved [org.springframework.http.converter.HttpMessageNotReadableException: JSON parse error: Cannot deserialize value of type `int` from Object value (token `JsonToken.START_OBJECT`)]
// 이런 오류 발생
//
// code라는 맴버변수로 클래스?를 만들어서 객체로 변환해야 한다는데
// 그것 때문ㅇ ㅔ클래스 정의를 할 수 없음 -> record라는 문법이 나음.
// recode로 간이 클래스? 를 만들고,

// {
//        "value": 1
// }
// 형식으로 JOSN을 주면 created! 출력.

// 다음 : 테스트코드 생성 후 5장으로 진입