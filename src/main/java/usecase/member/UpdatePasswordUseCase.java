package usecase.member;

import domain.entity.Member;
import domain.repository.MemberRepository;
import infrastructure.persistence.MemberRepositoryImpl;
import usecase.UseCaseException;
import util.PasswordManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdatePasswordUseCase {
    private static final Logger log = LoggerFactory.getLogger(UpdatePasswordUseCase.class);
    private final MemberRepository memberRepo;

    public UpdatePasswordUseCase() { this(MemberRepositoryImpl.getInstance()); }
    public UpdatePasswordUseCase(MemberRepository memberRepo) { this.memberRepo = memberRepo; }

    public void execute(String userId, String currentPw, String newPw) {
        if (currentPw == null || currentPw.isEmpty())
            throw new UseCaseException("현재 비밀번호를 입력해주세요.");
        if (newPw == null || newPw.length() < 8 || newPw.length() > 50)
            throw new UseCaseException("새 비밀번호는 8자 이상 50자 이하로 입력해주세요.");
        Member dbMember = memberRepo.findById(userId);
        if (dbMember == null || !PasswordManager.checkPassword(currentPw, dbMember.getPw())) {
            throw new UseCaseException("입력한 비밀번호가 계정의 비밀번호와 같지 않습니다.");
        }

        Member member = new Member();
        member.setId(userId);
        member.setPw(PasswordManager.hashPassword(newPw));
        memberRepo.updatePassword(member);
        log.info("비밀번호 변경 성공 - ID: {}", userId);
    }
}
