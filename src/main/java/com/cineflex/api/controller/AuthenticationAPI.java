package com.cineflex.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cineflex.api.entity.UserPrincipal;
import com.cineflex.api.model.Account;
import com.cineflex.api.model.VerificationToken;
import com.cineflex.api.service.AccountDetailService;
import com.cineflex.api.service.AuthenticationService;
import com.cineflex.api.service.EmailService;
import com.cineflex.api.service.JsonService;
import com.cineflex.api.service.TokenService;
import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api/authentication")
public class AuthenticationAPI {

    private final AccountDetailService accountDetailService;
    private final AuthenticationService authenticationService;
    private final TokenService tokenService;
    private final JsonService jsonService;
    private final EmailService emailService;
    
    public AuthenticationAPI (
        AuthenticationService authenticationService,
        JsonService jsonService,
        TokenService tokenService, 
        AccountDetailService accountDetailService,
        EmailService emailService
    ) {
        this.authenticationService = authenticationService;
        this.jsonService = jsonService;
        this.tokenService = tokenService;
        this.accountDetailService = accountDetailService;
        this.emailService = emailService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody JsonNode jsonNode) {
        try {
            String email = jsonService.getOrNull(jsonNode, "email", String.class);
            String password = jsonService.getOrNull(jsonNode, "password", String.class);
            
            if (email == null || password == null) {
                return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                    HttpStatus.UNAUTHORIZED, 
                    "Cannot login"
                )).build();
            }

            String token = authenticationService.login(email, password);

            return new ResponseEntity<>(token, HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(), 
                e.getReason()
            )).build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody JsonNode jsonNode) {
        try {
            String email = jsonService.getOrNull(jsonNode, "email", String.class);
            String password = jsonService.getOrNull(jsonNode, "password", String.class);
            String username = jsonService.getOrNull(jsonNode, "username", String.class);
            
            if (email == null || password == null || username == null) {
                return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                    HttpStatus.UNAUTHORIZED, 
                    "Cannot register"
                )).build();
            }

            Account account = authenticationService.register(username, email, password);

            return new ResponseEntity<>(account.getEmail(), HttpStatus.ACCEPTED);

        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(), 
                e.getReason()
            )).build();
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<Account> getProfile() {
        Account account = authenticationService.getAccount();

        if (account != null) {
            account.setPassword(null);
            return new ResponseEntity<>(account, HttpStatus.OK);
        }

        return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
            HttpStatus.UNAUTHORIZED, 
            "request is not authenticated"
        )).build();
    }
    
    @GetMapping("/verify/{token}")
    public ResponseEntity<String> verify(@PathVariable String token) {
        try {
            tokenService.verifyToken(token);

            return new ResponseEntity<>("Xác thực thành công vui lòng quay về trang đang nhập", HttpStatus.NO_CONTENT);
        }
        catch (ResponseStatusException e) {
            return new ResponseEntity<>("Xác thực không thành công, vui lòng thử lại sau", e.getStatusCode());
        }
    }
    
    @PostMapping("/verify")
    public ResponseEntity<?> sendVerifyEmail(@RequestBody JsonNode jsonNode, @RequestHeader String host) {
        String email = jsonService.getOrNull(jsonNode, "email", String.class);

        
        if (email == null) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_ACCEPTABLE, 
                "Email cannot be null"
            )).build();
        }

        Account account = authenticationService.fromEmail(email);


        if (account == null) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                HttpStatus.UNAUTHORIZED, 
                "Email not found"
            )).build();
        }

        if (account.getVerify()) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT, 
                "This account has verified already"
            )).build();
        }


        VerificationToken token = tokenService.getAvailableToken(account);


        try {
            if (token == null) {
                token = tokenService.createToken(account);
            }
            emailService.sendActivationEmail(token.getToken(), email, host);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(),
                e.getReason()
            )).build();
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> verifyOtp(@RequestBody JsonNode body) {
        try {
            String email = jsonService.getOrNull(body, "email", String.class);
            String otp = jsonService.getOrNull(body, "otp", String.class);
            String newPassword = jsonService.getOrNull(body, "password", String.class);

            if (!authenticationService.validateCode(email, otp)) {
                throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Wrong otp"
                );
            }

            authenticationService.resetPassword(email, newPassword);

            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(),
                e.getReason()
            )).build();
        }
    }
    

    @PostMapping("/send-otp")
    public ResponseEntity<?> requestPasswordReset(@RequestBody JsonNode body) {
        try {
            String email = jsonService.getOrNull(body, "email", String.class);

            if (email == null) {
                throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "You did not provide email"
                );
            }


            String code = authenticationService.createOtp(email);

            emailService.sendEmail("Mã OTP của bạn là: " + code, email, "OTP code");

            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(),
                e.getReason()
            )).build();
        }
    }
    
    
    
    @GetMapping("/role")
    public ResponseEntity<Integer> getUserRole() {
        try {
            Account a = authenticationService.getAccount();

            if (a == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Request did not authenticate");
            }

            return new ResponseEntity<>(a.getRole(), HttpStatus.OK);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(
                e.getStatusCode(),
                e.getReason()
            )).build();
        }
    }
    
}
