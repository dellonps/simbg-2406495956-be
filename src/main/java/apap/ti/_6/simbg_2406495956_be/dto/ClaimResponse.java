package apap.ti._6.simbg_2406495956_be.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;

public record ClaimResponse(
        String id,
        LocalDate claimDate,
        Integer totalClaim,
        String status,
        LocalDateTime submittedAt,
        LocalDateTime approvedAt,
        String approvedBy,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String rejectedReason,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String anggaranId,
        Integer resubmitCount,
        List<ClaimItemResponse> claimItems
) {}