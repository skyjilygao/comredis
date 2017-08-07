package com.sky.service.impl;

import com.sky.commons.EntityUtils;
import com.sky.dao.UUserMapper;
import com.sky.entity.UUser;
import com.sky.service.RedisService;
import com.sky.service.UUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

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
	private Jedis jedis=new Jedis();
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
			//方法1：使用序列化 map。缺点是，增加了序列化/反序列化的开销，并且在需要修改其中一项信息时，
			// 需要把整个对象取回，并且修改操作需要对并发进行保护，引入CAS等复杂问题。
//			if(user.getId() != null){
//				//更新数据库数据
//				userMapper.update(user);
//				//更新redis
//				if(redisService.getMap(userClassName,UUser.class).containsKey(user.getId())){
//
//					redisService.getMap(userClassName,UUser.class).put(user.getId().toString(),user);
//				}else{
//					Map map=new ConcurrentHashMap();
//					map.put(user.getId(),user);
//					redisService.setMap(userClassName,map,UUser.class);
//				}
//			}
			//方法2：使用redis的hash存储
			if(user.getId() != null){
				//更新数据库数据
				userMapper.update(user);
				//更新redis
				Map map=EntityUtils.objectToHash(user);
				Map<String,String> map1=redisService.getHash(user.getId().toString());
				user.setRoleName("999999");
				map=EntityUtils.objectToHash(user);
				//更新hash
				redisService.cacheHash(user.getId().toString(),map);
				Map<String,String> map2=redisService.getHash(user.getId().toString());
				UUser user1=EntityUtils.hashToObject(map1,UUser.class);
			}
		}catch (Exception e){
			logger.error("update user error:",e);
			return false;
		}
		return true;
	}


}
