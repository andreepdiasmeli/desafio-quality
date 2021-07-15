package desafio_quality.dtos;

import desafio_quality.entities.Property;

import java.util.List;
import java.util.stream.Collectors;

public class PropertyDTO {
    private Long id;
    private String name;
    private DistrictDTO district;
    private List<RoomDTO> rooms;

    public PropertyDTO(Long id, String name, DistrictDTO district, List<RoomDTO> rooms) {
        this.id = id;
        this.name = name;
        this.district = district;
        this.rooms = rooms;
    }

    public static PropertyDTO toDTO(Property property) {
        List<RoomDTO> rooms = property.getRooms()
                                      .stream()
                                      .map(RoomDTO::toDTO)
                                      .collect(Collectors.toList());
        return new PropertyDTO( property.getId(),
                                property.getName(),
                                DistrictDTO.toDTO(property.getDistrict()),
                                rooms
                                );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public DistrictDTO getDistrict() {
        return district;
    }

    public List<RoomDTO> getRooms() {
        return rooms;
    }
}
