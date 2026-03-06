package controller.Cart; // 패키지명 확인

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.CartDAO;
import dto.CartDTO;
import dto.MemberDTO;

@WebServlet("/clearCart.do")
public class ClearCartServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	HttpSession session = request.getSession();
        MemberDTO member = (MemberDTO) session.getAttribute("loginUser"); // 세션에서 로그인 아이디 가져오기
        
        String userId = member.getId();
        
        if (userId != null) {
            CartDAO dao = CartDAO.getInstance();
            dao.deleteCartByUserId(userId); // DB에서 해당 사용자의 장바구니 삭제
        }

        response.sendRedirect(request.getContextPath() + "?msg=purchasesucceeded");
    }
}