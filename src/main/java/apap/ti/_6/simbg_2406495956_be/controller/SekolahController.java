package apap.ti._6.simbg_2406495956_be.controller;

import apap.ti._6.simbg_2406495956_be.dto.BaseResponse;
import apap.ti._6.simbg_2406495956_be.model.Sekolah;
import apap.ti._6.simbg_2406495956_be.repository.SekolahRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sekolah")
@RequiredArgsConstructor
public class SekolahController {

    private final SekolahRepository sekolahRepository;

    @GetMapping
    public BaseResponse<List<Sekolah>> getAll() {
        return BaseResponse.of(200, "Schools retrieved successfully",
                sekolahRepository.findAll());
    }
}