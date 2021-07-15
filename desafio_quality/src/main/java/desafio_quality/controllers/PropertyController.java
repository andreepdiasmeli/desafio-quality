package desafio_quality.controllers;

import desafio_quality.dtos.*;
import desafio_quality.services.PropertyService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("properties")
public class PropertyController {

    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public PropertyDTO createProperty(@RequestBody @Valid UpsertPropertyDTO createProperty) {
        return this.propertyService.createProperty(createProperty);
    }

    @GetMapping("{propertyId}")
    public PropertyDTO getProperty(@PathVariable Long propertyId) {
        return this.propertyService.getPropertyById(propertyId);
    }

    @GetMapping("")
    public List<PropertyDTO> getAllProperties() {
        return this.propertyService.getAllProperties();
    }

    @GetMapping("{propertyId}/rooms")
    public List<RoomDTO> getPropertyRooms(@PathVariable Long propertyId) {
        return this.propertyService.getPropertyRooms(propertyId);
    }

    @PutMapping("{propertyId}")
    public PropertyDTO updateProperty(@PathVariable Long propertyId, @RequestBody @Valid UpsertPropertyDTO upsertPropertyDTO) {
        return this.propertyService.updateProperty(propertyId, upsertPropertyDTO);
    }

    @DeleteMapping("{propertyId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProperty(@PathVariable Long propertyId) {
        this.propertyService.deleteProperty(propertyId);
    }


    @GetMapping("{id}/value")
    public PropertyValueDTO getValue(@PathVariable Long id) {
        return propertyService.getValue(id);
    }

    @GetMapping("{id}/totalArea")
    public PropertyAreaDTO getTotalArea(@PathVariable Long id){
        return propertyService.getTotalArea(id);

    }

    @GetMapping("{propertyId}/largestRoom")
    public RoomDTO getLargestRoom(@PathVariable(value = "propertyId") Long propertyId){
        return propertyService.getLargestRoom(propertyId);
    }
}
