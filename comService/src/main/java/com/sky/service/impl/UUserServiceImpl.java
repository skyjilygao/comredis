package com.sky.service.impl;

import com.sky.mapper.UUserMapper;
import com.sky.service.UUserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


@Service(value="uUserService")
public class UUserServiceImpl implements UUserService {

	@Autowired
	private UUserMapper uUserMapper;

}