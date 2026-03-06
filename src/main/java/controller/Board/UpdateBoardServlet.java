package controller.Board;

import java.io.IOException;
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
import dto.ProductDTO;
import util.DBManager;

@WebServlet("/updateBoard.do")
public class UpdateBoardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	    protected void doPost(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
	    	request.setCharacterEncoding("UTF-8");
	    	String Id = request.getParameter("Id");
	        String title = request.getParameter("title");
	        String body = request.getParameter("body");
	        BoardDTO board = new BoardDTO();
	        board.setId(Integer.parseInt(Id));
	        board.setSubject(title);
	        board.setContent(body);
	        
	        try {
				BoardDAO dao = BoardDAO.getInstance();
				String errorMsg = null;
				
				int result = dao.updatePost(board);

				response.sendRedirect(request.getContextPath() + "/Board?msg=updateproductsucceeded");
			} catch (Exception e) {
				throw new ServletException(e);
			}
	    }
	}
