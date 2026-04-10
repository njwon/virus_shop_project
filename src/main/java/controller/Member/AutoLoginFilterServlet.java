package controller.Member;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;

import domain.entity.Member;
import domain.repository.MemberRepository;
import infrastructure.persistence.MemberRepositoryImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebFilter("/*")
public class AutoLoginFilterServlet implements Filter {

    private static final Logger log = LoggerFactory.getLogger(AutoLoginFilterServlet.class);
    private final MemberRepository memberRepo = MemberRepositoryImpl.getInstance();

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request   = (HttpServletRequest) req;
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
            Member user = memberRepo.findByRememberMeToken(receivedToken);

            if (user != null) {
                String currentIp = request.getRemoteAddr();
                String currentUa = request.getHeader("User-Agent");
                boolean ipMatch = currentIp.equals(user.getLastIp());
                boolean uaMatch = currentUa != null && currentUa.equals(user.getLastUserAgent());

                if (ipMatch && uaMatch) {
                    memberRepo.deleteRememberMeToken(receivedToken);
                    String newToken = UUID.randomUUID().toString();
                    memberRepo.insertRememberMeToken(user.getId(), newToken, currentIp, currentUa);
                    updateSecureCookie(response, newToken);
                    request.getSession().setAttribute("loginUser", user);
                    log.info("자동 로그인 성공 - ID: {}", user.getId());
                } else {
                    log.warn("자동 로그인 거부 - IP/UA 불일치 - ID: {}", user.getId());
                    memberRepo.deleteRememberMeToken(receivedToken);
                    deleteCookie(response);
                }
            }
        }

        chain.doFilter(req, res);
    }

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
