package apap.ti._6.simbg_2406495956_be.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "payment_invoice")
@Getter
@Setter
@NoArgsConstructor
public class PaymentInvoice {
    @Id
    private String invoiceNumber; // INV-MBG-TAHUN-NNNN

    @Column(nullable = false)
    private LocalDate invoiceDate;

    @Column(nullable = false)
    private LocalDate dueDate;

    @Column(nullable = false)
    private Integer totalAmount;

    // BELUM_DIBAYAR, DIBAYAR_SEBAGIAN, LUNAS, DIBATALKAN
    @Column(nullable = false)
    private String status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "claim_id", nullable = false, unique = true)
    private Claim claim;

}