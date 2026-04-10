package usecase.board;

import java.util.List;

import domain.entity.Board;
import domain.repository.BoardRepository;
import infrastructure.persistence.BoardRepositoryImpl;

public class GetBoardListUseCase {
    private final BoardRepository boardRepo;

    public GetBoardListUseCase() { this(BoardRepositoryImpl.getInstance()); }
    public GetBoardListUseCase(BoardRepository boardRepo) { this.boardRepo = boardRepo; }

    public int countAll() {
        return boardRepo.countAll();
    }

    public List<Board> execute(int page, int pageSize) {
        return boardRepo.findAll(page, pageSize);
    }
}
