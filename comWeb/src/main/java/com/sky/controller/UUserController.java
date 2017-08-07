package com.sky.controller;

import com.sky.entity.UUser;
import com.sky.service.UUserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.jws.WebParam;

@Controller
@RequestMapping("/user")
public class UUserController {

	@Autowired
	private UUserService userService;

	@RequestMapping("/index")
	public String index(){
		add(new UUser());
		return "a";
	}

	public String add(UUser user){
		user=new UUser();
		user.setLoginName("aa");
		user.setName("bb");
		user.setPassword("cc");
		user.setRoleName("system");
		userService.insert(user);
		return "ok";
	}

	@RequestMapping("/getUserById")
	public String getUserById(){
		UUser user=userService.getById(2);

		userService.update(user);
		System.out.println("1_"+user.toString());
		return "../index";
	}

	public String update(UUser user){
		user=userService.getById(1);
		userService.update(user);
		return "ok";
	}
}