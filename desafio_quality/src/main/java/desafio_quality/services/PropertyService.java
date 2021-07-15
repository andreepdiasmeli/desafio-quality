package desafio_quality.services;

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

    public Property findPropertyById(Long propertyId) throws ResourceNotFoundException {
        return this.propertyRepository.findById(propertyId).orElseThrow(() ->
                new ResourceNotFoundException("Property with id " + propertyId + " was not found."));
    }
}
