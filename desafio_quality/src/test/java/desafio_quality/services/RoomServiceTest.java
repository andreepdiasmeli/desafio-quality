package desafio_quality.services;

import desafio_quality.dtos.RoomDTO;
import desafio_quality.dtos.UpsertRoomDTO;
import desafio_quality.entities.District;
import desafio_quality.entities.Property;
import desafio_quality.entities.Room;
import desafio_quality.exceptions.ResourceNotFoundException;
import desafio_quality.repositories.RoomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RoomServiceTest {
    @Autowired
    private RoomService roomService;

    @MockBean
    private PropertyService propertyService;

    @MockBean
    private RoomRepository roomRepository;

    @Test
    @DisplayName("Should return a dto of a created room.")
    void testCreateRoom() {
        Long propertyId = 1L;
        UpsertRoomDTO roomUpsert = new UpsertRoomDTO("Some Room", 1., 5.);
        District district = new District("Some District", new BigDecimal("10000"));
        Property property = new Property("Some House", district);
        Room room = new Room("Some Room", 1., 5., property);

        when(propertyService.findPropertyById(any(Long.class))).thenReturn(property);
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        RoomDTO roomDTO = roomService.createRoom(propertyId, roomUpsert);

        RoomDTO expected = new RoomDTO(1L, "Some Room", 1., 5.);
        assertThat(roomDTO).usingRecursiveComparison().ignoringFields("id", "property.id").isEqualTo(expected);
    }

    @Test
    @DisplayName("Should throw an exception when trying to create room and property does not exist.")
    void testFailureOfCreateRoom() {
        Long propertyId = 1L;
        UpsertRoomDTO roomUpsert = new UpsertRoomDTO("Some Room", 1., 3.);
        when(propertyService.findPropertyById(any(Long.class))).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> roomService.createRoom(propertyId, roomUpsert));
    }

    @Test
    @DisplayName("Should return a dto of room.")
    void testGetRoom() {
        Long roomId = 1L;
        District district = new District("Some District", new BigDecimal("10000"));
        Property property = new Property("Some House", district);
        Room room = new Room("Some Room", 1., 4., property);

        when(roomRepository.findById(any(Long.class))).thenReturn(Optional.of(room));

        RoomDTO roomDTO = roomService.getRoomById(roomId);

        RoomDTO expected = new RoomDTO(1L, "Some Room", 1., 4.);
        assertThat(roomDTO).usingRecursiveComparison().ignoringFields("id", "property.id").isEqualTo(expected);
    }

    @Test
    @DisplayName("Should throw an exception when trying to get a room that does not exist.")
    void testFailureOfGetRoom() {
        Long roomId = 1L;
        when(roomRepository.findById(any(Long.class))).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> roomService.getRoomById(roomId));
    }
}
