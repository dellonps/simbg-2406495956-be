package apap.ti._6.simbg_2406495956_be.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "claim")
@Getter
@Setter
@NoArgsConstructor
public class Claim {
    @Id
    private String id; // CLMxxxx

    @Column(nullable = false)
    private LocalDate claimDate;

    @Column(nullable = false)
    private Integer totalClaim = 0;

    // DRAFT, DIAJUKAN, DIVERIFIKASI, DISETUJUI, DITOLAK, DIBATALKAN
    @Column(nullable = false)
    private String status;

    private LocalDateTime submittedAt;
    private LocalDateTime approvedAt;
    private String approvedBy;
    private String rejectedReason;
    private String description;

    // berapa kali sudah diajukan ulang setelah ditolak (maks 3)
    @Column(nullable = false)
    private Integer resubmitCount = 0;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "anggaran_id", nullable = false)
    private Anggaran anggaran;

    @OneToMany(mappedBy = "claim", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClaimItem> claimItems = new ArrayList<>();

}