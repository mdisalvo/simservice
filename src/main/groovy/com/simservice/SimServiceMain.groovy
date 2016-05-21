package com.simservice

import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.PropertySource
import org.springframework.stereotype.Component

/**
 * @author Michael Di Salvo
 * michael.vincent.disalvo@gmail.com
 */
@Component
@ComponentScan
@EnableAutoConfiguration
@PropertySource("classpath:application.properties")
class SimServiceMain {
    static main(args) {
        new SpringApplicationBuilder()
                .sources(SimServiceMain.class)
                .bannerMode(Banner.Mode.OFF)
                .run()
    }
}
