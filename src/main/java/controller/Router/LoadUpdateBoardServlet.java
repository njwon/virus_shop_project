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

import dao.BoardDAO;
import dao.MemberDAO;
import dao.ProductDAO;
import dto.BoardDTO;
import dto.MemberDTO;
import dto.ProductDTO;
import util.DBManager;
import util.PasswordManager;

@WebServlet("/UpdateBoard")
public class LoadUpdateBoardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
				request.setCharacterEncoding("utf-8");
				String boardId = request.getParameter("id"); 
			    try {
			    	BoardDAO dao = BoardDAO.getInstance();
			    	BoardDTO board = dao.getBoardById(boardId);
			        request.setAttribute("board", board);
			        request.getRequestDispatcher("/WEB-INF/html/updateboard.jsp").forward(request, response);
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
