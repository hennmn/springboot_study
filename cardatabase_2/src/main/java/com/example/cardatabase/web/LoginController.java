package com.example.cardatabase.web;

import com.example.cardatabase.domain.AccountCredentials;
import com.example.cardatabase.service.JwtService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    private final JwtService jwtService;  // 토큰
    private final AuthenticationManager authenticationManager;    // Spring Security 프레임워크에서 제공하는 인터페이스
    // 사용자가 입력한 아이디(username), 비밀번호(password) 를 검증해서 "정상 사용자냐 아니냐"를 판단하는 역할을 맡아요.

    public LoginController(JwtService jwtService, AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }
    /*
        ReponseEntity는 Spring에서 제공하는 HTTP 응답(Response)을 표현하는 클래스
            상태 코드(200 OK, 404 Not Found등)
            응답 헤더(Authorization, Content-Type등)
            응답 본문(body, JSON 등)
        <?> 는 제네릭 타입 와일드 카드
        -> 어떤 타입이는 리턴할 수 있다는 뜻이고, 여기서는 응답 body를 따로 지정하지 않아서 ?로 둔 거임
        만약 ReponseEntity<String) 이라고 쓰면 body는 문자열만 올 수 있음
        [body에 "사용자 객체"를 담을 수도 있고, "토큰만" 보낼 수도 있고, 심지어 "아무 것도 안 보낼 수도" 있습니다.]

        getToken -> 개발자가 정의한 메서드 이름(로그인 후 토큰을 발급해준다 라는 의미로 네이밍)

        @RequestBody -> HTTP요청의 body(JSON 형식)를 자바 객체로 변환해줍니다.(Spring MVC 애너테이션)
        여기서는 클라이언트가 {"username":"admin", "password":"1234"} 같은 JSON을 보내면
        Spring이 자동으로 AccountCredentials 객체로 변환해서 credentials 매개 변수에 넣어줍니다.

        AccountCredentials credentials -> DTO(데이터 전송 객체) 역할을 하는 클래스입니다.
        즉, 로그인 시 클라이언트가 보낸 JSON 데이터가 여기로 매핑되는 거예요.

        정리 ==>
        1. 클라이언트가 /login으로 JSON 데이터를 보내면(@RequestBody)
        2. 그걸 AccountCredentials 객체로 받고,
        3. 인증 후 JWT를 발급해서
        4. ResponseEntity로 응답을 만들어 반환한다.
     */



    @PostMapping("/login")
    public ResponseEntity<?> getToken(@RequestBody AccountCredentials credentials) {  // 로그인할 때 JSON을 @RequestBody AccountCredentials로 받음
        // 이 응답이 오면
        // 여기에 토큰 생성하고 응답의 Authorization 헤더로 전송해주는 로직 작성할겁니다.
        UsernamePasswordAuthenticationToken creds =   // 사용자가 입력한 아이디/비번을 담은 인증 요청 객체
                new UsernamePasswordAuthenticationToken(credentials.username(), credentials.password()); // AccountCredentials에서 record써서 그럼 record는 getter setter가 자동 생성되기 때문에 get.username()이 아니라 credentials.username()
        // 여기서 꺼낸 값은 UsernamePasswordAuthenticationToken에 담아서 authenticationManager로 전달

        /*
        생성자에 (username, password)를 넣으면 아직 "검증자에 안 된 상태"의 토큰이 만들어짐
        즉, 그냥 "이 사람이 username, password를 제출했다" 라는 정보만 담은 상태.
        이걸 AuthenticationManager에 넘겨주면, 내부적으로 UserDetailsService + PasswordEncoder를 사용해서 진짜 DB에 있는 사용자 정보와 대조해
        비밀번호는 PasswordEncoder(BCrypt 등)로 암호화된 값과 비교.
         */

        Authentication auth = authenticationManager.authenticate(creds);  // 아이디 비번 검증 시작
        // 인증 성공 후 username을 subject로 하는 JWT를 생성함

        //

        // 토큰 생성 - jwts를 지역변수로 보셔도 무방하죠   // 로그인 한 걸로 토큰 생성
        String jwts = jwtService.getToken(auth.getName());  // 사용자 인증 성공 시 → JwtService로 JWT 발급
        // 여기서 다시 필터로감
        //



        // 생성된 토큰으로 응답을 빌드
        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, "Bearer " + jwts)
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Authorization")
                .build();  // .build()는 응답 body가 필요없을 때 사용
                            // HTTP 200 OK + 헤더(Authorization: Bearer xxx) 만 있는 응답



        // AuthEntryPoint 여기에 401에러 뜬 거 있음

        /*
        전체 순서 요약
        클라이언트 → /login (아이디/비번 전달)
        LoginController → AuthenticationManager 호출
        AuthenticationManager → UserDetailsServiceImpl로 사용자 조회
        사용자 인증 성공 시 → JwtService로 JWT 발급
        응답 헤더에 JWT 담아 반환 -> 이걸 필터로

        클라이언트는 이후 요청마다 JWT 포함
        AuthenticationFilter → JwtService.getAuthUser()로 토큰 검증
        검증 성공 시 요청 진행, 실패 시 접근 거부
    */

        /*

         */


    }
}