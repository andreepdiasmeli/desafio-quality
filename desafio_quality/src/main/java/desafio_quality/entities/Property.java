package desafio_quality.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn
    private District district;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL)
    private List<Room> rooms = new ArrayList<>();

    public Property() {
    }

    public Property(String name, District district) {
        this.name = name;
        this.district = district;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public District getDistrict() {
        return district;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public double getTotalArea(){
        return rooms.stream().map(Room::getArea).reduce(0.0, (a, b) -> a + b);
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }
}
