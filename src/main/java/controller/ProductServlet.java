package controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import domain.entity.Member;
import domain.entity.Product;
import usecase.UseCaseException;
import usecase.product.AddProductUseCase;
import usecase.product.DeleteProductUseCase;
import usecase.product.GetProductUseCase;
import usecase.product.ProductImageValidator;
import usecase.product.UpdateProductUseCase;

/**
 * GET    /products/{uuid}      → 상품 상세
 * GET    /products/{uuid}/edit → 수정 폼
 * POST   /products             → 상품 등록
 * PUT    /products/{uuid}      → 상품 수정
 * DELETE /products/{uuid}      → 상품 삭제
 */
@WebServlet("/products/*")
public class ProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String IMAGE_PATH = "/resources/images";
    private static final int MAX_FILE_SIZE = 5 * 1024 * 1024;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) { response.sendRedirect(request.getContextPath() + "/"); return; }

        String[] parts = pathInfo.split("/");
        String uuid = parts.length > 1 ? parts[1] : null;
        if (uuid == null || uuid.isEmpty()) { response.sendRedirect(request.getContextPath() + "/"); return; }

        Product product = new GetProductUseCase().execute(uuid);
        if (product == null) { response.sendRedirect(request.getContextPath() + "/"); return; }

        if (parts.length > 2 && "edit".equals(parts[2])) {
            HttpSession session = request.getSession(false);
            Member user = session != null ? (Member) session.getAttribute("loginUser") : null;
            if (user == null || !"ADMIN".equals(user.getRole())) { response.sendRedirect(request.getContextPath() + "/"); return; }
            request.setAttribute("product", product);
            request.getRequestDispatcher("/WEB-INF/html/updateproduct.jsp").forward(request, response);
            return;
        }

        request.setAttribute("product", product);
        request.getRequestDispatcher("/WEB-INF/html/product_detail.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Member user = session != null ? (Member) session.getAttribute("loginUser") : null;
        if (user == null || !"ADMIN".equals(user.getRole())) { response.sendRedirect(request.getContextPath() + "/login"); return; }

        String realFolder = request.getServletContext().getRealPath(IMAGE_PATH);
        new File(realFolder).mkdirs();
        try {
            MultipartRequest multi = new MultipartRequest(request, realFolder, MAX_FILE_SIZE, "UTF-8", new DefaultFileRenamePolicy());
            String fileName = saveWithUuid(multi, realFolder, "productImage");
            Product product = new Product(multi.getParameter("productId"), multi.getParameter("pname"), multi.getParameter("unitPrice"));
            product.setCategory(multi.getParameter("category"));
            product.setDescription(multi.getParameter("description"));
            product.setProductImage(fileName);
            new AddProductUseCase().execute(product);
            response.sendRedirect(request.getContextPath() + "/admin");
        } catch (UseCaseException e) {
            response.sendRedirect(request.getContextPath() + "/admin?error=" + java.net.URLEncoder.encode(e.getMessage(), "UTF-8"));
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin?error=" + java.net.URLEncoder.encode("가격은 숫자로 입력해주세요.", "UTF-8"));
        } catch (IOException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Member user = session != null ? (Member) session.getAttribute("loginUser") : null;
        if (user == null || !"ADMIN".equals(user.getRole())) { response.setStatus(HttpServletResponse.SC_FORBIDDEN); return; }

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) { response.setStatus(HttpServletResponse.SC_BAD_REQUEST); return; }

        String realFolder = request.getServletContext().getRealPath(IMAGE_PATH);
        new File(realFolder).mkdirs();
        try {
            MultipartRequest multi = new MultipartRequest(request, realFolder, MAX_FILE_SIZE, "UTF-8", new DefaultFileRenamePolicy());
            String fileName = saveWithUuid(multi, realFolder, "productImage");
            Product product = new Product(multi.getParameter("productId"), multi.getParameter("pname"), multi.getParameter("unitPrice"));
            product.setCategory(multi.getParameter("category"));
            product.setDescription(multi.getParameter("description"));
            if (!fileName.isEmpty()) product.setProductImage(fileName);
            new UpdateProductUseCase().execute(product);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (UseCaseException | NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("text/plain; charset=UTF-8");
            response.getWriter().write(e instanceof NumberFormatException ? "가격은 숫자로 입력해주세요." : e.getMessage());
        } catch (IOException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Member user = session != null ? (Member) session.getAttribute("loginUser") : null;
        if (user == null || !"ADMIN".equals(user.getRole())) { response.setStatus(HttpServletResponse.SC_FORBIDDEN); return; }

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) { response.setStatus(HttpServletResponse.SC_BAD_REQUEST); return; }

        new DeleteProductUseCase().execute(pathInfo.substring(1));
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private String saveWithUuid(MultipartRequest multi, String folder, String fieldName) throws IOException {
        String original = multi.getFilesystemName(fieldName);
        if (original == null || original.isEmpty()) return "";

        String ext = original.contains(".") ? original.substring(original.lastIndexOf(".")).toLowerCase() : "";
        if (!ProductImageValidator.ALLOWED_EXTENSIONS.contains(ext))
            throw new IOException("허용되지 않는 파일 형식입니다. (jpg, jpeg, png, gif, webp만 가능)");

        String contentType = multi.getContentType(fieldName);
        if (contentType == null || !ProductImageValidator.ALLOWED_MIME_TYPES.contains(contentType.toLowerCase()))
            throw new IOException("허용되지 않는 MIME 타입입니다.");

        File src = multi.getFile(fieldName);
        if (!ProductImageValidator.isValidSignature(src)) {
            src.delete();
            throw new IOException("파일 내용이 이미지가 아닙니다.");
        }

        String uuidName = UUID.randomUUID().toString() + ext;
        Files.move(src.toPath(), new File(folder, uuidName).toPath(), StandardCopyOption.REPLACE_EXISTING);
        return uuidName;
    }
}
