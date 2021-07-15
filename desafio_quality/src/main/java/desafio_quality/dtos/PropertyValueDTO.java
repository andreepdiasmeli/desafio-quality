package desafio_quality.dtos;

import desafio_quality.entities.Property;

import java.math.BigDecimal;

public class PropertyValueDTO {

    private BigDecimal value;

    public PropertyValueDTO() {
    }

    public PropertyValueDTO(BigDecimal value) {
        this.value = value;
    }

    public static PropertyValueDTO toDTO(Property property) {
        BigDecimal totalArea = new BigDecimal(property.getTotalArea());
        BigDecimal districtSquareMeterValue = property.getDistrict().getSquareMeterValue();
        BigDecimal value = totalArea.multiply(districtSquareMeterValue);

        return new PropertyValueDTO(value);
    }

    public BigDecimal getValue() {
        return value;
    }
}
