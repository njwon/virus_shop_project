package usecase.scan;

import java.io.File;
import java.time.LocalDate;

import org.json.JSONObject;

import domain.entity.Scan;
import domain.repository.ScanRepository;
import infrastructure.persistence.ScanRepositoryImpl;
import util.VirusTotalService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScanFileUseCase {
    private static final Logger log = LoggerFactory.getLogger(ScanFileUseCase.class);
    private final ScanRepository scanRepo;

    public ScanFileUseCase() { this(ScanRepositoryImpl.getInstance()); }
    public ScanFileUseCase(ScanRepository scanRepo) { this.scanRepo = scanRepo; }

    public Scan execute(File file, String originalName, String memberId) throws Exception {
        VirusTotalService vt = new VirusTotalService();
        String analysisId = vt.uploadFile(file);
        JSONObject stats = vt.getReport(analysisId);

        int malicious  = stats.getInt("malicious");
        int suspicious = stats.getInt("suspicious");
        int harmless   = stats.getInt("harmless");
        int undetected = stats.getInt("undetected");
        int total      = malicious + suspicious + harmless + undetected;
        log.info("VT 결과 - malicious={}, suspicious={}, harmless={}, undetected={}, total={}", malicious, suspicious, harmless, undetected, total);

        Scan scan = new Scan(LocalDate.now().toString(), memberId, originalName, malicious, suspicious, undetected, harmless, total);
        scanRepo.save(scan);
        return scan;
    }
}
