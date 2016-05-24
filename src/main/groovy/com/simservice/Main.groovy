package com.simservice

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.PropertySource
import org.springframework.stereotype.Component
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Component
@ComponentScan
@EnableSwagger2
@EnableAutoConfiguration
@PropertySource("classpath:application.properties")
class Main {
    static main(args) {
        new SpringApplicationBuilder()
                .sources(Main.class)
                .run()
    }

    @Bean
    def Docket api() {
        new Docket(DocumentationType.SWAGGER_2)
            .groupName("com.simservice")
            .apiInfo(apiInfo())
            .select()
            .paths(PathSelectors.regex("/docsim.*"))
            .build()
    }

    private static def apiInfo() {
        new ApiInfoBuilder()
            .title("Similarity Service")
            .description("Similarity Service")
            .contact("Michael Di Salvo")
            .build()
    }
}
