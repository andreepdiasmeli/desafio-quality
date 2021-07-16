package desafio_quality.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import desafio_quality.dtos.*;
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
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.*;
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
    @DisplayName("Should return status code No Content when deleting a valid property ID.")
    void testDeletePropertyReturnsNoContent() throws Exception {
        Long propertyId = 1L;

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete("/properties/" + propertyId);

        doNothing().when(propertyService).deleteProperty(any(Long.class));

        mock.perform(request)
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should return status code Unprocessable Entity when deleting a non existent property ID.")
    void testDeleteInvalidPropertyReturnsUnprocessableEntity() throws Exception {
        Long propertyId = 2L;

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete("/properties/" + propertyId);

        doThrow(ResourceNotFoundException.class)
                .when(propertyService)
                .deleteProperty(any(Long.class));

        mock.perform(request)
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("Should return ok status and updated property entity when using a valid ID.")
    void testUpdatePropertiesWithValidID() throws Exception {
        Long propertyId = 1L;

        UpsertPropertyDTO upsertPropertyDto = new UpsertPropertyDTO("Casa", 1L);

        RoomDTO roomDto = new RoomDTO(1L, "Quarto", 2.0, 2.0);
        List<RoomDTO> roomDTOList = new ArrayList<>();

        roomDTOList.add(roomDto);

        DistrictDTO districtDTO = new DistrictDTO(1L, "Bairro", new BigDecimal(1000));

        String propertyUpdateJson = mapper.writeValueAsString(upsertPropertyDto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put("/properties/" + propertyId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(propertyUpdateJson);

        PropertyDTO propertyDTO = new PropertyDTO(
                propertyId,
                upsertPropertyDto.getName(),
                districtDTO,
                roomDTOList);

        when(propertyService.updateProperty(any(Long.class), any(UpsertPropertyDTO.class))).thenReturn(propertyDTO);

        mock.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(propertyId))
                .andExpect(jsonPath("$.name").value(upsertPropertyDto.getName()))
                .andExpect(jsonPath("$.district.id").value(districtDTO.getId()))
                .andExpect(jsonPath("$.district.name").value(districtDTO.getName()))
                .andExpect(jsonPath("$.district.squareMeterValue").value(districtDTO.getSquareMeterValue()))
                .andExpect(jsonPath("$.rooms[0].id").value(roomDto.getId()))
                .andExpect(jsonPath("$.rooms[0].name").value(roomDto.getName()))
                .andExpect(jsonPath("$.rooms[0].width").value(roomDto.getWidth()))
                .andExpect(jsonPath("$.rooms[0].length").value(roomDto.getLength()));

    }

    @Test
    @DisplayName("Should return Unprocessable Entity when updating a property with a non existent ID.")
    void testFailureCreationOfAProperty() throws Exception {
        Long propertyId = 1L;

        UpsertPropertyDTO upsertPropertyDto = new UpsertPropertyDTO("Casa", 1L);
        String propertyUpdateJson = mapper.writeValueAsString(upsertPropertyDto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put("/properties/" + propertyId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(propertyUpdateJson);

        when(propertyService.updateProperty(any(Long.class), any(UpsertPropertyDTO.class))).thenThrow(ResourceNotFoundException.class);

        mock.perform(request)
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("Should return validation messages when updating a property with an invalid request body.")
    void testValidationWhenUpdatingRoomWithInvalidBody() throws Exception {
        Long propertyId = 1L;

        UpsertPropertyDTO upsertPropertyDTO = new UpsertPropertyDTO("",0L);
        String propertyUpdateJson = mapper.writeValueAsString(upsertPropertyDTO);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put("/properties/" + propertyId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(propertyUpdateJson);

        mock.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("O nome do cômodo deve começar com uma letra maiúscula."));
    }

    @Test
    @DisplayName("Should return the List of all rooms of a property.")
    void testGetAllProperties() throws Exception {
        Long propertyId = 1L;

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/properties/" + propertyId + "/rooms")
                .accept(MediaType.APPLICATION_JSON);

        RoomDTO roomDto1 = new RoomDTO(1L, "Quarto", 2.0, 3.0 );
        RoomDTO roomDto2 = new RoomDTO(2L, "Sala", 5.0, 4.0 );

        when(propertyService.getPropertyRooms(propertyId)).thenReturn(List.of(roomDto1, roomDto2));

        mock.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Quarto"))
                .andExpect(jsonPath("$[0].width").value(2.0))
                .andExpect(jsonPath("$[0].length").value(3.0))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Sala"))
                .andExpect(jsonPath("$[1].width").value(5.0))
                .andExpect(jsonPath("$[1].length").value(4.0));
    }

}