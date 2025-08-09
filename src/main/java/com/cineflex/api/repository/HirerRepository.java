package com.cineflex.api.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import com.cineflex.api.model.CommentSection;
import com.cineflex.api.model.Hirer;

@Repository
public class HirerRepository implements RepositoryInterface<Hirer>{
    private final JdbcClient jdbcClient;

    public HirerRepository(
        JdbcClient jdbcClient
    ) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public void create(Hirer t) {
        String sql = "INSERT INTO [dbo].[Hirer] ([Id], [Alias], [Email], [Phone], [CreatedTime], [UpdatedTime]) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcClient
            .sql(sql)
            .params(
                t.getId(),
                t.getAlias(),
                t.getEmail(),
                t.getPhone(),
                t.getCreatedTime(),
                t.getUpdatedTime()
            )
            .update();
    }

    @Override
    public Hirer read(UUID id) {
        String sql = "SELECT * FROM [dbo].[Hirer] WHERE [Id] = ?";
        Hirer hirer = jdbcClient
            .sql(sql)
            .params(id)
            .query(Hirer.class)
            .single();
        
        return hirer;
    }

    @Override
    public List<Hirer> readAll() {
        return readAll(0, 5);
    }

    @Override
    public List<Hirer> readAll(Integer page, Integer size) {
        String sql= "SELECT * FROM [dbo].[Hirer] ORDER BY [CreatedTime] OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        List<Hirer> hires = jdbcClient
            .sql(sql)
            .params(page * size, size)
            .query(Hirer.class)
            .list();
        
        return hires;
    }

    @Override
    public void update(UUID id, Hirer t) {
        String sql = "UPDATE [dbo].[Hirer] SET [Alias] = ?, [Email] = ?, [Phone] = ?, [CreatedTime] = ?, [UpdatedTime] = ? WHERE [Id] = ?";
        jdbcClient
            .sql(sql)
            .params(
                t.getAlias(),
                t.getEmail(),
                t.getPhone(),
                t.getCreatedTime(),
                t.getUpdatedTime(),
                t.getId()
            )
            .update();
    }

    @Override
    public void delete(UUID... ids) {
    }

    @Override
    public Integer getPageCount(Integer size) {
        String sql = "SELECT COUNT([Id])/? FROM [dbo].[Hirer]";

        Integer pageCount = jdbcClient
                .sql(sql)
                .params(size)
                .query(Integer.class).optional().orElse(0);

        return pageCount;
    }
}
