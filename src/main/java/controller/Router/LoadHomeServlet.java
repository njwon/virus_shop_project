package controller.Router;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.MemberDAO;
import dao.ProductDAO;
import dto.ProductDTO;

@WebServlet("")
public class LoadHomeServlet extends HttpServlet {	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		ProductDAO Pdao = ProductDAO.getInstance();
		MemberDAO Mdao = MemberDAO.getInstance();
		
		ArrayList<ProductDTO> list = Pdao.getAllProducts();
		
		request.setAttribute("productList", list);
		
		RequestDispatcher dis = request.getRequestDispatcher("/WEB-INF/html/index.jsp");
		dis.forward(request, response);
	}
}