package desafio_quality.services;

import desafio_quality.dtos.DistrictDTO;
import desafio_quality.dtos.PropertyDTO;
import desafio_quality.dtos.PropertyRoomsAreaDTO;
import desafio_quality.dtos.PropertyValueDTO;
import desafio_quality.dtos.RoomDTO;
import desafio_quality.dtos.RoomAreaDTO;
import desafio_quality.entities.District;
import desafio_quality.entities.Property;
import desafio_quality.entities.Room;
import desafio_quality.exceptions.PropertyHasNoRoomsException;
import desafio_quality.exceptions.ResourceNotFoundException;
import desafio_quality.repositories.PropertyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
@SpringBootTest
public class PropertyServiceTest {

    @Autowired
    private PropertyService propertyService;

    @MockBean
    private PropertyRepository propertyRepository;

    @Test
    @DisplayName("Should return a dto of property value.")
    void testGetValueWithValidId() {
        // given - cenário
        Long propertyId = 1L;

        District district = new District("Bom Retiro", new BigDecimal("2000"));
        Property property = new Property("Minha casa", district);
        List<Room> rooms = List.of(
                new Room("Quarto", 1.0, 1.0, property),
                new Room("Cozinha", 1.0, 1.0, property),
                new Room("Sala", 1.0, 1.0, property));
        property.setRooms(rooms);
        when(propertyRepository.findById(any(Long.class))).thenReturn(Optional.of(property));

        // when - execução
        PropertyValueDTO propertyValueDTO = propertyService.getValue(propertyId);

        // then - verificação
        PropertyValueDTO expected = new PropertyValueDTO(new BigDecimal("6000"));
        assertThat(propertyValueDTO).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("Should return exception when getting a value.")
    void testGetValueWithInvalidId() {
        Long propertyId = 1L;
        when(propertyRepository.findById(any(Long.class))).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> {
            propertyService.getValue(propertyId);
        });
    }

    @Test
    @DisplayName("Should return a dto of the largest room.")
    void testGetLargestRoom() {
        Long propertyId = 1L;

        District district = new District("Some District", new BigDecimal("2000"));
        Property property = new Property("Some House", district);
        List<Room> rooms = List.of(
                new Room("Room", 1.0, 2.0, property),
                new Room("Kitchen", 2.0, 3.0, property),
                new Room("Bathroom", 3.0, 4.0, property));
        property.setRooms(rooms);
        when(propertyRepository.findById(any(Long.class))).thenReturn(Optional.of(property));

        RoomDTO largestRoom = propertyService.getLargestRoom(propertyId);

        RoomDTO expected = new RoomDTO(1l, "Bathroom", 3., 4.);
        assertThat(largestRoom).usingRecursiveComparison().ignoringFields("id", "property.id").isEqualTo(expected);
    }

