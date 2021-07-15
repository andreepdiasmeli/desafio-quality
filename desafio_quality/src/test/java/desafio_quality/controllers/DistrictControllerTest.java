package desafio_quality.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import desafio_quality.dtos.CreateDistrictDTO;
import desafio_quality.dtos.DistrictDTO;
import desafio_quality.dtos.PropertyValueDTO;
import desafio_quality.services.DistrictService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    @DisplayName("Should return the value of a property from a valid id.")
    void testCreationOfADistrict() throws Exception {
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
    void testFailureCreationOfADistrict() throws Exception {
        CreateDistrictDTO district = new CreateDistrictDTO("", new BigDecimal("123456789123456789"));
        String districtJSON = mapper.writeValueAsString(district);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/districts")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(districtJSON);

        mock.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("O nome do bairro deve começar com uma letra maiúscula."))
                .andExpect(jsonPath("$.squareMeterValue").value("O valor de metros quadrados não deve exceder 13 digitos."));
    }

}