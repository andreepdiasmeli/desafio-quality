package desafio_quality.controllers;

import desafio_quality.services.PropertyService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PropertyController {

    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }
}
