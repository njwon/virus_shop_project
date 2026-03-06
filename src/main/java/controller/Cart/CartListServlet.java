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

@WebServlet("/cartList.do")
public class CartListServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 로그인 체크
        HttpSession session = request.getSession();
        MemberDTO member = (MemberDTO) session.getAttribute("loginUser");
        System.out.println("장바구니: "+ member);
        if (member == null) {
            // 로그인이 안 되어 있다면 로그인 페이지로 튕겨내기
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String memberId = member.getId();

        // DAO에서 목록 가져오기
        CartDTO dto = new CartDTO();
        CartDAO dao = CartDAO.getInstance();
        ArrayList<CartDTO> cartList = dao.getCartList(memberId);
        
        // request 영역에 저장 (JSP로 넘기기 위해)
        request.setAttribute("cartList", cartList);
        
        // 총 주문 금액 계산 (옵션)
        int totalSum = 0;
        for(CartDTO cart : cartList) {
            totalSum += cart.getTotalPrice();
        }
        request.setAttribute("total", totalSum);
        
        // JSP로 이동
        request.getRequestDispatcher("/WEB-INF/html/cart.jsp").forward(request, response);
    }
}