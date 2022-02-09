package com.poscoict.jblog.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poscoict.jblog.repository.BlogRepository;
import com.poscoict.jblog.vo.BlogVo;
import com.poscoict.jblog.vo.CategoryVo;
import com.poscoict.jblog.vo.PostVo;

@Service
public class BlogService {
	
	@Autowired
	private BlogRepository blogRepository;
	
	// 블로그 이용 유저 얻기
	public List<String> getUsers(){
		List<String> list = blogRepository.findUsers();
		return list;
	}
	
	// 메인게시판 카테고리별 게시글 제목 얻기
	public List<PostVo> getPost(int categoryNo){
		List<PostVo> list = blogRepository.findPost(categoryNo);
		return list;
	}
	
	// 메인게시판 제목별 제목, 내용 정보 얻기
	public PostVo get(int no) {
		PostVo postVo = blogRepository.find(no);
		return postVo;
	}
	
	// 블로그 조회
	public BlogVo getBlog(String userId) {
		return blogRepository.findBlog(userId);
	}
	
	// 블로그 업데이트
	public boolean updateBlog(BlogVo blogVo, String userId) {
		return blogRepository.updateBlog(blogVo, userId);
	}
	
	// 카테고리 목록 조회
	public Map<String, Object> getCategorys(String blogId) {
		List<CategoryVo> list = blogRepository.findCategory(blogId);
		List<Integer> cntList = new ArrayList<>();
		for(CategoryVo vo : list) {
			int cnt = blogRepository.findCategoryCount(vo.getNo());
			cntList.add(cnt);
		}
		Map<String, Object> map = new HashMap<>();
		map.put("list", list);
		map.put("cntList", cntList);
		return map;
	}
	
	// 카테고리 번호 얻기
	public int getCategoryNo(String name, String blogId) {
		int cnt = blogRepository.findCategoryNo(name, blogId);
		return cnt;
	}
	
	// 카테고리 추가
	public boolean addCategory(CategoryVo CategoryVo) {
		return blogRepository.insertCategory(CategoryVo);
	}
	
	// 카테고리 제거
	public boolean removeCategory(int no, String blogId) {
		return blogRepository.deleteCategory(no, blogId);
	}
	
	// 카테고리에 글 작성
	public boolean write(PostVo postVo) {
		return blogRepository.insert(postVo);
	}
	
}
