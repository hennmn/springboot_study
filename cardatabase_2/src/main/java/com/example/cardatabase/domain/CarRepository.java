package com.example.cardatabase.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;


// Spring Data REST 기능
// JpaRepository 같은 Repository 인터페이스를 자동으로 REST API 엔드포인트로 노출시켜줍니다.(그래서 Cotroller가 필요없음)
@RepositoryRestResource
public interface CarRepository extends JpaRepository<Car, Long> {
    // 기본키가 Long 타입인 Car 클래스와 연결

    // Car와 관련된 SQL문을 처리
    // 이 인터페이스에 메서드들을 구현하지 않아도 JpaRepository를 extends 받으면 쓸 수 있음
    // .findAll() / .findById() / .deleteById() ...

    /*
       첫 번째 제네릭 파라미터 Car -> 어떤 엔티티를 다룰지 지정

       두 번째 제네릭 파라미터 Long -> 그 엔티티의 기본키 타입
     */

    // 브랜드로 자동차를 검색하는 쿼리 메서드
    List<Car> findByBrand(@Param("brand") String brand);
    /*
       Spring Data JPA에서는 메서드 이름(findByColor)을 보고 자동으로 SQL 쿼리를 생성해요.
       따라서 단순히 String color만 있어도 매개변수 이름과 엔티티 필드 이름이 일치하면 자동 매핑됩니다.
       즉, 이 경우엔 @Param 없어도 잘 동작해요.

       하지만, @Query("SELECT c FROM Car c WHERE c.color = :color")
            List<Car> findByColor(@Param("color") String color);
       여기서 :color → @Param("color")에 바인딩
       즉, 파라미터 이름을 JPQL 쿼리의 변수와 연결해주는 역할이에요.
     */

    // 색상으로 자동차 검색하는 쿼리 메서드
    List<Car> findByColor(@Param("color") String color);





}
