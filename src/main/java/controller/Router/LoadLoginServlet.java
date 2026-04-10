package controller.Router;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import domain.entity.Product;

@WebServlet("/login")
public class LoadLoginServlet extends HttpServlet {	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session != null && session.getAttribute("loginUser") != null) {
			response.sendRedirect(request.getContextPath() + "/dashboard");
			return;
		}
		request.getRequestDispatcher("/WEB-INF/html/login.jsp").forward(request, response);
	}
}