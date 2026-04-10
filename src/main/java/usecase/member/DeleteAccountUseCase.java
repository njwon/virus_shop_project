package usecase.member;

import domain.entity.Member;
import domain.repository.MemberRepository;
import infrastructure.persistence.MemberRepositoryImpl;
import usecase.UseCaseException;
import util.PasswordManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeleteAccountUseCase {
    private static final Logger log = LoggerFactory.getLogger(DeleteAccountUseCase.class);
    private final MemberRepository memberRepo;

    public DeleteAccountUseCase() { this(MemberRepositoryImpl.getInstance()); }
    public DeleteAccountUseCase(MemberRepository memberRepo) { this.memberRepo = memberRepo; }

    public void execute(String userId, String confirmPw) {
        if (confirmPw == null || confirmPw.trim().isEmpty())
            throw new UseCaseException("비밀번호를 입력해주세요.");
        Member dbMember = memberRepo.findById(userId);
        if (dbMember == null || !PasswordManager.checkPassword(confirmPw, dbMember.getPw())) {
            log.warn("계정 삭제 거부 - 비밀번호 불일치 - ID: {}", userId);
            throw new UseCaseException("wrongpassword");
        }

        log.info("계정 삭제 시작 - ID: {}", userId);
        memberRepo.deleteAllRememberMeTokensByUserId(userId);
        memberRepo.delete(userId);
        log.info("계정 삭제 완료 - ID: {}", userId);
    }
}
