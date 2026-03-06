package controller.Member;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.MemberDAO;
import dto.MemberDTO;

@WebServlet("/DelAccount.do")
public class DeleteAccountServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        MemberDTO user = (MemberDTO) session.getAttribute("loginUser");
        // 1. 세션 삭제 (로그인 정보 지우기)
        session = request.getSession(false); // 세션이 없으면 null 반환
        if (session != null) {
            session.invalidate(); // 세션 무효화 (loginUser 정보 다 날아감)
            System.out.println("세션 무효화 완료 (로그아웃)");
        }

        // 2. 자동 로그인(Remember-me) 쿠키 및 DB 토큰 삭제
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("remember_me".equals(cookie.getName())) {
                    
                    // A. DB에서 토큰 삭제 (보안상 중요)
                    String token = cookie.getValue();
                    MemberDAO dao = MemberDAO.getInstance();
                    dao.deleteRememberMeToken(token); 
                    dao.deleteAccount(user.getId());
                    
                    // B. 브라우저 쿠키 삭제 (수명 0으로 설정)
                    cookie.setMaxAge(0);
                    cookie.setPath("/"); // [중요] 생성할 때 설정한 경로와 똑같아야 삭제됨
                    response.addCookie(cookie);
                    
                    System.out.println("자동 로그인 쿠키 삭제 완료");
                    break;
                }
            }
        }

        // 3. 메인 페이지나 로그인 페이지로 이동
        // 로그아웃 후 메시지를 띄우고 싶다면 쿼리스트링 추가
        response.sendRedirect(request.getContextPath() + "?msg=accountdeletesucceeded");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}