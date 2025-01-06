package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import javax.ws.rs.core.Application;

@SpringBootApplication
@EnableDiscoveryClient
public class App extends Application {
    public static void main(String[] args){
        SpringApplication.run(App.class, args);
    }
}