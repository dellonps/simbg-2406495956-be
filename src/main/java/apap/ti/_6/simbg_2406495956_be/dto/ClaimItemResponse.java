package apap.ti._6.simbg_2406495956_be.dto;

public record ClaimItemResponse(
        String id,
        String description,
        Integer quantity,
        Integer unitPrice,
        Integer amount,
        String claimId,
        String budgetComponentId,
        String componentName,
        String rateBasis,
        Integer maxRate
) {}