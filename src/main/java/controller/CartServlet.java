package controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.net.URLEncoder;

import domain.entity.Cart;
import domain.entity.Member;
import usecase.UseCaseException;
import usecase.cart.AddToCartUseCase;
import usecase.cart.ClearCartUseCase;
import usecase.cart.GetCartUseCase;

/**
 * GET    /cart → 장바구니 목록
 * POST   /cart → 상품 담기
 * DELETE /cart → 장바구니 비우기
 */
@WebServlet("/cart")
public class CartServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private Member getLoginUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null ? (Member) session.getAttribute("loginUser") : null;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Member member = getLoginUser(request);
        if (member == null) { response.sendRedirect(request.getContextPath() + "/login"); return; }

        ArrayList<Cart> cartList = new GetCartUseCase().execute(member.getId());
        int total = 0;
        for (Cart item : cartList) total += item.getTotalPrice();

        request.setAttribute("cartList", cartList);
        request.setAttribute("total", total);
        request.getRequestDispatcher("/WEB-INF/html/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Member member = getLoginUser(request);
        if (member == null) { response.sendRedirect(request.getContextPath() + "/login"); return; }

        try {
            String quantityStr = request.getParameter("quantity");
            int quantity = (quantityStr != null && !quantityStr.isEmpty()) ? Integer.parseInt(quantityStr) : 1;
            if (quantity <= 0) quantity = 1;
            String unitPriceStr = request.getParameter("unitPrice");
            int unitPrice = (unitPriceStr != null && !unitPriceStr.isEmpty()) ? Integer.parseInt(unitPriceStr) : 0;

            Cart cart = new Cart();
            cart.setMemberId(member.getId());
            cart.setProductId(request.getParameter("productId"));
            cart.setQuantity(quantity);
            cart.setUnitPrice(unitPrice);

            new AddToCartUseCase().execute(cart);
            response.sendRedirect(request.getContextPath() + "/cart");
        } catch (NumberFormatException | UseCaseException e) {
            response.sendRedirect(request.getContextPath() + "/?error=" + URLEncoder.encode("잘못된 요청입니다.", "UTF-8"));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Member member = getLoginUser(request);
        if (member == null) { response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); return; }

        new ClearCartUseCase().execute(member.getId());
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
