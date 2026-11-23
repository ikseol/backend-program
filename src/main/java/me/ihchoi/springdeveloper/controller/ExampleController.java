package me.ihchoi.springdeveloper.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller // RestController 는 JSON만 응답 가능,  HTML을 응답하려면 그냥 @Controller 를 사용
// RestController는 Controller + ResponseBody 의 합성
public class ExampleController {

    @GetMapping("/thymeleaf/example2")
    @ResponseBody // 이런식으로 ResponseBody라고 어노테이션을 붙인다면 HTML이 아닌 일반 문자열로 처리 (@Restcontroller와 동일한 기능)
    public String thymeleaf2() {
        return "example";  // "example" 문자열이 의미하는 것은 ResponseBody가 있기 때문에 그냥 문자열로 처리.
    }

    @GetMapping("/thymeleaf/example")
    public String thymeleaf(Model model) { //  HTML을 리턴하기 위해서는 리턴타입이 무조건 String 이여야 함.
        // 넘겨 줄 정보를 미리 생성
        Person person = new Person();
        person.setName("홍길동");
        person.setAge(23);
        // 운동 독서 영화감상 음악감상 취미를 가지도록 설정
//        List<String> hobbies = new ArrayList<>();
//        hobbies.add("운동");
//        hobbies.add("독서");
//        hobbies.add("영화감상");
//        hobbies.add("음악감상");
//        person.setHobbies(hobbies);
        person.setHobbies(List.of("운동", "독서", "영화감상", "음악감상"));  // List.of 로 간편하게 리스트 생성이 가능함.
        // List.of() 로 리스트를 생성하고 취미를 설정.

        // 블로그 id 추가 (글 조회용)
        person.setBlogId(1L);

        // 동적 HTML을 만들기 위해서 리턴 전에 정보를 html로 넘겨줘야 함.
        // model이라는 객체에 넣어서 전해줘야 함. 모델이라는 객체는 스프링부트에서 매개변수로 받아와야 함.
        // 이렇게 모델에 객체를 넣어주면 타임리프에 자동으로 전달.
        // 이름이 person으로 한 이유 : HTML에 person으로 넣어줬기때문.
        model.addAttribute("person", person);
        // 타임리프 문법에서 person.name 으로 접근하면 자동으로 getName을 호출해서 private인 필드에 접근 가능.

        // 만약 addAttribute 뒤에 person의 값 변경을 진행한다면? -> 그래도 값 변경이 됨, 이유 : 참조값이 전달되기 때문에

        // 날짜 주기
        model.addAttribute("today", LocalDate.now());

        return "example";  // "example" 문자열이 의미하는 것은 기본 경로 resources/tamplates/ 아래에 있는 HTML 파일 이름을 의미함
    }


    @Getter // 접근자 getName, getAge 자동 생성
    @Setter // setName, setAge 자동 생성
    class Person {
        private String name;
        private int age;
        private List<String> hobbies;
        // List는 인터페이스.
        // List를 구현한 구현체가 ArrayList, LinkedList 등
        // List<String> hobbies = new List 인터페이스는 객체 생성 불가능해서 안됨
        // List<String> hobbies = new ArrayList<>(); 혹은
        // List<String> hobbies = new LinkedList<>(); 등은 가능
        // 이런 것으로 다형성을 구현할 수 있다.
        // 예시 List.a 메소드 호출하면 list가 무슨 객체를 가르키는지 보고 ArrayList나 LinkedList의 메소드 실행

        // ArrayList 말고 List로 구현하는 이유 : 확장성을 위해서

        // blogId 맴버변수 추가
        private Long blogId;
    }

}
