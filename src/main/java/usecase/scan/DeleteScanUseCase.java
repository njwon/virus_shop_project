package usecase.scan;

import domain.repository.ScanRepository;
import infrastructure.persistence.ScanRepositoryImpl;
import usecase.UseCaseException;

public class DeleteScanUseCase {
    private final ScanRepository scanRepo;

    public DeleteScanUseCase() { this(ScanRepositoryImpl.getInstance()); }
    public DeleteScanUseCase(ScanRepository scanRepo) { this.scanRepo = scanRepo; }

    public void execute(String scanId, String memberId, boolean isAdmin) {
        String owner = scanRepo.findOwnerById(scanId);
        if (owner == null || (!owner.equals(memberId) && !isAdmin)) {
            throw new UseCaseException("삭제 권한이 없습니다.");
        }
        scanRepo.delete(scanId);
    }
}
