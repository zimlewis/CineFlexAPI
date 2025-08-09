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
        String sql = "INSERT INTO [dbo].[CommentSection] ([Id], [Alias]) VALUES (?, ?)";

        jdbcClient
            .sql(sql)
            .params(
                t.getId(),
                t.getAlias()
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
        String sql= "SELECT * FROM [dbo].[CommentSection] ORDER BY [Id] OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        List<CommentSection> commentSection = jdbcClient
            .sql(sql)
            .params(page * size, size)
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

    @Override
    public Integer getPageCount(Integer size) {
        String sql = "SELECT COUNT([Id])/? FROM [dbo].[CommentSection]";

        Integer pageCount = jdbcClient
            .sql(sql)
            .params(size)
            .query(Integer.class).optional().orElse(-1);
        
        return pageCount;
    }

    public CommentSection getCommentSectionOfEpisode(UUID episode) {
        String sql = "SELECT * FROM [dbo].[CommentSection] WHERE [Id] = (SELECT [CommentSection] FROM [dbo].[Episode] WHERE [Id] = ?)";

        CommentSection commentSection = jdbcClient
            .sql(sql)
            .param(episode)
            .query(CommentSection.class)
            .optional()
            .orElse(null);

        return commentSection;
    }

    public CommentSection getCommentSectionOfShow(UUID show) {
        String sql = "SELECT * FROM [dbo].[CommentSection] WHERE [Id] = (SELECT [CommentSection] FROM [dbo].[Show] WHERE [Id] = ?)";

        CommentSection commentSection = jdbcClient
            .sql(sql)
            .param(show)
            .query(CommentSection.class)
            .optional()
            .orElse(null);

        return commentSection;
    }


    
}