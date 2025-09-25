//package com.example.cardatabase.web;
//
//import com.example.cardatabase.domain.Car;
//import com.example.cardatabase.domain.CarRepository;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//// 해당 클래스가 RESTful API 웹 서비스 상의 Contoller가 될 것을 지정함
//@RestController
//public class CarController {
//    private final CarRepository carRepository;
//
//    public CarController(CarRepository carRepository) {
//        this.carRepository = carRepository;
//    }
//
//    @GetMapping("/cars")
//    public Iterable<Car> getCars() {
//        // 복잡하게 할 거 없이 자동차들이 저장된 테이블에서 전체 명단을 가지고 올겁니다.
//        return carRepository.findAll();
//    }
//    // GetMapping 했을 때 브라우저 주소창에 URL을 입력하거, 프론트앤드에서 데이터를 조회하려고 GET 방식으로 요청을 보낼 때 사용
//    //(/"cars") 이 메서드가 실행될 URL 경로를 지정하는 부분
//    // Jpa로 사용했을 땐 Iterable 보단 List로 사용하긴 함
//}
