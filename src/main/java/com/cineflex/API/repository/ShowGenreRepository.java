package com.cineflex.API.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.cineflex.API.model.ShowGenre;

@Repository
public class ShowGenreRepository implements RepositoryInterface<ShowGenre> {

    @Override
    public void create(ShowGenre t) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

    @Override
    public ShowGenre read(UUID id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'read'");
    }

    @Override
    public List<ShowGenre> readAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'readAll'");
    }

    @Override
    public void update(UUID id, ShowGenre t) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void delete(UUID id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }
    
}
