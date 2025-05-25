package com.cineflex.API.repository;

import java.util.List;
import java.util.UUID;

public interface RepositoryInterface<T> {
    
    public void create(T t);

    public T read(UUID id);

    public List<T> readAll();

    public void update(UUID id, T t);

    public void delete(UUID... ids);

}
