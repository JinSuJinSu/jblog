<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JBlog</title>
<Link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/jblog.css">
<link rel="stylesheet" href="//code.jquery.com/ui/1.13.1/themes/base/jquery-ui.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/jquery/jquery-1.9.0.js"></script>
<script src="https://code.jquery.com/ui/1.13.1/jquery-ui.js"></script>
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

$(function(){
	
	$("#join-form").submit(function(event){
		event.preventDefault();
		
		// 이름 유효성 체크(empty) 체크
		if($("#name").val()==""){
			messageBox("회원가입","이름은 필수 항목입니다", function(){
				$("#name").focus();
			})
			return;
		}
		
		// 아이디 유효성(empty) 체크
		if($("#id").val()==""){
			messageBox("회원가입","아이디는 필수 항목입니다", function(){
				$("#id").focus();
			})
			return;
		}
		
		// 중복체크 유무
		if(!$("img-checkid").is("visible")){
			messageBox("회원가입","아이디 중복 확인을 해주세요.",function(){
				$("#id").focus();
			})
			return;
		}
		
		// 비밀번호 유효성(empty) 체크
		if($("#password").val()==""){
			messageBox("회원가입","비밀번호는 필수 항목입니다", function(){
				$("#password").focus();
			})
			return;
		}
		
		// 유효성 OK
		console.log("OK!!");
		$("#join-form")[0].submit();
	})
	
	
	$("#id").change(function(){
		$("#img-checkid").hide();
		$("#btn-checkid").show();
	});
	
	$("#btn-checkid").click(function(){
		let id = $("#id").val();
		if(id===""){
			return;
		}
		$.ajax({
			url: "${pageContext.request.contextPath}/user/api/checkid?id=" + id,
			type: "get",
			dataType: "json",
			success: function(response) {
				if(response.result!=="success"){
					console.error(response.message);
					return;
				}
				if(response.data){
					messageBox("아이디 중복 확인","존재하는 아이디입니다.<br>다른 아이디를 사용해주세요",function(){
						$("#id").val("").focus();
					})
					return ;
				}
				
				$("#img-checkid").show();
				$("#btn-checkid").hide();
				
			},
			error: function(xhr, status, e) {
				console.error(status, e);
			}
		});
	});
});
</script>
</head>
<body>
	<div class="center-content">
		<c:import url="/WEB-INF/views/includes/main_menu.jsp"/>
			
			<form:form
					modelAttribute="userVo" 
					class="join-form"
					id="join-form" 
					name="joinForm" 
					method="post" 
					action="${pageContext.request.contextPath }/user/join">
					
			<label class="block-label" for="name">이름</label>
			<form:input path="name" />
			<p style="text-align:left; padding-left:0; color:#f00">
				<form:errors path="name" />
			</p>	
	
			<label class="block-label" for="blog-id">아이디</label>
			<form:input path="id" />
			<input id="btn-checkid" type="button" value="중복체크">
			<img id="img-checkid" style="display: none;" width="16px" src="${pageContext.request.contextPath}/assets/images/check.png">
			<p style="text-align:left; padding-left:0; color:#f00">
				<form:errors path="id" />
			</p>


			<label class="block-label" for="password">패스워드</label>
			<form:password path="password" />
			<p style="text-align:left; padding-left:0; color:#f00">
				<form:errors path="password" />
			</p>	
			

			<fieldset>
				<legend>약관동의</legend>
				<input id="agree-prov" type="checkbox" name="agreeProv" value="y">
				<label class="l-float">서비스 약관에 동의합니다.</label>
			</fieldset>

			<input type="submit" value="가입하기">
			</form:form>
			
			<div id="dialog-message" title="" style="display:none">
				<p style="line-height:50px"></p>
			</div>

	</div>
</body>
</html>
