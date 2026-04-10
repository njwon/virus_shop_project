package usecase.member;

import domain.entity.Member;
import domain.repository.MemberRepository;
import infrastructure.persistence.MemberRepositoryImpl;
import usecase.UseCaseException;

public class SignUpUseCase {
    private final MemberRepository memberRepo;

    public SignUpUseCase() { this(MemberRepositoryImpl.getInstance()); }
    public SignUpUseCase(MemberRepository memberRepo) { this.memberRepo = memberRepo; }

    public void execute(String id, String pw, String name, String mail, String phone) {
        String validationError = validate(id, pw, name, mail, phone);
        if (validationError != null) {
            throw new UseCaseException(validationError);
        }

        boolean[] duplicates = memberRepo.checkDuplicateOnSignUp(id, mail, phone);
        StringBuilder sb = new StringBuilder();
        if (duplicates[0]) sb.append("아이디");
        if (duplicates[1]) { if (sb.length() > 0) sb.append("와 "); sb.append("이메일"); }
        if (duplicates[2]) { if (sb.length() > 0) sb.append(", 그리고 "); sb.append("전화번호"); }
        if (sb.length() > 0) throw new UseCaseException(sb + "은(는) 이미 사용 중입니다.");

        Member member = new Member();
        member.setId(id);
        member.setPw(pw);
        member.setName(name);
        member.setMail(mail);
        member.setPhone(phone.replaceAll("-", ""));
        memberRepo.save(member);
    }

    private String validate(String id, String pw, String name, String mail, String phone) {
        if (id == null || !id.matches("[a-zA-Z0-9]{4,20}"))
            return "아이디는 영문/숫자 4~20자로 입력해주세요.";
        if (pw == null || pw.length() < 8 || pw.length() > 50)
            return "비밀번호는 8자 이상 50자 이하로 입력해주세요.";
        if (name == null || name.trim().length() < 2 || name.trim().length() > 20)
            return "이름은 2~20자로 입력해주세요.";
        if (mail == null || !mail.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$"))
            return "올바른 이메일 형식으로 입력해주세요.";
        if (mail.length() > 50)
            return "이메일은 50자 이하로 입력해주세요.";
        if (phone == null || !phone.replaceAll("-", "").matches("^[0-9]{10,11}$"))
            return "올바른 전화번호 형식으로 입력해주세요. (예: 010-1234-5678)";
        return null;
    }
}
