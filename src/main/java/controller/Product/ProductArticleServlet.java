package controller.Product;

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

import java.util.ArrayList;
import java.util.Enumeration;
import java.io.File;

import dao.ProductDAO;
import dto.ProductDTO;
import util.DBManager;
@WebServlet("/productArticle")
public class ProductArticleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	        try {
	        	ProductDAO dao = ProductDAO.getInstance();
	            ArrayList<ProductDTO> listOfProducts = dao.getAllProducts();
	            request.setAttribute("productList", listOfProducts);
	            RequestDispatcher dispatcher = request.getRequestDispatcher("product.jsp");
	            dispatcher.forward(request, response);
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
	    }
	}
