package usecase.board;

import domain.entity.Board;
import domain.repository.BoardRepository;
import infrastructure.persistence.BoardRepositoryImpl;
import usecase.UseCaseException;

public class DeletePostUseCase {
    private final BoardRepository boardRepo;

    public DeletePostUseCase() { this(BoardRepositoryImpl.getInstance()); }
    public DeletePostUseCase(BoardRepository boardRepo) { this.boardRepo = boardRepo; }

    public void execute(String uuid, String memberId, boolean isAdmin) {
        Board existing = boardRepo.findByUuid(uuid);
        if (existing == null || (!memberId.equals(existing.getMemberId()) && !isAdmin)) {
            throw new UseCaseException("삭제 권한이 없습니다.");
        }
        boardRepo.deleteByUuid(uuid);
    }
}
