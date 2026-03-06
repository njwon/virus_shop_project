<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="dto.ProductDTO"%>
<%@ include file="module/header.jsp"%>
<%
MemberDTO user = (MemberDTO) request.getAttribute("user");

String name = user.getName();
String firstChar = "";

//이름이 비어있지 않은지 꼭 확인해야 에러가 안 납니다.
if (name != null && name.length() > 0) {
	firstChar = name.substring(0, 1); // 0번째부터 1번째 앞까지 자름
}
%>
<body data-page="profile">
	<div class="grid-bg"></div>

	<%@ include file="module/nav.jsp"%>

	<main class="main">
		<section class="welcome-section">
			<h1>회원정보 수정</h1>
			<p>계정 정보를 업데이트하고 설정을 관리하세요</p>
		</section>
		<div class="profile-container">
			<div class="profile-sidebar">
				<div class="profile-avatar">
					<div class="avatar-circle">
						<span id="avatarInitial"><%=firstChar%></span>
					</div>
				</div>
				<div class="profile-stats">
					<div class="stat-item">
						<span class="stat-label">아이디</span> <span class="stat-value"><%=user.getId()%></span>
					</div>
					<div class="stat-item">
						<span class="stat-label">이름</span> <span class="stat-value"><%=user.getName()%></span>
					</div>
					<button class="btn btn-danger"
						onclick="location.href = '${pageContext.request.contextPath}/logOut.do'">로그아웃</button>
				</div>
			</div>
			<div class="profile-content">
				<section class="profile-section">
					<h2 class="profile-section-title">기본 정보</h2>
					<form
						action="${pageContext.request.contextPath}/updateAccountDetail.do"
						id="basicInfoForm" name="updateMember" method="post">
						<div class="form-group">
							<input type="hidden" id="id" name="id" value="<%=user.getId()%>">
						</div>
						<div class="form-group">
							<label for="name">이름</label> <input type="text" id="name"
								name="name" value="<%=user.getName()%>">
						</div>
						<div class="form-group">
							<label for="mail">이메일</label> <input type="email" id="mail"
								name="mail" value="<%=user.getMail()%>">
						</div>
						<div class="form-group">
							<label for="phone">전화번호</label> <input type="tel" id="phone"
								name="phone" value="<%=user.getPhone()%>">
						</div>
						<button type="button" class="btn btn-primary"
							onclick="checkForm('detail')">계정 정보 변경</button>
					</form>
				</section>
				<section class="profile-section">
					<h2 class="profile-section-title">비밀번호 변경</h2>
					<form
						action="${pageContext.request.contextPath}/updateAccountPw.do"
						id="passwordForm" name="updatePw" method="post">
						<div class="form-group">
							<label for="currentPassword">현재 비밀번호</label> <input
								type="password" id="currentPassword" name="currentPw"
								placeholder="현재 비밀번호를 입력하세요">
						</div>
						<div class="form-group">
							<label for="newPassword">새 비밀번호</label> <input type="password"
								id="newPassword" name="newPw" placeholder="새 비밀번호를 입력하세요">
						</div>
						<div class="form-group">
							<label for="confirmPassword">비밀번호 확인</label> <input
								type="password" id="confirmPassword" name="confirmPw"
								placeholder="비밀번호를 다시 입력하세요">
						</div>
						<button type="button" class="btn btn-primary"
							onclick="checkForm('pw')">비밀번호 변경</button>
					</form>
				</section>

				<section class="profile-section danger-zone">
					<h2 class="profile-section-title" style="color: var(--danger);">위험
						구역</h2>
					<p style="color: var(--text-secondary); margin-bottom: 1.5rem;">
						아래 작업은 되돌릴 수 없습니다. 신중하게 진행하세요.</p>
					<div class="danger-actions">
						<button class="btn btn-danger" onclick="location.href = '${pageContext.request.contextPath}/DelAccount.do'">계정
							삭제</button>
					</div>
				</section>
			</div>
		</div>
	</main>
	<script>
	const urlParams = new URLSearchParams(window.location.search);
	const message = urlParams.get('msg');

	if (message === 'updateaccountdetailsucceeded') {
	    alert("계정 정보가 성공적으로 변경되었습니다!");
	    history.replaceState(null, '', window.location.pathname);
	} else if (message === 'updateaccountpwsucceeded') {
	    alert("계정 비밀번호가 성공적으로 변경되었습니다!");
	    history.replaceState(null, '', window.location.pathname);
	} 
	<% 
        // null 처리를 확실하게 하여 에러 방지
        String errorMsg = (String) request.getAttribute("msg");
        if (errorMsg != null && !errorMsg.isEmpty()) { 
            // 자바 문자열의 줄바꿈을 자바스크립트 줄바꿈(\n)으로 치환해야 안전함
            String safeMsg = errorMsg.replace("\r", "").replace("\n", "\\n");
    %>
        alert("<%=safeMsg%>"); 
    <% } %>

		function checkForm(select) {
			if (select == "pw") {
				if (!document.updatePw.currentPw.value) {
					alert("현재 비밀번호를 입력해주세요.")
					return false;
				}
				if (!document.updatePw.newPw.value) {
					alert("새 비밀번호를 입력해주세요.")
					return false;
				}
				if (!document.updatePw.confirmPw.value) {
					alert("비밀번호 확인을 입력해주세요.")
					return false;
				}
				if (document.updatePw.newPw.value != document.updatePw.confirmPw.value) {
					alert("비번을 동일하게 입력해주세요.")
					return false;
				}
				document.updatePw.submit();
			} else if (select == "detail") {
				if (!document.updateMember.id.value) {
					alert("아이디를 입력해주세요.")
					return false;
				}
				if (!document.updateMember.name.value) {
					alert("이름을 입력해주세요.")
					return false;
				}
				if (!document.updateMember.mail.value) {
					alert("메일을 입력해주세요.")
					return false;
				}
				if (!document.updateMember.phone.value) {
					alert("전화번호를 입력해주세요.")
					return false;
				}
				document.updateMember.submit();
			}
		}
	</script>
	<%@ include file="module/header.jsp"%>