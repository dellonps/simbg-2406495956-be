package apap.ti._6.simbg_2406495956_be.dto;

import java.time.LocalDate;

public record InvoiceRequest(
        String claimId,
        LocalDate invoiceDate,
        LocalDate dueDate
) {}