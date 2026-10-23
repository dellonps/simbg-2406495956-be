package apap.ti._6.simbg_2406495956_be.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apap.ti._6.simbg_2406495956_be.dto.BaseResponse;
import apap.ti._6.simbg_2406495956_be.dto.HomeResponse;
import apap.ti._6.simbg_2406495956_be.service.InvoiceService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {

    private final InvoiceService service;

    @GetMapping
    public BaseResponse<HomeResponse> getHome() {
        return BaseResponse.of(200, "Dashboard retrieved successfully", service.getHome());
    }
}