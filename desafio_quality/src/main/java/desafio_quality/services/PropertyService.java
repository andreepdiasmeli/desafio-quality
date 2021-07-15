package desafio_quality.services;

import desafio_quality.dtos.PropertyDTO;
import desafio_quality.dtos.RoomDTO;
import desafio_quality.dtos.UpsertPropertyDTO;
import desafio_quality.entities.District;
import desafio_quality.entities.Property;
import desafio_quality.entities.Room;
import desafio_quality.exceptions.ResourceNotFoundException;
import desafio_quality.repositories.PropertyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final DistrictService districtService;

    public PropertyService(PropertyRepository propertyRepository, DistrictService districtService) {
        this.propertyRepository = propertyRepository;
        this.districtService = districtService;
    }

    public Property findPropertyById(Long propertyId) {
        return this.propertyRepository.findById(propertyId).orElseThrow(() ->
                new ResourceNotFoundException("Property with ID " + propertyId + " does not exist.")
        );
    }

    public PropertyDTO getPropertyById(Long propertyId) {
        Property property = this.findPropertyById(propertyId);
        return PropertyDTO.toDTO(property);
    }

    public List<PropertyDTO> getAllProperties() {
        List<Property> properties = this.propertyRepository.findAll();
        return properties.stream()
                .map(PropertyDTO::toDTO)
                .collect(Collectors.toList());
    }

    public List<RoomDTO> getPropertyRooms(Long propertyId) {
        Property property = this.findPropertyById(propertyId);
        List<Room> rooms = property.getRooms();
        return rooms.stream()
                .map(RoomDTO::toDTO)
                .collect(Collectors.toList());
    }


    public PropertyDTO createProperty(UpsertPropertyDTO createProperty) {
        District district = this.districtService.findDiscrict(createProperty.getDistrictId());
        Property property = new Property(createProperty.getName(), district);
        property = this.propertyRepository.save(property);
        return PropertyDTO.toDTO(property);
    }

    public PropertyDTO updateProperty(Long propertyId, UpsertPropertyDTO updateProperty) {
        Property property = this.findPropertyById(propertyId);
        property.setName(updateProperty.getName());
        District district = this.districtService.findDiscrict(updateProperty.getDistrictId());
        property.setDistrict(district);
        property = this.propertyRepository.save(property);
        return PropertyDTO.toDTO(property);
    }

    public void deleteProperty(Long propertyId) {
        if (!this.propertyRepository.existsById(propertyId)) {
            throw new ResourceNotFoundException("Property with ID " + propertyId + " does not exist.");
        }
        this.propertyRepository.deleteById(propertyId);
    }
}
