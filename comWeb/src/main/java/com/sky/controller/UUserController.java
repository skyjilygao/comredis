package com.sky.controller;

import com.sky.service.UUserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UUserController {

	@Autowired
	private UUserService uUserService;

	@RequestMapping("/index")
	public String index(){
		return "a";
	}
}