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

import java.math.BigDecimal;
import java.util.Arrays;
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
    @DisplayName("Should return a property by id.")
    void testGetOfAPropertyById() throws Exception {
        Long propertyId = 1L;
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/properties/" + propertyId)
                .accept(MediaType.APPLICATION_JSON);

        DistrictDTO districtDTO = new DistrictDTO();

        List<RoomDTO> rooms = Arrays.asList(
                new RoomDTO(1L, "Sala", 20.0, 18.0)
        );

        PropertyDTO propertyDTO = new PropertyDTO(propertyId,"propriedade", districtDTO, rooms);

        when(propertyService.getPropertyById(any(Long.class))).thenReturn(propertyDTO);

        mock.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(propertyId));
    }

    @Test
    @DisplayName("Should return Unprocessable Entity when updating a property with a non existent ID.")
    void testFailureGetOfAProperty() throws Exception {
        Long propertyId = 2L;

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/properties/" + propertyId)
                .accept(MediaType.APPLICATION_JSON);

        DistrictDTO districtDTO = new DistrictDTO();

        List<RoomDTO> rooms = Arrays.asList(
                new RoomDTO(1L, "Sala", 20.0, 18.0)
        );

        PropertyDTO propertyDTO = new PropertyDTO(propertyId,"propriedade", districtDTO, rooms);

        when(propertyService.getPropertyById(any(Long.class))).thenThrow(ResourceNotFoundException.class);

        mock.perform(request)
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("Should return total property area by id.")
    void testGetOfTotalAreaPropertyById() throws Exception {
        Long propertyId = 1L;

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/properties/" + propertyId + "/roomsArea")
                .accept(MediaType.APPLICATION_JSON);

        List<RoomAreaDTO> rooms = Arrays.asList(
                new RoomAreaDTO(1L, "Quartos", 200.0)
        );

        PropertyRoomsAreaDTO propertyRoomsAreaDTO = new PropertyRoomsAreaDTO(1L,"Quarto", rooms);

        when(propertyService.getRoomsArea(any(Long.class))).thenReturn(propertyRoomsAreaDTO);

        mock.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(propertyId));
    }


    @Test
    @DisplayName("Should create a property.")
    void testCreationOfProperty() throws Exception {

      UpsertPropertyDTO upsertPropertyDTO = new UpsertPropertyDTO("Propriedade", 1L);


        String propertyJSON = mapper.writeValueAsString(upsertPropertyDTO);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/properties")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(propertyJSON);

        DistrictDTO districtDTO = new DistrictDTO(1L,"Bairro",new BigDecimal(2000.00));

        List<RoomDTO> rooms = Arrays.asList(
                new RoomDTO(1L, "Sala", 20.0, 18.0)
        );

        PropertyDTO propertyDTO = new PropertyDTO(1L,"Propriedade", districtDTO, rooms);

        when(propertyService.createProperty(any(UpsertPropertyDTO.class))).thenReturn(propertyDTO);

        mock.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Propriedade"))
                .andExpect(jsonPath("$.district.name").value("Bairro"))
                .andExpect(jsonPath("$.rooms", hasSize(1)));
    }

}