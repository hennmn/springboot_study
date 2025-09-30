package com.example.cardatabase.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

/*
    @RepositoryRestResource
    Spring Data REST는 JpaRepository 같은 Repository 인터페이스를 자동으로 스캔해서 REST API 엔드포인트를 만들어 줍니다.
    이렇게 하면 별도 Controller를 작성하지 않아도,
GET /cars : 전체 목록 조회
GET /cars/{id} : 단건 조회
POST /cars : 생성
PUT /cars/{id} : 수정
DELETE /cars/{id} : 삭제
등 CRUD 엔드포인트를 자동으로 제공합니다.

즉, 컨트롤러 없이도 API가 이미 생겨버리는 거예요.

(exported = false) 이걸 붙이면
**“이 Repository를 REST 엔드포인트로 노출하지 않겠다”**라는 뜻
그래서 404가 나는 이유
기존에는 /appUsers나 /appUsers/search/findByUsername 같은 URL로 접근하면 Spring Data REST가 자동으로 JSON을 반환해줬기 때문에 비밀번호 필드까지 보였을 겁니다.
그런데 exported = false를 붙이면 이 URL이 등록조차 되지 않으므로, Postman으로 그 URL을 호출하면 **“존재하지 않는 엔드포인트”**라서 404가 떨어집니다.
즉, 전: Spring Data REST가 엔드포인트 자동 생성 → URL 접근 가능 → JSON(비밀번호까지) 노출
    후: 엔드포인트 자체를 막음 → URL 없음 → 404 Not Found
 */

// 이거 붙이니깐 postman에서 비밀번호 보이던 부분 404에러 뜸  (위에 이유 있음)
@RepositoryRestResource(exported = false)
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
    /*
        Optional<T> 는 값이 존재할 수도, 존재하지 않을 수도 있는 객체를 감싸는 래퍼
        즉, null을 직접 반환하는 대신 Optional 안에 값이 있을 수도 없을 수도 있다라고 명시적으로 표현하는 거예요.
        NullPointerException을 예방하고, 코드의 의도를 명확하게 만들어 줍니다.

        username이 unique이니까 List는 어차피 불필요함 (List는 중복이 될 수도 있다는 전제라서)
     */
}
