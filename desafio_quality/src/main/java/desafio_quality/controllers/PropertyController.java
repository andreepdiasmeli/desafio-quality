package desafio_quality.controllers;

import desafio_quality.dtos.PropertyRoomsAreaDTO;
import desafio_quality.dtos.RoomDTO;
import desafio_quality.entities.Room;
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

    @GetMapping("{propertyId}/largestRoom")
    public RoomDTO getLargestRoom(@PathVariable(value = "propertyId") Long propertyId){
        return propertyService.getLargestRoom(propertyId);
    }

    @GetMapping("{propertyId}/roomsArea")
    public PropertyRoomsAreaDTO getRoomsArea(@PathVariable(value = "propertyId") Long propertyId){
        return propertyService.getRoomsArea(propertyId);
    }
}
