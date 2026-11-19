package me.ihchoi.springdeveloper.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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

        // 동적 HTML을 만들기 위해서 리턴 전에 정보를 html로 넘겨줘야 함.
        // model이라는 객체에 넣어서 전해줘야 함. 모델이라는 객체는 스프링부트에서 매개변수로 받아와야 함.
        // 이렇게 모델에 객체를 넣어주면 타임리프에 자동으로 전달.
        // 이름이 person으로 한 이유 : HTML에 person으로 넣어줬기때문.
        model.addAttribute("person", person);
        // 타임리프 문법에서 person.name 으로 접근하면 자동으로 getName을 호출해서 private인 필드에 접근 가능.

        return "example";  // "example" 문자열이 의미하는 것은 기본 경로 resources/tamplates/ 아래에 있는 HTML 파일 이름을 의미함
    }


    @Getter // 접근자 getName, getAge 자동 생성
    @Setter // setName, setAge 자동 생성
    class Person {
        private String name;
        private int age;
    }

}
