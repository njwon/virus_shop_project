package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import domain.entity.Member;
import usecase.UseCaseException;
import usecase.member.DeleteAccountUseCase;
import usecase.member.IssueRememberMeTokenUseCase;
import usecase.member.RevokeAllRememberMeTokensUseCase;
import usecase.member.SignUpUseCase;
import usecase.member.UpdateMemberDetailUseCase;
import usecase.member.UpdatePasswordUseCase;

/**
 * POST /members              → 회원가입
 * GET  /members/me           → /account 로 리다이렉트
 * POST /members/me           → 계정 삭제
 * POST /members/me/detail    → 회원 정보 수정
 * POST /members/me/password  → 비밀번호 변경
 */
public class MemberServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/account");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            handleSignUp(request, response);
        } else if ("/me".equals(pathInfo)) {
            handleDeleteAccount(request, response);
        } else if ("/me/detail".equals(pathInfo)) {
            handleUpdateDetail(request, response);
        } else if ("/me/password".equals(pathInfo)) {
            handleUpdatePassword(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void handleSignUp(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            new SignUpUseCase().execute(
                request.getParameter("id"),
                request.getParameter("pw"),
                request.getParameter("name"),
                request.getParameter("mail"),
                request.getParameter("phone")
            );
            response.sendRedirect(request.getContextPath() + "?msg=signupsucceeded");
        } catch (UseCaseException e) {
            request.setAttribute("msg", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/html/signup.jsp").forward(request, response);
        }
    }

    private void handleDeleteAccount(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Member user = session != null ? (Member) session.getAttribute("loginUser") : null;
        if (user == null) { response.sendRedirect(request.getContextPath() + "/login"); return; }

        try {
            new DeleteAccountUseCase().execute(user.getId(), request.getParameter("confirmPw"));
            clearRememberMeCookie(request, response);
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/?msg=accountdeletesucceeded");
        } catch (UseCaseException e) {
            response.sendRedirect(request.getContextPath() + "/account?error=" + java.net.URLEncoder.encode(e.getMessage(), "UTF-8"));
        }
    }

    private void handleUpdateDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Member loginUser = session != null ? (Member) session.getAttribute("loginUser") : null;
        if (loginUser == null) { response.sendRedirect(request.getContextPath() + "/login"); return; }

        try {
            Member updated = new UpdateMemberDetailUseCase().execute(
                loginUser.getId(),
                request.getParameter("name"),
                request.getParameter("mail"),
                request.getParameter("phone")
            );
            session.setAttribute("loginUser", updated);
            response.sendRedirect(request.getContextPath() + "/account?msg=updateaccountdetailsucceeded");
        } catch (UseCaseException e) {
            request.setAttribute("msg", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/html/profile.jsp").forward(request, response);
        }
    }

    private void handleUpdatePassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Member user = session != null ? (Member) session.getAttribute("loginUser") : null;
        if (user == null) { response.sendRedirect(request.getContextPath() + "/login"); return; }

        try {
            new UpdatePasswordUseCase().execute(user.getId(), request.getParameter("currentPw"), request.getParameter("newPw"));
            new RevokeAllRememberMeTokensUseCase().execute(user.getId());
            clearRememberMeCookie(request, response);
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/login?msg=updateaccountpwsucceeded");
        } catch (UseCaseException e) {
            request.setAttribute("msg", e.getMessage());
            request.getRequestDispatcher("/account").forward(request, response);
        }
    }

    private void clearRememberMeCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("remember_me".equals(cookie.getName())) {
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                    break;
                }
            }
        }
    }
}
