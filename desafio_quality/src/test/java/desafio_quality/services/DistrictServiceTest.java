package desafio_quality.services;

import desafio_quality.dtos.CreateDistrictDTO;
import desafio_quality.dtos.DistrictDTO;
import desafio_quality.dtos.RoomDTO;
import desafio_quality.dtos.UpsertRoomDTO;
import desafio_quality.entities.District;
import desafio_quality.entities.Room;
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
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class DistrictServiceTest {

    @Autowired
    private DistrictService districtService;

    @MockBean
    private DistrictRepository districtRepository;

    @Test
    @DisplayName("Should update a district with a specific ID.")
    void testUpdateDistrictWithValidId() {
        Long districtID = 1L;

        District district = new District("Bom Retiro", new BigDecimal("2000"));

        when(districtRepository.findById(any(Long.class))).thenReturn(Optional.of(district));
        when(districtRepository.save(any(District.class))).thenReturn(district);

        CreateDistrictDTO createDistrictDTORoomDTO = new CreateDistrictDTO(district.getName(), district.getSquareMeterValue());

        DistrictDTO actualDistrict = districtService.updateDistrict(districtID, createDistrictDTORoomDTO);
        DistrictDTO expectedDistrict = new DistrictDTO(district.getId(), district.getName(), district.getSquareMeterValue());

        assertThat(actualDistrict).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedDistrict);
    }
}
