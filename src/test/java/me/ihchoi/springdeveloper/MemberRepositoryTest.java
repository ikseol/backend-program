package me.ihchoi.springdeveloper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
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
        final List<Member> result = memberRepository.findAll(); // select * from member
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
        final Optional<Member> member = memberRepository.findById(2L); // select * from member where id = 2;
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

    // 10-22 추가 작성
    @DisplayName("member테이블에서 name이 B인 레코드 가져오기")
    @Test
    @Sql("/insert-members.sql") // 테스트 실행 전 해당하는 sql 파일을 실행시켜주는 어노테이션
    public void getMemberByName() {
        // given

        // when
//        final List<Member> member = memberRepository.findByName("B"); // select * from member where name = 'B';
        // 이렇게 검색 시 : 제약 상황 고려 -> 같은 '이름' 레코드가 여러개가 나올 수 있음.
        // ID가 같은 레코드 : primary key -> 오직 1개만 가능 -> Optional객체를 받고, 거기서 get하여서 꺼내야 함.
        // 이름이 같은 레코드 : 0개 혹은 여러개 -> List객체가 반환 되어야 함.

        // list가 아닌 optional로 받는 코드.
        // final Optional<Member> member2 = memberRepository.findByName2("B");
        Member member = memberRepository.findByName("B").get();


        // then
//        assertThat(member.size()).isEqualTo(1); // 반환된 리스트의 사이즈가 1인가?
//        assertThat(member.get(0).getId()).isEqualTo(2); // 반환된 리스트의 0번 원소의 id가 2인가?

        // optional로 받은 코드 검증
        assertThat(member.getId()).isEqualTo(2L);
    }

    // 10분 쉬었다가 save 등 다른 메소드 테스트 진행 (save saveAll deleteById deleteALl 등)

    @DisplayName("하나의 맴버를 저장 ")
    @Test
    public void saveMember() {
        //given
//        Member member = new Member(1L, "A"); // id가 가진 객체가 save에 들어오면 update진행.
        Member member = new Member("A"); // 이름만 받는 생성자로 생성 해줘야 함.

        //when
        memberRepository.save(member);

        //then
        List<Member> result = memberRepository.findAll();
        assertThat(result.get(0).getId()).isEqualTo(1); //
        // 첫번째 원소 .get(0)
        // 의 Id getId()가
        // 1인가?isEqualTo(1)
    }

    @DisplayName("3개의 Member 객체를 DB에 저장하기")
    @Test
    public void saveMembers() {
        //given

        // 여러개의 객체를 saveAll에 주려면 리스트에 넣어서 전달하면 됨.

        // List<Member> members = new ArrayList<Member>();
        List<Member> members = new ArrayList<>(); // 빈 배열 members를 선언.
        // 둘 중 어느 방식으로 선언 하든 상관 없음

        // for문으로 전달해도 됨.
        // for (int i = 1; i <= 3; i++) {
        //     members.add(new Member("A" + i)); // A1, A2, A3
        // }

        members = List.of(new Member("A1"), new Member("A2"), new Member("A3"));

        //when
        memberRepository.saveAll(members);

        //then
//        List<Member> result = memberRepository.findAll();
//        assertThat(result.size()).isEqualTo(3);

        assertThat(memberRepository.findAll().size()).isEqualTo(3);

    }

    // 더티 체킹 업데이트
    @DisplayName("ID가 2인 레코드 삭제하기 테스트")
    @Test
    @Sql("/insert-members.sql")
    public void deleteMemberById() {
        // 준비 @Sql로 대신

        // 실행
        memberRepository.deleteById(2L);
        // id가 2인 레코드를 삭제.

        // 검증
//        assertThat(memberRepository.findAll().size()).isEqualTo(2); 맴버 리포지토리의 사이즈가 2인가?

        Optional<Member> member = memberRepository.findById(2L); // 2번 레코드가 있는지 find
        assertThat(member.isEmpty()).isTrue();
        // Optional<Member> member의 내부가 비어있니?
        // 비어있으면 True -> 통과
        // 안 비어있으면 False -> 통과 못함.

        // assertThat(member.isEmpty()).isEqualTo(true);
        // assertThat(member).isEmpty();
        // 위 두개의 방법으로도 검증 가능.
    }

    @DisplayName("모든 레코드 삭제하기")
    @Test
    @Sql("/insert-members.sql")
    public void deleteMember() {
        // 준비 @Sql 어노테이션으로 대체

        // 실행
        memberRepository.deleteAll();

        // 검증
        List<Member> result = memberRepository.findAll();
        assertThat(result.size()).isEqualTo(0);
    }

    // 내부적으로 사용되는 영속성 컨텍스트의 변경감지(Dirty-check) 기능을 이용한 레코드 변경
    @DisplayName("변경 감지 기능을 이용해 id가 2인 레코드의 name을 홍길동으로 변경하기")
    @Test
    @Sql("/insert-members.sql") // DB에 이름이 ABC인 레코드 삽입됨
    public void updateMember() {
        // 준비  -  레코드 변경을 위해서 DB의 내용을 가져옴.
        Member member = memberRepository.findById(2L).get();
        // 가져오면 내부적으로 영속성 컨텍스트 1차 캐시에서 id 2번이 있는지 확인
        // 확인 결과 없으면 가져와서 1차캐시에 넣어둠.

        // 실행  -  이름을 변경해주는 메소드를 실행.  (없으면 Member에 새로 구현)
        member.setName("홍길동");
        // 검증
        member = memberRepository.findById(2L).get();
        assertThat(member.getName()).isEqualTo("홍길동");

        // DB에 save메소드로 반영을 안 했는데 더티체크로 반영이 되서 검증이 통과.
    }
}

