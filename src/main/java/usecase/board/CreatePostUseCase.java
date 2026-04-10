package usecase.board;

import domain.entity.Board;
import domain.repository.BoardRepository;
import infrastructure.persistence.BoardRepositoryImpl;
import usecase.UseCaseException;

public class CreatePostUseCase {
    private final BoardRepository boardRepo;

    public CreatePostUseCase() { this(BoardRepositoryImpl.getInstance()); }
    public CreatePostUseCase(BoardRepository boardRepo) { this.boardRepo = boardRepo; }

    public void execute(Board board) {
        String err = validate(board);
        if (err != null) throw new UseCaseException(err);
        boardRepo.save(board);
    }

    private String validate(Board board) {
        String title = board.getSubject();
        if (title == null || title.trim().isEmpty())
            return "제목을 입력해주세요.";
        if (title.trim().length() > 100)
            return "제목은 100자 이하로 입력해주세요.";
        String content = board.getContent();
        if (content == null || content.trim().isEmpty())
            return "내용을 입력해주세요.";
        return null;
    }
}
