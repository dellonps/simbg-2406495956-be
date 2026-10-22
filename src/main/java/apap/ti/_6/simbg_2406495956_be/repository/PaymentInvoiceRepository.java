package apap.ti._6.simbg_2406495956_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import apap.ti._6.simbg_2406495956_be.model.PaymentInvoice;

public interface PaymentInvoiceRepository extends JpaRepository<PaymentInvoice, String> {
    boolean existsByClaimId(String claimId);
}