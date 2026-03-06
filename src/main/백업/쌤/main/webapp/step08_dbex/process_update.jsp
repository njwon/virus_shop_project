<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="connection.jsp" %> 
<%@ page import="java.sql.*" %>   
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>180_update_process.jsp</title>
</head>
<body>
	<%
		request.setCharacterEncoding("utf-8");
		
		String id = request.getParameter("id");
		String pw = request.getParameter("pw");
		String name = request.getParameter("name");
		
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		
		try{
			String sql = "select id, pw from member_test where id=?";
 			pstmt = conn.prepareStatement(sql);
 			pstmt.setString(1, id);
 			rs=pstmt.executeQuery();
 			
 			if(rs.next()){
 				String r_id = rs.getString("id");
 				String r_pw = rs.getString("pw");
 			
 				if(id.equals(r_id) && pw.equals(r_pw)){
	 				sql = "update member_test set name =? where id=?";
	 				pstmt = conn.prepareStatement(sql);
	 				pstmt.setString(1, name);
	 				pstmt.setString(2, id);
	 				pstmt.executeUpdate();
	 				out.println("데이터를 수정하였습니다.");
	 			}else out.print("아이디 비밀번호가 맞지 않습니다.");
	 				
 			}else out.print("일치하는 아이디가 없습니다..");
		}catch (SQLException ex){
				ex.getMessage();
		}finally{
			if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();
		}
	%>
</body>
</html>












