package desafio_quality.services;

import desafio_quality.dtos.PropertyRoomsAreaDTO;
import desafio_quality.dtos.PropertyValueDTO;
import desafio_quality.dtos.RoomAreaDTO;
import desafio_quality.entities.District;
import desafio_quality.entities.Property;
import desafio_quality.entities.Room;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
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
    @DisplayName("Should return a dto of property value given a valid id.")
    void testGetValueWithValidId(){
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
    @DisplayName("Should return exception when getting a value of a property with invalid id.")
    void testGetValueWithInvalidId(){
        Long propertyId = 1L;
        when(propertyRepository.findById(any(Long.class))).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> {
            propertyService.getValue(propertyId);
        });
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