package controller.Router;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.UUID;
import java.io.File;

import dao.MemberDAO;
import dao.ProductDAO;
import dto.MemberDTO;
import dto.ProductDTO;
import util.DBManager;
import util.PasswordManager;

@WebServlet("/AdminBoard.do")
public class LoadAdminDashboardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    
	    HttpSession session = request.getSession();
	    MemberDTO user = (MemberDTO) session.getAttribute("loginUser");

	    if (user == null || !"ADMIN".equals(user.getRole())) {
	        
	        response.setContentType("text/html; charset=UTF-8");
	        PrintWriter out = response.getWriter();
	        out.println("<script>");
	        out.println("alert('관리자만 접근 가능합니다.');");
	        out.println("location.href='" + request.getContextPath() + "';");
	        out.println("</script>");
	        out.close();
	        return;
	    }

	    System.out.println("관리자님 어서오세요.");
	    ProductDAO Pdao = ProductDAO.getInstance();
		MemberDAO Mdao = MemberDAO.getInstance();
		
		ArrayList<ProductDTO> list = Pdao.getAllProducts();
		int totalCount = (list != null) ? list.size() : 0;
		
		request.setAttribute("productList", list);
		request.setAttribute("productCount", totalCount);
		request.setAttribute("accountName", user.getName());
	    request.getRequestDispatcher("/WEB-INF/html/admindashboard.jsp").forward(request, response);
	}
}
