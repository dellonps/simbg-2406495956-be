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
import apap.ti._6.simbg_2406495956_be.dto.ClaimItemRequest;
import apap.ti._6.simbg_2406495956_be.dto.ClaimItemResponse;
import apap.ti._6.simbg_2406495956_be.service.ClaimItemService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/claim/{claimId}/item")
@RequiredArgsConstructor
public class ClaimItemController {

    private final ClaimItemService service;

    @GetMapping
    public BaseResponse<List<ClaimItemResponse>> getAll(@PathVariable String claimId) {
        return BaseResponse.of(200, "Claim items retrieved successfully", service.getAll(claimId));
    }

    @GetMapping("/{id}")
    public BaseResponse<ClaimItemResponse> getById(@PathVariable String claimId, @PathVariable String id) {
        return BaseResponse.of(200, "Claim item retrieved successfully", service.getById(claimId, id));
    }

    @PostMapping
    public ResponseEntity<BaseResponse<ClaimItemResponse>> create(@PathVariable String claimId,
                                                                  @RequestBody ClaimItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.of(201, "Claim item created successfully", service.create(claimId, request)));
    }

    @PutMapping("/{id}")
    public BaseResponse<ClaimItemResponse> update(@PathVariable String claimId, @PathVariable String id,
                                                  @RequestBody ClaimItemRequest request) {
        return BaseResponse.of(200, "Claim item updated successfully", service.update(claimId, id, request));
    }
}