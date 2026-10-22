package apap.ti._6.simbg_2406495956_be.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "claim")
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

    public Claim() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public LocalDate getClaimDate() { return claimDate; }
    public void setClaimDate(LocalDate claimDate) { this.claimDate = claimDate; }

    public Integer getTotalClaim() { return totalClaim; }
    public void setTotalClaim(Integer totalClaim) { this.totalClaim = totalClaim; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }

    public LocalDateTime getApprovedAt() { return approvedAt; }
    public void setApprovedAt(LocalDateTime approvedAt) { this.approvedAt = approvedAt; }

    public String getApprovedBy() { return approvedBy; }
    public void setApprovedBy(String approvedBy) { this.approvedBy = approvedBy; }

    public String getRejectedReason() { return rejectedReason; }
    public void setRejectedReason(String rejectedReason) { this.rejectedReason = rejectedReason; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getResubmitCount() { return resubmitCount; }
    public void setResubmitCount(Integer resubmitCount) { this.resubmitCount = resubmitCount; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Anggaran getAnggaran() { return anggaran; }
    public void setAnggaran(Anggaran anggaran) { this.anggaran = anggaran; }

    public List<ClaimItem> getClaimItems() { return claimItems; }
    public void setClaimItems(List<ClaimItem> claimItems) { this.claimItems = claimItems; }
}