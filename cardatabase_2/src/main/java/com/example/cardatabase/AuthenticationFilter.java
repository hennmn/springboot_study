package com.example.cardatabase;

import com.example.cardatabase.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/*
    AuthenticationFilter클래스는 OncePerRequestFilter를 상속함
        - 의미 : 한 HTTP 요청당 한 번만 실행되는 보장(중복 실행 방지)

        OncePerRequestFilter → 같은 HTTP 요청 안에서 중복 실행을 방지.
        로그인 요청, 일반 API 요청 모두 “요청이 들어올 때마다” 실행됨.

        그래서 JWT 기반 인증에서는 매 요청마다 필터가 JWT를 꺼내서 검증하고,
 */

@Component
@RequiredArgsConstructor    // Filter라는 개념은 요청이 Controller까지 가기 전에 또는 응답이 나가기 전에 중간에 거쳐 가는 관문
public class AuthenticationFilter extends OncePerRequestFilter {  // 이 클래스는 다른 모든 수신 요청을 인증 처리할 겁니다.
    private final JwtService jwtService;

//    public AuthenticationFilter(JwtService jwtService) {
//        this.jwtService = jwtService;
//    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 토큰 가져오기  JwtService에 인증이 된 토큰을 가져온
        String jws = request.getHeader(HttpHeaders.AUTHORIZATION);   // 로그인 입력한 아이디 정보가 요청 들어온 헤더에 담겨있음 그거 가지고 // 요청에 Authorization: Bearer <jwt>가 있는 지 확인함
        if(jws != null) {
            // 토큰 검증 + 사용자 식별자 추출
            String user = jwtService.getAuthUser(request);  // 서명/만료를 검증하고, 토큰의 subject(대개 username)를 꺼냄
                                                            // 유효하지 않으면 null을 돌리거나 예외를 던지도록 구현
            // 인증
            Authentication authentication = new UsernamePasswordAuthenticationToken(user,null, Collections.emptyList());

            SecurityContextHolder.getContext().setAuthentication(authentication);  // 인증 객체 정보 저장
        }
        filterChain.doFilter(request, response);  // 여기서 LoginController로 요청이 그대로 전달됨 (@RestController)

        // 헤더에서 토큰 -> 유효성 검증 -> SecurityContext에 인증 심기 -> 다음 필터로
        // 이 작업이 매 요청마다 한 번씩 이루어짐
    }

}
