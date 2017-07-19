package com.sky.service;

import com.sky.entity.UUser;

import java.util.List;

public interface UUserService {
    public List<UUser> getUsers();
    public UUser getById(Integer id);
    public void insert(UUser user);
    public boolean update(UUser user);
}
