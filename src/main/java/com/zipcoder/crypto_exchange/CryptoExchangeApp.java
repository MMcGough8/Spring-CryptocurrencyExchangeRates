package com.zipcoder.crypto_exchange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CryptoExchangeApp {

    public static void main(String[] args) {
        SpringApplication.run(CryptoExchangeApp.class, args);
    }
}