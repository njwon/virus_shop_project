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
import java.util.Enumeration;
import java.util.UUID;
import java.io.File;

import dao.MemberDAO;
import dao.ProductDAO;
import dto.MemberDTO;
import util.DBManager;
import util.PasswordManager;

@WebServlet("/UserCheckForm.do")
public class LoadUserCheckFormServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    
	    HttpSession session = request.getSession();
	    MemberDTO user = (MemberDTO) session.getAttribute("loginUser");

	    if (user == null) {
	        
	        response.setContentType("text/html; charset=UTF-8");
	        PrintWriter out = response.getWriter();
	        out.println("<script>");
	        out.println("alert('로그인 후 접근 가능합니다.');");
	        out.println("location.href='" + request.getContextPath() + "';");
	        out.println("</script>");
	        out.close();
	        return;
	    }

	    System.out.println("유저님 어서오세요.");
	    request.getRequestDispatcher("/WEB-INF/html/dashboard.jsp").forward(request, response);
	}
}
