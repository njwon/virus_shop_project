package util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VirusTotalService {

    private static final Logger log = LoggerFactory.getLogger(VirusTotalService.class);

    private static final String API_KEY = loadApiKey();

    private static String loadApiKey() {
        String fromEnv = System.getenv("VIRUSTOTAL_API_KEY");
        if (fromEnv != null && !fromEnv.isEmpty()) return fromEnv;

        String[] envPaths = {
            System.getProperty("catalina.home") + "/.env",
            System.getProperty("user.home") + "/.env"
        };
        for (String envPath : envPaths) {
            try (BufferedReader reader = new BufferedReader(new FileReader(envPath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.startsWith("VIRUSTOTAL_API_KEY=")) {
                        return line.substring("VIRUSTOTAL_API_KEY=".length()).trim();
                    }
                }
            } catch (Exception e) {
                log.debug(".env 파일을 읽을 수 없습니다: {}", envPath);
            }
        }

        throw new IllegalStateException("[VirusTotalService] VIRUSTOTAL_API_KEY가 설정되지 않았습니다. .env 파일 또는 환경변수를 확인하세요.");
    }

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

        writer.append("--" + boundary).append("\r\n");
        writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getName() + "\"").append("\r\n");
        writer.append("Content-Type: application/octet-stream").append("\r\n");
        writer.append("\r\n").flush();

        Files.copy(file.toPath(), os);
        os.flush();

        writer.append("\r\n").flush();
        writer.append("--" + boundary + "--").append("\r\n");
        writer.close();

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) response.append(line);

        JSONObject json = new JSONObject(response.toString());
        return json.getJSONObject("data").getString("id");
    }

    public JSONObject getReport(String analysisId) throws Exception {
        final int MAX_RETRIES = 20;
        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
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

            if ("completed".equals(status)) {
                return json.getJSONObject("data").getJSONObject("attributes").getJSONObject("stats");
            }

            log.info("검사 진행 중... ({}/{}), 3초 대기...", attempt, MAX_RETRIES);
            Thread.sleep(3000);
        }
        throw new Exception("[VirusTotalService] 분석 타임아웃: " + MAX_RETRIES + "회 재시도 초과 (약 60초)");
    }
}