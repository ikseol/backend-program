package me.ihchoi.springdeveloper;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)   // 어노테이션은 생성자.? 와 비슷함.
@AllArgsConstructor
@Getter                                  // 이런 어노테이션은 lombok라이브러리에서 가져옴

@Entity // 엔티티 클래스라고 명시.
public class Member { // 이 아래에 칼럼을 주면 실제 DB랑 연동? 1 대 1 매칭? 된다고 함. 이게 JPA
    // 유저ID와 이름만 만듦
    @Id  // 바로 아래에 있는 줄을 primary key 로 지정하는 어노테이션.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 테이블 생성될때 이게 지정되어 있으면, ID가 자동 생성됨, 오라클의 시퀀스 등도 사용 가능
    @Column(name = "id", updatable = false) // 보통 여기는 변수 안함, 참조무결성제약조건에 따라서 변경 안함. 그래서 업데이트 불가 걸어야 함
    private Long id;
    @Column(nullable = false, name="name") // 이 칼럼이, nullable=false 널값 비허용이라고 지정
    private String name;
// @entity  어노테이션에 테이블 지정해놓은것이 DB에 없으면 자동으로 생성
//    lombok이 이걸 내부적으로 만들어 준다는 뜻.
//    public member(Long id, String name) {
//        this.id = id;
//        this.name = name;
//    }

}
// 이런식으로 적어두면 실행될 떄 DB가 없으면 DB가 생성 + 매칭됨.

// 컴파일 시 생성자 자동으로 만들어주는 툴 있음 : @AllArgsConstructor 라는 어노테이션 lombok이 지원함.

// DB에 접근하기 위해서? get메소드도 만들어야 함.-> 이것도 반복작업, 롬복이 만들어 줌. getter 어노테이션 활용

// 아무 생성자도 안 지정하면 ? 기본 생성자 안 만들어짐, -> 생성자도 자동으로 만들어지는 lombok기능이 있음 ㅋㅋ

// 엔티티 클래스를 만든 이유 : DB에 접근하기 위해서.
// spring에서는 sql은 딱히 안 써도 지장 없음 가끔 어려운 상황에 join 등 사용하긴 함.

// 리포지토리는 자바의 인터페이스로 구현.
// 자바 인터페이스의 특징 : 클래스는 실제 구현, 인터페이스는 메서드 선언만 있고,
//  구현은 이 인터페이스를 implemonts 라는 클래스에서 구현함

// public interface person {
//  public string getname
//  public string getAge();
// person p = new person << 불가 따로 구현 해야 됨
// getter로 구현? 하면 가능함???
// }
//member m = new member(1l, ‘홍길동;);
//
//m.id로 접근 불가
//
//member m2 = new member();
//
//m.getid()메소드로 접근은 가능.



// 하이버네이트? 라는 친구가 리포지토리를 구현해줌