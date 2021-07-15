package desafio_quality.dtos;
import desafio_quality.entities.Room;

public class RoomAreaDTO {
    private Long id;
    private String name;
    private double area;

    public RoomAreaDTO() {
    }

    public RoomAreaDTO(Long id, String name, double area) {
        this.id = id;
        this.name = name;
        this.area = area;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public static RoomAreaDTO toDTO(Room room) {
        return new RoomAreaDTO(
                room.getId(),
                room.getName(),
                room.getArea()
        );
    }
}