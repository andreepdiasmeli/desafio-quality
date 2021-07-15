package desafio_quality.dtos;

import desafio_quality.entities.District;
import desafio_quality.entities.Property;
import desafio_quality.entities.Room;

import java.util.List;
import java.util.stream.Collectors;

public class PropertyRoomsAreaDTO {

    private Long id;
    private String name;
    private List<RoomAreaDTO> rooms;

    public PropertyRoomsAreaDTO() {
    }

    public PropertyRoomsAreaDTO(Long id, String name, List<RoomAreaDTO> rooms) {
        this.id = id;
        this.name = name;
        this.rooms = rooms;
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

    public List<RoomAreaDTO> getRooms() {
        return rooms;
    }

    public void setRooms(List<RoomAreaDTO> rooms) {
        this.rooms = rooms;
    }

    public static PropertyRoomsAreaDTO toDTO(Property property) {
        List<RoomAreaDTO> roomAreaDTOList = property.getRooms()
                .stream()
                .map(RoomAreaDTO::toDTO)
                .collect(Collectors.toList());

        return new PropertyRoomsAreaDTO(property.getId(), property.getName(), roomAreaDTOList);
    }
}
