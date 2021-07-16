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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
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

    @Test
    @DisplayName("Should update a room with a specific ID.")
    void testUpdateRoomWithValidId() {
        Long roomId = 1L;

        District district = new District("Bom Retiro", new BigDecimal("2000"));
        Property property = new Property("Minha casa", district);
        Room room = new Room("Quarto", 2.0, 3.0, property);

        when(roomRepository.findById(any(Long.class))).thenReturn(Optional.of(room));
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        UpsertRoomDTO upsertRoomDTO = new UpsertRoomDTO(room.getName(), room.getWidth(), room.getLength());

        RoomDTO actualRoom = roomService.updateRoom(roomId, upsertRoomDTO);
        RoomDTO expectedRoom = new RoomDTO(roomId, room.getName(), room.getWidth(), room.getLength());

        assertThat(actualRoom).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedRoom);
    }

    @Test
    @DisplayName("Should return exception when updating a room with invalid ID.")
    void testUpdateRoomWithInvalidId(){
        Long roomId = 2L;

        District district = new District("Bom Retiro", new BigDecimal("2000"));
        Property property = new Property("Minha casa", district);
        Room room = new Room("Quarto", 2.0, 3.0, property);

        when(roomRepository.findById(any(Long.class))).thenThrow(ResourceNotFoundException.class);
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        UpsertRoomDTO upsertRoomDTO = new UpsertRoomDTO(room.getName(), room.getWidth(), room.getLength());

        assertThrows(ResourceNotFoundException.class,
            () -> roomService.updateRoom(roomId, upsertRoomDTO));
    }

    @Test
    @DisplayName("Should delete a room given a valid id.")
    void testDeleteRoomWithAValidId(){
        Long roomId = 1L;

        when(roomRepository.findById(any(Long.class))).thenReturn(Optional.of(new Room()));
        doNothing().when(roomRepository).deleteById(any(Long.class));

        assertDoesNotThrow(() -> {
            roomService.deleteRoom(roomId);
        });
    }

    @Test
    @DisplayName("Should throw exception when deleting a room given an invalid id.")
    void testDeleteRoomWithAnInvalidId(){
        Long roomId = 11L;

        when(roomRepository.findById(any(Long.class))).thenThrow(ResourceNotFoundException.class);
        doNothing().when(roomRepository).deleteById(any(Long.class));

        assertThrows(ResourceNotFoundException.class, () -> {
            roomService.deleteRoom(roomId);
        });
    }

    @Test
    @DisplayName("Should return a room when finding with a valid id.")
    void testFindRoomByIdWithAValidId(){
        Long roomId = 1L;

        District mockDistrict = new District("Costa e Silva", new BigDecimal("3000"));
        Property mockProperty = new Property("Casinha", mockDistrict);
        Room mockRoom = new Room("Quarto", 1.0, 1.0, mockProperty);
        when(roomRepository.findById(any(Long.class))).thenReturn(Optional.of(mockRoom));

        Room room = roomService.findById(roomId);

        District expectedDistrict = new District(mockDistrict.getName(), mockDistrict.getSquareMeterValue());
        Property expectedProperty = new Property(mockProperty.getName(), expectedDistrict);
        Room expectedRoom = new Room(mockRoom.getName(), mockRoom.getWidth(), mockRoom.getLength(), expectedProperty);
        assertThat(expectedRoom).usingRecursiveComparison().isEqualTo(room);
    }

    @Test
    @DisplayName("Should throw exception when finding a room with invalid id.")
    void testFindDistrictyByIdWithAnInvalidId(){
        Long roomId = 1L;

        when(roomRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        Exception ex = assertThrows(ResourceNotFoundException.class, () -> {
            roomService.findById(roomId);
        });

        assertEquals("Room " + roomId + " does not exist.", ex.getMessage());
    }
}