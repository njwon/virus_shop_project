<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<table class="scan-table">
	<thead>
		<tr>
			<th>ID</th>
			<th>파일명</th>
			<th>날짜</th>
			<th>결과</th>
			<th>작업</th>
		</tr>
	</thead>
	<tbody id="scanTableBody">
		<c:choose>
			<c:when test="${not empty scanList}">
				<c:forEach var="scan" items="${scanList}">
					<tr>
						<td><span style="font-family: 'JetBrains Mono', monospace; color: var(--text-secondary);">#${scan.num}</span></td>
						<td><div class="scan-title"><c:out value="${scan.fileName}"/></div></td>
						<td>${scan.scanDate}</td>
						<td><span class="status-badge${scan.resultStatus eq '위험' ? ' status-badge--danger' : ''}"><c:out value="${scan.resultStatus}"/></span></td>
						<td>
							<div class="scan-actions">
								<button class="btn btn-small btn-danger" onclick="deleteScan(${scan.num})">삭제</button>
							</div>
						</td>
					</tr>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<tr><td colspan="5" style="text-align: center; padding: 20px;">등록된 스캔이 없습니다.</td></tr>
			</c:otherwise>
		</c:choose>
	</tbody>
</table>
