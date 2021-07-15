package desafio_quality.controllers;

import desafio_quality.dtos.CreateDistrictDTO;
import desafio_quality.dtos.DistrictDTO;
import desafio_quality.dtos.validation.ValidationSequence;
import desafio_quality.services.DistrictService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("districts")
public class DistrictController {

    private final DistrictService districtService;

    public DistrictController(DistrictService districtService) {
        this.districtService = districtService;
    }

    @GetMapping
    public List<DistrictDTO> getAllDistricts(){
        return this.districtService.getAllDistricts();
    }

    @GetMapping("{districtId}")
    public DistrictDTO getByIdDistrict(@PathVariable Long districtId){
        return this.districtService.getDistrictById(districtId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DistrictDTO createDistrict(@RequestBody @Valid CreateDistrictDTO createDistrictDTO){
        return this.districtService.createDistrict(createDistrictDTO);
    }

    @PutMapping("{districtId}")
    public DistrictDTO updateDistrict(@PathVariable Long districtId, @RequestBody @Valid CreateDistrictDTO createDistrictDTO){
        return this.districtService.updateDistrict(districtId, createDistrictDTO);
    }

    @DeleteMapping("{districtId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDistrict(@PathVariable Long districtId){
        this.districtService.deleteDistrict(districtId);
    }
}