    @Test
    @DisplayName("Should throw exception when looking for largest room and property does not exist.")
    void testFailToGetLargestRoom() {
        Long propertyId = 1L;

        when(propertyRepository.findById(any(Long.class))).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> {
            propertyService.getLargestRoom(propertyId);
        });
    }

    @Test
    @DisplayName("Should throw exception when looking for largest room and property does not have rooms.")
    void testFailToGetLargestRoom2() {
        Long propertyId = 1L;

        District district = new District("Some District", new BigDecimal("2000"));
        Property property = new Property("Some House", district);
        property.setRooms(new ArrayList<>());
        when(propertyRepository.findById(any(Long.class))).thenReturn(Optional.of(property));

        assertThrows(PropertyHasNoRoomsException.class, () -> {
            propertyService.getLargestRoom(propertyId);
        });
    }

    @Test
    @DisplayName("Should return a list of dtos of all properties.")
    void testGetAllProperties() {
        District district = new District("Some District", new BigDecimal("2000"));
        Property property1 = new Property("Some House 1", district);
        Property property2 = new Property("Some House 2", district);
        Property property3 = new Property("Some House 3", district);
        when(propertyRepository.findAll()).thenReturn(List.of(property1, property2, property3));

        List<PropertyDTO> listOfProperties = propertyService.getAllProperties();

        DistrictDTO districtDTO = new DistrictDTO(1L, "Some District", new BigDecimal("2000"));
        PropertyDTO propertyDTO1 =  new PropertyDTO(1L, "Some House 1", districtDTO, new ArrayList<>());
        PropertyDTO propertyDTO2 =  new PropertyDTO(1L, "Some House 2", districtDTO, new ArrayList<>());
        PropertyDTO propertyDTO3 =  new PropertyDTO(1L, "Some House 3", districtDTO, new ArrayList<>());


        List<PropertyDTO> expected = List.of(propertyDTO1, propertyDTO2, propertyDTO3);
        assertThat(listOfProperties).usingRecursiveComparison().ignoringFields("id", "district.id").isEqualTo(expected);
    }

    @Test
    @DisplayName("Should return the rooms areas using a valid property ID.")
    void testGetRoomsAreaByValidPropertyId() {
        Long propertyId = 1L;

        District district = new District("Bom Retiro", new BigDecimal("2000"));
        Property property = new Property("Minha casa", district);
        List<Room> rooms = List.of(
                new Room("Quarto", 2.0, 1.0, property),
                new Room("Cozinha", 4.0, 2.0, property),
                new Room("Sala", 2.0, 3.0, property));
        property.setRooms(rooms);

        List<RoomAreaDTO> roomAreaDTOList = List.of(
            new RoomAreaDTO(1L, "Quarto", 2.0),
            new RoomAreaDTO(2L, "Cozinha", 8.0),
            new RoomAreaDTO(3L, "Sala", 6.0)
        );

        when(propertyRepository.findById(any(Long.class))).thenReturn(Optional.of(property));

        PropertyRoomsAreaDTO expectPropertyRoomsAreaDTO =
            new PropertyRoomsAreaDTO(propertyId, property.getName(), roomAreaDTOList);

        PropertyRoomsAreaDTO actualPropertyRoomsAreaDTO =
            propertyService.getRoomsArea(propertyId);

        assertThat(actualPropertyRoomsAreaDTO)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .ignoringFields("rooms.id")
            .isEqualTo(expectPropertyRoomsAreaDTO);
    }

    @Test
    @DisplayName("Should return exception when getting room areas using an invalid property ID.")
    void testGetRoomsAreaByInvalidPropertyId() {
        Long propertyId = 2L;

        when(propertyRepository.findById(any(Long.class))).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> {
            propertyService.getRoomsArea(propertyId);
        });
    }

    @Test
    @DisplayName("Should return a property when finding with a valid id.")
    void testFindPropertyByIdWithAValidId(){
        Long propertyId = 1L;

        District mockDistrict = new District("Costa e Silva", new BigDecimal("3000"));
        Property mockProperty = new Property("Casinha", mockDistrict);
        when(propertyRepository.findById(any(Long.class))).thenReturn(Optional.of(mockProperty));

        Property property = propertyService.findPropertyById(propertyId);

        District expectedDistrict = new District(mockDistrict.getName(), mockDistrict.getSquareMeterValue());
        Property expectedProperty = new Property(mockProperty.getName(), expectedDistrict);
        assertThat(expectedProperty).usingRecursiveComparison().isEqualTo(property);
    }

    @Test
    @DisplayName("Should throw exception when finding a property with invalid id.")
    void testFindPropertyByIdWithAnInvalidId(){
        Long propertyId = 1L;

        when(propertyRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        Exception ex = assertThrows(ResourceNotFoundException.class, () -> {
            propertyService.findPropertyById(propertyId);
        });

        assertEquals("Property with ID " + propertyId + " does not exist.", ex.getMessage());
    }
}