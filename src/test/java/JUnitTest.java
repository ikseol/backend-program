import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.*;

public class JUnitTest {

    @DisplayName("1 + 2 는 3이다.")
    @Test
    public void JUnitTest1(){
        int a=1;
        int b=2;
        int sum=3;

        int result=a+b;
        System.out.println("1 + 2 는 3이다.");
        Assertions.assertEquals(sum,result);
    }

    @DisplayName("1 + 3 은 3이다.")
    @Test
    public void JUnitTest2(){
        int a=1;
        int b=3;
        int result=a+b;
        System.out.println("1 + 3 은 3이다.");
        Assertions.assertEquals(3,result);
    }

    @BeforeEach // 테스트코드 실행 전에 매번 실행됨
    public void prepare() {
        System.out.println("준비");
    }

    @AfterEach // 테스트코드 실행 후에 매번 실행됨
    public void cleanup() {
        System.out.println("설거지");
    }

    @BeforeAll // 모든 테스트코드 실행 전 1회, 정적 메소드(static)이 아니라면 실행 불가
    public static void beforeAll() {
        System.out.println("최초 준비");
    }
    @AfterAll // 모든 테스트코드 실행 후 1회 정적 메소드(static)이 아니라면 실행 불가
    public static void afterAll() {
        System.out.println("최종 마무리");
    }
}

// 자바 인스턴스 : 생성된 모든 객체
// Student S = new Student();
// S가 가지고 있는게 인스턴스 라고 하기도 함.
// 만약
// Class Student {
//      public void {
//          print(){
//                  구현내용
//          }
//      }
// 인스턴스 메소드는 인스턴스가 생성 되어야 사용 가능.

// 인스턴스 메소드기 떄문에
// S.print()로 사용해야 사용 가능.
// Student.print() 로는 사용 불가. (클래스 메소드)

// public static void out() 이라는 정적 메소드라면
// Studnet.out()으로 사용 가능

// 이런 식으로 @beforeAll, @AfterAll 이라면 정적 메소드로 바꿔 줘야함.

// 앞으로 junit으로 단위 테스트 진행.

// 패턴 3단계 준비 실행 검증.
// 검증할때 기존에 junit으로 Assertions.assertEquals(3,result); 이런 식으로 사용 했는데
// 어썰트J를 사용하는 것이 더 직관적
// Assertions.assertEquals(기대값, 비교값) vs. assertThat(비교값).isEqualTo(기대값)
