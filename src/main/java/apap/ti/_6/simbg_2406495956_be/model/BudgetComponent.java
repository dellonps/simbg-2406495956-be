package apap.ti._6.simbg_2406495956_be.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "budget_component")
@Getter
@Setter
@NoArgsConstructor
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

}