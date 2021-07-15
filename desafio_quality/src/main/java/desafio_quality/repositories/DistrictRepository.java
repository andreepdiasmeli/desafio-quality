package desafio_quality.repositories;


import desafio_quality.entities.District;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DistrictRepository extends JpaRepository<District, Long> {
}
