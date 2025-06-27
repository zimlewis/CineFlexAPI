package com.cineflex.api.repository;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import com.cineflex.api.model.VerificationToken;

@Repository
public class VerificationTokenRepository implements RepositoryInterface<VerificationToken> {

    private final JdbcClient jdbcClient;

    public VerificationTokenRepository (
        JdbcClient jdbcClient
    ) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public void create(VerificationToken t) {
        String sql = "INSERT INTO [dbo].[VerificationToken] ([Id], [Account], [Token], [CreatedTime], [ExpiredTime], [Verified]) VALUES (?, ?, ?, ?, ?, ?)";

        jdbcClient.sql(sql)
            .params(
                t.getId(),
                t.getAccount(),
                t.getToken(),
                t.getCreatedTime(),
                t.getExpiredTime(),
                t.getVerified()
            )
            .update();
    }

    @Override
    public VerificationToken read(UUID id) {
        String sql = "SELECT * FROM [dbo].[VerificationToken] WHERE [Id] = ?";

        VerificationToken verificationToken = jdbcClient.sql(sql)
            .params(id)
            .query(VerificationToken.class)
            .optional()
            .orElse(null);
        
        return verificationToken;
    }

    @Override
    public List<VerificationToken> readAll() {
        String sql = "SELECT * FROM [dbo].[VerificationToken]";

        List<VerificationToken> verificationTokens = jdbcClient.sql(sql)
            .query(VerificationToken.class)
            .list();
        
        return verificationTokens;
    }

    @Override
    public void update(UUID id, VerificationToken t) {
        String sql = "UPDATE [dbo].[VerificationToken] SET [Account] = ?, [Token] = ?, [CreatedTime] = ?, [ExpiredTime] = ?, [Verified] = ? WHERE [Id] = ?";

        jdbcClient.sql(sql)
            .params(
                t.getAccount(),
                t.getToken(),
                t.getCreatedTime(),
                t.getExpiredTime(),
                t.getVerified(),
                id
            )
            .update();
    }

    @Override
    public void delete(UUID... ids) {
        if (ids.length == 0) return; // avoid syntax error

        String placeholders = Arrays.stream(ids)
            .map(_ -> "?")
            .collect(Collectors.joining(", "));
        
        String sql = "DELETE FROM [dbo].[VerificationToken] WHERE [Id] IN (" + placeholders + ")";

        jdbcClient.sql(sql).params(Arrays.asList(ids)).update();
    }

    public VerificationToken readByTokenContent(String token) {
        String sql = "SELECT * FROM [dbo].[VerificationToken] WHERE [Token] = ?";

        VerificationToken verificationToken = jdbcClient.sql(sql)
            .params(token)
            .query(VerificationToken.class)
            .optional()
            .orElse(null);
        
        return verificationToken;
    }

    public List<VerificationToken> readByAccount(UUID id) {
        String sql = "SELECT * FROM [dbo].[VerificationToken] WHERE [Account] = ?";

        List<VerificationToken> verificationTokens = jdbcClient
            .sql(sql)
            .params(id)
            .query(VerificationToken.class)
            .list();
        
        return verificationTokens;
    }
    
}
