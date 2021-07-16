package desafio_quality.services;

import desafio_quality.dtos.*;
import desafio_quality.entities.District;
import desafio_quality.entities.Property;
import desafio_quality.entities.Room;
import desafio_quality.exceptions.ResourceNotFoundException;
import desafio_quality.repositories.DistrictRepository;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
public class PropertyServiceTest {

    @Autowired
    private PropertyService propertyService;

    @MockBean
    private PropertyRepository propertyRepository;

    @MockBean
    private DistrictService districtService;

    @MockBean
    private DistrictRepository districtRepository;

    @Test
    @DisplayName("Should return a dto of property value.")
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
    @DisplayName("Should return execption when getting a value.")
    void testGetValueWithInvalidId(){
        Long propertyId = 1L;
        when(propertyRepository.findById(any(Long.class))).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> {
            propertyService.getValue(propertyId);
        });
    }

    @Test
    @DisplayName("Should return a property by id.")
    void testGetPropertyById(){
        // given - cenário
        Long propertyId = 1L;

        District district = new District("Bom Retiro", new BigDecimal("2000"));
        Property property = new Property("Minha casa", district);

        List<Room> rooms = List.of(
                new Room("Quarto", 1.0, 1.0, property));

        property.setRooms(rooms);

        when(propertyRepository.findById(any(Long.class))).thenReturn(Optional.of(property));
        // when - execução

        PropertyDTO propertyDTO = propertyService.getPropertyById(propertyId);

        DistrictDTO districtDTO = new DistrictDTO(district.getId(),district.getName(), district.getSquareMeterValue());

        List<RoomDTO> roomsDTO = List.of(
                new RoomDTO(1L, "Quarto", 1.0, 1.0)
        );

        // then - verificação

        PropertyDTO expected = new PropertyDTO(propertyId, property.getName(), districtDTO, roomsDTO);
        assertThat(propertyDTO).usingRecursiveComparison().ignoringFields("id", "rooms.id").isEqualTo(expected);
    }

    @Test
    @DisplayName("Should return the total value of the property area.")
    void testGetTotalAreaProperty(){
        // given - cenário
        Long propertyId = 1L;

        District district = new District("Bom Retiro", new BigDecimal("2000"));
        Property property = new Property("Minha casa", district);
        List<Room> rooms = List.of(
                new Room("Quarto", 2.0, 2.0, property));
        property.setRooms(rooms);

        when(propertyRepository.findById(any(Long.class))).thenReturn(Optional.of(property));
        // when - execução

        PropertyAreaDTO propertyAreaDTO = propertyService.getTotalArea(propertyId);

        // then - verificação

        PropertyAreaDTO expected = new PropertyAreaDTO(4.0);
        assertThat(propertyAreaDTO).usingRecursiveComparison().ignoringFields("id").isEqualTo(expected);
    }

    @Test
    @DisplayName("Should create a property.")
    void testCreateProperty() {

        District district = new District("Bom Retiro", new BigDecimal("2000"));
        Property property = new Property("Minha casa", district);

        when(districtRepository.findById(any(Long.class))).thenReturn(Optional.of(district));
        when(propertyRepository.findById(any(Long.class))).thenReturn(Optional.of(property));
        when(propertyRepository.save(any(Property.class))).thenReturn(property);
        when(districtService.findDistrictById(any(Long.class))).thenReturn(district);

        UpsertPropertyDTO createProperty = new UpsertPropertyDTO(property.getName(),1L);

        PropertyDTO propertyDTO = propertyService.createProperty(createProperty);

        DistrictDTO districtDTO = new DistrictDTO(
                district.getId(),
                district.getName(),
                district.getSquareMeterValue());

        PropertyDTO expectedDTO = new PropertyDTO(
                property.getId(),
                property.getName(),
                districtDTO,
                new ArrayList<>()
                );

        assertThat(propertyDTO).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedDTO);
    }

}