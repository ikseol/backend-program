package me.ihchoi.springdeveloper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // 디펜던시 인젝션을 받아서 사용. IoC, 어노테이션이 붙은 객체를 스프링이 인젝션? 시킴
public interface MemberRepository extends JpaRepository<Member, Long> {
    // JPA리포지토리를 상속(extends) 받고, 테이블명(Member)과 Primary key의 데이터 타입을 적어줘야 함.
    // 내부에는 구현할 것이 없음. 얘를 통해서 DB에 접근 가능 왜 가능한가?
    // -> JPA리포지토리가 메소드를 다 가지고 있어서 + 하이버네이터 라는 친구가 인터페이스를 보고 구현을 자동으로 해줌.
    // member.java와 memberRepository까지 작성하면 리포지토리 영속 계층은 완성됨.
}
