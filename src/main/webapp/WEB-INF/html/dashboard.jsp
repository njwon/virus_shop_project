<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="domain.entity.Scan"%>
<%
List<Scan> hList = (List<Scan>) request.getAttribute("scanList");
int tCnt = 0;
int sCnt = 0;
int dCnt = 0;

if (hList != null) {
	tCnt = hList.size();
	for (Scan s : hList) {
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
			<div class="stat-card danger">
				<h3>위험</h3>
				<div class="stat-number"><%=dCnt%></div>
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

		function deleteScan(id) {
			showConfirm('정말 삭제하시겠습니까?', function(ok) {
				if (!ok) return;
				fetch('${pageContext.request.contextPath}/scans/' + id, { method: 'DELETE', headers: { 'X-CSRF-Token': '${sessionScope.csrfToken}' } })
					.then(res => { if (res.ok) { location.href = location.pathname + '?msg=delscansucceeded'; } else { alert('스캔 기록 삭제에 실패했습니다.'); } })
					.catch(() => alert('서버 오류가 발생했습니다. 다시 시도해주세요.'));
			});
		}
	</script>
	<%@ include file="module/footer.jsp"%>