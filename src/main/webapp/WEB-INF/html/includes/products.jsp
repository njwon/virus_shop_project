<%@ page import="dto.ProductDTO"%>
<%@ page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<section class="scans-section">
    <div class="section-header">
        <h2 class="section-title">판매 목록</h2>
    </div>
    
    <div class="scans-grid" id="scansGrid">
        <%
            ArrayList<ProductDTO> list = (ArrayList<ProductDTO>) request.getAttribute("productList");
            
            if (list == null || list.size() == 0) {
        %>
            <div class="no-data-msg" style="grid-column: 1/-1; text-align: center; padding: 50px;">
                <h3>등록된 상품이 없습니다.</h3>
                <p>관리자 페이지에서 상품을 등록해주세요.</p>
            </div>
        <%
            } else {
                // 리스트가 있을 때만 반복문 실행
                for (int i = 0; i < list.size(); i++) {
                    ProductDTO product = list.get(i);
        %>
            <div class="scan-card" onclick="window.location.href='${pageContext.request.contextPath}/productDetail?id=<%= product.getProductId() %>'">
                
                <div class="scan-meta">
                    <span><%= product.getPname() %></span> 
                    <span class="scan-status"><%= product.getCategory() %></span>
                </div>
                
                <% if(product.getProductImage() != null) { %>    
                    <img src="<%= request.getContextPath() %>/resources/images/<%= product.getProductImage() %>" style="width: 100%; height: 200px; object-fit: cover;">
                <% }%>
                
                <h3><%= product.getPname() %></h3>
                <p><%= product.getDescription() %></p>
                <div class="scan-url"><%=String.format("%,d", product.getUnitPrice()) %>원</div>
            </div>
        <%
                } // for문 종료
            } // else문 종료
        %>
    </div>
</section>