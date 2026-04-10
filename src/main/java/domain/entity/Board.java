package domain.entity;

public class Board {
    private int id;
    private String uuid;
    private String memberId;
    private String memberName;
    private String subject;
    private String content;
    private String date;
    private int hit;
    private String ip;

    public Board() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUuid() { return uuid; }
    public void setUuid(String uuid) { this.uuid = uuid; }

    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }

    public String getMemberName() { return memberName; }
    public void setMemberName(String memberName) { this.memberName = memberName; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public int getHit() { return hit; }
    public void setHit(int hit) { this.hit = hit; }

    public String getIp() { return ip; }
    public void setIp(String ip) { this.ip = ip; }
}
