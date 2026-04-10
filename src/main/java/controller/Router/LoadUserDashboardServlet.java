package controller.Router;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import domain.entity.Member;
import domain.entity.Scan;
import infrastructure.persistence.ScanRepositoryImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/dashboard")
public class LoadUserDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(LoadUserDashboardServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Member user = session != null ? (Member) session.getAttribute("loginUser") : null;

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login?error=auth");
            return;
        }

        List<Scan> list = ScanRepositoryImpl.getInstance().findHistoryByUserId(user.getId());

        request.setAttribute("scanList", list);
        request.setAttribute("scanCount", list != null ? list.size() : 0);
        request.setAttribute("accountName", user.getName());
        log.info("사용자 대시보드 접근 - ID: {}, 스캔 기록 수: {}", user.getId(), list != null ? list.size() : 0);
        request.getRequestDispatcher("/WEB-INF/html/dashboard.jsp").forward(request, response);
    }
}
