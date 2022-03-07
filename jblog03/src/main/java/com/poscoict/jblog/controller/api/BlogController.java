package com.poscoict.jblog.controller.api;

import java.util.ArrayList;
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
		List<CategoryVo> categoryList = (List<CategoryVo>) map.get("list"); // 카테고리 리스트 추출
		List<PostVo> postList = blogService.getPost(categoryList.get(0).getNo()); // 맨 처음 화면으로 들어갔을 때 제일 위에 해당하는 카테고리의 게시물을 가져온다.
		PostVo postVo = blogService.get(postList.get(0).getNo()); //카테고리를 클릭했을 때 제일 위에 있는 글 제목과 내용을 보여준다.
		
		List<Object> list = new ArrayList<>(); // 자료형이 달라 Object 형으로 바꾼 후 데이터를 넣어 JSON으로 보내준다
		list.add(postVo);
		list.addAll(postList);
		list.addAll(categoryList);
		
		return JsonResult.success(list);
	}
	
	@GetMapping("/post")
	public JsonResult CategoryNo(@PathVariable("id") String id,
			@RequestParam(value = "categoryNo", required = true, defaultValue = "-1") int categoryNo) {
		List<PostVo> postList = blogService.getPost(categoryNo); // 카테고리 번호에 해당하는 게시물을 가져온다.
		PostVo postVo = blogService.get(postList.get(0).getNo()); //카테고리를 클릭했을 때 제일 위에 있는 글 제목과 내용을 보여준다.
		
		List<Object> list = new ArrayList<>(); // 자료형이 달라 Object 형으로 바꾼 후 데이터를 넣어 JSON으로 보내준다.
		list.add(postVo);
		list.addAll(postList);
		
		return JsonResult.success(list);
	}
	
	@GetMapping("/postVo")
	public JsonResult PostNo(@PathVariable("id") String id,
			@RequestParam(value = "postNo", required = true, defaultValue = "-1") int postNo) {
		PostVo postVo = blogService.get(postNo); // 게시물 제목을 클릭했을 때 이에 해당하는 내용을 보여준다
		return JsonResult.success(postVo);
	}
	
	
	

}
