package com.farmconnect.crop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
public class CropServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CropServiceApplication.class, args);
    }

}
