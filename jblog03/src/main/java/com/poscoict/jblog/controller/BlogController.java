package com.poscoict.jblog.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.poscoict.jblog.security.Auth;
import com.poscoict.jblog.security.AuthUser;
import com.poscoict.jblog.service.BlogService;
import com.poscoict.jblog.service.FileUploadService;
import com.poscoict.jblog.vo.BlogVo;
import com.poscoict.jblog.vo.CategoryVo;
import com.poscoict.jblog.vo.PostVo;
import com.poscoict.jblog.vo.UserVo;

@Controller
@RequestMapping("/blog/{id:(?!assets|images).*}")
public class BlogController {
	
	// test

	@Autowired
	private BlogService blogService;

	@Autowired
	private FileUploadService fileUploadService;

	// 블로그 메인 화면 
	@RequestMapping({ "", "/{pathNo1}", "/{pathNo1}/{pathNo2}" })
//	@RequestMapping({""})
	public String index(Model model, @PathVariable("id") String id, @PathVariable("pathNo1") Optional<Integer> pathNo1,
			@PathVariable("pathNo2") Optional<Integer> pathNo2) {

		Integer categoryNo = 0;
		Integer postNo = 0;

		if (pathNo1.isPresent()) {
			categoryNo = pathNo1.get();
		}

		if (pathNo2.isPresent()) {
			categoryNo = pathNo1.get();
			postNo = pathNo2.get();
		}

		Map<String, Object> map = blogService.getCategorys(id); // 카테고리 리스트, 카테고리 글 개수 리스트 받아오기

		if (categoryNo == 0) {
			List<CategoryVo> categoryList = (List<CategoryVo>) map.get("list"); // 카테고리 리스트(맨 처음 화면이 나올 때)
			categoryNo = categoryList.get(0).getNo();
		}
		List<PostVo> list = blogService.getPost(categoryNo); // 카테고리 번호에 해당하는 게시물을 가져온다.

		if (postNo == 0) {
			if (!list.isEmpty()) { // 카테고리 게시물이 없을 경우 로직을 수행하지 않는다.
				postNo = list.get(0).getNo(); // 카테고리 게시물 중에서 맨 위에 있는 값을 가져온다
			}
		}

		PostVo postVo = blogService.get(postNo); // 게시물 제목을 클릭했을 때 이에 해당하는 내용을 보여준다.
		model.addAttribute("list", list); // 카테고리별 게시물
		model.addAttribute("map", map); // 카테고리 리스트
		model.addAttribute("categoryNo", categoryNo); // 카테고리 번호
		model.addAttribute("postVo", postVo); // 특정 게시물
		return "blog/blog-main";
	}
	
	@RequestMapping({"/ajax"})
	public String spa(Model model) {
		return "blog/blog-main-ajax";
	}
	
	@Auth
	@RequestMapping({"/admin/category/ajax"})
	public String categorySpa(Model model) {
		return "blog/blog-admin-category-ajax";
	}

	// 기본 설정 화면으로 이동
	@Auth
	@RequestMapping(value = "/admin/basic", method = RequestMethod.GET)
	public String basic(@AuthUser UserVo authUser, @PathVariable("id") String id) {
		return "blog/blog-admin-basic";
	}

	// 기본 설정 화면 업데이트
	@Auth
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(BlogVo blogVo, @RequestParam(value = "file") MultipartFile multipartFile, Model model,
			@AuthUser UserVo authUser) {

		if (blogVo.getTitle() == null || blogVo.getTitle().equals("")) {
			return "redirect:/blog/" + authUser.getId();
		}
		String url = fileUploadService.restore(multipartFile);
		if (url == null) {
			BlogVo originVo = blogService.getBlog(authUser.getId()); // 사진을 바꾸지 않을 경우 기존에 있는 사진 url을 가져온다.
			url = originVo.getLogo(); // url 값 바꾸기
		}
		blogVo.setLogo(url);
		blogService.updateBlog(blogVo, authUser.getId());
		return "redirect:/blog/" + authUser.getId();
	}

	// 유저의 카테고리 목록 조회
	@Auth
	@RequestMapping(value = "/admin/category", method = RequestMethod.GET)
	public String category(Model model, @AuthUser UserVo authUser, @PathVariable("id") String id) {

		Map<String, Object> map = blogService.getCategorys(authUser.getId());
		model.addAttribute("map", map);
		return "blog/blog-admin-category";
	}

	// 유저 카테고리 목록 추가
	@Auth
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(CategoryVo categoryVo, @AuthUser UserVo authUser) {
		if (categoryVo.getName() == null || categoryVo.getName().equals("") || categoryVo.getDescrpition() == null || categoryVo.getDescrpition().equals("")) {
			return "redirect:/blog/" + authUser.getId();
		}
		categoryVo.setBlogId(authUser.getId());
		blogService.addCategory(categoryVo);
		return "redirect:/blog/" + authUser.getId();
	}

	// 카테고리 목록 삭제
	@Auth
	@RequestMapping(value = "/delete/{no}", method = RequestMethod.GET)
	public String delete(@PathVariable("no") int no, @AuthUser UserVo authUser) {
		blogService.removeCategory(no, authUser.getId());
		return "redirect:/blog/" + authUser.getId();
	}

	// 글 작성 화면으로 이동 후 조회
	@Auth
	@RequestMapping(value = "/admin/write", method = RequestMethod.GET)
	public String write(Model model, @AuthUser UserVo authUser, @PathVariable("id") String id) {
		Map<String, Object> map = blogService.getCategorys(authUser.getId());
		model.addAttribute("map", map);
		return "blog/blog-admin-write";
	}

	// 카테고리에 글 추가
	@Auth
	@RequestMapping(value = "/write", method = RequestMethod.POST)
	public String add(PostVo postVo, @AuthUser UserVo authUser,
			@RequestParam(value = "category", required = true, defaultValue = "미분류") String category) {
		if (postVo.getTitle() == null || postVo.getTitle().equals("") || postVo.getContent() == null || postVo.getContent().equals("")) {
			return "redirect:/blog/" + authUser.getId();
		}
		int categoryNo = blogService.getCategoryNo(category, authUser.getId());
		postVo.setCategoryNo(categoryNo);
		boolean result = blogService.write(postVo);
		return "redirect:/blog/" + authUser.getId();
	}

}
