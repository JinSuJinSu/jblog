<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JBlog</title>
<Link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/jblog.css">
</head>
<body>
	<div id="container">
		<div id="header">
			<h1>${blogVo.title}</h1>
				<ul>
					<c:choose>
						<c:when test="${empty authUser}">
							<li><a href="${pageContext.request.contextPath}/user/login">로그인</a></li>
						</c:when>
						<c:otherwise>	
							<li><a href="${pageContext.request.contextPath}/user/logout">로그아웃</a></li>
							<c:if test="${authUser.id==blogId}">
								<li><a href="${pageContext.request.contextPath}/blog/${authUser.id}/admin/basic">블로그 관리</a></li>
							</c:if>
						</c:otherwise>
					</c:choose>
				</ul>
		</div>
		<div id="wrapper">
			<div id="content">
				<div class="blog-content">
					<h4>${postVo.title}</h4>
					<p>${postVo.content}</p>
				</div>
				<ul class="blog-list">
					<c:forEach items="${list}" var="vo" varStatus="status">
						<li><a href="${pageContext.request.contextPath}/blog/${blogId}/${categoryNo}/${vo.no}">${vo.title}</a><span>${vo.regDate}</span><li>
					</c:forEach>
				</ul>
			</div>
		</div>

		<div id="extra">
			<div class="blog-logo">
				<img src="${pageContext.request.contextPath}${blogVo.logo}">
			</div>
		</div>

		<div id="navigation">
			<h2>카테고리</h2>
			<ul>
				<c:forEach items="${map.list}" var="vo" varStatus="status">
		      		<li><a href="${pageContext.request.contextPath}/blog/${blogId}/${vo.no}">${vo.name}</a></li>
	      		</c:forEach>
			</ul>
		</div>
		
		<c:import url="/WEB-INF/views/includes/blog_footer.jsp"/>
	</div>
</body>
</html>