package apap.ti._6.simbg_2406495956_be.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "budget_component")
public class BudgetComponent {
    @Id
    private String id; // BGCxxxx

    @Column(nullable = false)
    private String componentName;

    @Column(nullable = false)
    private Integer allocatedAmount;

    @Column(nullable = false)
    private Integer maxRate;

    @Column(nullable = false)
    private String rateBasis; // PER_PORSI, PER_HARI, TOTAL

    private String description;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "anggaran_id", nullable = false)
    private Anggaran anggaran;

    public BudgetComponent() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getComponentName() { return componentName; }
    public void setComponentName(String componentName) { this.componentName = componentName; }

    public Integer getAllocatedAmount() { return allocatedAmount; }
    public void setAllocatedAmount(Integer allocatedAmount) { this.allocatedAmount = allocatedAmount; }

    public Integer getMaxRate() { return maxRate; }
    public void setMaxRate(Integer maxRate) { this.maxRate = maxRate; }

    public String getRateBasis() { return rateBasis; }
    public void setRateBasis(String rateBasis) { this.rateBasis = rateBasis; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Anggaran getAnggaran() { return anggaran; }
    public void setAnggaran(Anggaran anggaran) { this.anggaran = anggaran; }
}