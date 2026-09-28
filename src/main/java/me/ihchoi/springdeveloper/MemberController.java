package me.ihchoi.springdeveloper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MemberController {

    @Autowired // 이게 객체를 자동으로 주입(DI)받는 어노테이션, 의존성 주입. memberService에 객체를 자동으로 넣어줌.
    private MemberService memberService;

    // 요청을 받아서, 적절한 비즈니스 로직으로 연결
    // http://localhost:8080/member 요청과 매서드를 연결, url은 바뀔 수 있지만, /member부분을 보고 결정
    // 읽어 오는 요청을 보낼 때 : get 방식으로 요청을 보내야 함
    @GetMapping("/member")
    public List<Member> getAllMember() { // 리턴 타입은? 문자열, 자바객체, html등 할 수 있다.
        // 스프링과 클라이언트는 같은 프로세스가 아님. 서로 다른 프로세스 끼리 정보를 공유 하려면
        // 네트워크를 통하거나 공유 메모리를 통해야 함. 혹은 파일에 저장 해야함.
        // 인터넷으로 전달 하려면? 웹프로그래밍 해야 함.
        // 근데 우리는 리턴만 하면 스프링이 자동으로 데이터를 보내줌.
        // 문제점:자바 객체 리턴시 받는 쪽에서 인식 불가 왜? 클라이언트 프로그램이 무슨 언어일지 감도 안오기 떄문에
        // 객체 그대로 리턴 불가. 정보는 문자열이나 바이너리 스트링으로 전송됨,
        // 자바 객체이면서 인터넷으로 전달 가능하게 하려면 JSON으로 전달해야 함.
        // 인터넷에서 프로그램끼리 통신할 때 요즘엔 JSON, 과거에는 XML을 사용했음.
        // 이것도 자바 객체를 리턴하면 스프링이 그냥 자동으로 JSON으로 변환해서 전달함.
        // 서비스가 컨트롤러에서는 서비스를부를텐데 리스트 맴버 맴버의 리스트를 리턴 받아...

        // 스프링 빈은 new로 생성하지 않는다, DI 즉 주입 받아서 사용한다. 주입 받으려면 빈이여야 함,
        // 빈이란 어노테이션이 붙은 객체 ex> 우린 서비스를 주입받음 서비스 어노테이션이 붙어서 주입받을 수 있다.
        return memberService.getAllMembers();
    }

    // 맴버 정보를 저장하기위해서 매핑
    // 회원 정보를 등록하는 요청
    // localhost:8080/member 요청을  post 방식으로 했을 때 회원 등록을 처리하도록 구현.
    // 결과로서 member 라는 엔터티 객체 리턴 -> 원래는 보안상의 이유로 안됨.
    // -> 보통은 외부에 노출할 정보는 DTO라는 객체로 변환해서 노출
//    @PostMapping("/member")
//    public Member createMember(@RequestBody Member member) {
//        // 비지니스 로직을 호출. -> service에 구현
//        return memberService.saveMembers(member); // 인자로 받은 것을 그대로 saveMembers에 넘겨줌.
//    }
    // RequestBody 어노테이션 : 뒷 부분에 나오는 내용

    // 서버에게 데이터를 보내는 방법
    // 예시) localhost:8080/member?id=1&name=홍길동 == @RequestParam (추후에 설명)
    // -> 요청 보내면서 데이터도 줌, 이 데이터를 백엔드에서 볼 수 있어야 함
    // 예시2)localhost:8080/1/홍길동   == @PathVariable
    // get 방식일 때는 이렇게 사용.

    // http request : 1)http해더 2)http바디 로 구성됨.
    // post 요청일 때는 http바디에 데이터를 보내서 전달. -> 여기 들어있는 데이터를 뽑아낼 때 사용하는 어노테이션이
    // @RequestBody임.

    // 예시 )
    // @RequestBody
    // {"id":1, "name":"홍길동"}와 같이 JSON 형태로 HTTP BODY에 데이터를 넣어주면
    //

    // 새로운 레코드 저장을 위해 save메소드 사용 -> insert 또는 update 기능 수행 가능
    // 받아온 엔터티 객체에 primary키가 없으면 insert
    // 넘어온 엔터티 객체에 primary키가 있으면 update -> 이미 id 4번이 있고 이걸 변경해달라는 거구나~ 하고 업데이트 실행.
    // 그래서 {"id":4, "name":"최길동"} 이렇게 주면 이미 4번이 있는 줄 알고 업데이트 실행.
    // {"name":"최길동"}

    // Member 테이블에 id(P.K)와 name이 있음.
    // 기존에 Autoincrement -> id값 자동 생성 설정 되어있음.
    // id:1, name:고길동
    // id:2, name:김길동

    // 이 상태일때 id:2, name:이길동 으로 요청 하면

    // id:1, name:고길동
    // id:2, name:이길동 으로 수정
    // 이 상태일때 id:4, name:최길동 으로 요청 하면 오류 발생.
    // 4번이 없기 떄문에.


    // id:1, name:고길동
    // id:2, name:이길동
    // id:3, name:최길동
    // 상태일 때 id를 안 주고, name:홍길동 만 전달.
    // 1회 실행 : 1번에 넣어야 함 -> 이미 값이 있음 에러.
    // 2회 실행 : 2번에 넣어야 함 -> 이미 값이 있음 에러.
    // 3회 실행 : 3번에 넣어야 함 -> 이미 값이 있음 에러.
    // 4회 실행 : 4번에 넣어야 함 -> 값이 없어서 정상 처리.

    // id:1, name:고길동
    // id:2, name:이길동
    // id:3, name:최길동
    // id:4, name:홍길동 으로 변함.

    // 즉 post방식으로 요청할떄는 body에 데이터를 넣고,
    // body에서 값을 빼야할 때는 @RequestBody
    // 필드 값만 맵핑이 되면 => 새 객체? 만들어서 넣어주는것도 가능?

    // @responseentity 는 10분 쉬고 진행
    // 데이터와 함께 상태 코드 까지 전달하려면
    @PostMapping("/member")
    public ResponseEntity<Member> createMember(@RequestBody Member member) {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.saveMembers(member));
    }
    // 이렇게 만들어주면 POST방식으로 전달 성공했을때 200번이 아닌 201 created 라고 옴.
    // 왜 사용하는가?
}
