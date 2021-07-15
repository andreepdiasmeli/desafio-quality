package desafio_quality.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class District {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal squareMeterValue;

    @OneToMany(mappedBy = "district")
    private List<Property> properties = new ArrayList<>();

    public District() {
    }

    public District(String name, BigDecimal squareMeterValue) {
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

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }
}
