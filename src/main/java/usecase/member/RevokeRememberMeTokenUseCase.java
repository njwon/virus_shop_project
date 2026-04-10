package usecase.member;

import domain.repository.MemberRepository;
import infrastructure.persistence.MemberRepositoryImpl;

public class RevokeRememberMeTokenUseCase {
    private final MemberRepository memberRepo;

    public RevokeRememberMeTokenUseCase() { this(MemberRepositoryImpl.getInstance()); }
    public RevokeRememberMeTokenUseCase(MemberRepository memberRepo) { this.memberRepo = memberRepo; }

    public void execute(String token) {
        memberRepo.deleteRememberMeToken(token);
    }
}
