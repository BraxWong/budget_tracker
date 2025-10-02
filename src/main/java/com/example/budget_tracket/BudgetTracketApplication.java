package com.example.budget_tracket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.example.budget_tracket.repository")
public class BudgetTracketApplication {

	public static void main(String[] args) {
		SpringApplication.run(BudgetTracketApplication.class, args);
	}

}
