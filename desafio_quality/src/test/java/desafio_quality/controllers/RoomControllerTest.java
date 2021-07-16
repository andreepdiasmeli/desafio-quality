package desafio_quality.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import desafio_quality.dtos.RoomDTO;
import desafio_quality.exceptions.ResourceNotFoundException;
import desafio_quality.services.RoomService;
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

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@WebMvcTest(controllers = RoomController.class)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class RoomControllerTest {

    @Autowired
    private MockMvc mock;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private RoomService roomService;

    @Test
    @DisplayName("Should return the specific room by ID.")
    void testGetRoomById() throws Exception {
        Long roomId = 1L;

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/rooms/" + roomId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        RoomDTO roomDTO = new RoomDTO(1L, "Quarto 1", 3.0,5.0);
        when(roomService.getRoomById(any(Long.class))).thenReturn(roomDTO);

        mock.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Quarto 1"))
                .andExpect(jsonPath("$.width").value(3.0))
                .andExpect(jsonPath("$.length").value(5.0));
    }

    @Test
    @DisplayName("Should return Unprocessable Entity when searching a room with a non existent ID.")
    void testGetRoomByInvalidId() throws Exception {
        Long roomId = 2L;

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/rooms/" + roomId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        when(roomService.getRoomById(any(Long.class))).thenThrow(ResourceNotFoundException.class);

        mock.perform(request)
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("Should delete a room from a valid id.")
     void testDeleteARoom() throws Exception {
        Long roomId = 1L;
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete("/rooms/" + roomId)
                .accept(MediaType.APPLICATION_JSON);

        doNothing().when(roomService).deleteRoom(any(Long.class));

        mock.perform(request)
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should not delete a room from an invalid id.")
    void testDeleteAnInvalidRoom() throws Exception {
        Long roomId = 10L;
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete("/rooms/" + roomId)
                .accept(MediaType.APPLICATION_JSON);

        doThrow(ResourceNotFoundException.class).when(roomService).deleteRoom(any(Long.class));

        mock.perform(request)
                .andExpect(status().isUnprocessableEntity());
    }

}
