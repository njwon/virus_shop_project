package controller.Router;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import domain.entity.Member;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/account")
public class LoadAccountDetailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(LoadAccountDetailServlet.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		HttpSession session = request.getSession(false);
	    Member user = session != null ? (Member) session.getAttribute("loginUser") : null;

	    if (user == null) {
	        response.sendRedirect(request.getContextPath() + "/login");
	        return;
	    }

	    log.info("계정 상세 페이지 접근 - ID: {}, 이름: {}", user.getId(), user.getName());
	    try {
            request.setAttribute("user", user);

            request.getRequestDispatcher("/WEB-INF/html/profile.jsp").forward(request, response);
            
        } catch (Exception e) {
            log.error("오류 발생", e);
            response.sendRedirect(request.getContextPath());
        }
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
