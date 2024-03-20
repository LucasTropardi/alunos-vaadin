package com.lucas.services;

import com.lucas.model.Status;

import java.util.List;

public interface StatusService {
    public void save(Status status);
    public List<Status> findAll();
}
