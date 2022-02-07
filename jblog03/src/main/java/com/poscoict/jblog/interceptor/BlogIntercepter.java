package com.poscoict.jblog.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.poscoict.jblog.vo.BlogVo;

public class BlogIntercepter extends HandlerInterceptorAdapter{
	

	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		BlogVo blogVo = (BlogVo)session.getAttribute("site");
		if(blogVo==null) {
			blogVo = blogService.getBlog(id);
			request.getServletContext().setAttribute("site",siteVo);
		}
		return true;
	}
}
	

}
