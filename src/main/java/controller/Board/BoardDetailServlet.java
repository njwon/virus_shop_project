package controller.Board;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.BoardDAO;
import dao.ProductDAO;
import dto.BoardDTO;
import dto.ProductDTO;

@WebServlet("/BoardDetail")
public class BoardDetailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String Id = request.getParameter("id"); 
        
        if (Id == null || Id.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        try {
            BoardDAO dao = BoardDAO.getInstance();
            
            BoardDTO board = dao.getBoardById(Id);
            
            dao.increaseHit(board);
            
            request.setAttribute("board", board);
            
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/html/inquiry_detail.jsp");
            dispatcher.forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/");
        }
    }
}