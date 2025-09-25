package com.example.cardatabase.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
    /*
        Optional<T> 는 값이 존재할 수도, 존재하지 않을 수도 있는 객체를 감싸는 래퍼
        즉, null을 직접 반환하는 대신 Optional 안에 값이 있을 수도 없을 수도 있다라고 명시적으로 표현하는 거예요.
        NullPointerException을 예방하고, 코드의 의도를 명확하게 만들어 줍니다.

        username이 unique이니까 List는 어차피 불필요함 (List는 중복이 될 수도 있다는 전제라서)
     */
}
