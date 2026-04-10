package filter;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebFilter("/*")
public class CsrfFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(CsrfFilter.class);
    private static final SecureRandom random = new SecureRandom();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(true);

        if (session.getAttribute("csrfToken") == null) {
            session.setAttribute("csrfToken", generateToken());
        }

        String method = req.getMethod();
        if (method.equalsIgnoreCase("POST") || method.equalsIgnoreCase("PUT") || method.equalsIgnoreCase("DELETE")) {
            String sessionToken = (String) session.getAttribute("csrfToken");
            String requestToken = req.getParameter("_csrf");
            if (requestToken == null || requestToken.isEmpty()) {
                requestToken = req.getHeader("X-CSRF-Token");
            }

            if (sessionToken == null || !sessionToken.equals(requestToken)) {
                log.warn("CSRF 토큰 불일치 - URI: {}, IP: {}", req.getRequestURI(), req.getRemoteAddr());
                res.sendError(HttpServletResponse.SC_FORBIDDEN, "유효하지 않은 요청입니다.");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private String generateToken() {
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    @Override
    public void destroy() {}
}
