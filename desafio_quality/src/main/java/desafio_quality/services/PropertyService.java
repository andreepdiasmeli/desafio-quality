package desafio_quality.services;

import desafio_quality.entities.Property;
import desafio_quality.exceptions.ResourceNotFoundException;

import desafio_quality.dtos.PropertyValueDTO;
import desafio_quality.dtos.PropertyAreaDTO;
import desafio_quality.dtos.RoomDTO;
import desafio_quality.entities.Room;
import desafio_quality.exceptions.PropertyHasNoRoomsException;
import desafio_quality.repositories.PropertyRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service
public class PropertyService {

    private final PropertyRepository propertyRepository;

    
    public PropertyService(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    public Property findPropertyById(Long propertyId) throws ResourceNotFoundException {
        return this.propertyRepository.findById(propertyId).orElseThrow(() ->
            new ResourceNotFoundException("Property with id " + propertyId + " was not found.")); 
    }

    public PropertyValueDTO getValue(Long propertyId) {
        Property property = findPropertyById(propertyId);
        return PropertyValueDTO.toDTO(property);
    }

    public PropertyAreaDTO getTotalArea(Long propertyId) {
        Property property = findPropertyById(propertyId);
        return PropertyAreaDTO.toDTO(property);
    }

    public RoomDTO getLargestRoom(Long propertyId) {
        Property property = findPropertyById(propertyId);

        Room largestRoom =  property.getRooms().stream()
            .max(Comparator.comparing(Room::getArea))
            .orElseThrow(() -> new PropertyHasNoRoomsException(propertyId));

        return RoomDTO.toDTO(largestRoom);
    }
}
