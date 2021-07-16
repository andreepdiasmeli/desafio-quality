package desafio_quality.services;

import desafio_quality.dtos.CreateDistrictDTO;
import desafio_quality.dtos.DistrictDTO;
import desafio_quality.dtos.RoomDTO;
import desafio_quality.dtos.UpsertRoomDTO;
import desafio_quality.entities.District;
import desafio_quality.entities.Room;
import desafio_quality.dtos.DistrictDTO;
import desafio_quality.entities.District;
import desafio_quality.dtos.CreateDistrictDTO;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
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

    @Test
    @DisplayName("Should return a district when finding a with a valid id.")
    void testFindDistrictByIdWithAValidId(){
        Long districtId = 1L;

        District mockDistrict = new District("Costa e Silva", new BigDecimal("3000"));
        when(districtRepository.findById(any(Long.class))).thenReturn(Optional.of(mockDistrict));

        District district = districtService.findDistrictById(districtId);

        District expectedDistrict = new District(mockDistrict.getName(), mockDistrict.getSquareMeterValue());
        assertThat(expectedDistrict).usingRecursiveComparison().isEqualTo(district);
    }

    @Test
    @DisplayName("Should throw exception when finding a district with invalid id.")
    void testFindDistrictByIdWithAnInvalidId(){
        Long districtId = 1L;

        when(districtRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        Exception ex = assertThrows(ResourceNotFoundException.class, () -> {
            districtService.findDistrictById(districtId);
        });

        assertEquals("District " + districtId + " does not exist.", ex.getMessage());
    }

}