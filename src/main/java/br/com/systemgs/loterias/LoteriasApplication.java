package br.com.systemgs.loterias;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableFeignClients
@EnableAsync
@SpringBootApplication
public class LoteriasApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoteriasApplication.class, args);
	}
}
