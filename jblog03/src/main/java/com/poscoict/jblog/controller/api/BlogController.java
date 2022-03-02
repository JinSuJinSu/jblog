package com.poscoict.jblog.controller.api;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.poscoict.jblog.dto.JsonResult;
import com.poscoict.jblog.service.BlogService;
import com.poscoict.jblog.vo.CategoryVo;
import com.poscoict.jblog.vo.PostVo;



@RestController("blogMainApiController")
@RequestMapping("/api/blog/{id:(?!assets|images).*}")
public class BlogController {
	
	@Autowired
	private BlogService blogService;
	
	@GetMapping("/category")
	public JsonResult Category(@PathVariable("id") String id) {
		Map<String, Object> map = blogService.getCategorys(id); // 카테고리 리스트, 카테고리 글 개수 리스트 받아오기
		List<CategoryVo> categoryList = (List<CategoryVo>) map.get("list"); // 카테고리 리스트(맨 처음 화면이 나올 때)
		return JsonResult.success(categoryList);
	}
	
	@GetMapping("")
	public JsonResult CategoryNo(@PathVariable("id") String id,
			@RequestParam(value = "categoryNo", required = true, defaultValue = "-1") int categoryNo) {
		List<PostVo> list = blogService.getPost(categoryNo); // 카테고리 번호에 해당하는 게시물을 가져온다.
		return JsonResult.success(list);
	}
	
	@GetMapping("")
	public JsonResult PostNo(@PathVariable("id") String id,
			@RequestParam(value = "postNo", required = true, defaultValue = "-1") int postNo) {
		PostVo postVo = blogService.get(postNo); // 게시물 제목을 클릭했을 때 이에 해당하는 내용을 보여준다
		return JsonResult.success(postVo);
	}
	
	
	

}
