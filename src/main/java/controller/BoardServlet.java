package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import domain.entity.Board;
import domain.entity.Member;
import domain.entity.PageHandler;
import usecase.UseCaseException;
import usecase.board.CreatePostUseCase;
import usecase.board.DeletePostUseCase;
import usecase.board.GetBoardListUseCase;
import usecase.board.GetBoardUseCase;
import usecase.board.IncreaseHitUseCase;
import usecase.board.UpdatePostUseCase;

/**
 * GET    /boards        → 게시글 목록
 * GET    /boards/{uuid} → 게시글 상세
 * GET    /boards/{uuid}/edit → 수정 폼
 * POST   /boards        → 게시글 작성
 * PUT    /boards/{uuid} → 게시글 수정 (fetch)
 * DELETE /boards/{uuid} → 게시글 삭제 (fetch)
 */
@WebServlet("/boards/*")
public class BoardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            int page = 1;
            String pageStr = request.getParameter("page");
            if (pageStr != null && !pageStr.isEmpty()) {
                try { page = Integer.parseInt(pageStr); } catch (NumberFormatException e) { page = 1; }
            }
            int pageSize = 5;
            GetBoardListUseCase listUseCase = new GetBoardListUseCase();
            PageHandler pageHandler = new PageHandler(listUseCase.countAll(), page, pageSize);
            List<Board> list = listUseCase.execute(page, pageSize);
            request.setAttribute("list", list);
            request.setAttribute("ph", pageHandler);
            request.getRequestDispatcher("/WEB-INF/html/community.jsp").forward(request, response);
            return;
        }

        String[] parts = pathInfo.split("/");
        String uuid = parts.length > 1 ? parts[1] : null;
        if (uuid == null || uuid.isEmpty()) { response.sendRedirect(request.getContextPath() + "/boards"); return; }

        Board board = new GetBoardUseCase().execute(uuid);
        if (board == null) { response.sendRedirect(request.getContextPath() + "/boards"); return; }

        if (parts.length > 2 && "edit".equals(parts[2])) {
            HttpSession editSession = request.getSession(false);
            Member editUser = editSession != null ? (Member) editSession.getAttribute("loginUser") : null;
            if (editUser == null || (!editUser.getId().equals(board.getMemberId()) && !"ADMIN".equals(editUser.getRole()))) {
                response.sendRedirect(request.getContextPath() + "/boards");
                return;
            }
            request.setAttribute("board", board);
            request.getRequestDispatcher("/WEB-INF/html/updateboard.jsp").forward(request, response);
            return;
        }

        new IncreaseHitUseCase().execute(board);
        request.setAttribute("board", board);
        request.getRequestDispatcher("/WEB-INF/html/inquiry_detail.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        Member member = session != null ? (Member) session.getAttribute("loginUser") : null;
        if (member == null) { response.sendRedirect(request.getContextPath() + "/login"); return; }

        Board board = new Board();
        board.setMemberId(member.getId());
        board.setMemberName(member.getName());
        board.setSubject(request.getParameter("title"));
        board.setContent(request.getParameter("body"));
        board.setHit(0);
        board.setIp(request.getRemoteAddr());

        try {
            new CreatePostUseCase().execute(board);
            response.sendRedirect(request.getContextPath() + "/boards?msg=addpostsucceeded");
        } catch (UseCaseException e) {
            response.sendRedirect(request.getContextPath() + "/boards?error=" + URLEncoder.encode(e.getMessage(), "UTF-8"));
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        Member member = session != null ? (Member) session.getAttribute("loginUser") : null;
        if (member == null) { response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); return; }

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) { response.setStatus(HttpServletResponse.SC_BAD_REQUEST); return; }

        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
        }
        String title = "", body = "";
        for (String param : sb.toString().split("&")) {
            String[] kv = param.split("=", 2);
            if (kv.length == 2) {
                String key   = URLDecoder.decode(kv[0], "UTF-8");
                String value = URLDecoder.decode(kv[1], "UTF-8");
                if ("title".equals(key)) title = value;
                if ("body".equals(key))  body  = value;
            }
        }

        try {
            new UpdatePostUseCase().execute(pathInfo.substring(1), title, body, member.getId(), "ADMIN".equals(member.getRole()));
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (UseCaseException e) {
            int status = e.getMessage().contains("권한") ? HttpServletResponse.SC_FORBIDDEN : HttpServletResponse.SC_BAD_REQUEST;
            response.setStatus(status);
            response.setContentType("text/plain; charset=UTF-8");
            response.getWriter().write(e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Member member = session != null ? (Member) session.getAttribute("loginUser") : null;
        if (member == null) { response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); return; }

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) { response.setStatus(HttpServletResponse.SC_BAD_REQUEST); return; }

        try {
            new DeletePostUseCase().execute(pathInfo.substring(1), member.getId(), "ADMIN".equals(member.getRole()));
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (UseCaseException e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}
