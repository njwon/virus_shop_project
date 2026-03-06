<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="module/header.jsp"%>
<body data-page="login">
	<div class="grid-bg"></div>
	<div class="glow"></div>

	<div class="container">
		<a href="<%=request.getContextPath()%>" class="back-link"> <span>←</span>
			뒤로 가기
		</a>

		<div class="form-header">
			<h1>로그인</h1>
			<p class="form-subtitle">계정에 접속하세요</p>
		</div>

		<form name="loginMember"
			action='${pageContext.request.contextPath}/LogIn.do' method="post">
			<div class="form-group">
				<label for="id">아이디</label> 
				<input type="text" id="id" name="id" placeholder="Mrhong" required>
			</div>
			<div class="form-group">
				<label for="password">비밀번호</label> 
				<input type="password" id="pw" name="pw" placeholder="••••••••" required>
			</div>
			<div class="login-form-group">
				<label class="checkbox-container"> <input type="checkbox"
					id="remember" name="remember_me" /> <span class="checkbox-label">로그인
						유지하기</span>
				</label>
			</div>
			<button type="submit" class="form-submit">로그인</button>
		</form>

		<div class="form-footer">
			계정이 없으신가요? <a href="signup.jsp">회원가입</a>
		</div>
	</div>

	<script>
	<%String errorMsg = (String) request.getAttribute("message");%>
	
	<%if (errorMsg != null && !errorMsg.isEmpty()) {%>
		alert("<%=errorMsg%>");
	<%}%>
	</script>
	<%@ include file="module/footer.jsp"%>