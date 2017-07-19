package com.sky.service.impl;

import com.sky.dao.UUserMapper;
import com.sky.entity.UUser;
import com.sky.service.RedisService;
import com.sky.service.UUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service(value="uUserService")
public class UUserServiceImpl implements UUserService {

	@Autowired
	private UUserMapper userMapper;
	@Autowired
	private RedisService redisService;
	public final static String userClassName=UUser.class.getSimpleName();
	public Logger logger= LoggerFactory.getLogger(this.getClass());

	public List<UUser> getUsers(){
		List<UUser> list=userMapper.getList();
		return list;
	}
	public UUser getById(Integer id){
		UUser user=new UUser();

		return user;
	}

	@Override
	public void insert(UUser user) {

		userMapper.insert(user);
		Map map=new ConcurrentHashMap();
		map.put(user.getId(),user);
		redisService.setMap(userClassName,map,UUser.class);
	}

	public boolean update(UUser user) {
		try{
			if(user.getId() != null){
				userMapper.update(user);
				if(redisService.getMap(userClassName,UUser.class).containsKey(user.getId())){
					redisService.getMap(userClassName,UUser.class).put(user.getId().toString(),user);
				}else{
					Map map=new ConcurrentHashMap();
					map.put(user.getId(),user);
					redisService.setMap(userClassName,map,UUser.class);
				}
			}
		}catch (Exception e){
			logger.error("update user error:",e);
			return false;
		}
		return true;
	}


}
