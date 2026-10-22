package apap.ti._6.simbg_2406495956_be.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apap.ti._6.simbg_2406495956_be.dto.BaseResponse;
import apap.ti._6.simbg_2406495956_be.dto.InvoiceRequest;
import apap.ti._6.simbg_2406495956_be.dto.InvoiceResponse;
import apap.ti._6.simbg_2406495956_be.dto.InvoiceStatistikResponse;
import apap.ti._6.simbg_2406495956_be.service.InvoiceService;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {

    private final InvoiceService service;

    public InvoiceController(InvoiceService service) {
        this.service = service;
    }

    @GetMapping
    public BaseResponse<List<InvoiceResponse>> getAll() {
        return BaseResponse.of(200, "Invoices retrieved successfully", service.getAll());
    }

    @GetMapping("/statistik")
    public BaseResponse<InvoiceStatistikResponse> getStatistik() {
        return BaseResponse.of(200, "Invoice statistics retrieved successfully", service.getStatistik());
    }

    @GetMapping("/{invoiceNumber}")
    public BaseResponse<InvoiceResponse> getById(@PathVariable String invoiceNumber) {
        return BaseResponse.of(200, "Invoice retrieved successfully", service.getById(invoiceNumber));
    }

    @PostMapping
    public ResponseEntity<BaseResponse<InvoiceResponse>> create(@RequestBody InvoiceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.of(201, "Invoice created successfully", service.create(request)));
    }
}