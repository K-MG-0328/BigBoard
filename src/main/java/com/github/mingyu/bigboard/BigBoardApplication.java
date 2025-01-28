package com.github.mingyu.bigboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class BigBoardApplication {

    public static void main(String[] args) {
        SpringApplication.run(BigBoardApplication.class, args);
    }

}
