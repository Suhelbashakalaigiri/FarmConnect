package com.farmmarket.buyerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
public class BuyerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BuyerServiceApplication.class, args);
    }

}
