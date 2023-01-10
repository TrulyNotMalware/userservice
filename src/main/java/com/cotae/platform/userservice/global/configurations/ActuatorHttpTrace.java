package com.cotae.platform.userservice.global.configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.util.Objects;

@Configuration
@RequiredArgsConstructor
@PropertySource("classpath:application.yaml")
public class ActuatorHttpTrace {
    private final Environment environment;

    @Bean
    public HttpTraceRepository httpTraceRepository(){
//        if(Objects.equals(environment.getProperty("spring.config.activate.on-profile"), "cotae-local-database"))
        //FIXME Only Local HttpTrace uses InMemoryHttpTraceRepository. Replace in production solutions
        return new InMemoryHttpTraceRepository();
//        else return null;
    }
}
