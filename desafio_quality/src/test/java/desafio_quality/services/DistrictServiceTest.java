package desafio_quality.services;

import desafio_quality.dtos.CreateDistrictDTO;
import desafio_quality.dtos.DistrictDTO;
import desafio_quality.entities.District;
import desafio_quality.repositories.DistrictRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class DistrictServiceTest {

    @Autowired
    private DistrictService districtService;

    @MockBean
    private DistrictRepository districtRepository;

    @Test
    @DisplayName("Should return a list of district dtos.")
    void testGetAllDistricts(){
        List<District> districts = List.of(
                new District("Bom Retiro", new BigDecimal("3000")),
                new District("Costa e Silva", new BigDecimal("2500")),
                new District("Saguaçu", new BigDecimal("2750"))
        );
        when(districtRepository.findAll()).thenReturn(districts);

        List<DistrictDTO> districtDTOS = districtService.getAllDistricts();

        List<DistrictDTO> expetedDistrictDTOS = List.of(
                new DistrictDTO(districts.get(0).getId(), districts.get(0).getName(), districts.get(0).getSquareMeterValue()),
                new DistrictDTO(districts.get(1).getId(), districts.get(1).getName(), districts.get(1).getSquareMeterValue()),
                new DistrictDTO(districts.get(2).getId(), districts.get(2).getName(), districts.get(2).getSquareMeterValue())
        );

        assertThat(districtDTOS).usingRecursiveComparison().ignoringFields("id").isEqualTo(expetedDistrictDTOS);
    }

    @Test
    @DisplayName("Should create a district and return its dto.")
    void testCreateDistrict(){
        CreateDistrictDTO createDistrictDTO = new CreateDistrictDTO("Costa e Silva", new BigDecimal("3000"));

        District district = new District("Costa e Silva", new BigDecimal("3000"));
        when(districtRepository.save(any(District.class))).thenReturn(district);

        DistrictDTO districtDTO = districtService.createDistrict(createDistrictDTO);

        DistrictDTO expectedDTO = new DistrictDTO(district.getId(), district.getName(), district.getSquareMeterValue());
        assertThat(districtDTO).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedDTO);
    }

}