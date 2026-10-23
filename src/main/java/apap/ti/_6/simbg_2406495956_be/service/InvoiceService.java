package apap.ti._6.simbg_2406495956_be.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import apap.ti._6.simbg_2406495956_be.dto.ClaimItemResponse;
import apap.ti._6.simbg_2406495956_be.dto.HomeResponse;
import apap.ti._6.simbg_2406495956_be.dto.InvoiceRequest;
import apap.ti._6.simbg_2406495956_be.dto.InvoiceResponse;
import apap.ti._6.simbg_2406495956_be.dto.InvoiceStatistikResponse;
import apap.ti._6.simbg_2406495956_be.exception.BadRequestException;
import apap.ti._6.simbg_2406495956_be.exception.ResourceNotFoundException;
import apap.ti._6.simbg_2406495956_be.model.Claim;
import apap.ti._6.simbg_2406495956_be.model.ClaimItem;
import apap.ti._6.simbg_2406495956_be.model.PaymentInvoice;
import apap.ti._6.simbg_2406495956_be.repository.AnggaranRepository;
import apap.ti._6.simbg_2406495956_be.repository.ClaimRepository;
import apap.ti._6.simbg_2406495956_be.repository.PaymentInvoiceRepository;
import apap.ti._6.simbg_2406495956_be.repository.SekolahRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private static final List<String> CLAIM_STATUSES =
            List.of("DIAJUKAN", "DISETUJUI", "DITOLAK", "DIBATALKAN");

    private final PaymentInvoiceRepository invoiceRepository;
    private final ClaimRepository claimRepository;
    private final AnggaranRepository anggaranRepository;
    private final SekolahRepository sekolahRepository;

    @Transactional(readOnly = true)
    public List<InvoiceResponse> getAll() {
        return invoiceRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public InvoiceResponse getById(String invoiceNumber) {
        return toResponse(invoiceRepository.findById(invoiceNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice tidak ditemukan: " + invoiceNumber)));
    }

    @Transactional
    public InvoiceResponse create(InvoiceRequest req) {
        if (req.claimId() == null || req.claimId().isBlank()) {
            throw new BadRequestException("claimId wajib diisi");
        }
        if (req.invoiceDate() == null || req.dueDate() == null) {
            throw new BadRequestException("invoiceDate dan dueDate wajib diisi");
        }
        if (!req.dueDate().isAfter(req.invoiceDate())) {
            throw new BadRequestException("dueDate harus lebih besar dari invoiceDate");
        }
        Claim claim = claimRepository.findById(req.claimId())
                .orElseThrow(() -> new ResourceNotFoundException("Claim tidak ditemukan: " + req.claimId()));
        if (!"DISETUJUI".equals(claim.getStatus())) {
            throw new BadRequestException("Invoice hanya dapat diterbitkan untuk claim berstatus DISETUJUI");
        }
        if (invoiceRepository.existsByClaimId(claim.getId())) {
            throw new BadRequestException("Claim ini sudah pernah diterbitkan invoice-nya");
        }

        int year = req.invoiceDate().getYear();
        long seq = invoiceRepository.count() + 1;
        LocalDateTime now = LocalDateTime.now();

        PaymentInvoice inv = new PaymentInvoice();
        inv.setInvoiceNumber(String.format("INV-MBG-%d-%04d", year, seq));
        inv.setInvoiceDate(req.invoiceDate());
        inv.setDueDate(req.dueDate());
        inv.setTotalAmount(claim.getTotalClaim());
        inv.setStatus("BELUM_DIBAYAR");
        inv.setClaim(claim);
        inv.setCreatedAt(now);
        inv.setUpdatedAt(now);
        return toResponse(invoiceRepository.save(inv));
    }

    @Transactional(readOnly = true)
    public InvoiceStatistikResponse getStatistik() {
        List<Claim> claims = claimRepository.findAll();
        List<InvoiceStatistikResponse.ClaimStat> claimStats = new ArrayList<>();
        for (String status : CLAIM_STATUSES) {
            List<Claim> filtered = claims.stream().filter(c -> status.equals(c.getStatus())).toList();
            claimStats.add(new InvoiceStatistikResponse.ClaimStat(
                    status,
                    filtered.size(),
                    filtered.stream().mapToLong(Claim::getTotalClaim).sum()));
        }

        Map<LocalDate, long[]> perMonth = new TreeMap<>();
        for (PaymentInvoice inv : invoiceRepository.findAll()) {
            LocalDate month = inv.getInvoiceDate().withDayOfMonth(1);
            long[] agg = perMonth.computeIfAbsent(month, k -> new long[2]);
            agg[0]++;
            agg[1] += inv.getTotalAmount();
        }
        List<InvoiceStatistikResponse.InvoiceStat> invoiceStats = perMonth.entrySet().stream()
                .map(e -> new InvoiceStatistikResponse.InvoiceStat(
                        e.getKey().toString(), e.getValue()[0], e.getValue()[1]))
                .toList();

        return new InvoiceStatistikResponse(claimStats, invoiceStats);
    }

    @Transactional(readOnly = true)
    public HomeResponse getHome() {
        long totalAnggaran = anggaranRepository.findAll().stream()
                .mapToLong(a -> a.getTotalAnggaran()).sum();
        List<PaymentInvoice> invoices = invoiceRepository.findAll();
        return new HomeResponse(
                sekolahRepository.count(),
                totalAnggaran,
                invoices.size(),
                invoices.stream().mapToLong(PaymentInvoice::getTotalAmount).sum());
    }

    private InvoiceResponse toResponse(PaymentInvoice inv) {
        Claim c = inv.getClaim();
        List<ClaimItemResponse> items = c.getClaimItems().stream().map(this::toItemResponse).toList();
        return new InvoiceResponse(
                inv.getInvoiceNumber(), inv.getInvoiceDate(), inv.getDueDate(),
                inv.getTotalAmount(), inv.getStatus(), inv.getCreatedAt(), inv.getUpdatedAt(),
                c.getId(), c.getClaimDate(), items);
    }

    private ClaimItemResponse toItemResponse(ClaimItem i) {
        return new ClaimItemResponse(
                i.getId(), i.getDescription(), i.getQuantity(), i.getUnitPrice(), i.getAmount(),
                i.getClaim().getId(),
                i.getBudgetComponent().getId(),
                i.getBudgetComponent().getComponentName(),
                i.getBudgetComponent().getRateBasis(),
                i.getBudgetComponent().getMaxRate());
    }
}