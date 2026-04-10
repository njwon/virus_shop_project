package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import domain.entity.Member;
import usecase.UseCaseException;
import usecase.member.IssueRememberMeTokenUseCase;
import usecase.member.LoginUseCase;
import usecase.member.RevokeRememberMeTokenUseCase;

/**
 * POST   /sessions → 로그인
 * DELETE /sessions → 로그아웃 (fetch)
 */
@WebServlet("/sessions")
public class SessionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        try {
            Member member = new LoginUseCase().execute(
                request.getParameter("id"),
                request.getParameter("pw")
            );

            HttpSession session = request.getSession();
            session.setAttribute("loginUser", member);

            String rememberMe = request.getParameter("remember_me");
            if ("on".equals(rememberMe) || "true".equals(rememberMe)) {
                String token = new IssueRememberMeTokenUseCase().execute(
                    member.getId(), request.getRemoteAddr(), request.getHeader("User-Agent")
                );
                String cookieHeader = String.format("remember_me=%s; Max-Age=%d; Path=/; HttpOnly; SameSite=Strict",
                        token, 60 * 60 * 24 * 30);
                response.addHeader("Set-Cookie", cookieHeader);
            }

            response.sendRedirect(request.getContextPath() + "/?msg=loginsucceeded");

        } catch (UseCaseException e) {
            request.setAttribute("message", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/html/login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("remember_me".equals(cookie.getName())) {
                    new RevokeRememberMeTokenUseCase().execute(cookie.getValue());
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                    break;
                }
            }
        }

        response.setStatus(HttpServletResponse.SC_OK);
    }
}
