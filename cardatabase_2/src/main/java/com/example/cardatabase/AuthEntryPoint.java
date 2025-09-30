package com.example.cardatabase;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {
// 필터나 시큐리티가 “인증 실패”를 감지했을 때 이 commence()가 호출됩니다.
//여기서 JSON으로 에러 메시지를 만들어 내려주거나, 상태 코드를 401/403으로 설정할 수 있습니다.
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);// 인증 관련 요청을 보냈을 때 응답이 올 거니깐 그 상태 코드를 셋팅 해주겠다(권한이 없는 걸로 해주겠다.)
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        PrintWriter writer = response.getWriter();
        writer.println("Error : wrong id or password" + authException.getMessage());
    }
}