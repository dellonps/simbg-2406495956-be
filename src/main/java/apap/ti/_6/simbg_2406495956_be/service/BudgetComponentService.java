package apap.ti._6.simbg_2406495956_be.service;

import apap.ti._6.simbg_2406495956_be.dto.BudgetComponentRequest;
import apap.ti._6.simbg_2406495956_be.dto.BudgetComponentResponse;
import apap.ti._6.simbg_2406495956_be.exception.BadRequestException;
import apap.ti._6.simbg_2406495956_be.exception.ResourceNotFoundException;
import apap.ti._6.simbg_2406495956_be.model.Anggaran;
import apap.ti._6.simbg_2406495956_be.model.BudgetComponent;
import apap.ti._6.simbg_2406495956_be.repository.AnggaranRepository;
import apap.ti._6.simbg_2406495956_be.repository.BudgetComponentRepository;
import apap.ti._6.simbg_2406495956_be.repository.ClaimItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BudgetComponentService {

    private final BudgetComponentRepository componentRepository;
    private final AnggaranRepository anggaranRepository;
    private final ClaimItemRepository claimItemRepository;

    public BudgetComponentService(BudgetComponentRepository componentRepository,
                                  AnggaranRepository anggaranRepository,
                                  ClaimItemRepository claimItemRepository) {
        this.componentRepository = componentRepository;
        this.anggaranRepository = anggaranRepository;
        this.claimItemRepository = claimItemRepository;
    }

    @Transactional(readOnly = true)
    public List<BudgetComponentResponse> getAll(String anggaranId) {
        findAnggaran(anggaranId);
        return componentRepository.findByAnggaranId(anggaranId).stream()
                .map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public BudgetComponentResponse getById(String anggaranId, String id) {
        return toResponse(findComponent(anggaranId, id));
    }

    @Transactional
    public BudgetComponentResponse create(String anggaranId, BudgetComponentRequest req) {
        Anggaran anggaran = findAnggaran(anggaranId);
        validate(req);

        long totalAfter = componentRepository.sumAllocatedByAnggaranId(anggaranId) + req.allocatedAmount();
        if (totalAfter > anggaran.getTotalAnggaran()) {
            throw new BadRequestException("Total allocatedAmount melebihi totalAnggaran");
        }

        LocalDateTime now = LocalDateTime.now();
        BudgetComponent c = new BudgetComponent();
        c.setId(String.format("BGC%04d", componentRepository.count() + 1));
        c.setComponentName(req.componentName());
        c.setAllocatedAmount(req.allocatedAmount());
        c.setMaxRate(req.maxRate());
        c.setRateBasis(req.rateBasis());
        c.setDescription(req.description());
        c.setAnggaran(anggaran);
        c.setCreatedAt(now);
        c.setUpdatedAt(now);

        return toResponse(componentRepository.save(c));
    }

    @Transactional
    public BudgetComponentResponse update(String anggaranId, String id, BudgetComponentRequest req) {
        BudgetComponent c = findComponent(anggaranId, id);
        validate(req);

        long otherTotal = componentRepository.sumAllocatedByAnggaranId(anggaranId) - c.getAllocatedAmount();
        if (otherTotal + req.allocatedAmount() > c.getAnggaran().getTotalAnggaran()) {
            throw new BadRequestException("Total allocatedAmount melebihi totalAnggaran");
        }
        long usedByClaims = claimItemRepository.sumAmountByComponentId(c.getId());
        if (req.allocatedAmount() < usedByClaims) {
            throw new BadRequestException("allocatedAmount tidak boleh lebih kecil dari total ClaimItem yang sudah memakai komponen ini");
        }

        c.setComponentName(req.componentName());
        c.setAllocatedAmount(req.allocatedAmount());
        c.setMaxRate(req.maxRate());
        c.setRateBasis(req.rateBasis());
        c.setDescription(req.description());
        c.setUpdatedAt(LocalDateTime.now());

        return toResponse(componentRepository.save(c));
    }

    private Anggaran findAnggaran(String anggaranId) {
        return anggaranRepository.findById(anggaranId)
                .orElseThrow(() -> new ResourceNotFoundException("Anggaran tidak ditemukan: " + anggaranId));
    }

    private BudgetComponent findComponent(String anggaranId, String id) {
        findAnggaran(anggaranId);
        BudgetComponent c = componentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Budget component tidak ditemukan: " + id));
        if (!c.getAnggaran().getId().equals(anggaranId)) {
            throw new ResourceNotFoundException("Budget component tidak ditemukan: " + id);
        }
        return c;
    }

    private void validate(BudgetComponentRequest req) {
        if (req.componentName() == null || req.componentName().isBlank()) {
            throw new BadRequestException("componentName wajib diisi");
        }
        if (req.allocatedAmount() == null || req.allocatedAmount() <= 0) {
            throw new BadRequestException("allocatedAmount harus lebih dari 0");
        }
        if (req.maxRate() == null || req.maxRate() <= 0) {
            throw new BadRequestException("maxRate harus lebih dari 0");
        }
        if (!List.of("PER_PORSI", "PER_HARI", "TOTAL").contains(req.rateBasis())) {
            throw new BadRequestException("rateBasis harus PER_PORSI, PER_HARI, atau TOTAL");
        }
    }

    private BudgetComponentResponse toResponse(BudgetComponent c) {
        int used = claimItemRepository.sumAmountByComponentId(c.getId()).intValue();
        return new BudgetComponentResponse(
                c.getId(),
                c.getComponentName(),
                c.getAllocatedAmount(),
                c.getMaxRate(),
                c.getRateBasis(),
                c.getDescription(),
                c.getCreatedAt(),
                c.getUpdatedAt(),
                c.getAnggaran().getId(),
                used,
                c.getAllocatedAmount() - used
        );
    }
}
