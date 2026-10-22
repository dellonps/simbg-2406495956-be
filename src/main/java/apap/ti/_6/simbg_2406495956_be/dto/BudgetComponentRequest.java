package apap.ti._6.simbg_2406495956_be.dto;

public record BudgetComponentRequest(
        String componentName,
        Integer allocatedAmount,
        Integer maxRate,
        String rateBasis,
        String description
) {}