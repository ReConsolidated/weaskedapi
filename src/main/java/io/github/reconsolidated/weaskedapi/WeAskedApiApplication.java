package io.github.reconsolidated.weaskedapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableMongoRepositories
public class WeAskedApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeAskedApiApplication.class, args);
	}

}
