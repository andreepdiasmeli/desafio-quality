package desafio_quality.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import desafio_quality.dtos.RoomDTO;
import desafio_quality.dtos.UpsertRoomDTO;
import desafio_quality.exceptions.ResourceNotFoundException;
import desafio_quality.services.RoomService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    @DisplayName("Should return a new room")
    public void testCreateRoom() throws Exception {
        Long propertyId = 1L;
        UpsertRoomDTO room = new UpsertRoomDTO("Room", 5., 5.);
        String json = mapper.writeValueAsString(room);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/rooms/property/" + propertyId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        RoomDTO roomDTO = new RoomDTO(propertyId, "Room", 5., 5.);
        when(this.roomService.createRoom(any(Long.class), any(UpsertRoomDTO.class))).thenReturn(roomDTO);

        mock.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(propertyId))
                .andExpect(jsonPath("$.name").value("Room"))
                .andExpect(jsonPath("$.width").value(5.))
                .andExpect(jsonPath("$.length").value(5.));
    }

    @Test
    @DisplayName("Should not create Room when Property does not exist")
    public void testFailureToCreateRoom() throws Exception {
        Long propertyId = 1L;
        UpsertRoomDTO room = new UpsertRoomDTO("Room", 5., 5.);
        String json = mapper.writeValueAsString(room);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/rooms/property/" + propertyId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        when(this.roomService.createRoom(any(Long.class), any(UpsertRoomDTO.class)))
                .thenThrow(ResourceNotFoundException.class);

        mock.perform(request)
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("Should return a List of rooms")
    public void testGetRoom() throws Exception {
        Long propertyId = 1L;
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/rooms?pageNumber=1&pageSize=3")
                .accept(MediaType.APPLICATION_JSON);

        List<RoomDTO> rooms = this.createListOfRooms(3);
        Pageable paging = PageRequest.of(1, 3);
        Page<RoomDTO> page = new PageImpl<>(rooms, paging, rooms.size());
        when(this.roomService.getAllRooms(any(Integer.class), any(Integer.class))).thenReturn(page);

        mock.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(3)));
    }

    private List<RoomDTO> createListOfRooms(int number) {
        List<RoomDTO> rooms = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            rooms.add(new RoomDTO((long) i, "empty room", 1., 1.));
        }
        return rooms;
    }
}
