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
import dto.MemberDTO;
import util.DBManager;
import util.PasswordManager;

@WebServlet("/addPost.do")
public class AddPostServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
        HttpSession session = request.getSession();
        MemberDTO member = (MemberDTO) session.getAttribute("loginUser");

		String title = request.getParameter("title");
		String body = request.getParameter("body");

		BoardDTO board = new BoardDTO();
		board.setMemberId(member.getId());
		board.setMemberName(member.getName());
		board.setSubject(title);
		board.setContent(body);
		board.setHit(0);
		board.setIp(member.getLastIp());
		try {
			BoardDAO dao = BoardDAO.getInstance();
			dao.addPost(board);
			response.sendRedirect(request.getContextPath() + "/Board?msg=addpostsucceeded");
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
}
