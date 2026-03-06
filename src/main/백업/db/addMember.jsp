<%@page import="java.util.ArrayList"%>
<%@page import="dao.ProductRepository"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="dto.Product"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>       
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<%@ include file="../step09/header.jsp" %>
<title>products</title>
<style>
	html,body{
	height:3000px;
	}
</style>
</head>
<body class="blog-page">
	<%!
		DecimalFormat df = new DecimalFormat("#,###");
	%>
	<%
		String cartId = session.getId();//Jsession아이디 얻어옴
	%>

  <%@ include file="../step09/nav.jsp" %>

  <main class="main">

    <!-- Page Title -->
    <div class="page-title dark-background" style="background-image: url(assets/img/page-title-bg.webp);">
      <div class="container position-relative">
        <h1>회원가입</h1>
        <p>Esse dolorum voluptatum ullam est sint nemo et est ipsa porro placeat quibusdam quia assumenda numquam molestias.</p>
        <nav class="breadcrumbs">
          <ol>
            <li><a href="../step09/index.jsp">Home</a></li>
            <li class="current">Register</li>
          </ol>
        </nav>
      </div>
    </div><!-- End Page Title -->
	
	<!-- .container>.row-->
	<div class="container mt-5 pt-5">
		<div class="row">
			<form name="newMember" action='<c:url value="/member/processAddMember.jsp"/>'>
			  <div class="form-group">
			    <label for="id">아이디</label>
			    <input type="text" class="form-control" id="id" name="id">
			  </div>
			  <br/>
			  
			  <div class="form-group">
			    <label for="pw">비밀번호</label>
			    <input type="password" class="form-control" id="pw" name="pw">
			  </div>
			  <br />
			  
			  <div class="form-group">
			    <label for="pw_confirm">비밀번호 확인</label>
			    <input type="password" class="form-control" id="pw_confirm" name="pw_confirm">
			  </div>
			  <br />
			  
			  <div class="form-group">
			    <label for="name">이름</label>
			    <input type="text" class="form-control" id="name" name="name" required>
			  </div>
			  <br/>
			  
			  <div class="form-group">
			    <label for="name">성별</label>
			  	<div class="form-check">
				  <input class="form-check-input" type="radio" name="gender" id="gender1" value="남" checked>
				  <label class="form-check-label" for="gender1">
				    남성
				  </label>
				</div>
				<div class="form-check">
				  <input class="form-check-input" type="radio" name="gender" id="gender2" value="여">
				  <label class="form-check-label" for="gender2">
				    여성
				  </label>
				</div>
			 </div>
			 
			 <div class="form-group">
			    <label>생년월일</label>
			    <input type="date" name="birth"/>
			    <select class="custom-select my-1 mr-sm-2" id="inlineFormCustomSelectPref">
				    <option selected>출생 년도</option>
				 </select>
			  </div>
			  <br/>
			  
			  <div class="form-group">
			    <label>이메일</label>
			    <input type="text" name="mail1"/>
			    @
			    <select class="custom-select my-1 mr-sm-2" id="mail2" name="mail2">
				    <option selected>naver.com</option>
				    <option selected>gmail.com</option>
				    <option selected>daum.net</option>
				 </select>
			  </div>
			  <br/>
			  
			  <div class="form-group">
			    <label for="phone">연락처</label>
			    <input type="text" class="form-control" id="phone" name="phone">
			  </div>
			  <br/>
			  
			  <div class="form-group">
			    <label for="address">주소</label>
			    <input type="text" class="form-control" id="address" name="address">
			  </div>
			  <br/>
			
			 <input type="button" class="btn btn-info" value="회원가입" onclick="checkForm()"/>
			 <input type="reset" class="btn btn-warning" value="취소"/>
			</form>
		</div>
	</div>
</main>
<script>
function checkForm(){
	if(!document.newMember.id.value){
		alert("아이디를 입력해주세요.")
		return false;
	}
	if(!document.newMember.pw.value){
		alert("비번을 입력해주세요.")
		return false;
	}
	if(!document.newMember.pw_confirm.value){
		alert("비번확인을 입력해주세요.")
		return false;
	}
	if(document.newMember.pw.value != document.newMember.pw_confirm.value){
		alert("비번을 동일하게 입력해주세요.")
		return false;
	}
	if(!document.newMember.name.value){
		alert("이름을 입력해주세요.")
		return false;
	}
	document.newMember.submit();
}
</script>
	
	<%@ include file="..footer.jsp" %>
	
</body>
</html>