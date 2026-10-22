package apap.ti._6.simbg_2406495956_be.dto;

import java.time.LocalDate;

public record ClaimRequest(
        String anggaranId,   // wajib saat POST, diabaikan saat PUT
        LocalDate claimDate,
        String description
) {}