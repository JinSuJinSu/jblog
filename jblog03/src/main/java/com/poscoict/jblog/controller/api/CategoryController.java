package com.poscoict.jblog.controller.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.poscoict.jblog.dto.JsonResult;
import com.poscoict.jblog.security.AuthUser;
import com.poscoict.jblog.service.BlogService;
import com.poscoict.jblog.vo.CategoryVo;
import com.poscoict.jblog.vo.UserVo;

@RestController("categoryApiController")
@RequestMapping("/api/category/{id:(?!assets|images).*}")
public class CategoryController {
	
	@Autowired
	private BlogService blogService;
	
	@GetMapping("")
	public JsonResult Category(@AuthUser UserVo authUser, @PathVariable("id") String id) {
		Map<String, Object> map = blogService.getCategorys(authUser.getId());
		return JsonResult.success(map);
	}
	
	@PostMapping("")
	public JsonResult Category(@RequestBody CategoryVo categoryVo, @AuthUser UserVo authUser) {
		System.out.println("categoryVo" + categoryVo);
		categoryVo.setBlogId(authUser.getId());
		blogService.addCategory(categoryVo);
		return JsonResult.success(categoryVo);
	}
	
	@DeleteMapping("")
	public JsonResult Category(
			@RequestParam(value="no", required=true, defaultValue="-1") int no,
			@RequestParam(value="cnt", required=true, defaultValue="-1") int cnt,
			@AuthUser UserVo authUser) {
		boolean result=false;
		if(cnt==0) {
			blogService.removeCategory(no, authUser.getId());
			result=true;
		}
		return JsonResult.success(result);
	}

}
