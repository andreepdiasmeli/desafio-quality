package desafio_quality.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;

import com.fasterxml.jackson.databind.ObjectMapper;
import desafio_quality.dtos.DistrictDTO;
import desafio_quality.dtos.PropertyDTO;
import desafio_quality.dtos.*;

import desafio_quality.dtos.PropertyRoomsAreaDTO;
import desafio_quality.dtos.PropertyValueDTO;
import desafio_quality.dtos.RoomDTO;
import desafio_quality.exceptions.PropertyHasNoRoomsException;
import desafio_quality.dtos.RoomAreaDTO;
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
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.ArrayList;
import java.util.List;

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

