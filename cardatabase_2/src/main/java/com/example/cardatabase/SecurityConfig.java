package com.example.cardatabase;

import com.example.cardatabase.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration  // 스프링 설정 클래스로 등록  // “이 파일은 설정이에요. 메서드 반환값들을 빈으로 써주세요.”
@EnableWebSecurity
/*
@EnableWebSecurity
스프링 시큐리티를 활성화하고, 우리가 작성하는 SecurityFilterChain 등의 설정이 동작하도록 합니다.
→ “보안 필터 체인을 켜고, 아래에서 커스터마이징할게요.”
 */
public class SecurityConfig {
    private final UserDetailsServiceImpl userDetailsService;  // 로그인 username과 DB에 있는 username 비교
    private final AuthenticationFilter authenticationFilter;    // 모든 수신 응답이 거쳐감
    private final AuthEntryPoint exceptionHandler;          // 인증 실패시 코드

    public SecurityConfig(UserDetailsServiceImpl userDetailsService, AuthenticationFilter authenticationFilter, AuthEntryPoint exceptionHandler) {
        this.userDetailsService = userDetailsService;
        this.authenticationFilter = authenticationFilter;  // 모든 요청 전에 JWT 검증을 수행하는 커스텀 필터(보통 OncePerRequestFilter 상속)
        this.exceptionHandler = exceptionHandler;           // 인증 실패/미인증 접근 시 401 응답 등을 만드는 예외 엔트리 포인트
    }
    // 전역 설정 메서드
    // 이거 빈 등록 안 한 이유는 어차피 전역으로 쓰이는 거라 굳이 빈 등록 안 했다고 함  // 나는 return 값이 없어서 빈 등록을 안 했냐고 물어봄
    public void configGlobal (AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }
    /*
        역할 : 인증 매니저가 사용자 조회시 어떤 UserDetialsService + 어떤 PasswordEncoder를 쓸지 연결
     */
// 근데 UserDetailsServiceImpl에서 이미 implement를 받았는 데 여기서 또 정의를 해줘야 하나?
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // 비밀번호를 BCrypt 해시로 저장/검증
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    /*
        AuthenticationConfiguration는 스프링이 이미 알고 있는 UserDetailsService, PasswordEncoder, Provider들을 모아
        최종 **AuthenticationManager**를 만들어 제공합니다.
        컨트롤러에서 authenticationManager.authenticate(...)를 호출할 때 쓰는 핵심 엔진.
     */

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {  // 보안 규칙 핵심 !
        http.csrf(csrf -> csrf.disable())  // CSRF 비활성화. 토큰 기반(API) + 세션 미사용 환경(STATeless)에서는 보통 끕니다.
                .cors(withDefaults())  // CORS활성화 - withDefaults() 정적메서드(정확히는 Customizer.sithDefaults()의 정적 임포트를 써서 "기본 동작으로 설정해"라는 의미
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //세션을 만들지 않음(JWT 방식). 매 요청은 토큰 검증으로만 인증.
                .authorizeHttpRequests(authorizeHttpRequests ->
                        authorizeHttpRequests.requestMatchers(HttpMethod.POST, "/login").permitAll().anyRequest().authenticated())
                // POST /login 은 누구나 접근 허용(permitAll())
                //.anyRequest().authenticated() 그 외 모든 요청은 인증 필요.
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                /*
                    우리 JWT 필터를 스프링의 UsernamePasswordAuthenticationFilter 앞에 끼워 넣습니다.
                    → 컨트롤러에 도달하기 전에 JWT를 검증하고, 검증 성공 시 SecurityContext에 인증을 세팅.
                 */
                .exceptionHandling(exceptionHandling ->    // 인증 실패/미인증 접근 시 어떻게 응답할지 정의.
                        exceptionHandling.authenticationEntryPoint(exceptionHandler));      // → 보통 401과 에러 메시지(JSON)를 내려주는 커스텀 엔트리포인트.
        return http.build();
    }
    /*
        .cors(withDefaults())   여기서의 "기본"은 아래애 정의한 CorsConfigurationSource 빈을 자동으로 사용한다는 뜻.
        정적 임포트 예 : import static org.springframework.security.config.Customizer.withDefaults;
        그래서 Customizer.withDefaults()를 withDefaults()처럼 클래스명 없이 호출할 수 있어요.

     */



    @Bean
    public CorsConfigurationSource corsConfigurationSource() {  // CORS 설정 빈 (프론트와 백엔드 도메인이 다를 때 필수)
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("*"));  // 어떤 origin(도메인)도 허용
        config.setAllowedMethods(Arrays.asList("*"));  // GET/POST/PUT/DELETE 등 전부 허용
        config.setAllowedHeaders(Arrays.asList("*"));  // Authorization, Content-Type 등 전부 허용

        config.setAllowCredentials(false);   // 쿠키/자격증명 미허용
        config.applyPermitDefaultValues();   // ETag, Cache-Control 등 기본 헤더/옵션 추가

        source.registerCorsConfiguration("/**", config);   // 모든 경로에 이 CORS 정책 적용
        return source;
    }  // 실제 운영에선 * 대신 정확한 프론트 도메인으로 제한하세요.
} // http.cors(withDefaults())가 이 빈을 자동으로 찾아 사용합니다. 그래서 클래스 상단에 CorsConfigurationSource를 필드로 주입할 필요가 없어요.