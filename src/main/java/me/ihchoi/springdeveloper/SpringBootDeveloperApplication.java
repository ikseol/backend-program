package me.ihchoi.springdeveloper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // @어노테이션으로 이 부분이 스프링부트의 시작이라는것을 명시함. 반드시 필요
public class SpringBootDeveloperApplication {
    public static void main(String[] args) {
        // 이 라인이 실행되면, 스프링 부트가 실행됨. 무조건 제일 먼저 작성 해야 함
        SpringApplication.run(SpringBootDeveloperApplication.class, args);// 이 친구가 시작점임.
        // @ 어노테이션으로 이 친구가 시작이라는 것도 무조건 반드시 명시 해야함.
        // 스프링부트애플리캐이션.런 메소드에 생성한 클래스 자체 이름을 매개변수로 주면 됨.

        // 이럼 백엔드 서버 소프트웨어가 만들어짐. 상단 화살표로 실행 가능
    }
}
