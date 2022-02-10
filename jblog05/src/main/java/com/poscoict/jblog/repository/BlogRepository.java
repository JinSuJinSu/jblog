package com.poscoict.jblog.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.poscoict.jblog.vo.BlogVo;
import com.poscoict.jblog.vo.CategoryVo;
import com.poscoict.jblog.vo.PostVo;

@Repository
public class BlogRepository {
	
	@Autowired
	private SqlSession sqlSession;
	
	// 블로그 이용 유저 얻기
	public List<String> findUsers(){
		List<String> list = sqlSession.selectList("blog.findUsers");
		return list;
	}

	// 메인게시판 카테고리별 제목 조회
	public List<PostVo> findPost(int categoryNo){
		List<PostVo> list =  sqlSession.selectList("blog.findPost", categoryNo);
		return list;
	}
	
	// 메인게시판 제목별 제목, 내용 조회
	public PostVo find(int no) {
		PostVo postVo = sqlSession.selectOne("blog.find",no);
		return postVo;
	}
	
	// 블로그 조회
	public BlogVo findBlog(String userId) {
		BlogVo blogVo = sqlSession.selectOne("blog.findBlog",userId);
		return blogVo;
	}
	
	// 블로그 수정
	public boolean updateBlog(BlogVo blogVo, String userId) {
		Map<String, Object> map = new HashMap<>();
		map.put("title", blogVo.getTitle());
		map.put("logo", blogVo.getLogo());
		map.put("userId", userId);
		int cnt = sqlSession.update("blog.updateBlog", map);
		return cnt==1;	
	}
	
	
	// 카테고리 조회
	public List<CategoryVo> findCategory(String blogId) {
		// TODO Auto-generated method stub
		List<CategoryVo> list = sqlSession.selectList("blog.findCategory",blogId);
		return list;
	}
	
	// 카테고리 게시글 개수 구하기
	public int findCategoryCount(int categoryNo) {
		int cnt = sqlSession.selectOne("blog.findCategoryCount",categoryNo);
		return cnt;
	}
	
	// 카테고리 목록 추가
	public boolean insertCategory(CategoryVo categoryVo) {
		int cnt = sqlSession.insert("blog.insertCategory", categoryVo);
		return cnt==1;
	}
	
	// 카테고리 삭제
	public boolean deleteCategory(int no, String blogId) {
		Map<String, Object> map = new HashMap<>();
		map.put("no", no);
		map.put("blogId", blogId);
		int cnt = sqlSession.delete("blog.deleteCategory", map);
		return cnt==1;
	}
	
	// 특정 카테고리 번호 조회
	public int findCategoryNo(String name, String blogId) {
		Map<String, Object> map = new HashMap<>();
		map.put("name", name);
		map.put("blogId", blogId);
		int cnt = sqlSession.selectOne("blog.findCategoryNo", map);
		return cnt;
	}
	
	// 카테고리 글 추가
	public boolean insert(PostVo postVo) {
		int cnt = sqlSession.insert("blog.insert", postVo);
		return cnt==1;
	}
	
	
	
	

}
