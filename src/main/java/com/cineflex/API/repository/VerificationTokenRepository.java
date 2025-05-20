package com.cineflex.API.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import com.cineflex.API.model.VerificationToken;

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
        String sql = "INSERT INTO [dbo].[VerifyToken] ([Id], [Account], [Token]) VALUES (?, ?, ?)";

        int row = jdbcClient.sql(sql)
            .params(
                t.getId(),
                t.getAccount(),
                t.getToken()
            )
            .update();

        if (row == 0) {
            throw new RuntimeException("Cannont add this verification token");
        }
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
        String sql = "UPDATE [dbo].[VerifyToken] SET [Account] = ?, [Token] = ? WHERE [Id] = ?";

        int row = jdbcClient.sql(sql)
            .params(
                t.getAccount(),
                t.getToken(),
                id
            )
            .update();

        if (row == 0) {
            throw new RuntimeException("Cannont update this verification token");
        }
    }

    @Override
    public void delete(UUID id) {
        String sql = "DELETE FROM [dbo].[VerificationToken] WHERE [Id] = ?";

        int row = jdbcClient.sql(sql)
            .params(
                id
            )
            .update();

        if (row == 0) {
            throw new RuntimeException("Cannont remove this verification token");
        }
    }
    
}
