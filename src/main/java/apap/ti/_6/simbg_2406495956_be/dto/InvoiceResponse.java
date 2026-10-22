package apap.ti._6.simbg_2406495956_be.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record InvoiceResponse(
        String invoiceNumber,
        LocalDate invoiceDate,
        LocalDate dueDate,
        Integer totalAmount,
        String status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String claimId,
        LocalDate claimDate,
        List<ClaimItemResponse> claimItems
) {}