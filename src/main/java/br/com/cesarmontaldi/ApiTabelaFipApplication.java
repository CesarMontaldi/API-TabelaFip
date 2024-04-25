package br.com.cesarmontaldi;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.Principal;

@SpringBootApplication
public class ApiTabelaFipApplication implements CommandLineRunner {

	public static void main(String[] args) {

		SpringApplication.run(ApiTabelaFipApplication.class, args);

	}
	@Override
	public void run(String... args) throws Exception {

	}
}
