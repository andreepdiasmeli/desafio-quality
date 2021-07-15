package desafio_quality.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;

import com.fasterxml.jackson.databind.ObjectMapper;

import desafio_quality.dtos.PropertyRoomsAreaDTO;
import desafio_quality.dtos.PropertyValueDTO;
import desafio_quality.dtos.RoomAreaDTO;
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
    @DisplayName("Should return a list of the areas of the rooms.")
    void testRoomAreas() throws Exception {
        Long propertyId = 1L;
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .get("/properties/" + propertyId + "/roomsArea")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);
    
        List<RoomAreaDTO> roomAreas = new ArrayList<>();
        roomAreas.add(new RoomAreaDTO(1L, "Cozinha", 15.0));
        roomAreas.add(new RoomAreaDTO(2L, "Quarto", 5.0));

        PropertyRoomsAreaDTO propertyRoomsAreaDTO = new PropertyRoomsAreaDTO(
            1L, "Propriedade 1", roomAreas
        );

        when(propertyService.getRoomsArea(any(Long.class))).thenReturn(propertyRoomsAreaDTO);

        mock.perform(request)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(propertyId))
            .andExpect(jsonPath("$.name").value("Propriedade 1"))
            .andExpect(jsonPath("$.rooms", hasSize(2)));
    }

    @Test
    @DisplayName("Should return Unprocessable Entity when trying to list the areas of the rooms with an invalid property ID.")
    void testRoomAreasWithInvalidPropertyId() throws Exception {
        Long propertyId = 2L;
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .get("/properties/" + propertyId + "/roomsArea")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

        when(propertyService.getRoomsArea(any(Long.class))).thenThrow(ResourceNotFoundException.class);

        mock.perform(request)
            .andExpect(status().isUnprocessableEntity());
    }
}