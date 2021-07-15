package desafio_quality.services;

import desafio_quality.dtos.RoomDTO;
import desafio_quality.dtos.UpsertRoomDTO;
import desafio_quality.entities.Property;
import desafio_quality.entities.Room;
import desafio_quality.exceptions.ResourceNotFoundException;
import desafio_quality.repositories.RoomRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final PropertyService propertyService;
    private static final Integer defaultPageSize = 5;

    public RoomService(RoomRepository roomRepository, PropertyService propertyService) {
        this.roomRepository = roomRepository;
        this.propertyService = propertyService;
    }

    public Page<RoomDTO> getAllRooms(Integer pageNumber, Integer pageSize) {
        pageNumber = pageNumber != null ? pageNumber : 0;
        pageSize = pageSize != null ? pageSize : defaultPageSize;

        Pageable paging = PageRequest.of(pageNumber, pageSize);
        Page<Room> paginatedProducts = this.roomRepository.findAll(paging);

        return paginatedProducts.map(RoomDTO::toDTO);
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
