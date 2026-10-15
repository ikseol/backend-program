package me.ihchoi.springdeveloper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
// 리포지토리를 테스트하기 위해서 DataJpaTest 필요
// + 테스트 코드를 실행하고 나서 DB에 삽입, 수정, 삭제 된 내용을 DB에서 롤백 시켜줌
    // 뒤에 실행된 테스트가 앞의 테스트의 영향을 안 받게 하기 위해서 롤백함

// 컨트롤러나 서비스 등은 지금 테스트에 필요 없기 때문에 DataJpaTest를 사용
class MemberRepositoryTest {

    // 지금 하려는 것 : 리포지토리 객체 => 사용을 위해 맴버 변수 선언 필요 + 맴버변수=스프링빈 => 의존성 주입 Autowired
    @Autowired
    private MemberRepository memberRepository;

    @Sql("/insert-members.sql") // 테스트 실행 전 해당하는 sql 파일을 실행시켜주는 어노테이션
    @DisplayName("맴버 테이블의 모든 레코드 가져오기")
    @Test
    public void getAllMembers() {
        // given

        // when
        final List<Member> result = memberRepository.findAll();
        // findAll() -> List<Member>타입으로 리턴

        // then
        // 검증법 : 리턴된 List에 원소가 3개 있나?
        assertThat(result.size()).isEqualTo(3);
    }

    // 실행하다 멈추는 지점(브레이크 포인트)도 설정 가능. 그 상태에서 메모리 상의 변수를 보는 기능도 있음.
    // 왼쪽에 줄번호를 클릭해서 빨간색 변경 -> 디버그로 실행하면 됨.

    @DisplayName("member테이블에서 id가 2인 레코드를 가져오기")
    @Test
    @Sql("/insert-members.sql") // 테스트 실행 전 해당하는 sql 파일을 실행시켜주는 어노테이션
    public void getMemberById2() {  // 이 시점에서 DB에 6개 데이터가 들어가나? -> 아니다. 자동 롤백 된다.
        // given

        // when
        final Optional<Member> member = memberRepository.findById(2L);
        // 인자가 Long인 이유 : 맴버엔티티에 ID를 Long타입으로 선언했기 때문
        // 2L로 적어야 하는 이유 : 자바에서 리터럴은 기본적으로 int형이기 때문에 강제 형변환으로 L을 붙여야 함.
        // 리턴 타입 : Optional 객체 안에 Member객체가 들어감.
        // 위 코드로 검색했을때 id가 2번인 친구가 다 나옴 -> 근데 id는 primary 키 이기 때문에 -> 0개 혹은 1개가 나와서
        // 옵셔널 객체로 묶어야 함.

        Member result = null; // 변수 스코프. 미리 맴버타입 변수 선언.
        if (member.isPresent()) { // 옵셔널 객체에 있는지 검사
            result = member.get(); // 있으면 맴버 타입으로
        }

        // final Member member = memberRepository.findById(2L).get(); 간단하게 이렇게도 받을 수 있음.


        // then
        assertThat(result.getName()).isEqualTo("B");
    }


}

