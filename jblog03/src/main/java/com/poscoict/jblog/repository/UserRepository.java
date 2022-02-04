package com.poscoict.jblog.repository;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.poscoict.jblog.vo.UserVo;

@Repository
public class UserRepository {
	
	@Autowired
	private SqlSession sqlSession;
	
	public boolean insert(UserVo vo) {
		// TODO Auto-generated method stub
		int count = sqlSession.insert("user.insert",vo);
		return count==1;
	}
	
	public boolean insertBlog(UserVo vo) {
		// TODO Auto-generated method stub
		int count = sqlSession.insert("user.insertBlog",vo);
		return count==1;
	}
	
	public boolean insertCategory(UserVo vo) {
		// TODO Auto-generated method stub
		int count = sqlSession.insert("user.insertCategory",vo);
		return count==1;
	}
		
	public UserVo findUser(String id, String password){
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("password", password);
		UserVo vo = sqlSession.selectOne("user.findUser",map);
		return vo;
	}
//		
//	public boolean update(UserVo vo) {
//		int count = sqlSession.update("user.update", vo);
//		return count==1;
//	}
//		
//	
//	public UserVo findByNo(Long userNo) {
//		UserVo result = sqlSession.selectOne("user.findByNo", userNo);
//		return result;
//	}

}
