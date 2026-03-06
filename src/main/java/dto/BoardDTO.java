package dto;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BoardDTO {
	    private int id; //게시글 순번
	    private String memberId; //회원 아이디
	    private String memberName; // 회원 이름
	    private String subject; // 게시글 제목
	    private String content; // 게시글 내용
	    private String date; // 게시글 등록 일자
	    private int hit; // 게시글 조회 수
	    private String ip; // 게시글 등록 시 ip
		
		public BoardDTO() {
			super();
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getMemberId() {
			return memberId;
		}

		public void setMemberId(String memberId) {
			this.memberId = memberId;
		}

		public String getMemberName() {
			return memberName;
		}

		public void setMemberName(String memberName) {
			this.memberName = memberName;
		}

		public String getSubject() {
			return subject;
		}

		public void setSubject(String subject) {
			this.subject = subject;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public int getHit() {
			return hit;
		}

		public void setHit(int hit) {
			this.hit = hit;
		}

		public String getIp() {
			return ip;
		}

		public void setIp(String ip) {
			this.ip = ip;
		}
}
