package usecase.member;

import domain.repository.MemberRepository;
import infrastructure.persistence.MemberRepositoryImpl;

public class RevokeAllRememberMeTokensUseCase {
    private final MemberRepository memberRepo;

    public RevokeAllRememberMeTokensUseCase() { this(MemberRepositoryImpl.getInstance()); }
    public RevokeAllRememberMeTokensUseCase(MemberRepository memberRepo) { this.memberRepo = memberRepo; }

    public void execute(String userId) {
        memberRepo.deleteAllRememberMeTokensByUserId(userId);
    }
}
