package usecase.board;

import domain.entity.Board;
import domain.repository.BoardRepository;
import infrastructure.persistence.BoardRepositoryImpl;
import usecase.UseCaseException;

public class UpdatePostUseCase {
    private final BoardRepository boardRepo;

    public UpdatePostUseCase() { this(BoardRepositoryImpl.getInstance()); }
    public UpdatePostUseCase(BoardRepository boardRepo) { this.boardRepo = boardRepo; }

    public void execute(String uuid, String title, String body, String memberId, boolean isAdmin) {
        Board existing = boardRepo.findByUuid(uuid);
        if (existing == null || (!memberId.equals(existing.getMemberId()) && !isAdmin)) {
            throw new UseCaseException("수정 권한이 없습니다.");
        }
        if (title == null || title.trim().isEmpty())
            throw new UseCaseException("제목을 입력해주세요.");
        if (title.trim().length() > 100)
            throw new UseCaseException("제목은 100자 이하로 입력해주세요.");
        if (body == null || body.trim().isEmpty())
            throw new UseCaseException("내용을 입력해주세요.");

        Board board = new Board();
        board.setUuid(uuid);
        board.setSubject(title);
        board.setContent(body);
        boardRepo.updateByUuid(board);
    }
}
