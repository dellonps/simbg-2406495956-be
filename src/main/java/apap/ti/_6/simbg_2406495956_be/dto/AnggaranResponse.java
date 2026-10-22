package apap.ti._6.simbg_2406495956_be.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record AnggaranResponse(
        String id,
        LocalDate startPeriod,
        LocalDate endPeriod,
        Integer totalAnggaran,
        String status,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String sekolahId,
        String namaSekolah,
        Integer totalAllocated,
        Integer remainingAmount
) {}