package domain.entity;

import java.io.Serializable;

public class Member implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String uuid;
    private String pw;
    private String name;
    private String mail;
    private String phone;
    private String lastIp;
    private String lastUserAgent;
    private String role;

    public Member() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUuid() { return uuid; }
    public void setUuid(String uuid) { this.uuid = uuid; }

    public String getPw() { return pw; }
    public void setPw(String pw) { this.pw = pw; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getLastIp() { return lastIp; }
    public void setLastIp(String lastIp) { this.lastIp = lastIp; }

    public String getLastUserAgent() { return lastUserAgent; }
    public void setLastUserAgent(String lastUserAgent) { this.lastUserAgent = lastUserAgent; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
