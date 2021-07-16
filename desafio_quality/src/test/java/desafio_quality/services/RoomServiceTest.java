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
    private RoomRepository roomRepository;

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
}