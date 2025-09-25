package com.example.cardatabase;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
    @Configuration이 붙은 클래스는 스프링 빈(bean)을 정의하는 설정 클래스입니다.
    쉽게 말하면 “이 클래스 안에는 스프링 컨테이너가 관리할 객체들을 만들어 놓은 공장(factory)이 있다”라는 뜻이에요.
    "설정용" 이라는 의미가 핵심이고, 실제 로직 처리용은 아님
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI carDatabaseOpenApi() {  // 반환 타입: OpenAPI (Swagger/OpenAPI 문서 객체)
        // Swagger/OpenAPI 설정을 커스터마이징 하기 위해
        //스프링 부트가 자동 생성하는 Swagger 설정 외에, 제목, 설명, 버전 등을 직접 지정하기 위해

        return new OpenAPI()    // io.swagger.v3 어쩌구 에서 나오는 거 // 새 객체를 생성한 이유 : "우리 API문서 정보를 담을 컨테이너가 필요해서"
                .info(new Info()       // OpenAPI.info() → API 문서의 기본 정보(제목, 설명, 버전 등)를 설정
                        .title("Car REST API")    // new Info() 내에서 객체 생성을 빌더 패턴으로 함
                        .description("My car Stock")
                        .version("1.0"));
    }   // OpenAPI 객체 안에 Info 객체를 넣어서 API 문서의 메타데이터를 정의한 것
    /*
        @Configuration → 스프링이 AppConfig 클래스를 읽어서 설정용 클래스로 인식
        @Bean → myService()가 반환하는 객체를 스프링 빈으로 등록
     */


    /*
        스프링 부트가 애플리케이션 시작 시 @Configuration 클래스 읽음
        @Bean carDatabaseOpenApi() 호출 → 반환된 OpenAPI 객체를 스프링 컨테이너에 등록
        Swagger UI 또는 OpenAPI 문서 생성 시, 스프링 컨테이너에서 OpenAPI 빈을 가져와 문서에 사용
     */


}
