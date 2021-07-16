package desafio_quality.services;

import desafio_quality.dtos.DistrictDTO;
import desafio_quality.entities.District;
import desafio_quality.exceptions.ResourceNotFoundException;
import desafio_quality.repositories.DistrictRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class DistrictServiceTest {

    @Autowired
    private DistrictService districtService;

    @MockBean
    private DistrictRepository districtRepository;

    @Test
    @DisplayName("Should delete a district with a specific ID.")
    void testDeleteDistrictWithValidID() {
        Long districtId = 1L;

        when(districtRepository.findById(any(Long.class))).thenReturn(Optional.of(new District("", new BigDecimal("20000"))));
        doNothing().when(districtRepository).deleteById(any(Long.class));

        assertDoesNotThrow(() -> districtService.deleteDistrict(districtId));
    }

    @Test
    @DisplayName("Should not delete a district with an invalid ID.")
    void testDeleteDistrictWithInvalidID() {
        Long districtId = 2L;

        when(districtRepository.findById(any(Long.class))).thenThrow(ResourceNotFoundException.class);
        doNothing().when(districtRepository).deleteById(any(Long.class));

        assertThrows(ResourceNotFoundException.class, 
            () -> districtService.deleteDistrict(districtId));
    }

    @Test
    @DisplayName("Should get a district with a valid ID.")
    void testGetDistrictWithValidId() {
        Long districtId = 1L;

        District district = new District("", new BigDecimal("20000"));
        when(districtRepository.findById(any(Long.class))).thenReturn(Optional.of(district));

        DistrictDTO actualDistrict = districtService.getDistrictById(districtId);

        DistrictDTO expectedDistrict = 
            new DistrictDTO(districtId, district.getName(), district.getSquareMeterValue());
        
        assertThat(actualDistrict)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(expectedDistrict);
    }

    @Test
    @DisplayName("Should return exception when getting a district with an invalid ID.")
    void testGetDistrictWithInvalidId() {
        Long districtId = 2L;

        when(districtRepository.findById(any(Long.class))).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, 
            () -> districtService.getDistrictById(districtId));
    }
}