package desafio_quality.services;

import desafio_quality.entities.District;
import desafio_quality.entities.Property;
import desafio_quality.entities.Room;
import desafio_quality.repositories.DistrictRepository;
import desafio_quality.repositories.PropertyRepository;
import desafio_quality.repositories.RoomRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DBService {

    private final DistrictRepository districtRepository;
    private final PropertyRepository propertyRepository;
    private final RoomRepository roomRepository;

    public DBService(DistrictRepository districtRepository, PropertyRepository propertyRepository, RoomRepository roomRepository) {
        this.districtRepository = districtRepository;
        this.propertyRepository = propertyRepository;
        this.roomRepository = roomRepository;
    }

    public void instantiateDB(){

        District d1 = new District("Bela Vista", new BigDecimal("8537"));
        District d2 = new District("Pinheiros", new BigDecimal("10900"));
        District d3 = new District("Itacorubi", new BigDecimal("7411"));

        districtRepository.saveAll(List.of(d1, d2, d3));

        Property p1 = new Property("Bem Viver", d1);
        Property p2 = new Property("Vila Toscana", d2);
        Property p3 = new Property("Jardim Imperiale", d3);
        Property p4 = new Property("Bela Vista", d3);

        propertyRepository.saveAll(List.of(p1, p2, p3, p4));

        d1.setProperties(List.of((p1)));
        d2.setProperties(List.of((p2)));
        d3.setProperties(List.of(p3, p4));

        districtRepository.saveAll(List.of(d1, d2, d3));

        Room r1 = new Room("Quarto", 10.0, 5.0, p1);
        Room r2 = new Room("Cozinha", 15.0, 8.0, p1);

        Room r3 = new Room("Banheiro", 2.0, 3.0, p2);
        Room r4 = new Room("Sala de Estar", 10.0, 5.0, p2);

        Room r5 = new Room("Sala de Jantar", 12.0, 3.0, p3);
        Room r6 = new Room("Porão", 15.0, 5.0, p3);

        roomRepository.saveAll(List.of(r1, r2, r3, r4, r5, r6));

        p1.setRooms(List.of(r1, r2));
        p2.setRooms(List.of(r3, r4));
        p3.setRooms(List.of(r5, r6));

        propertyRepository.saveAll(List.of(p1, p2, p3));
    }

    public void knockDownDB() {
        propertyRepository.deleteAll();
        districtRepository.deleteAll();
    }
}
