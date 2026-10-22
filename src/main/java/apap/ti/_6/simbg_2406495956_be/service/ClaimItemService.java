package apap.ti._6.simbg_2406495956_be.service;

import apap.ti._6.simbg_2406495956_be.dto.ClaimItemRequest;
import apap.ti._6.simbg_2406495956_be.dto.ClaimItemResponse;
import apap.ti._6.simbg_2406495956_be.exception.BadRequestException;
import apap.ti._6.simbg_2406495956_be.exception.ResourceNotFoundException;
import apap.ti._6.simbg_2406495956_be.model.Claim;
import apap.ti._6.simbg_2406495956_be.model.ClaimItem;
import apap.ti._6.simbg_2406495956_be.model.BudgetComponent;
import apap.ti._6.simbg_2406495956_be.repository.BudgetComponentRepository;
import apap.ti._6.simbg_2406495956_be.repository.ClaimItemRepository;
import apap.ti._6.simbg_2406495956_be.repository.ClaimRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClaimItemService {

    private final ClaimItemRepository itemRepository;
    private final ClaimRepository claimRepository;
    private final BudgetComponentRepository componentRepository;

    public ClaimItemService(ClaimItemRepository itemRepository,
                            ClaimRepository claimRepository,
                            BudgetComponentRepository componentRepository) {
        this.itemRepository = itemRepository;
        this.claimRepository = claimRepository;
        this.componentRepository = componentRepository;
    }

    @Transactional(readOnly = true)
    public List<ClaimItemResponse> getAll(String claimId) {
        findClaim(claimId);
        return itemRepository.findByClaimId(claimId).stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ClaimItemResponse getById(String claimId, String id) {
        return toResponse(findItem(claimId, id));
    }

    @Transactional
    public ClaimItemResponse create(String claimId, ClaimItemRequest req) {
        Claim claim = findClaim(claimId);
        requireDraft(claim);
        validateBasic(req);
        BudgetComponent component = findComponent(req.budgetComponentId(), claim);
        validateAgainstComponent(component, req, 0);

        ClaimItem item = new ClaimItem();
        item.setId(String.format("CLI%04d", itemRepository.count() + 1));
        item.setClaim(claim);
        apply(item, component, req);
        itemRepository.save(item);

        recalculateTotal(claim);
        return toResponse(item);
    }

    @Transactional
    public ClaimItemResponse update(String claimId, String id, ClaimItemRequest req) {
        Claim claim = findClaim(claimId);
        requireDraft(claim);
        ClaimItem item = findItem(claimId, id);
        validateBasic(req);
        BudgetComponent component = findComponent(req.budgetComponentId(), claim);
        // nilai lama item ini tidak ikut dihitung bila komponen sama
        int oldAmount = item.getBudgetComponent().getId().equals(component.getId()) ? item.getAmount() : 0;
        validateAgainstComponent(component, req, oldAmount);

        apply(item, component, req);
        itemRepository.save(item);

        recalculateTotal(claim);
        return toResponse(item);
    }

    private void apply(ClaimItem item, BudgetComponent component, ClaimItemRequest req) {
        item.setBudgetComponent(component);
        item.setDescription(req.description());
        item.setQuantity(req.quantity());
        item.setUnitPrice(req.unitPrice());
        item.setAmount(req.quantity() * req.unitPrice());
    }

    private void validateBasic(ClaimItemRequest req) {
        if (req.budgetComponentId() == null || req.budgetComponentId().isBlank()) {
            throw new BadRequestException("budgetComponentId wajib diisi");
        }
        if (req.description() == null || req.description().isBlank()) {
            throw new BadRequestException("description wajib diisi");
        }
        if (req.quantity() == null || req.quantity() <= 0) {
            throw new BadRequestException("quantity harus lebih dari 0");
        }
        if (req.unitPrice() == null || req.unitPrice() <= 0) {
            throw new BadRequestException("unitPrice harus lebih dari 0");
        }
    }

    private void validateAgainstComponent(BudgetComponent component, ClaimItemRequest req, int excludedAmount) {
        if (req.unitPrice() > component.getMaxRate()) {
            throw new BadRequestException("unitPrice melebihi maxRate komponen (" + component.getMaxRate() + ")");
        }
        long newAmount = (long) req.quantity() * req.unitPrice();
        long used = itemRepository.sumAmountByComponentId(component.getId()) - excludedAmount;
        if (used + newAmount > component.getAllocatedAmount()) {
            throw new BadRequestException("Total claim melebihi allocatedAmount komponen " + component.getComponentName());
        }
    }

    private void recalculateTotal(Claim claim) {
        itemRepository.flush();
        int total = itemRepository.findByClaimId(claim.getId()).stream()
                .mapToInt(ClaimItem::getAmount).sum();
        claim.setTotalClaim(total);
        claim.setUpdatedAt(LocalDateTime.now());
        claimRepository.save(claim);
    }

    private void requireDraft(Claim claim) {
        if (!"DRAFT".equals(claim.getStatus())) {
            throw new BadRequestException("ClaimItem hanya dapat diubah saat claim berstatus DRAFT");
        }
    }

    private Claim findClaim(String claimId) {
        return claimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim tidak ditemukan: " + claimId));
    }

    private ClaimItem findItem(String claimId, String id) {
        findClaim(claimId);
        ClaimItem item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Claim item tidak ditemukan: " + id));
        if (!item.getClaim().getId().equals(claimId)) {
            throw new ResourceNotFoundException("Claim item tidak ditemukan: " + id);
        }
        return item;
    }

    private BudgetComponent findComponent(String componentId, Claim claim) {
        BudgetComponent c = componentRepository.findById(componentId)
                .orElseThrow(() -> new ResourceNotFoundException("Budget component tidak ditemukan: " + componentId));
        if (!c.getAnggaran().getId().equals(claim.getAnggaran().getId())) {
            throw new BadRequestException("Budget component bukan bagian dari anggaran claim ini");
        }
        return c;
    }

    private ClaimItemResponse toResponse(ClaimItem i) {
        return new ClaimItemResponse(
                i.getId(), i.getDescription(), i.getQuantity(), i.getUnitPrice(), i.getAmount(),
                i.getClaim().getId(),
                i.getBudgetComponent().getId(),
                i.getBudgetComponent().getComponentName(),
                i.getBudgetComponent().getRateBasis(),
                i.getBudgetComponent().getMaxRate());
    }
}