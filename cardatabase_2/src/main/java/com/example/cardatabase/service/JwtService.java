package com.example.cardatabase.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
// 공통 기능 클래스에 사용
@Component   // @Service와 역할상 유사함 // @Component가 붙어있는 클래스는 빈에 등록됨
public class JwtService {
    // 1일(밀리초). 실제 운영시에는 더 짧은 게 낫습니다.
    static final long EXPIRATIONTIME = 86400000;   // 만료시간 설정 // 여기는 24시간으로 잡아 놓은 거
    static final String PREFIX = "Bearer"; //Authoriztion 헤더 접두사(보통 "Bearer ")

    // 비밀 키 생성.(토큰의 서명을 만들 때 사용하는 비밀키!!)
    static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // HS256는 대칭 키(비밀키) 런타임에 생성
    // 이건 매번 애플리케이션 시작 시 생성하면, 앱을 재시작하면 이전에 발급된 토큰은 검증 불가해짐(개선할 여지가 있음) -> 고정키 필요


    // 서명이 이루어진 JWT 토큰을 생성  // Login하는 데이터를
    public String getToken(String username) {  // username은 AppUser에서 로그인 아이디로 필드 선언
        String token = Jwts.builder()  // 여기서 기본적으로 헤더가 자동으로 만들어짐
                .setSubject(username)  // 이 토큰의 주인  // 페이로드(sub(claim)을 설정하는 부분
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))  // 만료시간 설정
                .signWith(key)   // 서명       // 18번 라인에 생성한 비밀 키로 생성(검증을 위해 같은 키 필요)
                .compact();  // 이게 이 세 부분을 "헤더.페이로드.서명" 형태의 문자열로 만들어서 반환
        return token;
    }

    // 요청(Request)의 HTTP 요청의 Authorization 헤더에서 토큰을 가져온 뒤에 그 토큰 내부를 확인하고,
    // username을 가지고 오는 부분입니다.
    public String getAuthUser(HttpServletRequest request) {  // 사용자가 보낸 HTTP 요청
        String token = request.getHeader(   // 이 클래스의 객체가 정확히 뭔지는 모르겠지만 method명을 봤을 때 Header를 가지고 온다는 것은 알 수 있죠.
                // 여기 Header는 postman에서 볼 수 있는 headers에 해당합니다.
                HttpHeaders.AUTHORIZATION  // 토큰에서 PREFIX를 제거하고 파싱   // 여기서 다시 Filter
        );
        if(token != null) {
            String user = Jwts.parser()  // 실제 헤더와 페이로드를 읽으려면 이게 필요함
                    .setSigningKey(key) // 서명 검증 키를 설정
                    .build()   // 이걸 호출하면 실제로 JWT.parser 를 생성
                    .parseClaimsJws(token.replace(PREFIX, ""))
                    // parseClaimsJws() 에 헤더,페이로드,서명이 모두 들어있고 여기서 토큰 내부를 실제로 읽고 서명까지 검증함
                    .getBody()
                    // 위의 검증 결과에서 페이로드만 가져오는 메서드(페이로드 JSON을 가져옴)   // 요청이 들어오면 이렇게 되는 건가..?
                    // 예: { "sub": "username", "exp": 1234567890 }
                    .getSubject();
            /*
            페이로드에서 "sub"(subject) 값만 꺼내는 것
            우리가 토큰 만들 때 setSubject(username)로 설정했던 값이 여기서 나옵니다.
            결국 마지막 user 변수에는 JWT에 담긴 username이 들어가게 되는 거예요.
             */

            if(user != null) {
                return user;
            }
        }
        return null;
    }
}