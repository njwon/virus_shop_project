package dto;

public class ScanDTO { // 클래스 이름 수정 (ScantDTO -> ScanDTO)
	private int num;
	private String userId;
	private String fileName;
	private String scanDate;
	private int malicious;
	private int suspicious;
	private int harmless;
	private int undetected;
	private int total;
	private String resultStatus;

	// [1] 기본 생성자
	public ScanDTO() {
		super(); // 생성자 이름은 클래스 이름과 똑같아야 합니다.
	}

	// [2] 편의용 생성자
	// ★수정됨: void를 제거하고, 이름을 클래스명(ScanDTO)과 일치시킴
	public ScanDTO(String date, String userId, String fileName, int malicious, int suspicious, int undetected, int harmless, int total) {
		super(); // 이제 에러가 나지 않습니다.
		this.scanDate = date;
		this.userId = userId;
		this.fileName = fileName;
		this.malicious = malicious;
		this.suspicious = suspicious;
		this.harmless = harmless;
		this.undetected = undetected;
		this.total = total;
		// 위협이 1개라도 있으면 "위험", 없으면 "안전"
		this.resultStatus = (malicious+suspicious >= harmless+undetected) ? "위험" : "안전";
	}

	// Getter & Setter
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getSuspicious() {
		return suspicious;
	}

	public void setSuspicious(int suspicious) {
		this.suspicious = suspicious;
	}

	public int getUndetected() {
		return undetected;
	}

	public void setUndetected(int undetected) {
		this.undetected = undetected;
	}
	public int getNum() { return num; }
	public void setNum(int num) { this.num = num; }

	public String getFileName() { return fileName; }
	public void setFileName(String fileName) { this.fileName = fileName; }

	public String getScanDate() { return scanDate; }
	public void setScanDate(String scanDate) { this.scanDate = scanDate; }

	public int getMalicious() { return malicious; }
	public void setMalicious(int malicious) { this.malicious = malicious; }

	public int getHarmless() { return harmless; }
	public void setHarmless(int harmless) { this.harmless = harmless; }

	public int getTotal() { return total; }
	public void setTotal(int total) { this.total = total; }

	public String getResultStatus() { return resultStatus; }
	public void setResultStatus(String resultStatus) { this.resultStatus = resultStatus; }
}