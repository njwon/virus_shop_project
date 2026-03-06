package controller.Product;

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
import dto.ProductDTO;
import util.DBManager;

@WebServlet("/updateProduct.do")
public class UpdateProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	    protected void doPost(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {

	    	
	    	String savePath = "/resources/images"; 
	        String realFolder = request.getServletContext().getRealPath(savePath);
	    	String encType = "utf-8";
	    	//최대파일용량 설정
	    	
	    	File dir = new File(realFolder);

	        if (!dir.exists()) {
	            dir.mkdirs(); 
	        }
	    	
	    	int maxSize = 5 * 1024 * 1024; //최대 업로드될 파일의 크기5Mb
	    	
	    	MultipartRequest multi = new MultipartRequest(request, realFolder, maxSize, encType, new DefaultFileRenamePolicy());
	    	
	    	Enumeration files = multi.getFileNames();
	    	String fname = (String) files.nextElement();
	    	String fileName = "";
	    	if(multi.getFilesystemName("productImage") != null) {	    		
	    		fileName = multi.getFilesystemName("productImage");
	    	}
	    	
	    	String productId = multi.getParameter("productId");
	        String name = multi.getParameter("pname");
	        String unitPrice = multi.getParameter("unitPrice");
	        String category = multi.getParameter("category");
	        String description = multi.getParameter("description");
	        ProductDTO product = new ProductDTO(productId, name, unitPrice);
	        product.setCategory(category);
	        product.setDescription(description);
	        product.setProductImage(fileName);
	        
	        System.out.println(product.getProductId());
	        try {
				ProductDAO dao = ProductDAO.getInstance();
				String errorMsg = null;
				
				int result = dao.updateProductDetail(product);

				response.sendRedirect(request.getContextPath() + "/AdminBoard.do?msg=updateproductsucceeded");
			} catch (Exception e) {
				throw new ServletException(e);
			}
	    }
	}
