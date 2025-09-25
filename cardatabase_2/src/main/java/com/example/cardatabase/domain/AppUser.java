package com.example.cardatabase.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;


@Entity
@NoArgsConstructor(force = true)   // Jpa 때문에 기본생성자는 무조건 필요한데 @RequiredArgsConstructor 때문에 에러가 나서
                                    // (force = true)를 추가해놓은 거임
@RequiredArgsConstructor  // final 이나 NonNull 필드를 파라미터로 받는 생성자를 만들어 줌
@Getter
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false, unique = true)  // 아이디는 중복 x
    // 로그인 아이디 같은 거
    private final String username;
    // final 은 반드시 값이 한 번은 초기화 되어야 한다 그래서 롬복은 이 필드가 생성자에서 반드시
    // 초기화  되도록 생성자 파라미터에 포함시켜 준다.

    @Column(nullable = false)
    private final String password;

    @Column(nullable = false)
    private final String role;

    // Owner 에는 final 대신 nonnull로 지정


}
