package com.cineflex.api.repository;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import com.cineflex.api.model.ReportComment;

@Repository
public class ReportCommentRepository implements RepositoryInterface<ReportComment> {
    private final JdbcClient jdbcClient;

    public ReportCommentRepository (
        JdbcClient jdbcClient
    ) {
        this.jdbcClient = jdbcClient;
    } 

    @Override
    public void create(ReportComment t) {
        String sql = "INSERT INTO [dbo].[ReportComment] ([Id], [Content], [CreatedTime], [UpdatedTime], [Status], [Account], [Comment]) VALUES (?, ?, ?, ?, ?, ?, ?)";

        jdbcClient.sql(sql).params(
            t.getId(),
            t.getContent(),
            t.getCreatedTime(),
            t.getUpdatedTime(),
            t.getStatus(),
            t.getAccount(),
            t.getComment()
        ).update();
    }

    @Override
    public ReportComment read(UUID id) {
        String sql = "SELECT * FROM [dbo].[ReportComment] WHERE [Id] = ?";

        ReportComment reportComment = jdbcClient
            .sql(sql)
            .params(id)
            .query(ReportComment.class)
            .optional()
            .orElse(null);
        
        return reportComment;
    }

    @Override
    public List<ReportComment> readAll(Integer page, Integer size) {
        String sql = "SELECT * FROM [dbo].[ReportComment] ORDER BY [CreatedTime] DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        List<ReportComment> reportComments = jdbcClient
            .sql(sql)
            .params(page * size, size)
            .query(ReportComment.class)
            .list();
        
        return reportComments;
    }

    @Override
    public void update(UUID id, ReportComment t) {
        String sql = "UPDATE [dbo].[ReportComment] SET [Content] = ?, [CreatedTime] = ?, [UpdatedTime] = ?, [Status] = ?, [Account] = ?, [Comment] = ? WHERE [Id] = ?";

        jdbcClient.sql(sql).params(
            t.getContent(),
            t.getCreatedTime(),
            t.getUpdatedTime(),
            t.getStatus(),
            t.getAccount(),
            t.getComment(),
            id
        ).update();
    }

    @Override
    public void delete(UUID... ids) {
        if (ids.length == 0) return; // avoid syntax error

        String placeholders = Arrays.stream(ids)
            .map(_ -> "?")
            .collect(Collectors.joining(", "));

        String sql = "DELETE FROM [dbo].[ReportComment] WHERE [Id] IN (" + placeholders + ")";

        int row = jdbcClient.sql(sql).params(Arrays.asList(ids)).update();

        if (row == 0) {
            throw new RuntimeException("Cannot update this report to database");
        }
    }

    @Override
    public List<ReportComment> readAll() {
        return readAll(0, 5);
    }

    @Override
    public Integer getPageCount(Integer size) {
        String sql = "SELECT COUNT([Id])/? FROM [dbo].[ReportComment]";

        Integer pageCount = jdbcClient
            .sql(sql)
            .params(size)
            .query(Integer.class).optional().orElse(-1);
        
        return pageCount;
    }
    
}
