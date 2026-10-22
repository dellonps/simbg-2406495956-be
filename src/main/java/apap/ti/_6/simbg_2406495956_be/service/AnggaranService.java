package apap.ti._6.simbg_2406495956_be.service;

import apap.ti._6.simbg_2406495956_be.dto.AnggaranRequest;
import apap.ti._6.simbg_2406495956_be.dto.AnggaranResponse;
import apap.ti._6.simbg_2406495956_be.exception.BadRequestException;
import apap.ti._6.simbg_2406495956_be.exception.ResourceNotFoundException;
import apap.ti._6.simbg_2406495956_be.model.Anggaran;
import apap.ti._6.simbg_2406495956_be.model.Sekolah;
import apap.ti._6.simbg_2406495956_be.repository.AnggaranRepository;
import apap.ti._6.simbg_2406495956_be.repository.BudgetComponentRepository;
import apap.ti._6.simbg_2406495956_be.repository.SekolahRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AnggaranService {

    private final AnggaranRepository anggaranRepository;
    private final SekolahRepository sekolahRepository;
    private final BudgetComponentRepository budgetComponentRepository;

    public AnggaranService(AnggaranRepository anggaranRepository,
                           SekolahRepository sekolahRepository,
                           BudgetComponentRepository budgetComponentRepository) {
        this.anggaranRepository = anggaranRepository;
        this.sekolahRepository = sekolahRepository;
        this.budgetComponentRepository = budgetComponentRepository;
    }

    @Transactional(readOnly = true)
    public List<AnggaranResponse> getAll() {
        return anggaranRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public AnggaranResponse getById(String id) {
        Anggaran anggaran = anggaranRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Anggaran tidak ditemukan: " + id));
        return toResponse(anggaran);
    }

    @Transactional
    public AnggaranResponse create(AnggaranRequest req) {
        validate(req);

        Sekolah sekolah = sekolahRepository.findById(req.sekolahId())
                .orElseThrow(() -> new ResourceNotFoundException("Sekolah tidak ditemukan: " + req.sekolahId()));

        LocalDateTime now = LocalDateTime.now();
        Anggaran anggaran = new Anggaran();
        anggaran.setId(String.format("ANG%04d", anggaranRepository.count() + 1));
        anggaran.setStartPeriod(req.startPeriod());
        anggaran.setEndPeriod(req.endPeriod());
        anggaran.setTotalAnggaran(req.totalAnggaran());
        anggaran.setStatus(req.status());
        anggaran.setDescription(req.description());
        anggaran.setSekolah(sekolah);
        anggaran.setCreatedAt(now);
        anggaran.setUpdatedAt(now);

        return toResponse(anggaranRepository.save(anggaran));
    }

    private void validate(AnggaranRequest req) {
        if (req.sekolahId() == null || req.sekolahId().isBlank()) {
            throw new BadRequestException("sekolahId wajib diisi");
        }
        if (req.startPeriod() == null || req.endPeriod() == null) {
            throw new BadRequestException("startPeriod dan endPeriod wajib diisi");
        }
        if (req.endPeriod().isBefore(req.startPeriod())) {
            throw new BadRequestException("endPeriod tidak boleh lebih awal dari startPeriod");
        }
        if (req.totalAnggaran() == null || req.totalAnggaran() <= 0) {
            throw new BadRequestException("totalAnggaran harus lebih dari 0");
        }
        if (!"AKTIF".equals(req.status()) && !"NONAKTIF".equals(req.status())) {
            throw new BadRequestException("status harus AKTIF atau NONAKTIF");
        }
    }

    private AnggaranResponse toResponse(Anggaran a) {
        int totalAllocated = budgetComponentRepository.sumAllocatedByAnggaranId(a.getId()).intValue();
        return new AnggaranResponse(
                a.getId(),
                a.getStartPeriod(),
                a.getEndPeriod(),
                a.getTotalAnggaran(),
                a.getStatus(),
                a.getDescription(),
                a.getCreatedAt(),
                a.getUpdatedAt(),
                a.getSekolah().getId(),
                a.getSekolah().getNama(),
                totalAllocated,
                a.getTotalAnggaran() - totalAllocated
        );
    }
}