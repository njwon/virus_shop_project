package usecase.member;

import java.util.UUID;

import domain.repository.MemberRepository;
import infrastructure.persistence.MemberRepositoryImpl;

public class IssueRememberMeTokenUseCase {
    private final MemberRepository memberRepo;

    public IssueRememberMeTokenUseCase() { this(MemberRepositoryImpl.getInstance()); }
    public IssueRememberMeTokenUseCase(MemberRepository memberRepo) { this.memberRepo = memberRepo; }

    public String execute(String userId, String ip, String userAgent) {
        String token = UUID.randomUUID().toString();
        memberRepo.insertRememberMeToken(userId, token, ip, userAgent);
        return token;
    }
}
