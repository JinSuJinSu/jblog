package com.poscoict.jblog.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.poscoict.jblog.repository.FileUploadService;
import com.poscoict.jblog.service.BlogService;
import com.poscoict.jblog.vo.BlogVo;
import com.poscoict.jblog.vo.CategoryVo;
import com.poscoict.jblog.vo.PostVo;
import com.poscoict.jblog.vo.UserVo;

@Controller
@RequestMapping("/blog")
public class BlogController {
	
	@Autowired
	private BlogService blogService;
	
	@Autowired
	private FileUploadService fileUploadService;
	
	@RequestMapping({"","/{id}"})
	public String index(Model model, HttpSession session) {
		UserVo userVo = (UserVo)session.getAttribute("authUser");
		Map<String, Object> map = blogService.getCategorys(userVo.getId());
		List<CategoryVo> categoryList = (List<CategoryVo>)map.get("list");
		List<PostVo> list = blogService.getPost(categoryList.get(0).getNo());
		model.addAttribute("list", list);
		model.addAttribute("map", map);
		if(!list.isEmpty()) {
			PostVo postVo = blogService.get(list.get(0).getNo());
			model.addAttribute("postVo", postVo);
		}
		return "blog/blog-main";
	}
	
	@RequestMapping({"/{id}/{categoryNo}"})
	public String post(Model model, HttpSession session,
			@PathVariable("id") String id, @PathVariable("categoryNo") int categoryNo) {
		UserVo userVo = (UserVo)session.getAttribute("authUser");
		Map<String, Object> map = blogService.getCategorys(userVo.getId());
		List<PostVo> list = blogService.getPost(categoryNo);
		model.addAttribute("list", list);
		model.addAttribute("map", map);
		model.addAttribute("categoryNo", categoryNo);
		if(!list.isEmpty()) {
			PostVo postVo = blogService.get(list.get(0).getNo());
			model.addAttribute("postVo", postVo);
		}
		return "blog/blog-main";
	}
	
	@RequestMapping({"/{id}/{categoryNo}/{no}"})
	public String postDetail(Model model, HttpSession session,
			@PathVariable("id") String id, @PathVariable("categoryNo") int categoryNo, @PathVariable("no") int no) {
		UserVo userVo = (UserVo)session.getAttribute("authUser");
		Map<String, Object> map = blogService.getCategorys(userVo.getId());
		List<PostVo> list = blogService.getPost(categoryNo);
		PostVo postVo = blogService.get(no);
		model.addAttribute("list", list);
		model.addAttribute("map", map);
		model.addAttribute("postVo", postVo);
		return "blog/blog-main";
	}
	
	// 기본 설정 화면으로 이동
	@RequestMapping(value="/basic", method=RequestMethod.GET)
	public String basic(Model model, HttpSession session) {
		UserVo userVo = (UserVo)session.getAttribute("authUser");
		BlogVo blogVo = blogService.getBlog(userVo.getId());
		model.addAttribute("blogVo", blogVo);
		return "blog/blog-admin-basic";
	}
	
	// 기본 설정 화면 업데이트
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public String update(BlogVo blogVo, @RequestParam(value="file") MultipartFile multipartFile, 
			Model model, HttpSession session) {
		UserVo userVo = (UserVo)session.getAttribute("authUser");
		String url = fileUploadService.restore(multipartFile);
		if(url==null) {
			BlogVo originVo = blogService.getBlog(userVo.getId());
			url = originVo.getLogo();
		}
		blogVo.setLogo(url);
		boolean result = blogService.updateBlog(blogVo,userVo.getId());
		model.addAttribute("blogVo", blogVo);
		return "redirect:/blog";
	}
	
	
	// 유저의 카테고리 목록 조회
	@RequestMapping(value="/category", method=RequestMethod.GET)
	public String category(Model model, HttpSession session) {
		UserVo userVo = (UserVo)session.getAttribute("authUser");
		Map<String, Object> map = blogService.getCategorys(userVo.getId());	
		model.addAttribute("map", map);
		return "blog/blog-admin-category";
	}
	
	// 유저 카테고리 목록 추가
	@RequestMapping(value="/add", method=RequestMethod.POST)
	public String add(CategoryVo categoryVo, HttpSession session) {
		UserVo userVo = (UserVo)session.getAttribute("authUser");
		categoryVo.setBlogId(userVo.getId());
		blogService.addCategory(categoryVo);
		return "redirect:/blog";
	}
	
	// 카테고리 목록 삭제
	@RequestMapping(value="/delete/{no}", method=RequestMethod.GET)
	public String delete(@PathVariable("no") int no, HttpSession session) {
		UserVo userVo = (UserVo)session.getAttribute("authUser");
		blogService.removeCategory(no, userVo.getId());
		return "redirect:/blog";
	}
	
	
	// 글 작성 화면으로 이동 후 조회
	@RequestMapping(value="/write", method=RequestMethod.GET)
	public String write(Model model, HttpSession session) {
		UserVo userVo = (UserVo)session.getAttribute("authUser");
		Map<String, Object> map = blogService.getCategorys(userVo.getId());
		model.addAttribute("map", map);
		return "blog/blog-admin-write";
	}
	
	// 카테고리에 글 추가
	@RequestMapping(value="/write", method=RequestMethod.POST)
	public String add(PostVo postVo, HttpSession session,
			@RequestParam(value="category", required=true, defaultValue="미분류") String category) {	
		UserVo userVo = (UserVo)session.getAttribute("authUser");
		int categoryNo = blogService.getCategoryNo(category, userVo.getId());
		postVo.setCategoryNo(categoryNo);
		boolean result = blogService.write(postVo);
		return "redirect:/blog";
	}
	
	
	

	
	
	
	
	
	

}
