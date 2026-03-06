<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="connection.jsp" %> 
<%@ page import="java.sql.*" %>      
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>181_delete_process.jsp</title>
</head>
<body>
	<%
		request.setCharacterEncoding("utf-8");
	
		String id = request.getParameter("id");
		String pw = request.getParameter("pw");
		
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		
		try{
			String sql = "select id, pw from member_test where id=?";
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				String rId = rs.getString("id");
				String rPw = rs.getString("pw");
				
				if(id.equals(rId) && pw.equals(rPw)){
					sql="delete from member_test where id=? and pw=?";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1,id);
					pstmt.setString(2,pw);
					pstmt.executeUpdate();
					out.print("해당 아이디와 비번을 가진 사용자는 회원탈퇴되었습니다.");
				}else{
					out.print("아이디, 비번이 맞지 않습니다.");
				}
			}else{
				out.print("Member테이블에 일치하는 아이디가 없습니다.");
			}
		}catch(SQLException ex){
			out.print(ex.getMessage());
		}finally{
			if(rs !=null) rs.close();
			if(pstmt !=null) pstmt.close();
			if(conn !=null) conn.close();
		}
	%>
</body>
</html>














