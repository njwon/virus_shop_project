<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%
List<ScanDTO> hList = (List<ScanDTO>) request.getAttribute("historyList");
int tCnt = 0;
int sCnt = 0;
int dCnt = 0;

if (hList != null) {
	tCnt = hList.size();
	for (ScanDTO s : hList) {
		String st = (s.getResultStatus() != null) ? s.getResultStatus().trim() : "";
		if ("안전".equals(st)) {
	sCnt++;
		} else if ("위험".equals(st)) {
	dCnt++;
		}
	}
}
%>
<%@ include file="module/header.jsp"%>
<body data-page="dashboard">
	<%@ include file="module/nav.jsp"%>
	<main class="main">
		<section class="welcome-section">
			<h1>사용자 개인 대시보드</h1>
			<p>코드의 보안 결함을 살펴보세요</p>
		</section>

		<div class="stats-grid">
			<div class="stat-card info">
				<h3>스캔</h3>
				<div class="stat-number"><%=request.getAttribute("scanCount")%></div>
			</div>
			<div class="stat-card">
				<h3>계정</h3>
				<div class="stat-number"><%=request.getAttribute("accountName") %></div>
			</div>
		</div>

		<section class="admin-section">
			<h2>모든 스캔 관리</h2>
			<%@ include file="includes/scanList.jsp"%>
		</section>
	</main>

	<script>
		const urlParams = new URLSearchParams(window.location.search);
		const message = urlParams.get('msg');
		if (message === 'delscansucceeded') {
			alert("스캔 기록이 성공적으로 삭제되었습니다!");
			history.replaceState(null, '', window.location.pathname);
		}
	</script>
	<%@ include file="module/footer.jsp"%>