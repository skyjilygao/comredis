package com.sky.service.impl;

import com.sky.dao.UUserMapper;
import com.sky.entity.UUser;
import com.sky.service.UUserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

@Service(value="uUserService")
public class UUserServiceImpl implements UUserService {

	@Autowired
	private UUserMapper uUserMapper;

	public UUser getUsers(){
		return null;
	}

}
