package desafio_quality.services;

import desafio_quality.repositories.RoomRepository;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private PropertyService propertyService;

    public RoomService(RoomRepository roomRepository, PropertyService propertyService) {
        this.roomRepository = roomRepository;
        this.propertyService = propertyService;
    }
}
