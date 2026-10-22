package apap.ti._6.simbg_2406495956_be.dto;

import java.time.LocalDate;

public record AnggaranRequest(
        String sekolahId,
        LocalDate startPeriod,
        LocalDate endPeriod,
        Integer totalAnggaran,
        String status,
        String description
) {}