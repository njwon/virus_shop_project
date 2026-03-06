package controller.Member;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.MemberDAO;
import dto.MemberDTO;
import util.PasswordManager;

@WebServlet("/LogIn.do")
public class LogInServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	// 쿠키 생성 메서드
	private void createSecureRememberMeCookie(HttpServletResponse response, String token) {
		String cookieHeader = String.format("remember_me=%s; Max-Age=%d; Path=/; HttpOnly; SameSite=Strict", 
				token, 60 * 60 * 24 * 30);
		response.addHeader("Set-Cookie", cookieHeader);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		// 1. 한글 인코딩 설정
		request.setCharacterEncoding("utf-8");

		String id = request.getParameter("id");
		String pw = request.getParameter("pw");
		String rememberMe = request.getParameter("remember_me");

		try {
			MemberDAO dao = MemberDAO.getInstance();
			MemberDTO result = dao.getMember(id);
			
			// 아이디가 없는 경우
			if(result == null) {
				request.setAttribute("message", "존재하지 않는 계정입니다.");
				request.getRequestDispatcher("/WEB-INF/html/login.jsp").forward(request, response);
				return;
			}
			
			// 비밀번호가 맞는 경우 (로그인 성공)
			if(PasswordManager.checkPassword(pw, result.getPw())) {
				
				HttpSession session = request.getSession();
				session.setAttribute("loginUser", result);
				
				// 자동 로그인 체크 여부 확인
				if (rememberMe != null && (rememberMe.equals("on") || rememberMe.equals("true"))) {
					String token = UUID.randomUUID().toString();
					String ip = request.getRemoteAddr();
					String userAgent = request.getHeader("User-Agent");
					
					dao.insertRememberMeToken(id, token, ip, userAgent);
					createSecureRememberMeCookie(response, token);
				}
				
				System.out.println("=== 로그인 성공! ===");
				System.out.println("로그인 유저: " + result.getName());
				
				response.sendRedirect(request.getContextPath() + "?msg=loginsucceeded");
				
			} else {
				// 비밀번호가 틀린 경우
				request.setAttribute("message", "비밀번호가 일치하지 않습니다.");
				request.getRequestDispatcher("/WEB-INF/html/login.jsp").forward(request, response);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServletException("로그인 중 오류 발생", e);
		}
	}
}