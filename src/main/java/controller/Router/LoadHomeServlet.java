package controller.Router;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import domain.entity.Product;
import infrastructure.persistence.ProductRepositoryImpl;

@WebServlet("/")
public class LoadHomeServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ArrayList<Product> list = ProductRepositoryImpl.getInstance().findAll();
        request.setAttribute("productList", list);
        request.getRequestDispatcher("/WEB-INF/html/index.jsp").forward(request, response);
    }
}
