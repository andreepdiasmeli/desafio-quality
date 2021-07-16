package desafio_quality.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import desafio_quality.dtos.DistrictDTO;
import desafio_quality.entities.Room;
import desafio_quality.exceptions.ResourceNotFoundException;
import desafio_quality.services.DistrictService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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