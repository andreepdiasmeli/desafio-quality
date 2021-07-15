package desafio_quality.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import desafio_quality.dtos.CreateDistrictDTO;
import desafio_quality.dtos.PropertyValueDTO;
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

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
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
}