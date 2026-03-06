package controller.Member;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import dao.MemberDAO;
import dto.MemberDTO;
import util.PasswordManager;

@WebServlet("/updateAccountPw.do")
public class UpdateAccountPassword extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        
        HttpSession session = request.getSession();
        MemberDTO user = (MemberDTO) session.getAttribute("loginUser");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String currentPw = request.getParameter("currentPw");
        String newPw = request.getParameter("newPw");
        
        MemberDAO dao = MemberDAO.getInstance();
        
        // DB에서 현재 로그인한 사람의 최신 정보를 가져옴 (비밀번호 확인용)
        MemberDTO dbInfo = dao.getMember(user.getId());

        try {
            if (dbInfo != null && PasswordManager.checkPassword(currentPw,dbInfo.getPw())) {
                
                MemberDTO member = new MemberDTO();
                member.setId(user.getId());
                member.setPw(PasswordManager.hashPassword(newPw));
                
                // DB 업데이트
                dao.updateMemberPw(member);
                
                response.sendRedirect(request.getContextPath() + "/AccountDetail.do?msg=updateaccountpwsucceeded");
            
            } else {
                request.setAttribute("msg", "입력한 비밀번호가 계정의 비밀번호와 같지 않습니다.");
                request.getRequestDispatcher("/AccountDetail.do").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }
}