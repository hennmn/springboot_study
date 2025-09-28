package com.example.cardatabase.domain;

public record AccountCredentials(String username, String password) {
    // AppUser 필드에 있는 username, password 아님(로그인 시 들어오는 username,password)

    /*
        record는 field만을 정의하면, 컴파일러가 필요한 메서드들을 자동으로 생성합니다.
        생성자, getter / setter / equals(), hashCode(), toString()도 자동 생성

        getter / setter를 사용할 때는 getXXX가 아니라 필드명 그대로 메서드 이름이 됨
        ex) credentials.username
     */

    /*
       AccountCredentials 클래스는 record라서 단순히 로그인 시 클라이언트가 보내는 username, password를 담는 DTO역할
       즉, 로그인 요청시 JSON 같은 형태로 들어온 데이터를 받기 위한 그릇

     **로그인 요청 바디(JSON)**를 담기 위한 DTO(Data Transfer Object) 역할

     AccountCredentials -> 로그인 요청을 받을 때 사용(임시 객체, DB와 직접 연결 안됨.
     AppUser -> DB에 저장된 실제 사용자 정보
     */

    // 사용자가 /login 엔드 포인트에 AccountCredentials(username,password) 형태로 JSON 요청을 보냄
    /*
    {
  "username": "admin",
  "password": "admin"
}
     */
}
