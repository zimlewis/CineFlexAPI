package com.cineflex.API.controller;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.*;

import com.cineflex.API.model.Account;
import com.cineflex.API.repository.AccountRepository;
import org.springframework.web.bind.annotation.GetMapping;





@RestController
public class TestAPI {
    private final AccountRepository a;

    public TestAPI(AccountRepository a) {
        this.a = a;
    }

    @GetMapping("/add")
    public int addTeString() {
        a.create(Account.builder()
            .id(UUID.randomUUID())
            .username("mmb")
            .email("mmmb@gmail.com")
            .password("aaa")
            .createdTime(LocalDateTime.now())
            .updatedTime(LocalDateTime.now())
            .verify(true)
            .role(1)
            .build()
        );
        return 0;
    }
    

    @GetMapping("/list")
    public List<Account> test() {

        return a.readAll();
    }

    @GetMapping("/single")
    public Account getSingle() {
        return a.read(UUID.fromString("a2d03d36-1e26-459c-8c89-09d8fc8f5389"));
    }

    @GetMapping("/update")
    public String update() {
        a.update(
            UUID.fromString("a2d03d36-1e26-459c-8c89-09d8fc8f5389"), 
            Account.builder()
                .id(UUID.randomUUID())
                .username("mmb2")
                .email("mmmb2@gmail.com")
                .password("aaa2")
                .createdTime(LocalDateTime.now())
                .updatedTime(LocalDateTime.now())
                .verify(false)
                .role(2)
                .build()
        );
        return new String();
    }
    
    
    @GetMapping("/delete")
    public String deleteString() {
        a.delete(UUID.fromString("a2d03d36-1e26-459c-8c89-09d8fc8f5389"));
        return new String();
    }
    
    
}
