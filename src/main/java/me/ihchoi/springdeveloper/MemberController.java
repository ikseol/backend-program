package me.ihchoi.springdeveloper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MemberController {

    @Autowired // 이게 객체를 자동으로 주입(DI)받는 어노테이션
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
}
