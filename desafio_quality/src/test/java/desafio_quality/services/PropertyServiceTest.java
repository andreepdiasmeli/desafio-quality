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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
@SpringBootTest
public class PropertyServiceTest {

    @Autowired
    private PropertyService propertyService;

    @MockBean
    private PropertyRepository propertyRepository;

    @MockBean
    private DistrictService districtService;

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
    @DisplayName("Should return exception when getting a value.")
    void testGetValueWithInvalidId(){
        Long propertyId = 1L;
        when(propertyRepository.findById(any(Long.class))).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> {
            propertyService.getValue(propertyId);
        });
    }

    @Test
    @DisplayName("Should return a list of room dtos of a property.")
    void testGetPropertyRoomsWithValidId(){
        Long propertyId = 1L;

        District district = new District("Bom Retiro", new BigDecimal("2000"));
        Property property = new Property("Minha casa", district);
        List<Room> rooms = List.of(
                new Room("Quarto", 2.0, 3.0, property),
                new Room("Sala", 5.0, 3.0, property));
        property.setRooms(rooms);

        when(propertyRepository.findById(any(Long.class))).thenReturn(Optional.of(property));

        List<RoomDTO> roomDTOList = propertyService.getPropertyRooms(propertyId);

        List<RoomDTO> expected = List.of(
                new RoomDTO(1L, "Quarto", 2.0,3.0),
                new RoomDTO(2L, "Sala", 5.0,3.0)
        );

        assertThat(roomDTOList).usingRecursiveComparison().ignoringFields("id","room.id").isEqualTo(expected);
    }

    @Test
    @DisplayName("Should return exception when getting a value.")
    void testGetPropertyRoomsWithInvalidId(){
        Long propertyId = 1L;
        when(propertyRepository.findById(any(Long.class))).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> {
            propertyService.getPropertyRooms(propertyId);
        });
    }

    @Test
    @DisplayName("Should delete a property with a specific ID.")
    void testDeletePropertyWithValidID() {
        Long propertyId = 1L;

        District district = new District("Bom Retiro", new BigDecimal("2000"));

        when(propertyRepository.findById(any(Long.class))).thenReturn(Optional.of(new Property("Minha casa", district)));
        doNothing().when(propertyRepository).deleteById(any(Long.class));

        assertDoesNotThrow(() -> propertyService.deleteProperty(propertyId));
    }

    @Test
    @DisplayName("Should not delete a property with an invalid ID.")
    void testDeletePropertyWithInvalidID() {
        Long propertyId = 2L;

        when(propertyRepository.findById(any(Long.class))).thenThrow(ResourceNotFoundException.class);
        doNothing().when(propertyRepository).deleteById(any(Long.class));

        assertThrows(ResourceNotFoundException.class,
                () -> propertyService.deleteProperty(propertyId));
    }

    @Test
    @DisplayName("Should update a property with a specific ID.")
    void testUpdatePropertyWithValidId() {
        Long propertyId = 1L;

        District district = new District("Bom Retiro", new BigDecimal("2000"));
        Property property = new Property("Minha casa", district);
        List<RoomDTO> roomDTOList = new ArrayList<>();

        when(propertyRepository.findById(any(Long.class))).thenReturn(Optional.of(property));
        when(propertyRepository.save(any(Property.class))).thenReturn(property);
        when(districtService.findDiscrict(any(Long.class))).thenReturn(district);

        UpsertPropertyDTO upsertPropertyDTO = new UpsertPropertyDTO(property.getName(), 1L);

        DistrictDTO districtDTO = new DistrictDTO(propertyId, district.getName(), district.getSquareMeterValue());

        PropertyDTO actualProperty = propertyService.updateProperty(propertyId, upsertPropertyDTO);
        PropertyDTO expectedProperty = new PropertyDTO(propertyId, property.getName(), districtDTO, roomDTOList);

        assertThat(actualProperty).usingRecursiveComparison().ignoringFields("id","district.id").isEqualTo(expectedProperty);
    }

    @Test
    @DisplayName("Should return exception when updating a property with invalid ID.")
    void testUpdatePropertyWithInvalidId(){
        Long roomId = 2L;

        District district = new District("Bom Retiro", new BigDecimal("2000"));
        Property property = new Property("Minha casa", district);
        Room room = new Room("Quarto", 2.0, 3.0, property);


        when(propertyRepository.findById(any(Long.class))).thenThrow(ResourceNotFoundException.class);
        when(propertyRepository.save(any(Property.class))).thenReturn(property);

        UpsertPropertyDTO upsertPropertyDTO = new UpsertPropertyDTO(property.getName(), 1L);

        UpsertRoomDTO upsertRoomDTO = new UpsertRoomDTO(room.getName(), room.getWidth(), room.getLength());

        assertThrows(ResourceNotFoundException.class,
                () -> propertyService.updateProperty(roomId, upsertPropertyDTO));
    }

}