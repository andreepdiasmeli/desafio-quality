package desafio_quality.controllers;

import desafio_quality.dtos.PropertyAreaDTO;
import desafio_quality.services.PropertyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("properties")
public class PropertyController {

    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @GetMapping("{id}/totalArea")
    public PropertyAreaDTO getTotalArea(@PathVariable Long id){
        return propertyService.getTotalArea(id);
    }
}
