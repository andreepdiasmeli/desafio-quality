package desafio_quality.dtos;

import desafio_quality.entities.District;

import java.math.BigDecimal;

public class DistrictDTO {

    private Long id;
    private String name;
    private BigDecimal squareMeterValue;

    public DistrictDTO(){}

    public DistrictDTO(Long id, String name, BigDecimal squareMeterValue) {
        this.id = id;
        this.name = name;
        this.squareMeterValue = squareMeterValue;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getSquareMeterValue() {
        return squareMeterValue;
    }

    public static DistrictDTO toDTO(District district){
        return new DistrictDTO(
                district.getId(),
                district.getName(),
                district.getSquareMeterValue()
                );
    }
}
