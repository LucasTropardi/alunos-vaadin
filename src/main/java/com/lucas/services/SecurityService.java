package com.lucas.services;

public interface SecurityService {
    public void save(String username, String password);
    public void logout();
    public boolean hasRole(String role);
}
