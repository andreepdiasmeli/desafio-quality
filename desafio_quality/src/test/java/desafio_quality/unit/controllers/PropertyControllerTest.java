package desafio_quality.unit.services.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import desafio_quality.controllers.PropertyController;
import desafio_quality.dtos.*;
import desafio_quality.exceptions.PropertyHasNoRoomsException;
import desafio_quality.exceptions.ResourceNotFoundException;
import desafio_quality.services.PropertyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PropertyController.class)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class PropertyControllerTest {

    @Autowired
    private MockMvc mock;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private PropertyService propertyService;

    @Test
    @DisplayName("Should return the value of a property from a valid id.")
    void testValueOfAProperty() throws Exception {
        Long propertyId = 1L;
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/properties/" + propertyId + "/value")
                .accept(MediaType.APPLICATION_JSON);

        PropertyValueDTO valueDTO = new PropertyValueDTO(new BigDecimal("200000.0"));
        when(propertyService.getValue(any(Long.class))).thenReturn(valueDTO);

        mock.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value").value("200000.0"));
    }

    @Test
    @DisplayName("Should return UNPROCESSABLE_ENTITY when getting the value of a non existent property.")
    void testValueOfAPropertyNonExistent() throws Exception {
        Long propertyId = 10L;
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/properties/" + propertyId + "/value")
                .accept(MediaType.APPLICATION_JSON);

        when(propertyService.getValue(any(Long.class))).thenThrow(ResourceNotFoundException.class);

        mock.perform(request)
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("Should return status code No Content when deleting a valid property ID.")
    void testDeletePropertyReturnsNoContent() throws Exception {
        Long propertyId = 1L;

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete("/properties/" + propertyId);

        doNothing().when(propertyService).deleteProperty(any(Long.class));

        mock.perform(request)
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should return status code Unprocessable Entity when deleting a non existent property ID.")
    void testDeleteInvalidPropertyReturnsUnprocessableEntity() throws Exception {
        Long propertyId = 2L;

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete("/properties/" + propertyId);

        doThrow(ResourceNotFoundException.class)
                .when(propertyService)
                .deleteProperty(any(Long.class));

        mock.perform(request)
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("Should return ok status and updated property entity when using a valid ID.")
    void testUpdatePropertiesWithValidID() throws Exception {
        Long propertyId = 1L;

        UpsertPropertyDTO upsertPropertyDto = new UpsertPropertyDTO("Casa", 1L);

        RoomDTO roomDto = new RoomDTO(1L, "Quarto", 2.0, 2.0);
        List<RoomDTO> roomDTOList = new ArrayList<>();

        roomDTOList.add(roomDto);

        DistrictDTO districtDTO = new DistrictDTO(1L, "Bairro", new BigDecimal(1000));

        String propertyUpdateJson = mapper.writeValueAsString(upsertPropertyDto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put("/properties/" + propertyId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(propertyUpdateJson);

        PropertyDTO propertyDTO = new PropertyDTO(
                propertyId,
                upsertPropertyDto.getName(),
                districtDTO,
                roomDTOList);

        when(propertyService.updateProperty(any(Long.class), any(UpsertPropertyDTO.class))).thenReturn(propertyDTO);

        mock.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(propertyId))
                .andExpect(jsonPath("$.name").value(upsertPropertyDto.getName()))
                .andExpect(jsonPath("$.district.id").value(districtDTO.getId()))
                .andExpect(jsonPath("$.district.name").value(districtDTO.getName()))
                .andExpect(jsonPath("$.district.squareMeterValue").value(districtDTO.getSquareMeterValue()))
                .andExpect(jsonPath("$.rooms[0].id").value(roomDto.getId()))
                .andExpect(jsonPath("$.rooms[0].name").value(roomDto.getName()))
                .andExpect(jsonPath("$.rooms[0].width").value(roomDto.getWidth()))
                .andExpect(jsonPath("$.rooms[0].length").value(roomDto.getLength()));

    }

    @Test
    @DisplayName("Should return Unprocessable Entity when updating a property with a non existent ID.")
    void testFailureCreationOfAProperty() throws Exception {
        Long propertyId = 1L;

        UpsertPropertyDTO upsertPropertyDto = new UpsertPropertyDTO("Casa", 1L);
        String propertyUpdateJson = mapper.writeValueAsString(upsertPropertyDto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put("/properties/" + propertyId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(propertyUpdateJson);

        when(propertyService.updateProperty(any(Long.class), any(UpsertPropertyDTO.class))).thenThrow(ResourceNotFoundException.class);

        mock.perform(request)
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("Should return validation messages when updating a property with an invalid request body.")
    void testValidationWhenUpdatingRoomWithInvalidBody() throws Exception {
        Long propertyId = 1L;

        UpsertPropertyDTO upsertPropertyDTO = new UpsertPropertyDTO(null,0L);
        String propertyUpdateJson = mapper.writeValueAsString(upsertPropertyDTO);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put("/properties/" + propertyId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(propertyUpdateJson);

        mock.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return the List of all rooms of a property.")
    void testGetAllRoomsOfAProperty() throws Exception {
        Long propertyId = 1L;

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/properties/" + propertyId + "/rooms")
                .accept(MediaType.APPLICATION_JSON);

        RoomDTO roomDto1 = new RoomDTO(1L, "Quarto", 2.0, 3.0 );
        RoomDTO roomDto2 = new RoomDTO(2L, "Sala", 5.0, 4.0 );

        when(propertyService.getPropertyRooms(propertyId)).thenReturn(List.of(roomDto1, roomDto2));

        mock.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Quarto"))
                .andExpect(jsonPath("$[0].width").value(2.0))
                .andExpect(jsonPath("$[0].length").value(3.0))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Sala"))
                .andExpect(jsonPath("$[1].width").value(5.0))
                .andExpect(jsonPath("$[1].length").value(4.0));
    }


    @Test
    @DisplayName("Should return a list of the areas of the rooms.")
    void testRoomAreas() throws Exception {
        Long propertyId = 1L;
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .get("/properties/" + propertyId + "/roomsArea")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

        List<RoomAreaDTO> roomAreas = new ArrayList<>();
        roomAreas.add(new RoomAreaDTO(1L, "Cozinha", 15.0));
        roomAreas.add(new RoomAreaDTO(2L, "Quarto", 5.0));

        PropertyRoomsAreaDTO propertyRoomsAreaDTO = new PropertyRoomsAreaDTO(
            1L, "Propriedade 1", roomAreas
        );

        when(propertyService.getRoomsArea(any(Long.class))).thenReturn(propertyRoomsAreaDTO);

        mock.perform(request)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(propertyId))
            .andExpect(jsonPath("$.name").value("Propriedade 1"))
            .andExpect(jsonPath("$.rooms", hasSize(2)));
    }

    @Test
    @DisplayName("Should return Unprocessable Entity when trying to list the areas of the rooms with an invalid property ID.")
    void testRoomAreasWithInvalidPropertyId() throws Exception {
        Long propertyId = 2L;
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .get("/properties/" + propertyId + "/roomsArea")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

        when(propertyService.getRoomsArea(any(Long.class))).thenThrow(ResourceNotFoundException.class);

        mock.perform(request)
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("Should return a property by id.")
    void testGetOfAPropertyById() throws Exception {
        Long propertyId = 1L;
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/properties/" + propertyId)
                .accept(MediaType.APPLICATION_JSON);

        DistrictDTO districtDTO = new DistrictDTO();

        List<RoomDTO> rooms = Arrays.asList(
                new RoomDTO(1L, "Sala", 20.0, 18.0)
        );

        PropertyDTO propertyDTO = new PropertyDTO(propertyId,"propriedade", districtDTO, rooms);

        when(propertyService.getPropertyById(any(Long.class))).thenReturn(propertyDTO);

        mock.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(propertyId));
    }

    @Test
    @DisplayName("Should return Unprocessable Entity when updating a property with a non existent ID.")
    void testFailureGetOfAProperty() throws Exception {
        Long propertyId = 2L;

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/properties/" + propertyId)
                .accept(MediaType.APPLICATION_JSON);

        DistrictDTO districtDTO = new DistrictDTO();

        List<RoomDTO> rooms = Arrays.asList(
                new RoomDTO(1L, "Sala", 20.0, 18.0)
        );

        PropertyDTO propertyDTO = new PropertyDTO(propertyId,"propriedade", districtDTO, rooms);

        when(propertyService.getPropertyById(any(Long.class))).thenThrow(ResourceNotFoundException.class);

        mock.perform(request)
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("Should return total property area by id.")
    void testGetOfTotalAreaPropertyById() throws Exception {
        Long propertyId = 1L;

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/properties/" + propertyId + "/roomsArea")
                .accept(MediaType.APPLICATION_JSON);

        List<RoomAreaDTO> rooms = Arrays.asList(
                new RoomAreaDTO(1L, "Quartos", 200.0)
        );

        PropertyRoomsAreaDTO propertyRoomsAreaDTO = new PropertyRoomsAreaDTO(1L,"Quarto", rooms);

        when(propertyService.getRoomsArea(any(Long.class))).thenReturn(propertyRoomsAreaDTO);

        mock.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(propertyId));
    }


    @Test
    @DisplayName("Should create a property.")
    void testCreationOfProperty() throws Exception {

      UpsertPropertyDTO upsertPropertyDTO = new UpsertPropertyDTO("Propriedade", 1L);


        String propertyJSON = mapper.writeValueAsString(upsertPropertyDTO);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/properties")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(propertyJSON);

        DistrictDTO districtDTO = new DistrictDTO(1L,"Bairro",new BigDecimal(2000.00));

        List<RoomDTO> rooms = Arrays.asList(
                new RoomDTO(1L, "Sala", 20.0, 18.0)
        );

        PropertyDTO propertyDTO = new PropertyDTO(1L,"Propriedade", districtDTO, rooms);

        when(propertyService.createProperty(any(UpsertPropertyDTO.class))).thenReturn(propertyDTO);

        mock.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Propriedade"))
                .andExpect(jsonPath("$.district.name").value("Bairro"))
                .andExpect(jsonPath("$.rooms", hasSize(1)));
    }


    @Test
    @DisplayName("Should return the List of all properties.")
    void testGetAllProperties() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/properties/")
                .accept(MediaType.APPLICATION_JSON);
        PropertyDTO property1 = this.createSomePropertyDTO("Yellow House", 2);
        PropertyDTO property2 = this.createSomePropertyDTO("Green House", 1);

        when(propertyService.getAllProperties()).thenReturn(List.of(property1, property2));

        mock.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Yellow House"))
                .andExpect(jsonPath("$[0].rooms", hasSize(2)));
    }

    @Test
    @DisplayName("Should return a room with the largest area")
    void testGetRoomWithLargestArea() throws Exception {
        Long propertyId = 1L;
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/properties/" + propertyId + "/largestRoom")
                .accept(MediaType.APPLICATION_JSON);
        RoomDTO room = new RoomDTO(3L, "largest empty room", 10., 10.);

        when(propertyService.getLargestRoom(propertyId)).thenReturn(room);

        mock.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.width").value(10.))
                .andExpect(jsonPath("$.length").value(10.))
                .andExpect(jsonPath("$.name").value("largest empty room"));
    }

    @Test
    @DisplayName("Should throw exception when property does not exist")
    void testFailGetRoomWithLargestArea() throws Exception {
        Long propertyId = 1L;
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/properties/" + propertyId + "/largestRoom")
                .accept(MediaType.APPLICATION_JSON);

        when(propertyService.getLargestRoom(propertyId)).thenThrow(ResourceNotFoundException.class);

        mock.perform(request)
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("Should throw exception when property has no room")
    void testFailToFindRoomWithLargestArea() throws Exception {
        Long propertyId = 1L;
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/properties/" + propertyId + "/largestRoom")
                .accept(MediaType.APPLICATION_JSON);

        when(propertyService.getLargestRoom(propertyId)).thenThrow(PropertyHasNoRoomsException.class);

        mock.perform(request)
                .andExpect(status().isUnprocessableEntity());
    }

    private PropertyDTO createSomePropertyDTO(String name, int numberRooms) {
        DistrictDTO district = new DistrictDTO(1L,
                "Downtown",
                new BigDecimal("9540.50"));
        return new PropertyDTO(1L,
                name,
                district,
                this.createListOfRooms(numberRooms)
                );
    }

    private List<RoomDTO> createListOfRooms(int number) {
        List<RoomDTO> rooms = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            rooms.add(new RoomDTO((long) (i+1), "empty room", 1., 1.));
        }
        return rooms;
    }
}

