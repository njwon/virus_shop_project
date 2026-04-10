package usecase.member;

import domain.entity.Member;
import domain.repository.MemberRepository;
import infrastructure.persistence.MemberRepositoryImpl;
import usecase.UseCaseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateMemberDetailUseCase {
    private static final Logger log = LoggerFactory.getLogger(UpdateMemberDetailUseCase.class);
    private final MemberRepository memberRepo;

    public UpdateMemberDetailUseCase() { this(MemberRepositoryImpl.getInstance()); }
    public UpdateMemberDetailUseCase(MemberRepository memberRepo) { this.memberRepo = memberRepo; }

    public Member execute(String id, String name, String mail, String phone) {
        String validationError = validate(name, mail, phone);
        if (validationError != null) throw new UseCaseException(validationError);

        boolean[] duplicates = memberRepo.checkDuplicateOnUpdate(id, mail, phone);
        StringBuilder sb = new StringBuilder();
        if (duplicates[0]) sb.append("이메일");
        if (duplicates[1]) { if (sb.length() > 0) sb.append(", 그리고 "); sb.append("전화번호"); }
        if (sb.length() > 0) throw new UseCaseException(sb + "은(는) 이미 사용 중입니다.");

        Member existing = memberRepo.findById(id);
        Member member = new Member();
        member.setId(id);
        member.setName(name);
        member.setMail(mail);
        member.setPhone(phone.replaceAll("-", ""));
        member.setRole(existing.getRole());

        log.info("회원 정보 수정 - ID: {}", id);
        memberRepo.updateDetail(member);
        return member;
    }

    private String validate(String name, String mail, String phone) {
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
