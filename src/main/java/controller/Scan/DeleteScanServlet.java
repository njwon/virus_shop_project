package controller.Scan;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.MemberDAO;
import dao.ProductDAO;
import dao.ScanDAO;
import dto.MemberDTO;

@WebServlet("/DeleteScan.do")
public class DeleteScanServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String scanId = request.getParameter("id");
		ScanDAO dao = ScanDAO.getInstance();
		dao.deleteScan(scanId);

		// 메인 페이지나 로그인 페이지로 이동
		response.sendRedirect(request.getContextPath() + "/UserBoard.do?msg=delscansucceeded");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}