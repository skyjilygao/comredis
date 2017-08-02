package com.sky.service.impl;

import com.sky.dao.UUserMapper;
import com.sky.entity.UUser;
import com.sky.service.RedisService;
import com.sky.service.UUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.HashMap;
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
String testid="3";
	public List<UUser> getUsers(){
		List<UUser> list=userMapper.getList();
		return list;
	}
	public UUser getById(Integer id){
		//testid为测试数据，正式使用时需要替换
		UUser user=new UUser();
		testid=1+"";
		Map<String,UUser> map=redisService.getMap(userClassName,UUser.class);
		if(map!=null ){
			if(map.containsKey(testid)){
				user= map.get(testid);//test
			}else{
				user=userMapper.getById(Integer.parseInt(testid));
				map.put(String.valueOf(user.getId()),user);
				redisService.setMap(userClassName,  map,UUser.class);
			}
		}else{
			user=userMapper.getById(Integer.parseInt(testid));
			map=new HashMap();
			map.put(user.getId().toString(),user);
			redisService.setMap(userClassName,  map,UUser.class);
		}
		return user;
	}

	@Override
	public void insert(UUser user) {
		Map<String,UUser> map=new HashMap();
		userMapper.insert(user);
		if(redisService.containsKey(userClassName)){
			map=redisService.getMap(userClassName,UUser.class);
			map.put(String.valueOf(user.getId()),user);
			redisService.setMap(userClassName,  map,UUser.class);
		}else{
			map.put(user.getId().toString(),user);
			redisService.setMap(userClassName,  map,UUser.class);
		}
		testid= user.getId().toString();
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
