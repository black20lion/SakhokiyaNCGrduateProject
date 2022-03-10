package com.netcracker;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Shop API", version = "1.0", description = "Clothes shop"))
public class ShopRestApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShopRestApplication.class);
    }
}
