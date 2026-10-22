package apap.ti._6.simbg_2406495956_be.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "anggaran")
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

    public Anggaran() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public LocalDate getStartPeriod() { return startPeriod; }
    public void setStartPeriod(LocalDate startPeriod) { this.startPeriod = startPeriod; }

    public LocalDate getEndPeriod() { return endPeriod; }
    public void setEndPeriod(LocalDate endPeriod) { this.endPeriod = endPeriod; }

    public Integer getTotalAnggaran() { return totalAnggaran; }
    public void setTotalAnggaran(Integer totalAnggaran) { this.totalAnggaran = totalAnggaran; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Sekolah getSekolah() { return sekolah; }
    public void setSekolah(Sekolah sekolah) { this.sekolah = sekolah; }
}