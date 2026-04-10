package controller.Router;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import domain.entity.Member;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/account/verify")
public class LoadUserCheckFormServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(LoadUserCheckFormServlet.class);
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    
	    HttpSession session = request.getSession(false);
	    Member user = session != null ? (Member) session.getAttribute("loginUser") : null;

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

	    log.info("계정 인증 폼 접근 - ID: {}, 이름: {}", user.getId(), user.getName());
	    request.getRequestDispatcher("/WEB-INF/html/profile.jsp").forward(request, response);
	}
}
