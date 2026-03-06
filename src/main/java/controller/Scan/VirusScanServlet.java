package controller.Scan;

import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import org.json.JSONObject;
import dao.ScanDAO;
import dto.MemberDTO;
import dto.ScanDTO;
import util.VirusTotalService;

@WebServlet("/ScanFile.do")
public class VirusScanServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
        HttpSession session = request.getSession();
        MemberDTO member = (MemberDTO) session.getAttribute("loginUser");
		
		// 파일이 저장될 서버의 실제 경로 찾기
		String savePath = request.getServletContext().getRealPath("upload");
		File dir = new File(savePath);
		if(!dir.exists()) dir.mkdirs(); // 폴더 없으면 생성
		
		int sizeLimit = 32 * 1024 * 1024; // 32MB 제한

		try {
			// JSP의 <input name="file">과 일치해야 함
			MultipartRequest multi = new MultipartRequest(request, savePath, sizeLimit, "UTF-8", new DefaultFileRenamePolicy());
			
			File file = multi.getFile("file"); 

			if(file == null) {
				response.sendRedirect("write.jsp");
				return;
			}

			// VirusTotal API 서비스 호출
			System.out.println(">>> VirusTotal 검사 시작: " + file.getName());
			VirusTotalService vtService = new VirusTotalService();
			
			String analysisId = vtService.uploadFile(file);
			JSONObject stats = vtService.getReport(analysisId);

			int malicious = stats.getInt("malicious");
			int suspicious = stats.getInt("suspicious");
			int harmless = stats.getInt("harmless");
			int undetected = stats.getInt("undetected");
			int total = malicious + suspicious + harmless + undetected;
			String date = LocalDate.now().toString();

			ScanDTO dto = new ScanDTO(date, member.getId(), file.getName(), malicious, suspicious, harmless, undetected, total);
			ScanDAO.getInstance().saveResult(dto);

			System.out.println(">>> 검사 완료 및 DB 저장 성공");

			request.setAttribute("result", dto);
			request.getRequestDispatcher("/scanArticle").forward(request, response);

		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("index.jsp?error=scanfailed");
		}
	}
}