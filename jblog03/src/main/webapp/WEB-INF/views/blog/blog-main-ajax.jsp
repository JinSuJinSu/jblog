<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JBlog</title>
<Link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/jblog.css">
<link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<script type="text/javascript" src="${pageContext.request.contextPath }/assets/js/jquery/jquery-1.9.0.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script>

let renderCategory = function(vo) {
	let html = 
			"<li>" +
			"<a href=" + "${pageContext.request.contextPath}/api/blog/${blogId}?categoryNo=" + vo.no + 
			" category-no=" + vo.no + ">" + vo.name + "</a>" + 
			"</li>";			
	 return html;		
}

let renderPost = function(vo){
	let html = 
		"<li>" +
		"<a href=" + "${pageContext.request.contextPath}/api/blog/${blogId}/" + vo.no + 
		" post-no=" + vo.no + ">" + vo.title + "</a>" + 
		"<span>" + vo.regDate + "</span>"
		"</li>";		
 	return html;	
}

let renderPostVo = function(vo){
	let html = 
		"<h4>" + vo.title +"</h4>" +
		"<p>" + vo.content +"</p>";	
 	return html;	
}

let categoryList = function(){	
	$.ajax({
		url: '${pageContext.request.contextPath}/api/blog/${blogId}/category',
		type: 'get',
		dataType: 'json',
		success: function(response) {
			if(response.result !== 'success') {
				console.error(response.message);
				return;
			}
			response.data.forEach(element=>{
				if(response.data.indexOf(element)===0){
					$(".blog-content").append(renderPostVo(element));
				}
				else{
					if(element.title){
						$(".blog-list").append(renderPost(element));
					}
					else{
						$("#navigation ul").append(renderCategory(element));
					}
				}				
			});
		}
	});
};

let PostList = function(categoryNo){
	$.ajax({
		url: "${pageContext.request.contextPath}/api/blog/${blogId}/post?categoryNo=" + categoryNo,
		type: 'get',
		dataType: 'json',
		success: function(response) {
			if(response.result !== 'success') {
				console.error(response.message);
				return;
			}
			 $(".blog-list li").remove();
			response.data.forEach(element=>{
				if(response.data.indexOf(element)===0){
					$(".blog-content h4").remove();
					$(".blog-content p").remove();
					$(".blog-content").append(renderPostVo(element));
				}
				else{
					$(".blog-list").append(renderPost(element));
				}				
			});
		}
	});
};

let postVo = function(postNo){
	$.ajax({
		url: "${pageContext.request.contextPath}/api/blog/${blogId}/postVo?postNo=" + postNo,
		type: 'get',
		dataType: 'json',
		success: function(response) {
			if(response.result !== 'success') {
				console.error(response.message);
				return;
			}
			 $(".blog-content h4").remove();
			 $(".blog-content p").remove();
			 $(".blog-content").append(renderPostVo(response.data));	 		
		}
	});
};

$(function(){
	categoryList();
	
	// 카테고리 이벤트 처리
	$(document).on('click', "#navigation li a", function(event) {
		event.preventDefault();
		let categoryNo = $(this).attr("category-no");
		PostList(categoryNo);
	});
	
	// 포스트 이벤트 처리
	$(document).on('click', ".blog-list li a", function(event) {
		event.preventDefault();
		let postNo = $(this).attr("post-no");
		postVo(postNo);
	});

});
</script>
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
			</ul>
		</div>
		
		<c:import url="/WEB-INF/views/includes/blog_footer.jsp"/>
	</div>
</body>
</html>