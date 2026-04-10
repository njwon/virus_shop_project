package usecase.board;

import domain.entity.Board;
import domain.repository.BoardRepository;
import infrastructure.persistence.BoardRepositoryImpl;

public class IncreaseHitUseCase {
    private final BoardRepository boardRepo;

    public IncreaseHitUseCase() { this(BoardRepositoryImpl.getInstance()); }
    public IncreaseHitUseCase(BoardRepository boardRepo) { this.boardRepo = boardRepo; }

    public void execute(Board board) {
        boardRepo.increaseHit(board);
    }
}
