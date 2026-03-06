package controller.Router;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import java.util.Enumeration;
import java.io.File;

import dao.MemberDAO;
import dao.ProductDAO;
import dto.MemberDTO;
import dto.ProductDTO;
import util.DBManager;
import util.PasswordManager;

@WebServlet("/UpdateProduct")
public class LoadUpdateProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
				request.setCharacterEncoding("utf-8");
				String productId = request.getParameter("id"); 
			    
			    try {
			    	ProductDAO dao = ProductDAO.getInstance();
			    	ProductDTO product = dao.getProductById(productId);
			        request.setAttribute("product", product);
			        request.getRequestDispatcher("/WEB-INF/html/updateproduct.jsp").forward(request, response);
		        } catch (Exception e) {
		            e.printStackTrace();
		            response.sendRedirect(request.getContextPath());
		        }
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
