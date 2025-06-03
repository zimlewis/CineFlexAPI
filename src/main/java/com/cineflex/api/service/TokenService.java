package com.cineflex.api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.cineflex.api.model.Account;
import com.cineflex.api.model.VerificationToken;
import com.cineflex.api.repository.AccountRepository;
import com.cineflex.api.repository.VerificationTokenRepository;


@Service
public class TokenService {
    private final VerificationTokenRepository verificationTokenRepository;
    private final AccountRepository accountRepository;

    public TokenService (
        VerificationTokenRepository verificationTokenRepository,
        AccountRepository accountRepository
    ) {
        this.verificationTokenRepository = verificationTokenRepository;
        this.accountRepository = accountRepository;
    }

    public VerificationToken createToken(Account account) {
        try {
            UUID id = UUID.randomUUID();
            VerificationToken token = VerificationToken.builder()
                .id(id)
                .account(account.getId())
                .token(UUID.randomUUID().toString())
                .createdTime(LocalDateTime.now())
                .expiredTime(LocalDateTime.now().plusDays(1))
                .verified(false)
                .build();
            
            verificationTokenRepository.create(token);


            return verificationTokenRepository.read(id);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    @Transactional
    public void verifyToken(String token) {
        VerificationToken verificationToken = verificationTokenRepository.readByTokenContent(token);

        if (verificationToken.getVerified()) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "This verify token is already used");
        }

        verificationToken.setVerified(true);

        Account account = accountRepository.read(verificationToken.getAccount());
        account.setVerify(true);
        
        accountRepository.update(account.getId(), account);
        verificationTokenRepository.update(verificationToken.getId(), verificationToken);
    }

    public VerificationToken getAvailableToken(Account account) {
        List<VerificationToken> verificationTokens = verificationTokenRepository.readByAccount(account.getId());

        Stream<VerificationToken> stream = verificationTokens.stream().filter((token) -> !token.getExpiredTime().isBefore(LocalDateTime.now()) && !token.getVerified());

        VerificationToken chosenToken = stream.findAny().orElse(null);

        return chosenToken;
    }
}
