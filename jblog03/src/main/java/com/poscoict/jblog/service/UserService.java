package com.poscoict.jblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poscoict.jblog.repository.UserRepository;
import com.poscoict.jblog.vo.UserVo;


@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;

	// 회원가입
	public void join(UserVo userVo) {
		userRepository.insert(userVo);
		userRepository.insertBlog(userVo); // 회원가입시 디폴트 블로그 제목, 블로그 사진 데이터를 추가해준다.
		userRepository.insertCategory(userVo); // 회원가입시 디폴트 카테고리 제목, 카테고리 내용 데이터를 추가해준다.
	}
	
	// 회원가입 전 중복 체크
	public UserVo checkUser(String id) {
		UserVo userVo = userRepository.findUserWithId(id);
		return userVo;
	}

	// 로그인 시 유저 정보 얻기
	public UserVo getUser(String id, String password) {
		return userRepository.findUser(id, password);
	}
	
}
