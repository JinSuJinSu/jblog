package com.poscoict.jblog.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.poscoict.jblog.service.BlogService;
import com.poscoict.jblog.vo.UserVo;

@Controller
public class MainController {
	
	@Autowired
	private BlogService blogService;
	
	@RequestMapping({"","/main"})
	public String index() {
		return "main/index";
	}
	
//	@RequestMapping({"/{id}"})
//	public String myblog(@PathVariable("id") String id, Model model) {
//		Map<String, Object> map = blogService.getCategorys(id);	
//		model.addAttribute("map", map);
//		return "blog/blog-main";
//	}
//	
	

}
