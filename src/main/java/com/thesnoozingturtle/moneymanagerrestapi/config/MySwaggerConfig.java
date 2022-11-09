package com.thesnoozingturtle.moneymanagerrestapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

@Configuration
@EnableWebMvc
public class MySwaggerConfig {

    @Bean
    public Docket swaggerConfiguration() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(PathSelectors.ant("/api/**"))
                .apis(RequestHandlerSelectors.basePackage("com.thesnoozingturtle"))
                .build()
                .apiInfo(apiDetails());
    }

    private ApiInfo apiDetails() {
        return new ApiInfo(
                "Money Manager API",
                "User, Expense and Income API for Money Manager Application",
                "1.0",
                "Free to use",
                new springfox.documentation.service.Contact("Arth Srivastava", "https://www.linkedin.com/in/arth-srivastava/", "thesnoozingturtle@gmail.com"),
                "API License",
                "https://www.linkedin.com/in/arth-srivastava/",
                Collections.emptyList()
        );
    }
}
