package apap.ti._6.simbg_2406495956_be.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "anggaran")
@Getter
@Setter
@NoArgsConstructor
public class Anggaran {
    @Id
    private String id; // ANGxxxx

    @Column(nullable = false)
    private LocalDate startPeriod;

    @Column(nullable = false)
    private LocalDate endPeriod;

    @Column(nullable = false)
    private Integer totalAnggaran;

    @Column(nullable = false)
    private String status; // AKTIF / NONAKTIF

    private String description; // nullable

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sekolah_id", nullable = false)
    private Sekolah sekolah;

}