package desafio_quality.dtos;

import desafio_quality.entities.Room;

public class RoomDTO {
    private Long id;
    private String name;
    private Double width;
    private Double length;

    public RoomDTO() {
    }

    public RoomDTO(Long id, String name, Double width, Double length) {
        this.id = id;
        this.name = name;
        this.width = width;
        this.length = length;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getWidth() {
        return width;
    }

    public Double getLength() {
        return length;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public static RoomDTO toDTO(Room room) {
        return new RoomDTO(
            room.getId(),
            room.getName(),
            room.getWidth(),
            room.getLength()
        );
    }
}
