package com.poscoict.jblog.controller;


import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.poscoict.jblog.service.UserService;
import com.poscoict.jblog.vo.UserVo;
@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value="/join", method=RequestMethod.GET)
	public String join() {
		return "user/join";
	}
	
	@RequestMapping(value="/join", method=RequestMethod.POST)
	public String join(UserVo userVo) {
		System.out.println(userVo);
		if(userVo.getId()==null || userVo.getId().equals("") || userVo.getName()==null || userVo.getName().equals("") || 
				userVo.getPassword()==null || userVo.getPassword().equals("")) {
			return "redirect:/user/join";
		}
		userService.join(userVo);
		return "redirect:/user/joinsuccess";
	}
	
	@RequestMapping(value="/joinsuccess", method=RequestMethod.GET)
	public String joinsuccess() {
		return "user/joinsuccess";
	}
	
	@RequestMapping(value="/login", method=RequestMethod.GET)
	public String login() {
		return "user/login";
	}


	

}
