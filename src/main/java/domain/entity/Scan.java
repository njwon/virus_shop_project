package domain.entity;

public class Scan {
    private int num;
    private String uuid;
    private String userId;
    private String fileName;
    private String scanDate;
    private int malicious;
    private int suspicious;
    private int harmless;
    private int undetected;

    public Scan() {}

    public Scan(String date, String userId, String fileName, int malicious, int suspicious, int undetected, int harmless, int total) {
        this.scanDate  = date;
        this.userId    = userId;
        this.fileName  = fileName;
        this.malicious  = malicious;
        this.suspicious = suspicious;
        this.harmless   = harmless;
        this.undetected = undetected;
    }

    public int getTotal() { return malicious + suspicious + harmless + undetected; }
    public String getResultStatus() { return (malicious > 0 || suspicious > 0) ? "위험" : "안전"; }

    public int getNum() { return num; }
    public void setNum(int num) { this.num = num; }

    public String getUuid() { return uuid; }
    public void setUuid(String uuid) { this.uuid = uuid; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getScanDate() { return scanDate; }
    public void setScanDate(String scanDate) { this.scanDate = scanDate; }

    public int getMalicious() { return malicious; }
    public void setMalicious(int malicious) { this.malicious = malicious; }

    public int getSuspicious() { return suspicious; }
    public void setSuspicious(int suspicious) { this.suspicious = suspicious; }

    public int getHarmless() { return harmless; }
    public void setHarmless(int harmless) { this.harmless = harmless; }

    public int getUndetected() { return undetected; }
    public void setUndetected(int undetected) { this.undetected = undetected; }
}
