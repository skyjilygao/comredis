package com.sky.dao;
import com.sky.entity.UUser;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UUserMapper {
    public void insert(UUser user);
    public void update(UUser user);
    public void delete(Integer id);
    public UUser getById(Integer id);
    public List getList();
}
