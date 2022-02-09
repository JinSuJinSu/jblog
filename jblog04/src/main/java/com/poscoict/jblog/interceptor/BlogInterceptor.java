package com.poscoict.jblog.interceptor;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.poscoict.jblog.service.BlogService;
import com.poscoict.jblog.vo.BlogVo;
import com.poscoict.jblog.vo.UserVo;

public class BlogInterceptor extends HandlerInterceptorAdapter{
	
	@Autowired
	private BlogService blogService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		String blogId=""; // 블로그 작성 유저 아이디
		Map pathVariables = (Map)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		if(pathVariables.containsKey("id")) { // 경로에 PathVariable인 id 가 있는 경우
			Object obj=pathVariables.get("id");
			blogId = (String)obj; // 아이디 추출
		}
		
		HttpSession session = request.getSession(true);
		UserVo authUser = (UserVo)session.getAttribute("authUser"); // 유저 객체
		
		BlogVo blogVo = blogService.getBlog(blogId);
		request.setAttribute("blogId", blogId);
		request.setAttribute("blogVo",blogVo);
		
		
		// 블로그에 없는 유저로 접근할 때
		List<String> userList = blogService.getUsers();
		if(!userList.contains(blogId)) {
			response.sendRedirect(request.getContextPath());
			return false;
		}
					
		if(authUser==null) {
			authUser = new UserVo();
			authUser.setId(""); // 비회원일 경우 null 포인트 에러 방지를 위해 빈문자열 셋팅
		}
		
		// 블로그 작성 유저와 로그인 유저가 같은지 확인하고 권한이 없을 경우 튕기게 해준다.
		// 단 읽기는 가능하며 편집 권한은 없다.(비회원 포함)
		String url = request.getRequestURI(); // 현재 매핑되어 있는 url을 받아온다.
		if(!authUser.getId().equals(blogId) && url.contains("admin")) {
			response.sendRedirect(request.getContextPath() +"/blog/" + blogId); // html 화면에 글자가 없어도 url로 접속할 수도 있으므로 이를 방지한다.
			return false;
		}
		return true;
	}
}

	


