package com.tcc.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

import ilog.concert.IloException;

@SpringBootApplication
@RestController
public class BackendApplication {

    public static void main(String[] args) throws IloException {
        SpringApplication.run(BackendApplication.class, args);

    }

}
