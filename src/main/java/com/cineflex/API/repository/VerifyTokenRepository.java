package com.cineflex.API.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.cineflex.API.model.VerifyToken;

@Repository
public class VerifyTokenRepository implements RepositoryInterface<VerifyToken> {

    @Override
    public void create(VerifyToken t) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

    @Override
    public VerifyToken read(UUID id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'read'");
    }

    @Override
    public List<VerifyToken> readAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'readAll'");
    }

    @Override
    public void update(UUID id, VerifyToken t) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void delete(UUID id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }
    
}
