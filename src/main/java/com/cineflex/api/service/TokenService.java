package com.cineflex.api.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.cineflex.api.model.Account;
import com.cineflex.api.model.VerificationToken;
import com.cineflex.api.repository.VerificationTokenRepository;

@Service
public class TokenService {
    private final VerificationTokenRepository verificationTokenRepository;

    public TokenService (
        VerificationTokenRepository verificationTokenRepository
    ) {
        this.verificationTokenRepository = verificationTokenRepository;
    }

    public VerificationToken createToken(Account account) {
        try {
            UUID id = UUID.randomUUID();
            VerificationToken token = VerificationToken.builder()
                .id(id)
                .account(account.getId())
                .token(id.toString())
                .build();
            
            verificationTokenRepository.create(token);


            return verificationTokenRepository.read(id);
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }
}
