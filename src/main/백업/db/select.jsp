<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.SQLException"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="connection.jsp" %>  
    <section class="scans-section">
        <div class="section-header">
            <h2 class="section-title">판매 목록</h2>
        </div>
        <div class="scans-grid" id="scansGrid">
        	<%
			ResultSet rs = null;
			/* Statement stmt = null; */
			PreparedStatement pstmt = null;
			
			try{
				String sql ="select * from product";
				pstmt= conn.prepareStatement(sql);
				rs = pstmt.executeQuery();
				
				while(rs.next()){
					String id = rs.getString("id");
					String name = rs.getString("name");
					String price = rs.getString("price");
					String category = rs.getString("category");
					String description = rs.getString("description");
					String img = rs.getString("img");
					String date = rs.getString("date_");
				
			%>
				<div class="scan-card" onclick="window.location.href='product_article.jsp?id=<%=id%>'">
                <div class="scan-meta">
                    <span><%=date %></span>
                    <span class="scan-status"><%=category %></span>
                </div>
                <img src="<%=request.getContextPath()%>/resources/images/<%=img%>" style="max-width: 100%;">
                <h3><%=name %></h3>
                <p><%=description %></p>
                <div class="scan-url"><%=price %>원</div>
            </div>
						<%
				}
			}catch(SQLException ex){
				out.print("Member 테이블 호출이 실패하였습니다.<br>");
				out.print(ex.getMessage());
			}finally{
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			}
			%>
        </div>
    </section>
