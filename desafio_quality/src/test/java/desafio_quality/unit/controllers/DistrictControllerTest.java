package desafio_quality.unit.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

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

import desafio_quality.controllers.DistrictController;
import desafio_quality.dtos.CreateDistrictDTO;
import desafio_quality.dtos.DistrictDTO;
import desafio_quality.exceptions.ResourceNotFoundException;
import desafio_quality.services.DistrictService;

@WebMvcTest(controllers = DistrictController.class)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class DistrictControllerTest {

    @Autowired
    private MockMvc mock;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private DistrictService districtService;

    @Test
    @DisplayName("Should return the value of a district from a valid id.")
    void testPostADistrict() throws Exception {
        CreateDistrictDTO district = new CreateDistrictDTO("Costa e Silva", new BigDecimal("1000"));
        String districtJSON = mapper.writeValueAsString(district);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .post("/districts")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(districtJSON);

        DistrictDTO districtDTO = new DistrictDTO(1L, "Costa e Silva", new BigDecimal("1000"));
        when(districtService.createDistrict(any(CreateDistrictDTO.class))).thenReturn(districtDTO);

        mock.perform(request)
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Costa e Silva"))
            .andExpect(jsonPath("$.squareMeterValue").value("1000"));
    }

    @Test
    @DisplayName("Should not create a district with incorrect data.")
    void testPostAnInvalidDistrict() throws Exception {
        CreateDistrictDTO district = new CreateDistrictDTO("", new BigDecimal("123456789123456789"));
        String districtJSON = mapper.writeValueAsString(district);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .post("/districts")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(districtJSON);

        mock.perform(request)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.name.[0].error").value("O nome do bairro não pode estar vazio."))
            .andExpect(jsonPath("$.squareMeterValue.[0].error").value("O valor de metros quadrados não deve exceder 13 digitos."));
    }

    @Test
    @DisplayName("Should update a district.")
    void testUpdateOfDistrict() throws Exception {

        Long districtId = 1L;

        CreateDistrictDTO createDistrictDTO = new CreateDistrictDTO("District", new BigDecimal(2000));
        String districtJSON = mapper.writeValueAsString(createDistrictDTO);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put("/districts/"+ districtId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(districtJSON);

        DistrictDTO districtDTO = new DistrictDTO(
                districtId,
                createDistrictDTO.getName(),
                createDistrictDTO.getSquareMeterValue());

        when(districtService.updateDistrict(any(Long.class), any(CreateDistrictDTO.class))).thenReturn(districtDTO);

        mock.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(districtId))
                .andExpect(jsonPath("$.name").value(createDistrictDTO.getName()))
                .andExpect(jsonPath("$.squareMeterValue").value(createDistrictDTO.getSquareMeterValue()));
    }

    @Test
    @DisplayName("Should return Unprocessable Entity when updating a district with a non existent ID.")
    void testFailureUpdateOfDistrict() throws Exception {

        Long districtId = 2L;

        CreateDistrictDTO createDistrictDTO = new CreateDistrictDTO("District", new BigDecimal(2000));
        String districtJSON = mapper.writeValueAsString(createDistrictDTO);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put("/districts/"+ districtId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(districtJSON);

        when(districtService.updateDistrict(any(Long.class), any(CreateDistrictDTO.class))).thenThrow(ResourceNotFoundException.class);

        mock.perform(request)
                .andExpect(status().isUnprocessableEntity());
    }


    @Test
    @DisplayName("Should return the specific district by ID.")
    void testGetDistrictById() throws Exception {
        Long districtId = 1L;

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .get("/districts/" + districtId)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

        DistrictDTO districtDTO = new DistrictDTO(1L, "Costa e Silva", new BigDecimal("1000"));
        when(districtService.getDistrictById(any(Long.class))).thenReturn(districtDTO);

        mock.perform(request)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.name").value("Costa e Silva"))
            .andExpect(jsonPath("$.squareMeterValue").value("1000"));
    }

    @Test
    @DisplayName("Should return Unprocessable Entity when searching a district with a non existent ID.")
    void testGetDistrictByInvalidId() throws Exception {
        Long districtId = 2L;

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .get("/districts/" + districtId)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

        when(districtService.getDistrictById(any(Long.class))).thenThrow(ResourceNotFoundException.class);

        mock.perform(request)
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("Should return status code No Content when deleting a valid district ID.")
    void testDeleteDistrictReturnsNoContent() throws Exception {
        Long districtId = 1L;

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .delete("/districts/" + districtId);

        doNothing().when(districtService).deleteDistrict(any(Long.class));

        mock.perform(request)
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should return status code Unprocessable Entity when deleting a non existent district ID.")
    void testDeleteInvalidDistrictReturnsUnprocessableEntity() throws Exception {
        Long districtId = 2L;

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .delete("/districts/" + districtId);

        doThrow(ResourceNotFoundException.class)
            .when(districtService)
            .deleteDistrict(any(Long.class));

        mock.perform(request)
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("Should return a district from a valid id.")
    void testGetADistrict() throws Exception {
        Long propertyId = 1L;
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/districts/" + propertyId)
                .accept(MediaType.APPLICATION_JSON);

        DistrictDTO districtDTO = new DistrictDTO(1L, "Costa e Silva", new BigDecimal("200000.0"));
        when(districtService.getDistrictById(any(Long.class))).thenReturn(districtDTO);

        mock.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Costa e Silva"))
                .andExpect(jsonPath("$.squareMeterValue").value("200000.0"));
    }

    @Test
    @DisplayName("Should return UnprocessableEntity when getting a district from an invalid id.")
    void testGetAnInvalidDistrict() throws Exception {
        Long propertyId = 10L;
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/districts/" + propertyId)
                .accept(MediaType.APPLICATION_JSON);

        when(districtService.getDistrictById(any(Long.class))).thenThrow(ResourceNotFoundException.class);

        mock.perform(request)
                .andExpect(status().isUnprocessableEntity());
    }
}