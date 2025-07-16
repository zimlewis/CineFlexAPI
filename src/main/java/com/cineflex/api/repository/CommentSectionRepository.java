package com.cineflex.api.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import com.cineflex.api.model.CommentSection;

@Service
public class CommentSectionRepository implements RepositoryInterface<CommentSection> {

    private final JdbcClient jdbcClient;

    public CommentSectionRepository (
        JdbcClient jdbcClient
    ) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public void create(CommentSection t) {
        String sql = "INSERT INTO [dbo].[CommentSection] ([Id]) VALUES (?)";

        jdbcClient
            .sql(sql)
            .params(
                t.getId()
            )
            .update();
    }

    @Override
    public CommentSection read(UUID id) {
        String sql = "SELECT * FROM [dbo].[CommentSection] WHERE [id] = ?";

        CommentSection commentSection = jdbcClient
            .sql(sql)
            .params(id)
            .query(CommentSection.class)
            .optional()
            .orElseGet(null);

        return commentSection;
    }

    @Override
    public List<CommentSection> readAll(Integer page, Integer size) {
        String sql= "SELECT * FROM [dbo].[CommentSection] LIMIT ? OFFSET ?";

        List<CommentSection> commentSection = jdbcClient
            .sql(sql)
            .params(size, page * size)
            .query(CommentSection.class)
            .list();
        
        return commentSection;
    }

    @Override
    public List<CommentSection> readAll() {
        return readAll(0, 5);
    }

    @Override
    public void update(UUID id, CommentSection t) {
    }

    @Override
    public void delete(UUID... ids) {
    }



    
}