package com.ibum.jokes

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.mongodb.config.EnableMongoAuditing

@EnableMongoAuditing
@SpringBootApplication
class JokesApplication {

	static void main(String[] args) {
		SpringApplication.run JokesApplication, args
	}
}
