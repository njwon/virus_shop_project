<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="domain.entity.Member"%>
<%@ page import="util.HtmlEscaper"%>
<%@ include file="module/header.jsp"%>
<%
Member user = (Member) request.getAttribute("user");
if (user == null) {
    response.sendRedirect(request.getContextPath() + "/login");
    return;
}

String name = user.getName();
String firstChar = "";

//이름이 비어있지 않은지 꼭 확인해야 에러가 안 납니다.
if (name != null && name.length() > 0) {
	firstChar = name.substring(0, 1); // 0번째부터 1번째 앞까지 자름
}
%>

<style>
	.form-group .deleteConfirmPw:focus{
		border-color: var(--danger);
	}
</style>
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
						<span id="avatarInitial"><%=HtmlEscaper.h(firstChar)%></span>
					</div>
				</div>
				<div class="profile-stats">
					<div class="stat-item">
						<span class="stat-label">아이디</span> <span class="stat-value"><%=HtmlEscaper.h(user.getId())%></span>
					</div>
					<div class="stat-item">
						<span class="stat-label">이름</span> <span class="stat-value"><%=HtmlEscaper.h(user.getName())%></span>
					</div>
					<button class="btn btn-danger"
						onclick="logout()">로그아웃</button>
				</div>
			</div>
			<div class="profile-content">
				<section class="profile-section">
					<h2 class="profile-section-title">기본 정보</h2>
					<form
						action="${pageContext.request.contextPath}/members/me/detail"
						id="basicInfoForm" name="updateMember" method="post">
						<div class="form-group">
							<input type="hidden" id="id" name="id" value="<%=HtmlEscaper.h(user.getId())%>">
						</div>
						<div class="form-group">
							<label for="name">이름</label> <input type="text" id="name"
								name="name" value="<%=HtmlEscaper.h(user.getName())%>">
						</div>
						<div class="form-group">
							<label for="mail">이메일</label> <input type="email" id="mail"
								name="mail" value="<%=HtmlEscaper.h(user.getMail())%>">
						</div>
						<div class="form-group">
							<label for="phone">전화번호</label> <input type="tel" id="phone"
								name="phone" value="<%=HtmlEscaper.h(user.getPhone())%>">
						</div>
						<button type="button" class="btn btn-primary"
							onclick="checkForm('detail')">계정 정보 변경</button>
						<input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
					</form>
				</section>
				<section class="profile-section">
					<h2 class="profile-section-title">비밀번호 변경</h2>
					<form
						action="${pageContext.request.contextPath}/members/me/password"
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
						<input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
					</form>
				</section>

				<section class="profile-section danger-zone">
					<h2 class="profile-section-title" style="color: var(--danger);">위험 구역</h2>
					<p style="color: var(--text-secondary); margin-bottom: 1.5rem;">아래 작업은 되돌릴 수 없습니다. 신중하게 진행하세요.</p>
					<form method="post" action="${pageContext.request.contextPath}/members/me" name="deleteForm">
						<input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
						<div class="form-group">
							<label for="deleteConfirmPw">비밀번호 확인</label>
							<input type="password" name="confirmPw" class="deleteConfirmPw" id="deleteConfirmPw" placeholder="현재 비밀번호를 입력하세요">
						</div>
						<button type="button" class="btn btn-danger" onclick="checkDeleteForm()">계정 삭제</button>
					</form>
				</section>
			</div>
		</div>
	</main>
	<script>
	const urlParams = new URLSearchParams(window.location.search);
	const message = urlParams.get('msg');
	const errorParam = urlParams.get('error');

	if (message === 'updateaccountdetailsucceeded') {
	    alert("계정 정보가 성공적으로 변경되었습니다!");
	    history.replaceState(null, '', window.location.pathname);
	} else if (message === 'updateaccountpwsucceeded') {
	    alert("계정 비밀번호가 성공적으로 변경되었습니다!");
	    history.replaceState(null, '', window.location.pathname);
	} else if (errorParam === 'wrongpassword') {
	    alert("비밀번호가 일치하지 않습니다.");
	    history.replaceState(null, '', window.location.pathname);
	}
	<%
        String errorMsg = (String) request.getAttribute("msg");
        if (errorMsg != null && !errorMsg.isEmpty()) {
    %>
        <div id="_profileSrvMsg" style="display:none"><%=HtmlEscaper.h(errorMsg)%></div>
        <script>(function(){ var m=document.getElementById('_profileSrvMsg'); if(m) alert(m.textContent); })();</script>
    <% } %>

		function checkForm(select) {
			if (select === "pw") {
				const currentPw = document.updatePw.currentPw.value;
				const newPw     = document.updatePw.newPw.value;
				const confirmPw = document.updatePw.confirmPw.value;

				if (!currentPw) { alert("현재 비밀번호를 입력해주세요."); return false; }
				if (!newPw) { alert("새 비밀번호를 입력해주세요."); return false; }
				if (newPw.length < 8 || newPw.length > 50) {
					alert("새 비밀번호는 8자 이상 50자 이하로 입력해주세요."); return false;
				}
				if (!confirmPw) { alert("비밀번호 확인을 입력해주세요."); return false; }
				if (newPw !== confirmPw) { alert("새 비밀번호가 일치하지 않습니다."); return false; }
				document.updatePw.submit();

			} else if (select === "detail") {
				const name  = document.updateMember.name.value.trim();
				const mail  = document.updateMember.mail.value.trim();
				const phone = document.updateMember.phone.value.trim();

				if (!name) { alert("이름을 입력해주세요."); return false; }
				if (name.length < 2 || name.length > 20) {
					alert("이름은 2~20자로 입력해주세요."); return false;
				}
				if (!mail) { alert("이메일을 입력해주세요."); return false; }
				if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(mail)) {
					alert("올바른 이메일 형식으로 입력해주세요."); return false;
				}
				if (mail.length > 50) { alert("이메일은 50자 이하로 입력해주세요."); return false; }
				if (!phone) { alert("전화번호를 입력해주세요."); return false; }
				if (!/^[0-9]{10,11}$/.test(phone.replaceAll("-", ""))) {
					alert("올바른 전화번호 형식으로 입력해주세요. (예: 010-1234-5678)"); return false;
				}
				document.updateMember.submit();
			}
		}

		function checkDeleteForm() {
			const pw = document.getElementById('deleteConfirmPw').value;
			if (!pw) { alert("비밀번호를 입력해주세요."); return false; }
			showConfirm("정말 계정을 삭제하시겠습니까? 이 작업은 되돌릴 수 없습니다.", function(ok) {
				if (ok) document.deleteForm.submit();
			});
		}

		function logout() {
			fetch('${pageContext.request.contextPath}/sessions', { method: 'DELETE', headers: { 'X-CSRF-Token': '${sessionScope.csrfToken}' } })
				.then(() => { location.href = '${pageContext.request.contextPath}/?msg=logoutsucceeded'; })
				.catch(() => alert('로그아웃 중 오류가 발생했습니다.'));
		}
	</script>