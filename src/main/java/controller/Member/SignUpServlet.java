package controller.Member;

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

import dao.MemberDAO;
import dao.ProductDAO;
import dto.MemberDTO;
import util.DBManager;
import util.PasswordManager;

@WebServlet("/signUp.do")
public class SignUpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");

		String id = request.getParameter("id");
		String pw = request.getParameter("pw");
		String name = request.getParameter("name");
		String mail = request.getParameter("mail");
		String phone = request.getParameter("phone");

		MemberDTO member = new MemberDTO();
		member.setId(id);
		member.setPw(pw);
		member.setName(name);
		member.setMail(mail);
		member.setPhone(phone);
		try {
			MemberDAO dao = MemberDAO.getInstance();

			String errorMsg = null;
			boolean[] checkResults = dao.isIdDuplicate(id, mail, phone);
			// checkResults[0]: ID 중복 여부
			// checkResults[1]: Mail 중복 여부
			// checkResults[2]: Phone 중복 여부

			StringBuilder sb = new StringBuilder();
			
			if (checkResults[0]) {
				sb.append("아이디");
			}

			if (checkResults[1]) {
				if (sb.length() > 0) {
					sb.append("와 ");
				}
				sb.append("이메일");
			}

			if (checkResults[2]) {
				if (sb.length() > 0) {
					sb.append(", 그리고 ");
				}
				sb.append("전화번호");
			}

			if (sb.length() > 0) {
				sb.append("은(는) 이미 사용 중입니다.");
				errorMsg = sb.toString();
			}

			if (errorMsg != null) {
				request.setAttribute("msg", errorMsg);
				request.getRequestDispatcher("/signup").forward(request, response);
				return;
			}
			
			dao.addMember(member);
			response.sendRedirect(request.getContextPath() + "?msg=signupsucceeded");
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
}
