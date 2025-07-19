package com.cineflex.api.repository;

import java.util.List;
import java.util.UUID;

public interface RepositoryInterface<T> {
    
    public void create(T t);

    public T read(UUID id);

    public List<T> readAll();

    public List<T> readAll(Integer page, Integer size);

    public void update(UUID id, T t);

    public void delete(UUID... ids);

    public Integer getPageCount(Integer size);
}
