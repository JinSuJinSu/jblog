package com.poscoict.jblog.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.poscoict.jblog.repository.FileUploadService;
import com.poscoict.jblog.security.Auth;
import com.poscoict.jblog.security.AuthUser;
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
	
	@Autowired
	private ServletContext servletContext;
	
	@RequestMapping({"/{id}"})
	public String index(Model model, @PathVariable("id") String id) {	
		Map<String, Object> map = blogService.getCategorys(id);
		List<CategoryVo> categoryList = (List<CategoryVo>)map.get("list");
		List<PostVo> list = blogService.getPost(categoryList.get(0).getNo());
		BlogVo blogVo = blogService.getBlog(id);
		servletContext.setAttribute("blogId", id);
		servletContext.setAttribute("blogVo", blogVo);
		model.addAttribute("list", list);
		model.addAttribute("map", map);
		model.addAttribute("categoryNo", categoryList.get(0).getNo());
		if(!list.isEmpty()) {
			PostVo postVo = blogService.get(list.get(0).getNo());
			model.addAttribute("postVo", postVo);
		}
		return "blog/blog-main";
	}
	
	@RequestMapping({"/{id}/{categoryNo}"})
	public String post(Model model, @PathVariable("id") String id, @PathVariable("categoryNo") int categoryNo) {
		Map<String, Object> map = blogService.getCategorys(id);
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
	public String postDetail(Model model, @PathVariable("id") String id, @PathVariable("categoryNo") int categoryNo, @PathVariable("no") int no) {
		Map<String, Object> map = blogService.getCategorys(id);
		List<PostVo> list = blogService.getPost(categoryNo);
		PostVo postVo = blogService.get(no);
		model.addAttribute("list", list);
		model.addAttribute("map", map);
		model.addAttribute("postVo", postVo);
		return "blog/blog-main";
	}
	
	// 기본 설정 화면으로 이동
	@Auth
	@RequestMapping(value="/basic", method=RequestMethod.GET)
	public String basic(@AuthUser UserVo authUser) {
		String blogId = (String)servletContext.getAttribute("blogId");
		if(!authUser.getId().equals(blogId)) {
			return "redirect:/blog/" + blogId; // html 화면에 글자가 없어도 url로 접속할 수도 있으므로 이를 방지한다.
		}	
		return "blog/blog-admin-basic";
	}
	
	// 기본 설정 화면 업데이트
	@Auth
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public String update(BlogVo blogVo, @RequestParam(value="file") MultipartFile multipartFile, Model model, @AuthUser UserVo authUser) {
		if(blogVo.getTitle()==null || blogVo.getTitle().equals("")){
			return "redirect:/blog/" + authUser.getId();
		}	
		
		String url = fileUploadService.restore(multipartFile);
		if(url==null) {
			BlogVo originVo = blogService.getBlog(authUser.getId());
			url = originVo.getLogo();
		}
		blogVo.setLogo(url);
		blogService.updateBlog(blogVo,authUser.getId());
		return "redirect:/blog/" + authUser.getId();
	}
	
	
	// 유저의 카테고리 목록 조회
	@Auth
	@RequestMapping(value="/category", method=RequestMethod.GET)
	public String category(Model model, @AuthUser UserVo authUser) {
		String blogId = (String)servletContext.getAttribute("blogId");
		if(!authUser.getId().equals(blogId)) {
			return "redirect:/blog/" + blogId; // html 화면에 글자가 없어도 url로 접속할 수도 있으므로 이를 방지한다.
		}	
		Map<String, Object> map = blogService.getCategorys(authUser.getId());	
		model.addAttribute("map", map);
		return "blog/blog-admin-category";
	}
	
	// 유저 카테고리 목록 추가
	@Auth
	@RequestMapping(value="/add", method=RequestMethod.POST)
	public String add(CategoryVo categoryVo, @AuthUser UserVo authUser) {		
		if(categoryVo.getName()==null || categoryVo.getName().equals("") || 
				categoryVo.getDescrpition()==null || categoryVo.getDescrpition().equals("")) {
			return "redirect:/blog/" + authUser.getId();
		}		
		categoryVo.setBlogId(authUser.getId());
		blogService.addCategory(categoryVo);
		return "redirect:/blog/" + authUser.getId();
	}
	
	// 카테고리 목록 삭제
	@Auth
	@RequestMapping(value="/delete/{no}", method=RequestMethod.GET)
	public String delete(@PathVariable("no") int no, @AuthUser UserVo authUser) {
		blogService.removeCategory(no, authUser.getId());
		return "redirect:/blog/" + authUser.getId();
	}
	
	
	// 글 작성 화면으로 이동 후 조회
	@Auth
	@RequestMapping(value="/write", method=RequestMethod.GET)
	public String write(Model model, @AuthUser UserVo authUser) {
		String blogId = (String)servletContext.getAttribute("blogId");
		if(!authUser.getId().equals(blogId)) {
			return "redirect:/blog/" + blogId; // html 화면에 글자가 없어도 url로 접속할 수도 있으므로 이를 방지한다.
		}	
		Map<String, Object> map = blogService.getCategorys(authUser.getId());
		model.addAttribute("map", map);
		return "blog/blog-admin-write";
	}
	
	// 카테고리에 글 추가
	@Auth
	@RequestMapping(value="/write", method=RequestMethod.POST)
	public String add(PostVo postVo, @AuthUser UserVo authUser,
			@RequestParam(value="category", required=true, defaultValue="미분류") String category) {
		if(postVo.getTitle()==null || postVo.getTitle().equals("") || 
				postVo.getContent()==null || postVo.getContent().equals("")) {
			return "redirect:/blog/" + authUser.getId();
		}	
		int categoryNo = blogService.getCategoryNo(category, authUser.getId());
		postVo.setCategoryNo(categoryNo);
		boolean result = blogService.write(postVo);
		return "redirect:/blog/" + authUser.getId();
	}

}
