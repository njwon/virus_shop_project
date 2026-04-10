package usecase.member;

import domain.entity.Member;
import domain.repository.MemberRepository;
import infrastructure.persistence.MemberRepositoryImpl;
import usecase.UseCaseException;
import util.PasswordManager;

public class LoginUseCase {
    private final MemberRepository memberRepo;

    public LoginUseCase() { this(MemberRepositoryImpl.getInstance()); }
    public LoginUseCase(MemberRepository memberRepo) { this.memberRepo = memberRepo; }

    public Member execute(String id, String pw) {
        if (id == null || id.isEmpty() || pw == null || pw.isEmpty())
            throw new UseCaseException("아이디와 비밀번호를 입력해주세요.");
        Member member = memberRepo.findById(id);
        if (member == null) {
            throw new UseCaseException("존재하지 않는 계정입니다.");
        }
        if (!PasswordManager.checkPassword(pw, member.getPw())) {
            throw new UseCaseException("비밀번호가 일치하지 않습니다.");
        }
        return member;
    }
}
