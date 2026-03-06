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
import dto.ProductDTO;
import util.DBManager;
import util.PasswordManager;

@WebServlet("/updateAccountDetail.do")
public class UpdateAccountDetail extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		
		MemberDAO dao = MemberDAO.getInstance();

		String id = request.getParameter("id");
		String name = request.getParameter("name");
		String mail = request.getParameter("mail");
		String phone = request.getParameter("phone");

		MemberDTO existingMember = dao.getMember(id);

		MemberDTO member = new MemberDTO();
		member.setId(id);
		member.setName(name);
		member.setMail(mail);
		member.setPhone(phone);
		member.setRole(existingMember.getRole());

		try {
			String errorMsg = null;
			boolean[] checkResults = dao.isIdDuplicateUpdate(id, mail, phone);
			// checkResults[0]: ID 중복 여부
			// checkResults[1]: Mail 중복 여부
			// checkResults[2]: Phone 중복 여부

			StringBuilder sb = new StringBuilder();
			
			if (checkResults[0]) {
				sb.append("이메일");
			}

			if (checkResults[1]) {
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
				request.getRequestDispatcher(request.getContextPath() + "/AccountDetail.do").forward(request, response);
				return;
			}
			
			System.out.println("수정 시도 ID: [" + member.getId() + "]"); 
			System.out.println("수정할 이름: " + member.getName());
			int result = dao.updateMemberDetail(member);
			if (result == 1) {
			    HttpSession session = request.getSession();
			    
			    session.setAttribute("loginUser", member); 
			}
			response.sendRedirect(request.getContextPath() + "/AccountDetail.do?msg=updateaccountdetailsucceeded");
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
}
