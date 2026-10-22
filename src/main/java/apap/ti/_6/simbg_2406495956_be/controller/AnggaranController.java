package apap.ti._6.simbg_2406495956_be.controller;

import apap.ti._6.simbg_2406495956_be.dto.AnggaranRequest;
import apap.ti._6.simbg_2406495956_be.dto.AnggaranResponse;
import apap.ti._6.simbg_2406495956_be.dto.BaseResponse;
import apap.ti._6.simbg_2406495956_be.service.AnggaranService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/anggaran")
public class AnggaranController {

    private final AnggaranService anggaranService;

    public AnggaranController(AnggaranService anggaranService) {
        this.anggaranService = anggaranService;
    }

    @GetMapping
    public BaseResponse<List<AnggaranResponse>> getAll() {
        return BaseResponse.of(200, "Anggaran retrieved successfully", anggaranService.getAll());
    }

    @GetMapping("/{id}")
    public BaseResponse<AnggaranResponse> getById(@PathVariable String id) {
        return BaseResponse.of(200, "Anggaran retrieved successfully", anggaranService.getById(id));
    }

    @PostMapping
    public ResponseEntity<BaseResponse<AnggaranResponse>> create(@RequestBody AnggaranRequest request) {
        AnggaranResponse created = anggaranService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.of(201, "Anggaran created successfully", created));
    }
}