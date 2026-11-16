package me.ihchoi.springdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.ihchoi.springdeveloper.dao.Article;
import me.ihchoi.springdeveloper.dto.ArticleCreationRequest;
import me.ihchoi.springdeveloper.dto.ArticleResponse;
import me.ihchoi.springdeveloper.dto.ArticleUpdateRequest;
import me.ihchoi.springdeveloper.repository.BlogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor //  @Autowired 안쓰고 의존성 주입을 하기 위해 생성자 호출?
@Service // 리포지토리와는 다르게 상속을 안 받기 떄문에 서비스라고 명시해줘야함.
public class BlogService {
     // 의존성 주입 : @Autowired를 안 쓰고 생성자로 받을 수도 있음, 생성자를 통해서 하려면 final이 붙어야 함.
    private final BlogRepository blogRepository;

    // 글 저장하는 서비스
    public Article save(ArticleCreationRequest articleCreationRequest) {
        return  blogRepository.save(articleCreationRequest.toEntity());
    }

    // 11-05 글 목록을 조회하는 서비스
    // 모든 글을 리턴해야 하기 때문에 List<Article>이 리턴되어야 함.
    public List<Article> findAll() {
        return blogRepository.findAll(); // 블로그 리포지토리에서 모든 정보를 find한 후 리턴
    }


    // 11-09 findById 메소드로 글 하나만 불러오기
    public Article findById(Long id) {
        // 리포지토리에 요청해서 리포지토리에 상속을 받았기 때문에 id로 레코드를 가져오는 건 미리 구현되어있음.
        // return blogRepository.findById(id).orElse(null); 주석처리

        return blogRepository.findById(id).get();
        // 근데 PK로 레코드를 찾음 -> PK는 단 하나 -> 1개 혹은 0개 0-> optional객체로 article을 싸서 구현
        // 안전하게 하려면 else일떄 null을 채우게 처리, 지금은 그냥 optional객체를 바로 get 해서 article을 반환할것.
    }

    // 11-09 findById 메소드로 글 하나만 불러오기2 (Optional<Article>을 그대로 반환)
    public Optional<Article> findById2(Long id) {
        return blogRepository.findById(id);
    }

    // 11-09 특정 id를 가지는 글 하나를 삭제하는 메소드
    public void deleteById(Long id) {
        blogRepository.deleteById(id);
    }


    // 서비스에 업데이트 아티클 구현
    // 리턴타입 아티클 , 퍼블릭으로 구현
    // 매개변수 Long id,  ArticleUpdateRequest req
    // 구현법 : 방법 1 -> ArticleUpdateRequest를 아티클 객체로 변환 빌더패턴 사용
    // Article.bulider().title(getTitle())
    //                  .content(req.getContent()); 이런식으로 구현

    // 빌더패턴 req.getTitle로 접근하는 이유 -> private로 선언되어있음. 정보은닉 때문에 퍼블릭으로 구현 안함.
    // get 메소드는 public String getTitle() {return title} 이렇게 선언해야 함.
    // 하지만 lombok이 있기 때문에 @Getter 어노테이션 으로 자동 구현 가능

    // blogRepository.save(article)을 하면 수정이 아니라, 새로운 정보가 들어감.
    // 업데이트를 하기 위해서는 id를 줘야함. id를 주기 위해서는 Article객체에 setter도 구현해야함. setter도 어노테이션으로 가능

    // id는 article객체 안에 있음.
    // save할떄 id(pk)를 같이 주면 새로운 정보 등록이 아닌 update를 실행함.
    // save할떄 id(pk)가 없으면 새로운 정보를 등록 진행. insert 수행

    // 그래서 빌더패턴으로 req를 article객체로 바꾸고
    // 변환된 article객체의 id 필드를 setter 메소드를 이용해 지정
    // id가 지정된 article 객체를 save 메소드에 전달하면 동일한 id를 가진 레코드의 정보를 변경

    // 하지만 우리는 방법1로 구현 안 함.

    // 변경감지를 이용해 구현 진행

