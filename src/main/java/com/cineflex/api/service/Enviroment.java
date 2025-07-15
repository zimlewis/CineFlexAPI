package com.cineflex.api.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Enviroment {

    @Value("${my.api.key}")
    private String apiKey;

    @PostConstruct
    public void init() {
        System.out.println("My API Key: " + apiKey);
    }
}
