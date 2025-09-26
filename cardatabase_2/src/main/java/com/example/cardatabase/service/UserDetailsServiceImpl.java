package com.example.cardatabase.service;

import com.example.cardatabase.domain.AppUser;
import com.example.cardatabase.domain.AppUserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
// @Component와 똑같이 빈에 등록됨. @Service는 비즈니스 로직 전용
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
                                            // 이 인터페이스는 스프링 시큐리티 - 사용자 인증 시에
                                            // "이 사용자가 존재하는지, 비밀번호가 맞는지" 확인 과정을 담당하는 인터페이스
    private final AppUserRepository userRepository;

    public UserDetailsServiceImpl(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {  // 사용자가 로그인 요청을 보낼 때 입력한 아이디(username)
        Optional<AppUser> user = userRepository.findByUsername(username);

        UserBuilder builder = null;   // 아이디는 중복이 아니니깐 어차피 하나임 그래서 객체 하나만 들고 와지는 거임
        if (user.isPresent()) { // 이하의 실행문이 실행된다면 user에 AppUser 객체가 있다는 의미(검색이 된다면)
            AppUser currentUser = user.get();   // 존재한다면 그 객체를 와서 집어넣겠다.
            builder = User.withUsername(username);  // 정적메서드 호출
            builder.password(currentUser.getPassword());
            builder.roles(currentUser.getRole());
        } else {
            throw new UsernameNotFoundException("User not found");
        }

        return builder.build();
    }
}