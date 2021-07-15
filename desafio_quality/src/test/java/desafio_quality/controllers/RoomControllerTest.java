package desafio_quality.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import desafio_quality.dtos.RoomDTO;
import desafio_quality.dtos.UpsertRoomDTO;
import desafio_quality.exceptions.ResourceNotFoundException;
import desafio_quality.services.RoomService;

@WebMvcTest(controllers = RoomController.class)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class RoomControllerTest {
    
    @Autowired
    private MockMvc mock;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private RoomService roomService;

    @Test
    @DisplayName("Should return ok status and updated entity when using a valid ID.")
    void testUpdateRoomsWithValidID() throws Exception {
        Long roomId = 1L;

        UpsertRoomDTO upsertRoomDto = new UpsertRoomDTO("Quarto", 5.0, 3.0);
        String roomUpdateJson = mapper.writeValueAsString(upsertRoomDto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .put("/rooms/" + roomId)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(roomUpdateJson);

        RoomDTO roomDto = new RoomDTO(
            roomId, 
            upsertRoomDto.getName(), 
            upsertRoomDto.getWidth(), 
            upsertRoomDto.getLength());

        when(roomService.updateRoom(any(Long.class), any(UpsertRoomDTO.class))).thenReturn(roomDto);

        mock.perform(request)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(roomId))
            .andExpect(jsonPath("$.name").value(upsertRoomDto.getName()))
            .andExpect(jsonPath("$.width").value(upsertRoomDto.getWidth()))
            .andExpect(jsonPath("$.length").value(upsertRoomDto.getLength()));
    }

    @Test
    @DisplayName("Should return Unprocessable Entity when updating a room with a non existent ID.")
    void testFailureCreationOfADistrict() throws Exception {
        Long roomId = 2L;

        UpsertRoomDTO upsertRoomDto = new UpsertRoomDTO("Quarto", 5.0, 3.0);
        String roomUpdateJson = mapper.writeValueAsString(upsertRoomDto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .put("/rooms/" + roomId)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(roomUpdateJson);

        when(roomService.updateRoom(any(Long.class), any(UpsertRoomDTO.class))).thenThrow(ResourceNotFoundException.class);

        mock.perform(request)
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("Should return validation messages when updating a room with an invalid request body.")
    void testValidationWhenUpdatingRoomWithInvalidBody() throws Exception {
        Long roomId = 1L;

        UpsertRoomDTO upsertRoomDto = new UpsertRoomDTO("", 0.0, 34.0);
        String roomUpdateJson = mapper.writeValueAsString(upsertRoomDto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .put("/rooms/" + roomId)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(roomUpdateJson);

        mock.perform(request)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.name").value("O nome do cômodo deve começar com uma letra maiúscula."))
            .andExpect(jsonPath("$.width").value("A largura mínima permitida por cômodo é de 1 metro."))
            .andExpect(jsonPath("$.length").value("O comprimento máximo permitido por cômodo é de 33 metros."));
    }
}
