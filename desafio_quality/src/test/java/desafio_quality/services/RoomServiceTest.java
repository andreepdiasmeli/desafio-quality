package desafio_quality.services;

import desafio_quality.dtos.PropertyValueDTO;
import desafio_quality.dtos.RoomDTO;
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
    private RoomRepository roomRepository;

    @Test
    @DisplayName("Should return a dto of room.")
    void testGetRoomWithValidId(){
        Long roomId = 1L;

        District district = new District("Bom Retiro", new BigDecimal("2000"));
        Property property = new Property("Minha casa", district);
        Room room = new Room("Quarto 1", 3.0,5.0, property);

        when(roomRepository.findById(any(Long.class))).thenReturn(Optional.of(room));

        RoomDTO roomDTO = roomService.getRoomById(roomId);

        RoomDTO expected = new RoomDTO(roomId, room.getName(), room.getWidth(), room.getLength());
        assertThat(roomDTO).usingRecursiveComparison().ignoringFields("id").isEqualTo(expected);
    }


    @Test
    @DisplayName("Should return exception when getting a value.")
    void testGetRoomWithInvalidId(){
        Long roomId = 1L;
        when(roomRepository.findById(any(Long.class))).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> {
            roomService.getRoomById(roomId);
        });
    }
}