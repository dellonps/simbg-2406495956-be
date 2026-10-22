package apap.ti._6.simbg_2406495956_be.dto;

import java.util.List;

public record InvoiceStatistikResponse(
        List<ClaimStat> claimStatistics,
        List<InvoiceStat> invoiceStatistics
) {
    public record ClaimStat(String status, long jumlah, long totalNominal) {}

    // field "status" berisi tanggal awal bulan (mis. "2024-06-01"), sesuai contoh soal
    public record InvoiceStat(String status, long jumlah, long totalNominal) {}
}