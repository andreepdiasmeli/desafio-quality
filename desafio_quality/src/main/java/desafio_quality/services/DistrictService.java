package desafio_quality.services;

import desafio_quality.repositories.DistrictRepository;
import org.springframework.stereotype.Service;

@Service
public class DistrictService {

    private final DistrictRepository districtRepository;

    public DistrictService(DistrictRepository districtRepository) {
        this.districtRepository = districtRepository;
    }
}
