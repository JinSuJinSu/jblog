<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JBlog</title>
<Link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/jblog.css">
<link rel="stylesheet"
	href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<script type="text/javascript"
	src="${pageContext.request.contextPath }/assets/js/jquery/jquery-1.9.0.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script>
let messageBox = function(title, message, callback){
	$("#dialog-message p").html(message);
	$("#dialog-message")
	.attr("title",title)
	.dialog({
		width:300,
		modal: true,
		buttons: {
	        "확인": function() {
	          $(this).dialog("close");
	        }
	      },
		close:callback
	});
}

let renderCategory = function(vo, index, length) {
	let html = 
			"<tr>" + 
			"<td>" + (length-index) + "</td>" + 
			"<td>" + vo.name + "</td>" + 
			"<td>" + vo.cnt + "</td>" + 
			"<td>" + vo.descrpition + "</td>" + 
			"<td>" + 
			"<a href=" + "${pageContext.request.contextPath}/api/category/${blogId}?categoryNo=" + vo.no + "&cnt=" + vo.cnt + ">" +
			"<img src=" + "${pageContext.request.contextPath}/assets/images/delete.jpg" + "></a>" + 
			"</td>" +
			"</tr>";			
	 return html;		
}

let categoryList = function(){	
	$.ajax({
		url: '${pageContext.request.contextPath}/api/category/${blogId}',
		type: 'get',
		dataType: 'json',
		success: function(response) {
			if(response.result !== 'success') {
				console.error(response.message);
				return;
			}
			
			let data = response.data;
			let dataList = data.list;
			let length = dataList.length;
			for(let i=0; i<dataList.length; i++){
				dataList[i].cnt=data.cntList[i];
				$(".admin-cat tr").last().after(renderCategory(dataList[i],i,length));
			}			
		}
	});
};


$(function(){
	categoryList();
	
	// 카테고리 삭제 버튼 Click 이벤트 처리(Live Event)
	$(document).on('click', ".admin-cat tr td a", function(event) {
		event.preventDefault();		
	}); 
	
	$("#add-form").submit(function(event){
		event.preventDefault();
		
		// 이름 유효성 체크(empty) 체크
		if($("#category-name").val()==""){
			messageBox("카테고리 추가","카테고리 이름은 필수 항목입니다", function(){
				$("#category-name").focus();
			})
			return;
		}
		
		// 설명 유효성 체크(empty) 체크
		if($("#category-descrpition").val()==""){
			messageBox("카테고리 추가","카테고리 설명은 필수 항목입니다", function(){
				$("#category-descrpition").focus();
			})
			return;
		}
		let vo={}
		vo.name = $("#category-name").val();
		vo.descrpition = $("#category-descrpition").val();
		
		// 유효성 OK
		$.ajax({
			url: '${pageContext.request.contextPath}/api/category/${blogId}',
			type: 'post',
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify(vo),
			success: function(response) {
				if(response.result !== 'success') {
					console.error(response.message);
					return;
				}
				
				let data = response.data;
				data.cnt=0;
				let length=$(".admin-cat tr").length;
				$(".admin-cat tr").first().after(renderCategory(data,0,length));
				}			
		});
	});
});
</script>
</head>
<body>
	<div id="container">
		<c:import url="/WEB-INF/views/includes/blog_header.jsp" />
		<div id="wrapper">
			<div id="content" class="full-screen">
				<ul class="admin-menu">
					<li><a
						href="${pageContext.request.contextPath}/blog/${authUser.id}/admin/basic">기본설정</a></li>
					<li><a
						href="${pageContext.request.contextPath}/blog/${authUser.id}/admin/category">카테고리</a></li>
					<li class="selected">카테고리(ajax)</li>
					<li><a
						href="${pageContext.request.contextPath}/blog/${authUser.id}/admin/write">글작성</a></li>
				</ul>
				<table class="admin-cat">
					<tr>
						<th>번호</th>
						<th>카테고리명</th>
						<th>포스트 수</th>
						<th>설명</th>
						<th>삭제</th>
					</tr>
				</table>

				<h4 class="n-c">새로운 카테고리 추가</h4>
				<form id="add-form"
					action="${pageContext.request.contextPath}/blog/${authUser.id}/add"
					method="post">
					<table id="admin-cat-add">
						<tr>
							<td class="t">카테고리명</td>
							<td><input id="category-name" type="text" name="name"></td>
						</tr>
						<tr>
							<td class="t">설명</td>
							<td><input id="category-descrpition" type="text"
								name="descrpition"></td>
						</tr>
						<tr>
							<td class="s">&nbsp;</td>
							<td><input type="submit" value="카테고리 추가"></td>
						</tr>
					</table>
				</form>
			</div>
		</div>
		<div id="dialog-message" title="" style="display: none">
			<p style="line-height: 50px"></p>
		</div>
		<c:import url="/WEB-INF/views/includes/blog_footer.jsp" />

	</div>
</body>
</html>