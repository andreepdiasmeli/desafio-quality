package desafio_quality.services;

import desafio_quality.entities.District;
import desafio_quality.exceptions.ResourceNotFoundException;
import desafio_quality.repositories.DistrictRepository;
import org.springframework.stereotype.Service;

@Service
public class DistrictService {

    private final DistrictRepository districtRepository;

    public DistrictService(DistrictRepository districtRepository) {
        this.districtRepository = districtRepository;
    }

    public District findDiscrict(Long districtId) {
        return this.districtRepository.findById(districtId).orElseThrow(() ->
                new ResourceNotFoundException("Resource with ID " + districtId + " does not exist."));
    }
}
