package com.cineflex.api.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

import org.springframework.boot.autoconfigure.cache.CacheProperties.Redis;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.cineflex.api.entity.UserPrincipal;
import com.cineflex.api.model.Account;
import com.cineflex.api.repository.AccountRepository;

@Service
public class AuthenticationService {
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    private final AccountRepository accountRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenService tokenService;
    private final RedisTemplate<String, String> otpTemplate;
    private final String OTP_PREFIX = "auth:otp:";

    public AuthenticationService (
        AccountRepository accountRepository,
        AuthenticationManager authenticationManager,
        JwtService jwtService,
        TokenService tokenService,
        RedisTemplate<String, String> otpTemplate
    ) {
        this.accountRepository = accountRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.tokenService = tokenService;
        this.otpTemplate = otpTemplate;
    }

    public void resetPassword(String email, String password) {
        try {
            String encodedPassword = encoder.encode(password);
            Account a = accountRepository.readByEmail(email);

            if (a == null) {
                throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Does not exist account for this email"
                );
            }

            a.setPassword(encodedPassword);

            accountRepository.update(a.getId(), a);

            String key = OTP_PREFIX + email;
            otpTemplate.delete(key);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    public String createOtp(String email) {
        try {
            Account a = accountRepository.readByEmail(email);

            if (a == null) {
                throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Does not exist account for this email"
                );
            }

            String key = OTP_PREFIX + email;
            String value = otpTemplate.opsForValue().get(key);

            if (value != null) {
                return value;
            }

            Random random = new Random();
            int code = random.nextInt(100000, 999999);
            value = String.format("%d", code);

            otpTemplate.opsForValue().set(key, value, Duration.ofMinutes(5));

            return value;
        }
        catch (ResponseStatusException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    public Boolean validateCode(String email, String otp) {
        try {
            String key = OTP_PREFIX + email;
            String value = otpTemplate.opsForValue().get(key);

            return otp.equals(value);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    public Account fromUsername(String username) {
        return accountRepository.readByUsername(username);
    }

    public Account fromEmail(String email) {
        return accountRepository.readByEmail(email);
    }

    @Transactional
    public Account register(String username, String email, String password) {
        if (accountRepository.readAll().stream().anyMatch(a -> a.getUsername().equals(username))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username has already taken");
        }

        if (accountRepository.readAll().stream().anyMatch(a -> a.getEmail().equals(email))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email has already taken");
        }
        
        try {

            UUID id = UUID.randomUUID();

            Account account = Account.builder()
                .id(id)
                .username(username)
                .email(email)
                .password(encoder.encode(password))
                .createdTime(LocalDateTime.now())
                .updatedTime(LocalDateTime.now())
                .verify(false)
                .role(0)
                .build();

            accountRepository.create(account);

            tokenService.createToken(account);

            return accountRepository.read(id);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    public Account getAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            UserPrincipal up = (UserPrincipal) authentication.getPrincipal();
            return fromUsername(up.getUsername());
        }

        return null;
    }


    public String login(String email, String password) {
        Account account = accountRepository.readByEmail(email);
        String username = account != null?account.getUsername():null;

        if (username == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email does not exist");
        }

        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        }
        catch (DisabledException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } 
        catch (LockedException e) {
            throw new ResponseStatusException(HttpStatus.LOCKED, e.getMessage());
        }
        catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

        if (!authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Cannot login");
        }


        return jwtService.createToken(username);
    }
}
