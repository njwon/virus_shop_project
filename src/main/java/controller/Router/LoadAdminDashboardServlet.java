package controller.Router;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import domain.entity.Member;
import domain.entity.Product;
import infrastructure.persistence.ProductRepositoryImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/admin")
public class LoadAdminDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(LoadAdminDashboardServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Member user = session != null ? (Member) session.getAttribute("loginUser") : null;

        if (user == null || !"ADMIN".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/?error=forbidden");
            return;
        }

        log.info("관리자 대시보드 접근 - ID: {}", user.getId());
        ArrayList<Product> list = ProductRepositoryImpl.getInstance().findAll();

        request.setAttribute("productList", list);
        request.setAttribute("productCount", list != null ? list.size() : 0);
        request.setAttribute("accountName", user.getName());
        request.getRequestDispatcher("/WEB-INF/html/admindashboard.jsp").forward(request, response);
    }
}