    @Transactional
    public Article update(Long id, ArticleUpdateRequest req) {
        // 기존에 있는 레코드를 변경 -> 기존에 저정된 레코드를 가져옴.
        // findById 리턴타입 : Optional<Article> 바로 get 진행해 Article 엔터티 객체로 리턴
        Article article = blogRepository.findById(id).get();
        // 이렇게 DB에서 가져오면 영속성 컨텍스트의 1차 캐시에 넣어두고 반환해줌. (응용프로그램의 성능을 올리기 위함)

        // article.update(req.getTitle(), req.getContent()) 현재 상태로는 불가능함
        // 이유 : Article에 업데이트 메소드가 없기 때문
        // Article에
        // public void update(String title, String content) {
        //      this.title =title;
        //      this.content = content;
        // } 이런 update메소드를 추가하면 호출 가능

        // blogRepository.save(article) 하면 업데이트 진행 (id, title, content가 모두 있기때문)
        // 하지만 이걸 안 해도 더티체킹에 의해서 업데이트를 진행
        // 메소드의 종료 = 트랜잭션의 종료
        // 메소드가 종료되면 1차캐시에 있는 article과 수정한 article을 비교하고 다르면 자동 업데이트 진행
        // 기본적으로 JDBC에서 문장 하나 하나가 트랜잭션임
        // 우리는 값 가져오기, 변경하기 등을 하나의 트랜잭션으로 묶어야 함.
        // 트랜잭션 :더이상 쪼개질 수 없는 논리적인 작업의 단위
        // 트랜잭션의 작업 100개가 다 성공해야 커밋 하나라도 실패하면 롤백, 전문적으로 이야기 시 ACID 성질을 만족하는 연산 집합

        // 변경 감지 만으로 값 수정을 위해서 @Transactional을 메소드 위에 추가해야함.

        // @Transactional으로 묶으면 메소드가 하나의 트랜잭션 단위가 됨
        // @Transactional으로 묶지 않으면 한 문장 한 문장이 트랜잭션 단위임.

        article.update(req.getTitle(), req.getContent());

        return article;
    }

    // 업데이트 구현 (save 사용) 11월 16일
//    public Article updateArticle(Long id, ArticleUpdateRequest req) {
//        // db반영을 위해서는?
//        // blogRepository.save(req); // 이렇게 주면 dto와 타입이 다름 블로그 리포지토리는 엔터티 객체를 받아가야 함
//
//        // blogRepository.save(new Article(req.getTitle(), req.getContent()));
//        // 이렇게 하면 새로 만든 객체의 id값이 부여되지 않아서 새로운 row를 만들게 됨. (insert)
//
//        // 엔터티 객체에 Id 유무에 따라 내부적으로 update, insert 가 바뀜
//
//        // id를 이용해 기존의 객체를 가져옴
//        // blogRepository.findById(id); // 1혹은 0개 리턴하기 때문에 옵셔널 객체를 리턴 (객체가 최대 1개인 경우)
//        // 옵셔널을 리턴하기 때문에 .get() 이나 .orElseThrow()를 사용해서 꺼내는 과정이 필요함.
//        Article article = blogRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Article Not Found"));
//        // 람다식을 이용해 예외를 반환  // 만약 4번 글이 없으면? orElseThrow()로 예외를 반환
//
//        // 이렇게 갖고온 아티클을 변경
//        // 기본적으로 변경하는것을 구현해두지 않았으니
//        // Article의 맴버를 퍼블릭으로 변환하거나, 세터 함수를 추가하거나, update()메소드를 정의해서 변경 가능
//        // req의 타이틀과 컨텐츠를 가져와서 (getTitle, getContent) DB에서 가져온 article의 업데이트 메소드 실행
//        article.update(req.getTitle(), req.getContent());
//
//        // 이런 변경은 save 메소드 를 사용해서 저장도 가능함.
//        return blogRepository.save(article);
//
//        // 여기 주석은 노션에 메모 옮김 11-16
//
//
//    }
    // 영속성 컨텍스트를 사용한 저장 (11월 16일)
    @Transactional
    public Article updateArticle(Long id, ArticleUpdateRequest req) {
        Article article = blogRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Article Not Found"));
        article.update(req.getTitle(), req.getContent());
        return article;
    }
    // 앞으로는 위의 방식으로 update를 진행.
}
