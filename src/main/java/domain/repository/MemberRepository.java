package domain.repository;

import domain.entity.Member;

public interface MemberRepository {
    Member findById(String id);
    boolean[] checkDuplicateOnSignUp(String id, String mail, String phone);
    boolean[] checkDuplicateOnUpdate(String id, String mail, String phone);
    void save(Member member);
    int updateDetail(Member member);
    int updatePassword(Member member);
    void insertRememberMeToken(String userId, String token, String ip, String userAgent);
    Member findByRememberMeToken(String token);
    void deleteRememberMeToken(String token);
    void deleteAllRememberMeTokensByUserId(String userId);
    void delete(String id);
}
