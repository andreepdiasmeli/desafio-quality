package desafio_quality.controllers;

import desafio_quality.dtos.PropertyValueDTO;
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

    @GetMapping("{id}/value")
    public PropertyValueDTO getValue(@PathVariable Long id){
        return propertyService.getValue(id);
    }
}
