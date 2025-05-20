package com.cineflex.API.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import com.cineflex.API.model.ReportComment;

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
        String sql = "INSERT INTO [dbo].[ReportComment] ([Id], [Content], [ReportedTime], [Status], [Account], [Comment]) VALUES (?, ?, ?, ?, ?, ?)";

        int row = jdbcClient.sql(sql).params(
            t.getId(),
            t.getContent(),
            t.getReportedTime(),
            t.getStatus(),
            t.getAccount(),
            t.getComment()
        ).update();

        if (row == 0) {
            throw new RuntimeException("Cannot add this report to database");
        }
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
    public List<ReportComment> readAll() {
        String sql = "SELECT * FROM [dbo].[ReportComment]";

        List<ReportComment> reportComments = jdbcClient
            .sql(sql)
            .query(ReportComment.class)
            .list();
        
        return reportComments;
    }

    @Override
    public void update(UUID id, ReportComment t) {
        String sql = "UPDATE [dbo].[ReportComment] SET [Content] = ?, [ReportedTime] = ?, [Status] = ?, [Account] = ?, [Comment] = ? WHERE [Id] = ?";

        int row = jdbcClient.sql(sql).params(
            t.getContent(),
            t.getReportedTime(),
            t.getStatus(),
            t.getAccount(),
            t.getComment(),
            id
        ).update();

        if (row == 0) {
            throw new RuntimeException("Cannot update this report to database");
        }
    }

    @Override
    public void delete(UUID id) {
        String sql = "DELETE FROM [dbo].[ReportComment] WHERE [Id] = ?";

        int row = jdbcClient.sql(sql).params(
            id
        ).update();

        if (row == 0) {
            throw new RuntimeException("Cannot update this report to database");
        }
    }
    
}
