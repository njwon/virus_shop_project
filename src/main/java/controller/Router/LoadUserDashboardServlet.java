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
import java.util.List;
import java.util.UUID;
import java.io.File;

import dao.MemberDAO;
import dao.ProductDAO;
import dao.ScanDAO;
import dto.MemberDTO;
import dto.ProductDTO;
import dto.ScanDTO;
import util.DBManager;
import util.PasswordManager;

@WebServlet("/UserBoard.do")
public class LoadUserDashboardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    
	    HttpSession session = request.getSession();
	    MemberDTO user = (MemberDTO) session.getAttribute("loginUser");

	    if (user == null) {
	        
	        response.setContentType("text/html; charset=UTF-8");
	        PrintWriter out = response.getWriter();
	        out.println("<script>");
	        out.println("alert('로그인 후 접근 가능합니다.');");
	        out.println("location.href='" + request.getContextPath() + "/login';");
	        out.println("</script>");
	        out.close();
	        return;
	    }

	    ScanDAO Sdao = ScanDAO.getInstance();
		MemberDAO Mdao = MemberDAO.getInstance();
		
		List<ScanDTO> list = Sdao.getHistory(user.getId());
		int totalCount = (list != null) ? list.size() : 0;
		
		request.setAttribute("scanList", list);
		request.setAttribute("scanCount", totalCount);
		request.setAttribute("accountName", user.getName());
	    System.out.println("유저님 어서오세요.");
	    request.getRequestDispatcher("/WEB-INF/html/dashboard.jsp").forward(request, response);
	}
}
