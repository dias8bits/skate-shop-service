package com.skateshop.config;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

@TestConfiguration
@Lazy
public class RestAssuredConfig {

    @LocalServerPort
    int port;

    @Bean(name = "requestSpecification")
    public RequestSpecification requestSpecification() {
        return RestAssured.given()
                .baseUri("http://localhost:" + port);
    }
}
