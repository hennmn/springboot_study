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

    /*
       HttpServletRequest : 들어온 요청 정보 (헤더, 파라미터, 바디, URL 등)
       HttpServletResponse : 응답 정보 (상태코드, 헤더, 바디 등)
       FilterChain : “다음 필터” 또는 “다음 단계(컨트롤러)”로 요청을 넘길 수 있게 해주는 객체
     */


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 토큰 가져오기  JwtService에 인증이 된 토큰을 가져온다
        String jws = request.getHeader(HttpHeaders.AUTHORIZATION);   // 들어온 HTTP 요청의 헤더에서 Authorization 값을 꺼냅니다.
        // 클라이언트가 보낸 헤더 예 : Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
        if(jws != null) {
            // 토큰 검증 + 사용자 식별자 추출
            String user = jwtService.getAuthUser(request);  // 서명/만료를 검증하고, 토큰의 subject(대개 username)를 꺼냄
                                                            // 유효하지 않으면 null을 돌리거나 예외를 던지도록 구현
            //jwtService.getAuthUser() 안에서 JWT를 파싱할 때 예외가 발생하면(만료, 서명 오류 등)
            // Spring Security 필터 체인은 이 예외를 최상위에 잡아서 ExceptionTranslationFilter를 거치게 합니다.
            /*
            ExceptionTranslationFilter는 이런 식으로 동작합니다(실제 코드 일부):
            try {
    filterChain.doFilter(request, response);
} catch (AuthenticationException ex) {
    SecurityContextHolder.clearContext();
    authenticationEntryPoint.commence(request, response, ex);
} catch (AccessDeniedException ex) {
    // ...
}
즉, 체인 안에서 발생한 예외가
AuthenticationException이면 → AuthEntryPoint.commence() 호출
AccessDeniedException이면 → AccessDeniedHandler.handle()
또한 우리가 필터에서 직접 던지지 않더라도,
jwtService가 예외를 던짐
스프링 시큐리티 필터들이 catch
AuthenticationException으로 변환
AuthEntryPoint 호출

             */
            // 인증
            Authentication authentication = new UsernamePasswordAuthenticationToken(user,null, Collections.emptyList());

            SecurityContextHolder.getContext().setAuthentication(authentication);  // 현재 요청의 시큐리티 컨텍스트에 이 인증 객체를 저장.
            /*
                → 이걸 해줘야 컨트롤러나 서비스 단에서 @AuthenticationPrincipal이나
                SecurityContextHolder.getContext().getAuthentication()으로 현재 사용자 정보를 꺼낼 수 있습니다.
             */
        }
        filterChain.doFilter(request, response);  // 여기서 LoginController로 요청이 그대로 전달됨 (@RestController)

        //이렇게 하면 컨트롤러는 “이미 인증된 사용자 정보”를 바로 활용할 수 있고,
        // 인증 실패 시에는 컨트롤러까지 가지 않고 AuthEntryPoint(예외 처리)에서 401/403을 내려보낼 수 있습니다.
    }

}
