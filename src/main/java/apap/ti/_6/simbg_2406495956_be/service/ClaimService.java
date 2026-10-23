package apap.ti._6.simbg_2406495956_be.service;

import apap.ti._6.simbg_2406495956_be.dto.ClaimItemResponse;
import apap.ti._6.simbg_2406495956_be.dto.ClaimRequest;
import apap.ti._6.simbg_2406495956_be.dto.ClaimResponse;
import apap.ti._6.simbg_2406495956_be.dto.ClaimStatusRequest;
import apap.ti._6.simbg_2406495956_be.exception.BadRequestException;
import apap.ti._6.simbg_2406495956_be.exception.ResourceNotFoundException;
import apap.ti._6.simbg_2406495956_be.model.Anggaran;
import apap.ti._6.simbg_2406495956_be.model.Claim;
import apap.ti._6.simbg_2406495956_be.model.ClaimItem;
import apap.ti._6.simbg_2406495956_be.repository.AnggaranRepository;
import apap.ti._6.simbg_2406495956_be.repository.ClaimRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ClaimService {

    private static final int MAX_RESUBMIT = 3;

    private static final Map<String, Set<String>> ALLOWED_TRANSITIONS = Map.of(
            "DRAFT", Set.of("DIAJUKAN", "DIBATALKAN"),
            "DIAJUKAN", Set.of("DIVERIFIKASI", "DIBATALKAN"),
            "DIVERIFIKASI", Set.of("DISETUJUI", "DITOLAK"),
            "DITOLAK", Set.of("DIAJUKAN") // pengajuan ulang
    );

    private final ClaimRepository claimRepository;
    private final AnggaranRepository anggaranRepository;

    @Transactional(readOnly = true)
    public List<ClaimResponse> getAll() {
        return claimRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ClaimResponse getById(String id) {
        return toResponse(findClaim(id));
    }

    @Transactional
    public ClaimResponse create(ClaimRequest req) {
        if (req.anggaranId() == null || req.anggaranId().isBlank()) {
            throw new BadRequestException("anggaranId wajib diisi");
        }
        if (req.claimDate() == null) {
            throw new BadRequestException("claimDate wajib diisi");
        }
        Anggaran anggaran = anggaranRepository.findById(req.anggaranId())
                .orElseThrow(() -> new ResourceNotFoundException("Anggaran tidak ditemukan: " + req.anggaranId()));

        LocalDateTime now = LocalDateTime.now();
        Claim c = new Claim();
        c.setId(String.format("CLM%04d", claimRepository.count() + 1));
        c.setClaimDate(req.claimDate());
        c.setTotalClaim(0);
        c.setStatus("DRAFT");
        c.setDescription(req.description());
        c.setResubmitCount(0);
        c.setAnggaran(anggaran);
        c.setCreatedAt(now);
        c.setUpdatedAt(now);
        return toResponse(claimRepository.save(c));
    }

    @Transactional
    public ClaimResponse update(String id, ClaimRequest req) {
        Claim c = findClaim(id);
        if (!"DRAFT".equals(c.getStatus())) {
            throw new BadRequestException("Claim hanya dapat diubah saat berstatus DRAFT");
        }
        if (req.claimDate() == null) {
            throw new BadRequestException("claimDate wajib diisi");
        }
        c.setClaimDate(req.claimDate());
        c.setDescription(req.description());
        c.setUpdatedAt(LocalDateTime.now());
        return toResponse(claimRepository.save(c));
    }

    @Transactional
    public ClaimResponse updateStatus(String id, ClaimStatusRequest req) {
        Claim c = findClaim(id);
        String from = c.getStatus();
        String to = req.status();

        if (to == null || !ALLOWED_TRANSITIONS.getOrDefault(from, Set.of()).contains(to)) {
            throw new BadRequestException("Perpindahan status dari " + from + " ke " + to + " tidak diperbolehkan");
        }

        LocalDateTime now = LocalDateTime.now();
        switch (to) {
            case "DIAJUKAN" -> {
                if (c.getClaimItems().isEmpty()) {
                    throw new BadRequestException("Claim harus memiliki minimal satu ClaimItem");
                }
                if ("DITOLAK".equals(from)) {
                    if (c.getResubmitCount() >= MAX_RESUBMIT) {
                        throw new BadRequestException("Claim sudah mencapai batas pengajuan ulang (" + MAX_RESUBMIT + " kali)");
                    }
                    c.setResubmitCount(c.getResubmitCount() + 1);
                    c.setRejectedReason(null);
                }
                c.setSubmittedAt(now);
            }
            case "DISETUJUI" -> {
                if (req.approvedBy() == null || req.approvedBy().isBlank()) {
                    throw new BadRequestException("approvedBy wajib diisi");
                }
                c.setApprovedAt(now);
                c.setApprovedBy(req.approvedBy());
            }
            case "DITOLAK" -> {
                if (req.rejectedReason() == null || req.rejectedReason().isBlank()) {
                    throw new BadRequestException("rejectedReason wajib diisi");
                }
                c.setRejectedReason(req.rejectedReason());
            }
            default -> { }
        }

        c.setStatus(to);
        c.setUpdatedAt(now);
        return toResponse(claimRepository.save(c));
    }

    private Claim findClaim(String id) {
        return claimRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Claim tidak ditemukan: " + id));
    }

    private ClaimResponse toResponse(Claim c) {
        List<ClaimItemResponse> items = c.getClaimItems().stream().map(this::toItemResponse).toList();
        return new ClaimResponse(
                c.getId(),
                c.getClaimDate(),
                c.getTotalClaim(),
                c.getStatus(),
                c.getSubmittedAt(),
                c.getApprovedAt(),
                c.getApprovedBy(),
                c.getRejectedReason(),
                c.getDescription(),
                c.getCreatedAt(),
                c.getUpdatedAt(),
                c.getAnggaran().getId(),
                c.getResubmitCount(),
                items
        );
    }

    private ClaimItemResponse toItemResponse(ClaimItem i) {
        return new ClaimItemResponse(
                i.getId(),
                i.getDescription(),
                i.getQuantity(),
                i.getUnitPrice(),
                i.getAmount(),
                i.getClaim().getId(),
                i.getBudgetComponent().getId(),
                i.getBudgetComponent().getComponentName(),
                i.getBudgetComponent().getRateBasis(),
                i.getBudgetComponent().getMaxRate()
        );
    }
}