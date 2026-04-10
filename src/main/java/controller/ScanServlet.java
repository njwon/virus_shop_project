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
import domain.entity.Scan;
import usecase.UseCaseException;
import usecase.scan.DeleteScanUseCase;
import usecase.scan.ScanFileUseCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * GET    /scans/new    → 스캔 폼
 * POST   /scans        → 파일 스캔
 * GET    /scans/result → 스캔 결과
 * DELETE /scans/{id}   → 스캔 기록 삭제
 */
@WebServlet("/scans/*")
public class ScanServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(ScanServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        if ("/new".equals(pathInfo)) {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("loginUser") == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }
            request.getRequestDispatcher("/WEB-INF/html/write.jsp").forward(request, response);
            return;
        }

        if ("/result".equals(pathInfo)) {
            request.getRequestDispatcher("/WEB-INF/html/article.jsp").forward(request, response);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/dashboard");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Member member = session != null ? (Member) session.getAttribute("loginUser") : null;
        if (member == null) { response.sendRedirect(request.getContextPath() + "/login"); return; }

        String savePath = System.getProperty("java.io.tmpdir") + File.separator + "vt_scan";
        new File(savePath).mkdirs();

        File file = null;
        try {
            MultipartRequest multi = new MultipartRequest(request, savePath, 32 * 1024 * 1024, "UTF-8", new DefaultFileRenamePolicy());
            File uploaded = multi.getFile("file");
            if (uploaded == null) { response.sendRedirect(request.getContextPath() + "/scans/new"); return; }

            String original    = multi.getOriginalFileName("file");
            String contentType = multi.getContentType("file");
            if (contentType == null) contentType = "";

            String ext = original.contains(".") ? original.substring(original.lastIndexOf(".")).toLowerCase() : "";
            file = new File(savePath, UUID.randomUUID().toString() + ext);
            Files.move(uploaded.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);

            log.info("업로드 파일: {}, MIME: {}, 요청자: {}", original, contentType, member.getId());

            Scan scan = new ScanFileUseCase().execute(file, original, member.getId());
            request.setAttribute("result", scan);
            request.getRequestDispatcher("/WEB-INF/html/article.jsp").forward(request, response);

        } catch (Exception e) {
            log.error("오류 발생", e);
            response.sendRedirect(request.getContextPath() + "/?error=scanfailed");
        } finally {
            if (file != null && file.exists()) {
                file.delete();
                log.debug("임시 파일 삭제 완료: {}", file.getName());
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Member member = session != null ? (Member) session.getAttribute("loginUser") : null;
        if (member == null) { response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); return; }

        String pathInfo = request.getPathInfo();
        log.info("DELETE 요청 - pathInfo: {}, memberId: {}", pathInfo, member.getId());
        if (pathInfo == null || pathInfo.equals("/")) { response.setStatus(HttpServletResponse.SC_BAD_REQUEST); return; }

        try {
            new DeleteScanUseCase().execute(pathInfo.substring(1), member.getId(), "ADMIN".equals(member.getRole()));
            log.info("DELETE 성공 - scanId: {}", pathInfo.substring(1));
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (UseCaseException e) {
            log.warn("DELETE 실패 - {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } catch (Exception e) {
            log.error("DELETE 예외", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
