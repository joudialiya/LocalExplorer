package com.le.localexplorer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@SpringBootApplication
public class LocalExplorerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LocalExplorerApplication.class, args);
    }
}
