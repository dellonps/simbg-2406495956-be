package apap.ti._6.simbg_2406495956_be.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apap.ti._6.simbg_2406495956_be.dto.BaseResponse;
import apap.ti._6.simbg_2406495956_be.dto.BudgetComponentRequest;
import apap.ti._6.simbg_2406495956_be.dto.BudgetComponentResponse;
import apap.ti._6.simbg_2406495956_be.service.BudgetComponentService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/anggaran/{anggaranId}/komponen")
@RequiredArgsConstructor
public class BudgetComponentController {

    private final BudgetComponentService service;

    @GetMapping
    public BaseResponse<List<BudgetComponentResponse>> getAll(@PathVariable String anggaranId) {
        return BaseResponse.of(200, "Budget components retrieved successfully", service.getAll(anggaranId));
    }

    @GetMapping("/{id}")
    public BaseResponse<BudgetComponentResponse> getById(@PathVariable String anggaranId,
                                                         @PathVariable String id) {
        return BaseResponse.of(200, "Budget component retrieved successfully", service.getById(anggaranId, id));
    }

    @PostMapping
    public ResponseEntity<BaseResponse<BudgetComponentResponse>> create(@PathVariable String anggaranId,
                                                                        @RequestBody BudgetComponentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.of(201, "Budget component created successfully",
                        service.create(anggaranId, request)));
    }

    @PutMapping("/{id}")
    public BaseResponse<BudgetComponentResponse> update(@PathVariable String anggaranId,
                                                        @PathVariable String id,
                                                        @RequestBody BudgetComponentRequest request) {
        return BaseResponse.of(200, "Budget component updated successfully", service.update(anggaranId, id, request));
    }
}