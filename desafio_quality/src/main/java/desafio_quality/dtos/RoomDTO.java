package desafio_quality.dtos;

import desafio_quality.entities.Room;

public class RoomDTO {
    
    private Long id;
    private String name;
    private double width;
    private double length;


    public RoomDTO() {}

    public RoomDTO(
            Long id,
            String name, 
            double width, 
            double length) {
        this.id = id;
        this.name = name;
        this.width = width;
        this.length = length;
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

    public static RoomDTO toDTO(Room room) {
        return new RoomDTO(
            room.getId(),
            room.getName(),
            room.getWidth(),
            room.getLength()
        );
    }
}
