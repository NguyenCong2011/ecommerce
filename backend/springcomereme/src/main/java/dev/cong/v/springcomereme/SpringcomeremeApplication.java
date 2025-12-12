package dev.cong.v.springcomereme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class SpringcomeremeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringcomeremeApplication.class, args);
	}

}
