package domain.repository;

import java.util.List;

import domain.entity.Board;

public interface BoardRepository {
    int countAll();
    List<Board> findAll(int page, int pageSize);
    Board findByUuid(String uuid);
    int save(Board board);
    int updateByUuid(Board board);
    void deleteByUuid(String uuid);
    int increaseHit(Board board);
}
