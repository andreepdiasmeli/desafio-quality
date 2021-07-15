package desafio_quality.dtos;

import desafio_quality.entities.Property;

public class PropertyAreaDTO {

    private double totalArea;

    public PropertyAreaDTO() {
    }

    public PropertyAreaDTO(double totalArea) {
        this.totalArea = totalArea;
    }

    public static PropertyAreaDTO toDTO(Property property) {
        return new PropertyAreaDTO(property.getTotalArea());
    }

    public double getTotalArea() {
        return totalArea;
    }
}
