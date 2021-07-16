package desafio_quality.services;

import desafio_quality.dtos.CreateDistrictDTO;
import desafio_quality.dtos.DistrictDTO;
import desafio_quality.entities.District;
import desafio_quality.exceptions.ResourceNotFoundException;
import desafio_quality.repositories.DistrictRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DistrictService {

    private final DistrictRepository districtRepository;

    public DistrictService(DistrictRepository districtRepository) {
        this.districtRepository = districtRepository;
    }

    public District findDiscrict(Long districtId) {
        return this.districtRepository.findById(districtId).orElseThrow(() ->
                new ResourceNotFoundException("District with ID " + districtId + " does not exist."));
    }

    public District findDistrictById(Long districtId){
        return this.districtRepository.findById(districtId).orElseThrow(() ->
                new ResourceNotFoundException("District "+ districtId+ " does not exist.")
        );
    }

    public List<DistrictDTO> getAllDistricts(){
        List<District> district = this.districtRepository.findAll();
        return district.stream().map(DistrictDTO::toDTO).collect(Collectors.toList());
    }

    public DistrictDTO getDistrictById(Long districtID){
        District district = this.findDistrictById(districtID);
        return DistrictDTO.toDTO(district);
    }

    public DistrictDTO createDistrict(CreateDistrictDTO createDistrict){
        District district = new District(createDistrict.getName(), createDistrict.getSquareMeterValue());

        district = this.districtRepository.save(district);
        return DistrictDTO.toDTO(district);
    }

    public DistrictDTO updateDistrict(Long districtId, CreateDistrictDTO createDistrictDTO){
        District district = findDistrictById(districtId);

        district.setName(createDistrictDTO.getName());
        district.setSquareMeterValue(createDistrictDTO.getSquareMeterValue());

        district = this.districtRepository.save(district);
        return DistrictDTO.toDTO(district);
    }

    public void deleteDistrict(Long districtId){
        findDistrictById(districtId);
        this.districtRepository.deleteById(districtId);
    }

}
