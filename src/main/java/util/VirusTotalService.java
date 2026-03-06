package util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;

// ★ 중요: json-xxxx.jar 라이브러리가 lib 폴더에 있어야 작동합니다.
import org.json.JSONObject; 

public class VirusTotalService {

    // 환경변수 VIRUSTOTAL_API_KEY 에서 API 키를 읽어옵니다.
    private static final String API_KEY = System.getenv("VIRUSTOTAL_API_KEY");

    // [1] 파일을 바이러스 토탈 서버로 전송하는 기능
    public String uploadFile(File file) throws Exception {
        String boundary = "---" + System.currentTimeMillis();
        URL url = new URL("https://www.virustotal.com/api/v3/files");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
        conn.setRequestMethod("POST");
        conn.setRequestProperty("x-apikey", API_KEY);
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        conn.setDoOutput(true);

        OutputStream os = conn.getOutputStream();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(os, "UTF-8"), true);

        // 파일 전송을 위한 헤더 작성
        writer.append("--" + boundary).append("\r\n");
        writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getName() + "\"").append("\r\n");
        writer.append("Content-Type: application/octet-stream").append("\r\n"); 
        writer.append("\r\n").flush();
        
        // 실제 파일 데이터 전송
        Files.copy(file.toPath(), os);
        os.flush();
        
        // 종료 헤더
        writer.append("\r\n").flush();
        writer.append("--" + boundary + "--").append("\r\n");
        writer.close();

        // 응답 받기 (분석 ID 획득)
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) response.append(line);
        
        JSONObject json = new JSONObject(response.toString());
        return json.getJSONObject("data").getString("id"); // 분석 ID 리턴
    }

    // [2] 검사 결과가 나올 때까지 기다렸다가 받아오는 기능
    public JSONObject getReport(String analysisId) throws Exception {
        while (true) {
            URL url = new URL("https://www.virustotal.com/api/v3/analyses/" + analysisId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("x-apikey", API_KEY);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) response.append(line);
            
            JSONObject json = new JSONObject(response.toString());
            String status = json.getJSONObject("data").getJSONObject("attributes").getString("status");

            // 상태가 'completed'가 되면 결과를 반환
            if ("completed".equals(status)) {
                return json.getJSONObject("data").getJSONObject("attributes").getJSONObject("stats");
            }

            // 아직 검사 중이면 3초 대기 후 다시 질문
            System.out.println("검사 진행 중... 3초 대기...");
            Thread.sleep(3000); 
        }
    }
}