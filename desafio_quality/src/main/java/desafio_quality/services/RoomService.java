package desafio_quality.services;

import desafio_quality.dtos.RoomDTO;
import desafio_quality.dtos.UpsertRoomDTO;
import desafio_quality.entities.Property;
import desafio_quality.entities.Room;
import desafio_quality.exceptions.ResourceNotFoundException;
import desafio_quality.repositories.RoomRepository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final PropertyService propertyService;

    
    public RoomService(
            RoomRepository roomRepository,
            PropertyService propertyService) {
        this.roomRepository = roomRepository;
        this.propertyService = propertyService;
    }

    public List<RoomDTO> getAllRooms() {
        List<Room> rooms = this.roomRepository.findAll();
        return rooms.stream().map(RoomDTO::toDTO).collect(Collectors.toList());
    }

    public Room findById(Long roomId) throws ResourceNotFoundException {
        return this.roomRepository.findById(roomId).orElseThrow(() ->
                new ResourceNotFoundException("Room " + roomId + " does not exist.")
        );
    }

    public RoomDTO getRoomById(Long roomId){
        Room room = findById(roomId);
        return RoomDTO.toDTO(room);
    }

    public RoomDTO createRoom(
            Long propertyId, 
            UpsertRoomDTO upsertRoomDto) throws ResourceNotFoundException {
        Property property = this.propertyService.findPropertyById(propertyId);

        Room room = new Room(
            upsertRoomDto.getName(),
            upsertRoomDto.getWidth(),
            upsertRoomDto.getLength(),
            property
        );

        Room savedRoom = this.roomRepository.save(room);
        return RoomDTO.toDTO(savedRoom);
    }

    public RoomDTO updateRoom(
            Long roomId, 
            UpsertRoomDTO upsertRoomDto) throws ResourceNotFoundException {
        Room room = findById(roomId);

        room.setName(upsertRoomDto.getName());
        room.setLength(upsertRoomDto.getLength());
        room.setWidth(upsertRoomDto.getWidth());
        
        Room updatedRoom = this.roomRepository.save(room);
        return RoomDTO.toDTO(updatedRoom);
    }

    public void deleteRoom(Long roomId) throws ResourceNotFoundException {
        findById(roomId);
        this.roomRepository.deleteById(roomId);
    }
}
