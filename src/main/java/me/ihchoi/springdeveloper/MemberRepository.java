package me.ihchoi.springdeveloper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository // 디펜던시 인젝션을 받아서 사용. IoC, 어노테이션이 붙은 객체를 스프링이 인젝션? 시킴
public interface MemberRepository extends JpaRepository<Member, Long> {
    Long id(Long id);
    // JPA리포지토리를 상속(extends) 받고, 테이블명(Member)과 Primary key의 데이터 타입을 적어줘야 함.
    // 내부에는 구현할 것이 없음. 얘를 통해서 DB에 접근 가능 왜 가능한가?
    // -> JPA리포지토리가 메소드를 다 가지고 있어서 + 하이버네이터 라는 친구가 인터페이스를 보고 구현을 자동으로 해줌.
    // member.java와 memberRepository까지 작성하면 리포지토리 영속 계층은 완성됨.

    // 10-15 메모
    // findAll() : select * from member;
    // findById() : select * from member where id=?;
    // deleteAll() : delete from member
    // deleteById() : delete from member where id=?
    // save() : insert into member(id, name) value (?, ?); update member set ,,, where id=?;

    // findByName() : select * from member where name = ?; -> 이것도 불가능 직접 구현 해야함
//    public List<Member> findByName(String name);  // 다음시간(10-22일)에 테스트코드 작성

    // 10-22 추가. 이런 식으로 작성해도 작동함.
    public Optional<Member> findByName(String name);
    // SELECT * FROM MEMBER WHERE NAME = ? LIMIT 1; Top-N 쿼리의 SQL문은 DBMS마다 다름.
    // 리턴 타입과 메소드 선언에 따라 내부적으로 SQL문을 자동으로 만들어 줌.

    // ID를 기반으로 찾기는 있어서 가능, Save도 엔티티 객체 있어서 상관 X

    // 근데 name이나 email로 조회 하려면 JPA가 어떻게 알 수 있는가?
    // 예시 ) select * from member where email=?;
//    public Optional<Member> findByEmail(String email);  // 현재 맴버테이블에 email이 없으니 실행시 오류,

    // 예시 ) select * from member where email=? and name=?;
//    public Optional<Member> findNameandEmail(String name, String email);

    // 1개 혹은 0개가 결과로 나오는 메소드는 Optional객체 사용

    // 등을 찾기 위해서는 직접 구현 해야함.

    // 10-22
    // 모든 테이블에 name이라는 컬럼이 존재하는게 아니기 때문에
    // 따로 메소드를 구현해줘야 함.
    // Member라는 테이블에 name이 있는지, email이 있는지 알 수 없어서 직접 만들어 줘야함.
    // ID는 생성을 강제하기때문에 무조건 있는걸 알 수 있어서 기본 사용 가능
}
