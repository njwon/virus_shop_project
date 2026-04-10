<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="domain.entity.Scan"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
Scan result = (Scan) request.getAttribute("result");
%>
<%@ include file="module/header.jsp"%>
<body data-page="article">
	<%@ include file="module/nav.jsp"%>

	<main class="main">
		<a href="<%=request.getContextPath()%>" class="back-link"> <span>←</span>
			홈으로
		</a>

		<div class="article-header">
			<div class="article-meta">
				<span id="articleDate">${result.scanDate}</span> <span
					class="status-badge" id="status">완료</span>
			</div>
			<h1 id="articleTitle">스캔 결과</h1>
			<div class="url-display" id="urlDisplay">파일: <c:out value="${result.fileName}"/></div>
		</div>

		<div class="ai-section">
			<h2>AI 분석 결과</h2>
			<div class="vulnerability-list">
				<c:if test="${result.malicious > 0}">
					<div class="vulnerability-card">
						<span class="severity-badge severity-high">높은 심각도</span>
						<h3>⚠️ 위험</h3>
						<div class="vulnerability-description">${result.malicious}개의
							바이러스 백신이 위협을 감지했습니다!</div>
					</div>
				</c:if>
				<c:if test="${result.suspicious > 0}">
					<div class="vulnerability-card">
						<span class="severity-badge severity-medium">중간 심각도</span>
						<h3>⚠️ 경고</h3>
						<div class="vulnerability-description">${result.suspicious}개의
							바이러스 백신이 수상하다고 감지했습니다!</div>
					</div>
				</c:if>
				<c:if test="${result.undetected > 0}">
					<div class="vulnerability-card">
						<span class="severity-badge severity-medium">중간 심각도</span>
						<h3>✅ 무감지</h3>
						<div class="vulnerability-description">${result.undetected}개의
							바이러스 백신이 감지에 실패했습니다!</div>
					</div>
				</c:if>
				<c:if test="${result.harmless > 0}">
					<div class="vulnerability-card">
						<span class="severity-badge severity-low">낮은 심각도</span>
						<h3>✅ 안전</h3>
						<div class="vulnerability-description">${result.harmless}개의
							바이러스 백신이 위협이 감지하지 않았습니다!</div>
					</div>
				</c:if>
				<p>총 검사 엔진: ${result.total}개</p>
			</div>
		</div>
	</main>

	<%@ include file="module/footer.jsp"%>