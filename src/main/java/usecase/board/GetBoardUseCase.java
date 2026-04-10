package usecase.board;

import domain.entity.Board;
import domain.repository.BoardRepository;
import infrastructure.persistence.BoardRepositoryImpl;

public class GetBoardUseCase {
    private final BoardRepository boardRepo;

    public GetBoardUseCase() { this(BoardRepositoryImpl.getInstance()); }
    public GetBoardUseCase(BoardRepository boardRepo) { this.boardRepo = boardRepo; }

    public Board execute(String uuid) {
        return boardRepo.findByUuid(uuid);
    }
}
