package me.ihchoi.springdeveloper.test;

import java.util.ArrayList;
import java.util.List;

// Stream 테스트용 코드
public class Test {
    public static void main(String[] args) {
        List<Integer> nums = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
//        List<Integer> result = new ArrayList<>();
        // 하고싶은것 : 짝수만 x2배 해서 result에 넣기
//        for (int i : nums) {
//            System.out.println(i);
//            if (i % 2 == 0) {
//                result.add(i * 2);
//            }
//        }
//        System.out.println(result);

        // 이렇게 긴 코드를 stream을 활용하면 한줄에 작성 가능
        // List<Integer> result = nums.stream().filter().map().toList(); 이 기초 코드에서 필터와 맵에 람다 함수만 정의하면 됨
        List<Integer> result = nums.stream().filter(n->n%2==0).map(n->n*2).toList();
        // .filter(n->n%2==0) n이 짝수인것만 필터시켜서 map으로 넘김
        // .map(n->n*2) map에 넘어온 n(짝수)를 모두 *2 처리하고
        // 리스트로 만들어줌,   개발자가 선호하는 이유 : 짧고 명확해서

        // 자바스크립트 n => n*2
        // 파이썬 lambda n
        // 자바 n -> n*2
        // 각자 사용하는 형식은 다른데 기능은 비슷

        System.out.println(result);
    }
}
