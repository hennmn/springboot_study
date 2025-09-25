package com.example.cardatabase.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String brand, model, color, registrationNumber;
    private int modelYear, price;

    @ManyToOne(fetch = FetchType.LAZY)
    /*
        @ManyToOne
        Car 엔티티가 Owner 엔티티와 N:1관계로 매핑된다는 뜻
       즉 여러 대의 자동차(Car)가 한 명의 소유자(Owner)를 가질 수 있음

       fetch = FetchType.LAZY
       LAZY(지연로딩)
       - 기본값은 @ManyToOne에서 EAGER(즉시 로딩) 인데, 보통 실무에서는 성능 때문에 LAZY
       - LAZY: Car 객체를 조회할 때 Owner 정보를 바로 DB에서 가져오자 않고, 실제로 car.getOwner() 를 호출하는 순간 쿼리를 날려서 가져
       - EAGER: Car 객체를 조회하는 순간 Owner 도 즉시 같이 가져온다. (불필요한 조인이 많아져 성능 문제를 일으키기도 함)
     */
    @JoinColumn(name = "owner")
    /*
       Car 테이블에 만들어질 외래 키 컬럼 이름을 지정하는 부분
       즉, Car 테이블에 "ower"라는 이름의 컬럼이 생기고, 이 컬럼이 Owner 엔티티의 기본 키(id라고 가정)에 매핑됨
     */
    private Owner owner;

    public Car(String brand, String model, String color, String registrationNumber, int modelYear, int price, Owner owner) {
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.registrationNumber = registrationNumber;
        this.modelYear = modelYear;
        this.price = price;
        this.owner = owner;
    }


}