<%@ page import="dto.ProductDTO"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<table class="scan-table">
    <thead>
        <tr>
            <th>ID</th>
            <th>상품명</th>
            <th>날짜</th>
            <th>카테고리</th>
            <th>작업</th>
        </tr>
    </thead>
    <tbody id="scanTableBody">
        <%
        ArrayList<ProductDTO> list = (ArrayList<ProductDTO>) request.getAttribute("productList");
     	
        // null 체크
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                ProductDTO product = list.get(i);
                
        %>
        <tr>
            <td>
                <span style="font-family: 'JetBrains Mono', monospace; color: var(--text-secondary);">
                    #<%= product.getProductId() %>
                </span>
            </td>
            
            <td>
                <div class="scan-title"><%= product.getPname() %></div>
            </td>
            <td><%=product.getSimpleDate() %></td>
            
            <td>
                <span class="status-badge"><%= product.getCategory() %></span>
            </td>
            
            <td>
                <div class="scan-actions">
                    <button class="btn btn-small btn-info" onclick="window.location.href='${pageContext.request.contextPath}/productDetail?id=<%=product.getProductId() %>'">보기</button>
                    <button class="btn btn-small btn-safe" onclick="window.location.href='${pageContext.request.contextPath}/UpdateProduct?id=<%= product.getProductId() %>'">수정</button>
                    <button class="btn btn-small btn-danger" onclick="window.location.href='${pageContext.request.contextPath}/DeleteProduct.do?id=<%= product.getProductId() %>'">삭제</button>
                </div>
            </td>
        </tr>
        <%
            } // for loop end
        } else {
        %>
            <tr>
                <td colspan="5" style="text-align: center; padding: 20px;">등록된 상품이 없습니다.</td>
            </tr>
        <%
        } // if-else end
        %>
    </tbody>
</table>