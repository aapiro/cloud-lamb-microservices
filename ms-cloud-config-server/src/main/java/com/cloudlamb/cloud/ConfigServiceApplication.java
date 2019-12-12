package com.cloudlamb.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

//@EnableDiscoveryClient //this enables service registration and discovery. In this case, this process registers itself with the discovery-server service using its application name
@EnableConfigServer
@SpringBootApplication
public class ConfigServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ConfigServiceApplication.class, args);
    }
}
