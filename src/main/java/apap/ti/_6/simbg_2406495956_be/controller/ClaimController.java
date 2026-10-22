package apap.ti._6.simbg_2406495956_be.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apap.ti._6.simbg_2406495956_be.dto.BaseResponse;
import apap.ti._6.simbg_2406495956_be.dto.ClaimRequest;
import apap.ti._6.simbg_2406495956_be.dto.ClaimResponse;
import apap.ti._6.simbg_2406495956_be.dto.ClaimStatusRequest;
import apap.ti._6.simbg_2406495956_be.service.ClaimService;

@RestController
@RequestMapping("/claim")
public class ClaimController {

    private final ClaimService claimService;

    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }

    @GetMapping
    public BaseResponse<List<ClaimResponse>> getAll() {
        return BaseResponse.of(200, "Claims retrieved successfully", claimService.getAll());
    }

    @GetMapping("/{id}")
    public BaseResponse<ClaimResponse> getById(@PathVariable String id) {
        return BaseResponse.of(200, "Claim retrieved successfully", claimService.getById(id));
    }

    @PostMapping
    public ResponseEntity<BaseResponse<ClaimResponse>> create(@RequestBody ClaimRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.of(201, "Claim created successfully", claimService.create(request)));
    }

    @PutMapping("/{id}")
    public BaseResponse<ClaimResponse> update(@PathVariable String id, @RequestBody ClaimRequest request) {
        return BaseResponse.of(200, "Claim updated successfully", claimService.update(id, request));
    }

    @PatchMapping("/{id}/status")
    public BaseResponse<ClaimResponse> updateStatus(@PathVariable String id,
                                                    @RequestBody ClaimStatusRequest request) {
        return BaseResponse.of(200, "Claim status updated successfully", claimService.updateStatus(id, request));
    }
}