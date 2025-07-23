package com.cineflex.api.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import com.cineflex.api.model.Advertisement;

@Repository
public class AdvertisementRepository implements RepositoryInterface<Advertisement> {
    private final JdbcClient jdbcClient;

    public AdvertisementRepository (
        JdbcClient jdbcClient
    ) {
        this.jdbcClient = jdbcClient;
    }

    public Advertisement findRandomOnType(Integer type) {
        String sql = "SELECT TOP 1 * FROM [dbo].[Advertisement] WHERE [Type] = ? AND [Enabled] = 1 ORDER BY NEWID()";

        Advertisement advertisement = jdbcClient.sql(sql)
            .params(type).query(Advertisement.class).optional().orElseGet(null);
        
        return advertisement;
    }

    @Override
    public void create(Advertisement t) {
        String sql = "INSERT INTO [dbo].[Advertisement] ([Id], [Link], [Image], [Enabled], [Type], [CreatedTime], [UpdatedTime], [Hirer]) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcClient
            .sql(sql)
            .params(
                t.getId(), 
                t.getLink(), 
                t.getImage(), 
                t.getEnabled(), 
                t.getType(), 
                t.getCreatedTime(), 
                t.getUpdatedTime(),
                t.getHirer()
            )
            .update();
    }

    @Override
    public Advertisement read(UUID id) {
        String sql = "SELECT * FROM [dbo].[Advertisement] WHERE [Id] = ?";

        Advertisement advertisement = jdbcClient.sql(sql)
            .params(id).query(Advertisement.class).optional().orElseGet(null);
        
        return advertisement;
    }

    @Override
    public List<Advertisement> readAll() {
        return readAll(0, 5);
    }

    @Override
    public List<Advertisement> readAll(Integer page, Integer size) {
        String sql= "SELECT * FROM [dbo].[Advertisement] ORDER BY [CreatedTime] OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        List<Advertisement> advertisements = jdbcClient
            .sql(sql)
            .params(page * size, size)
            .query(Advertisement.class)
            .list();
        
        return advertisements;
    }

    @Override
    public void update(UUID id, Advertisement t) {
        String sql = "UPDATE [dbo].[Advertisement] SET [Link] = ?, [Image] = ?, [Enabled] = ?, [Type] = ?, [CreatedTime] = ?, [UpdatedTime] = ? WHERE [Id] = ?";
        jdbcClient
            .sql(sql)
            .params(t.getLink(), t.getImage(), t.getEnabled(), t.getType(), t.getCreatedTime(), t.getUpdatedTime(), t.getId())
            .update();
    }

    @Override
    public void delete(UUID... ids) {
    }

    @Override
    public Integer getPageCount(Integer size) {
        String sql = "SELECT COUNT([Id])/? FROM [dbo].[Advertisement]";

        Integer pageCount = jdbcClient
                .sql(sql)
                .params(size)
                .query(Integer.class).optional().orElse(-1);

        return pageCount;
    }
    
}
