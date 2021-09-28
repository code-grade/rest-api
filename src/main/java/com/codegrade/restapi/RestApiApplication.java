package com.codegrade.restapi;

import com.codegrade.restapi.config.SuperUserConfig;
import com.codegrade.restapi.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

@Getter @Setter
@Slf4j
@EnableAsync
@SpringBootApplication
@RequiredArgsConstructor
@EnableConfigurationProperties
public class RestApiApplication {

    private final UserService userService;
    private final SuperUserConfig superUserConfig;

    public static void main(String[] args) {
        SpringApplication.run(RestApiApplication.class, args);
    }

    @EventListener(ContextRefreshedEvent.class)
    public void feedRequiredDataToDatabase() {
        log.info("Feeding: data into database");
        log.info("Feeding: super user details");
        userService.getUserDetails(superUserConfig.getUsername())
                .ifPresentOrElse(
                        (user) -> log.info("Feeding: super user admin is already present"),
                        () -> {
                            userService.addUser(superUserConfig.getUserAccount());
                            log.info("Feeding: super user admin is added");
                        }
                );
    }

    @Bean
//    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
