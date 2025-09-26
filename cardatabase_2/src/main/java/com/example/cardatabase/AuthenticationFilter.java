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

@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {  // 이 클래스는 다른 모든 수신 요청을 인증 처리할 겁니다.
    private final JwtService jwtService;

//    public AuthenticationFilter(JwtService jwtService) {
//        this.jwtService = jwtService;
//    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 토큰 가져오기
        String jws = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(jws != null) {
            // 토큰 검증 및 사용자 가져오기
            String user = jwtService.getAuthUser(request);  // 여기 작성 방식이 매우 유사합니다.
            // 인증
            Authentication authentication = new UsernamePasswordAuthenticationToken(user,null, Collections.emptyList());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);

    }

}
