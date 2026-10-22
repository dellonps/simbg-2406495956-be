package apap.ti._6.simbg_2406495956_be.dto;

public record ClaimItemRequest(
        String budgetComponentId,
        String description,
        Integer quantity,
        Integer unitPrice
) {}