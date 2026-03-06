<%@ page import="dto.ScanDTO"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
		<%
		ArrayList<ScanDTO> list = (ArrayList<ScanDTO>) request.getAttribute("scanList");

		// null 체크
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				ScanDTO scan = list.get(i);
		%>
		<tr>
			<td><span
				style="font-family: 'JetBrains Mono', monospace; color: var(- -text-secondary);">
					#<%=scan.getNum()%>
			</span></td>

			<td>
				<div class="scan-title"><%=scan.getFileName()%></div>
			</td>
			<td><%=scan.getScanDate()%></td>

			<td><span class="status-badge"><%=scan.getResultStatus()%></span>
			</td>

			<td>
				<div class="scan-actions">
					<button class="btn btn-small btn-danger"
						onclick="window.location.href='${pageContext.request.contextPath}/DeleteScan.do?id=<%= scan.getNum() %>'">삭제</button>
				</div>
			</td>
		</tr>
		<%
		} // for loop end
		} else {
		%>
		<tr>
			<td colspan="5" style="text-align: center; padding: 20px;">등록된
				상품이 없습니다.</td>
		</tr>
		<%
		} // if-else end
		%>
	</tbody>
</table>