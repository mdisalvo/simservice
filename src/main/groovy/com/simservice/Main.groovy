package com.simservice

import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.PropertySource
import org.springframework.stereotype.Component

/**
 * @author Michael Di Salvo
 * mdisalvo@kcura.com
 */
@Component
@ComponentScan
@EnableAutoConfiguration
@PropertySource("classpath:application.properties")
class Main {
    static main(args) {
        new SpringApplicationBuilder()
                .sources(Main.class)
                .run()
    }
}
