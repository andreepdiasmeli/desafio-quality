package desafio_quality.entities;

import javax.persistence.*;

@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double width;
    private double length;

    @ManyToOne
    @JoinColumn
    private Property property;

    
    public Room() {}

    public Room(
            String name, 
            double width, 
            double length, 
            Property property) {
        this.name = name;
        this.width = width;
        this.length = length;
        this.property = property;
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWidth() {
        return this.width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getLength() {
        return this.length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public Property getProperty() {
        return this.property;
    }

    public double getArea(){
        return this.length * this.width;
    }
}
