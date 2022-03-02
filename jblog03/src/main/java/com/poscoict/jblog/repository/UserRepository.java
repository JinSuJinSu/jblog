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
	
	// 유저 데이터 추가
	public boolean insert(UserVo vo) {
		// TODO Auto-generated method stub
		int count = sqlSession.insert("user.insert",vo);
		return count==1;
	}
	
	// 블로그 데이터 추가
	public boolean insertBlog(UserVo vo) {
		// TODO Auto-generated method stub
		int count = sqlSession.insert("user.insertBlog",vo);
		return count==1;
	}
	
	// 카테고리 데이터 추가
	public boolean insertCategory(UserVo vo) {
		// TODO Auto-generated method stub
		int count = sqlSession.insert("user.insertCategory",vo);
		return count==1;
	}
	
	// 유저 정보 찾기(아이디 중복 체크)
	public UserVo findUserWithId(String id) {
		UserVo userVo = sqlSession.selectOne("user.findUserWithId",id);
		return userVo;
	}
		
	// 유저 정보 찾기
	public UserVo findUser(String id, String password){
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("password", password);
		UserVo vo = sqlSession.selectOne("user.findUser",map);
		return vo;
	}


}
