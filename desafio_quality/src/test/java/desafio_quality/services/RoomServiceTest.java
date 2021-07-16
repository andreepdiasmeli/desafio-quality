package desafio_quality.services;

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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class RoomServiceTest {

    @Autowired
    private RoomService roomService;

    @MockBean
    private RoomRepository roomRepository;

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
}