package desafio_quality.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import desafio_quality.dtos.DistrictDTO;
import desafio_quality.dtos.PropertyDTO;
import desafio_quality.dtos.PropertyValueDTO;
import desafio_quality.dtos.RoomDTO;
import desafio_quality.exceptions.PropertyHasNoRoomsException;
import desafio_quality.exceptions.ResourceNotFoundException;
import desafio_quality.services.PropertyService;
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
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PropertyController.class)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class PropertyControllerTest {

    @Autowired
    private MockMvc mock;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private PropertyService propertyService;

    @Test
    @DisplayName("Should return the value of a property from a valid id.")
    void testValueOfAProperty() throws Exception {
        Long propertyId = 1L;
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/properties/" + propertyId + "/value")
                .accept(MediaType.APPLICATION_JSON);

        PropertyValueDTO valueDTO = new PropertyValueDTO(new BigDecimal("200000.0"));
        when(propertyService.getValue(any(Long.class))).thenReturn(valueDTO);

        mock.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value").value("200000.0"));
    }

    @Test
    @DisplayName("Should return UNPROCESSABLE_ENTITY when getting the value of a non existent property.")
    void testValueOfAPropertyNonExistent() throws Exception {
        Long propertyId = 10L;
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/properties/" + propertyId + "/value")
                .accept(MediaType.APPLICATION_JSON);

        when(propertyService.getValue(any(Long.class))).thenThrow(ResourceNotFoundException.class);

        mock.perform(request)
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("Should return the List of all properties.")
    void testGetAllProperties() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/properties/")
                .accept(MediaType.APPLICATION_JSON);
        PropertyDTO property1 = this.createSomePropertyDTO("Yellow House", 2);
        PropertyDTO property2 = this.createSomePropertyDTO("Green House", 1);

        when(propertyService.getAllProperties()).thenReturn(List.of(property1, property2));

        mock.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Yellow House"))
                .andExpect(jsonPath("$[0].rooms", hasSize(2)));
    }

    @Test
    @DisplayName("Should return a room with the largest area")
    void testGetRoomWithLargestArea() throws Exception {
        Long propertyId = 1L;
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/properties/" + propertyId + "/largestRoom")
                .accept(MediaType.APPLICATION_JSON);
        RoomDTO room = new RoomDTO(3L, "largest empty room", 10., 10.);

        when(propertyService.getLargestRoom(propertyId)).thenReturn(room);

        mock.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.width").value(10.))
                .andExpect(jsonPath("$.length").value(10.))
                .andExpect(jsonPath("$.name").value("largest empty room"));
    }

    @Test
    @DisplayName("Should throw exception when property does not exist")
    void testFailGetRoomWithLargestArea() throws Exception {
        Long propertyId = 1L;
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/properties/" + propertyId + "/largestRoom")
                .accept(MediaType.APPLICATION_JSON);

        when(propertyService.getLargestRoom(propertyId)).thenThrow(ResourceNotFoundException.class);

        mock.perform(request)
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("Should throw exception when property has no room")
    void testFailToFindRoomWithLargestArea() throws Exception {
        Long propertyId = 1L;
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/properties/" + propertyId + "/largestRoom")
                .accept(MediaType.APPLICATION_JSON);

        when(propertyService.getLargestRoom(propertyId)).thenThrow(PropertyHasNoRoomsException.class);

        mock.perform(request)
                .andExpect(status().isUnprocessableEntity());
    }

    private PropertyDTO createSomePropertyDTO(String name, int numberRooms) {
        DistrictDTO district = new DistrictDTO(1L,
                "Downtown",
                new BigDecimal("9540.50"));
        return new PropertyDTO(1L,
                name,
                district,
                this.createListOfRooms(numberRooms)
                );
    }

    private List<RoomDTO> createListOfRooms(int number) {
        List<RoomDTO> rooms = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            rooms.add(new RoomDTO((long) (i+1), "empty room", 1., 1.));
        }
        return rooms;
    }
}

