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
import java.util.List;
import java.io.File;

import dao.BoardDAO;
import dao.MemberDAO;
import dao.ProductDAO;
import dto.BoardDTO;
import dto.MemberDTO;
import dto.PageHandler;
import dto.ProductDTO;
import util.DBManager;
import util.PasswordManager;

@WebServlet("/Board")
public class LoadBoardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int page = 1;
        String pageStr = request.getParameter("page");
        if (pageStr != null && !pageStr.isEmpty()) {
            page = Integer.parseInt(pageStr);
        }
        int pageSize = 5;

        BoardDAO dao = new BoardDAO();

        int totalCnt = dao.countAll();

        PageHandler pageHandler = new PageHandler(totalCnt, page, pageSize);

        List<BoardDTO> list = dao.selectAll(page, pageSize);

        request.setAttribute("list", list);
        request.setAttribute("ph", pageHandler);

        request.getRequestDispatcher("/WEB-INF/html/community.jsp").forward(request, response);
    }
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
