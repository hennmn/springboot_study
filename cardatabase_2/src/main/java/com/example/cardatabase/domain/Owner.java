package com.example.cardatabase.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor  // 롬복이 final 필드를 위한 생성자 자동 생성
@NoArgsConstructor(force = true)
@Data
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
// JPA랑 Jackson(JSON 변환기)이 충돌하면서 생기는 전형적인 문제
/*
    JPA 프록시 객체에서 발생하게 되는 특수한 필드 중에 이상의 두 속성은 JPA가 지연로딩(Lazy Loading)을 위해서 생성하는 내부 필드인데,
    얘네가 JSON에 포함되게 되면 오류가 발생할 수 있어서 default 값으로 제외시키는 용도 입니다.
    이게 포함되면 왜 오류가 일어나는 거야?

    JPA랑 Jackson(JSON 변환기)이 충돌하면서 생기는 전형적인 문제
*/


public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)   // AUTO_INCREMENT 랑 같은 뜻
    private Long ownerId;

    @NonNull
    private final String firstName;
    @NonNull
    private final String lastName;

    // 소유자는 다수의 차들을 가질 수 있기 때문에 Collections를 사용
    @JsonIgnoreProperties  // 이 필드는 직렬화가 되지 않는다(JSON화 되지 않는다)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
    private List<Car> cars;
    /*
        cascade = CascadeType.ALL은
        Owner를 저장/수정/삭제할 때 관련된 Car 엔티티에도 같은 동작을 전이하라는 의미

        mappedBy="owner" 속성 설정 :
        Car 클래스 사이에 이 관계의 외래 키인 owner 필드가 존재함을 명시함.
     */

}