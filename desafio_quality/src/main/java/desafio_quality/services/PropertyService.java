package desafio_quality.services;

import desafio_quality.dtos.PropertyAreaDTO;
import desafio_quality.entities.Property;
import desafio_quality.exceptions.ResourceNotFoundException;
import desafio_quality.repositories.PropertyRepository;
import org.springframework.stereotype.Service;

@Service
public class PropertyService {

    private final PropertyRepository propertyRepository;

    public PropertyService(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    private Property findById(Long propertyId) {
        return propertyRepository.findById(propertyId).orElseThrow(() ->
                new ResourceNotFoundException("Property with id " + propertyId + " was not found.")
        );
    }

    public PropertyAreaDTO getTotalArea(Long propertyId) {
        Property property = findById(propertyId);
        return PropertyAreaDTO.toDTO(property);
    }
}
