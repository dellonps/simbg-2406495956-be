package apap.ti._6.simbg_2406495956_be.dto;

import java.time.LocalDateTime;

public record BudgetComponentResponse(
        String id,
        String componentName,
        Integer allocatedAmount,
        Integer maxRate,
        String rateBasis,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String anggaranId,
        Integer usedAmount,
        Integer remainingAmount
) {}