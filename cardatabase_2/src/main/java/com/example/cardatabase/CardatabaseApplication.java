package com.example.cardatabase;

import com.example.cardatabase.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

/*
	@SpringBootApplication
	이 클래스가 애플리케이션 시작 클래스가 되고,
	main()에서 실행하면 스프링 부트가 자동 설정 + 빈 스캔 + 서버 실행까지 해줍니다.
 */
@SpringBootApplication
public class CardatabaseApplication implements CommandLineRunner {
	private static final Logger logger = LoggerFactory.getLogger(
			CardatabaseApplication.class
	);

	// 여기에 생성자 주입 부분 적겠습니다(그리고 .md파일로 옮기는 것도 함께 하겠습니다)
	// 이 필드와 아래 생성자 부분을 정의해야 .findById() 같은 메서드들 사용 가능
	private final CarRepository repository;
	private final OwnerRepository ownerRepository;
	private final AppUserRepository userRepository;

	public CardatabaseApplication(CarRepository repository, OwnerRepository ownerRepository, AppUserRepository userRepository) {
		this.repository = repository;
        this.ownerRepository = ownerRepository;
        this.userRepository = userRepository;
    }

	public static void main(String[] args) {
		SpringApplication.run(CardatabaseApplication.class, args);
	}
	// CommandLineRunner 인터페이스의 추상메서드인 run()을 여기서 구현하는거네요.
	// 애플리케이션이 시작되면, run(String... args) 메서드가 자동으로 호출
	// 보통 초기 데이터 세팅, 테스트용 출력, DB 준비 같은 작업에 사용됩니다.

	@Override
	public void run(String... args) throws Exception {
		// 소유자 객체를 추가
		Owner owner1 = new Owner("일","김");
		Owner owner2 = new Owner("이", "강");
		// 다수의 객체를 한 번에 저장하는 메서드 처음 사용해보겠습니다.
		ownerRepository.saveAll(Arrays.asList(owner1, owner2));

		// 내부에서 CarRepository의 객체인 repository의 메서드를 호출할겁니다.
		repository.save(new Car("Kia", "Seltos", "Chacol", "370SU5690", 2020, 30000000,owner1));
		repository.save(new Car("Hyundai", "Sonata", "White", "123456", 2025, 25000000,owner2));
		repository.save(new Car("Honda", "CR-V", "Black-White", "987654", 2024, 45000000,owner2));
		// -> 이상의 코드는 testdb 내의 CAR 테이블 내에 3 개의 row를 추가하여 저장한다는 의미입니다.
		// Java 기준으로는 객체 세 개를 만들어서 저장했다고도 볼 수 있겠네요.

		// 모든 자동차를 가져와서 Console에 로깅해보도록 하겠습니다.
		for (Car car : repository.findAll()) {
			logger.info("brand : {}, model : {}", car.getBrand(), car.getModel());
		}

		// AppUser 더미데이터 추가
		// 저 위에 보시면 Owner에 경우에는 owner1 / owner2 만들어가지고 ownerRepository에 저장했었습니다.
		// username : user / password : user
		userRepository.save(new AppUser("user", "$2y$04$7XSGgfbTIwenxylkuDCGeez7ETZv3HGCZnYXa9rt01dD3FrXoVPni","USER"));
		// username : admin / password : admin
		userRepository.save(new AppUser("admin", "$2y$04$0yJEG6qW/Q/.03WfMjmSDOiKJR8zZsWRzamL6n9Jg4plwZ.o6gV8K", "ADMIN"));

	}
}