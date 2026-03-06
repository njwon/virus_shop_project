package controller.Cart;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.CartDAO;
import dto.CartDTO;
import dto.MemberDTO;

@WebServlet("/addCart.do")
public class AddCartServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 한글 처리
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        MemberDTO member = (MemberDTO) session.getAttribute("loginUser");
        System.out.println("장바구니: "+ member);
        if (member == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String memberId = member.getId();

        String productId = request.getParameter("productId");
        String price = request.getParameter("unitPrice");
        
        int quantity = 1;
        String quantityStr = request.getParameter("quantity");
        if(quantityStr != null && !quantityStr.isEmpty()) {
            quantity = Integer.parseInt(quantityStr);
        }

        CartDTO cart = new CartDTO();
        cart.setMemberId(memberId);
        cart.setProductId(productId);
        cart.setQuantity(quantity);
        cart.setUnitPrice(Integer.parseInt(price));

        try {
            CartDAO dao = CartDAO.getInstance();
            dao.addCart(cart);

            response.sendRedirect(request.getContextPath() + "/cartList.do"); 

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("장바구니 담기 중 에러 발생", e);
        }
    }
}