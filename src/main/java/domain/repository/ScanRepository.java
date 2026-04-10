package domain.repository;

import java.util.List;

import domain.entity.Scan;

public interface ScanRepository {
    void save(Scan scan);
    String findOwnerById(String id);
    void delete(String id);
    List<Scan> findHistoryByUserId(String userId);
}
