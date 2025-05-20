package com.cineflex.API.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.cineflex.API.model.ReportComment;

@Repository
public class ReportCommentRepository implements RepositoryInterface<ReportComment> {

    @Override
    public void create(ReportComment t) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

    @Override
    public ReportComment read(UUID id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'read'");
    }

    @Override
    public List<ReportComment> readAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'readAll'");
    }

    @Override
    public void update(UUID id, ReportComment t) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void delete(UUID id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }
    
}
