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

    public Room() {
    }

    public Room(String name, double width, double length, Property property) {
        this.name = name;
        this.width = width;
        this.length = length;
        this.property = property;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getWidth() {
        return width;
    }

    public double getLength() {
        return length;
    }

    public Property getProperty() {
        return property;
    }

    public double getArea(){
        return length * width;
    }
}
