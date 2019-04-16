package com.nowcoder;

import com.nowcoder.model.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class WendaApplication {

	public static void main(String[] args) {
		SpringApplication.run(WendaApplication.class, args);
	}
}
