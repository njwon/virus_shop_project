package controller.Member;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
import dao.MemberDAO;
import dto.MemberDTO;
import java.util.UUID;

@WebFilter("/*")
public class AutoLoginFilterServlet implements Filter {

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) 
            throws IOException, ServletException {
        
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("loginUser") != null) {
            chain.doFilter(req, res);
            return;
        }

        Cookie[] cookies = request.getCookies();
        String receivedToken = null;
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("remember_me".equals(c.getName())) {
                    receivedToken = c.getValue();
                    break;
                }
            }
        }

        if (receivedToken != null) {
            MemberDAO dao = MemberDAO.getInstance();
            MemberDTO user = dao.checkRememberMeToken(receivedToken);

            if (user != null) {
                if (user.getLastIp().equals(request.getRemoteAddr())) {
                    
                    dao.deleteRememberMeToken(receivedToken);

                    String newToken = UUID.randomUUID().toString();
                    dao.insertRememberMeToken(user.getId(), newToken, request.getRemoteAddr(), request.getHeader("User-Agent"));

                    updateSecureCookie(response, newToken);

                    request.getSession().setAttribute("loginUser", user);
                    
                } else {
                    deleteCookie(response);
                }
            }
        }

        chain.doFilter(req, res);
    }

    // 쿠키 업데이트/삭제 편의 메서드들
    private void updateSecureCookie(HttpServletResponse response, String token) {
        String cookieHeader = String.format("remember_me=%s; Max-Age=%d; Path=/; HttpOnly; SameSite=Strict", 
                                            token, 60 * 60 * 24 * 30);
        response.addHeader("Set-Cookie", cookieHeader);
    }

    private void deleteCookie(HttpServletResponse response) {
        Cookie c = new Cookie("remember_me", null);
        c.setMaxAge(0);
        c.setPath("/");
        response.addCookie(c);
    }

    public void init(FilterConfig fConfig) throws ServletException {}
    public void destroy() {}
}