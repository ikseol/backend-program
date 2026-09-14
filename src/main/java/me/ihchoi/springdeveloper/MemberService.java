package me.ihchoi.springdeveloper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
// 이 클래스가 '서비스' 라는 것을
// @springbootapplication 어노테이션에게 알려 줌, 비즈니스 로직을 구현하는 파일

public class MemberService { // 비즈니스 로직을 처리함. = DB에 접근해야 함 = repository로 구현

    @Autowired // 이 어노테이션을 붙이면 MemberR..y 라는 인터페이스 객체는 스프링 프레임워크에 등록 + 자동 주입 => JPA에서 상속받아서 사용가능
    private MemberRepository memberRepository; // 변수 선언 -> 선언한 변수에 스프링이 자동으로 하이버레이트 객체를 주입
    // @Autowired 주입 시키기 : 디펜던시 인젝션, DI, 의존성 주입  -> IoC라는 개념을 구현하는 방법
    // 할수있는 이유 : @Re,,tory어노테이션이 붙여있어서 + JPA 상속 받아서 + 스프링 빈으로 등록 되어있어서
    // 스프링 빈으로 등록되는 클래스의 객체만 자동으로 의존성 주입 가능.
    // -> 서비스 레스트컨트롤러 리포지토리 등 되어있는 친구들만 DI 가능??

    // 맴버 테이블에 있는 모든 레코드드를 읽어서 반환    <<- 구현하고자 하는 비즈니스 로직
    // 사실 바로 요청해도 되는데, 현업에서의 로직은 복잡하기 때문에

    // 맴버 테이블의 레코드 : 자바 객체가 아닌데, 객체로 만들어야 함. -> 리스트에 넣어야 함,
    // List<테이블명>은 임포트를 해야 사용 가능.
    public List<Member> getAllMembers(){
        // 맴버의 모든 레코드 읽기 => 리포지토리가 해야 함.
        // 어노테이션이 붙은 클래스 는 우리가 생성 안하고 스프링부트가 생성+나에게 주입 => DI 의존성 주입 IoC(제어의 역전) 구현
        return memberRepository.findAll(); // 내부적으로 DB서버에 sql문 select * from member 실행
    }
}