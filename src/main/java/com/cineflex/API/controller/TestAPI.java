package com.cineflex.API.controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
public class TestAPI {
    @GetMapping("/test")
    public String test() {
        return "1";
    }
    
}
