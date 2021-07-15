package desafio_quality.controllers;

import desafio_quality.services.DistrictService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DistrictController {

    private final DistrictService districtService;

    public DistrictController(DistrictService districtService) {
        this.districtService = districtService;
    }
}
