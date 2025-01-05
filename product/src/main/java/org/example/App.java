package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Properties;

@SpringBootApplication
public class App extends Application {
    public static void main(String[] args){
        SpringApplication.run(App.class, args);
    }
}