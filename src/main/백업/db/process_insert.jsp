<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.Statement"%>
<%@ page import="com.oreilly.servlet.MultipartRequest" %>
<%@ page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy" %>
<%@ page import="java.util.Enumeration" %>
<%@ page import="java.io.File" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="connection.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>process_insert.jsp</title>
</head>
<body>
	<%
	request.setCharacterEncoding("utf-8");
	
	String filename = "";
	
	String savePath = "/resources/images"; 
    String realFolder = request.getServletContext().getRealPath(savePath);
	String encType = "utf-8"; //인코딩 타입
	//최대파일용량 설정
	
	File dir = new File(realFolder);

    if (!dir.exists()) {
        dir.mkdirs(); 
    }
	
	int maxSize = 5 * 1024 * 1024; //최대 업로드될 파일의 크기5Mb
	
	
	MultipartRequest multi = new MultipartRequest(request, realFolder, maxSize, encType, new DefaultFileRenamePolicy());
	
	Enumeration files = multi.getFileNames();
	//type="file"속성을 가진 파라미터의 이름을 Enumeration형식으로 리턴
	String fname = (String) files.nextElement();
	String fileName = multi.getFilesystemName("productImage");
	
	
	String productId = multi.getParameter("productId");
	String pname = multi.getParameter("pname");
	String unitPrice = multi.getParameter("unitPrice");
	String description = multi.getParameter("description");
	String category = multi.getParameter("category");
	
	//문자열 데이터를 정수로 변환
	Integer price;
	if(unitPrice.isEmpty()) price=0;
	else price = Integer.valueOf(unitPrice);
	
	long stock;
		
		PreparedStatement pstmt = null;
		
		try{
			String sql ="insert into product values(?,?,?,?,?,?,sysdate)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, productId);
			pstmt.setString(2, pname);
			pstmt.setString(3, unitPrice);
			pstmt.setString(4, category);
			pstmt.setString(5, description);
			pstmt.setString(6, fileName);
			pstmt.executeUpdate();
			out.println("product 데이터가 정상적으로 db에 저장되었습니다.");
		}catch(SQLException e){
			out.println("product 데이터가 정상적으로 db에 저장되는 것에 실패하였습니다.");
			out.println(e.getMessage());
		}finally{
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();
			response.sendRedirect("../index.jsp");
		}
	%>
</body>
</html>


