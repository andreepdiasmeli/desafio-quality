package desafio_quality.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import desafio_quality.repositories.PropertyRepository;
import desafio_quality.services.DBService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class IntegrationPropertyControllerTest {

    @Autowired
    private MockMvc mock;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private DBService dbService;

    @Autowired
    private PropertyRepository propertyRepository;

    @BeforeEach
    void setup() {
        System.out.println("Setting DB up...");
        this.dbService.instantiateDB();
        System.out.println("Complete!");
    }

    @AfterEach
    void teardown() {
        System.out.println("Tearing DB Down...");
        this.dbService.knockDownDB();
        System.out.println("Complete!");
    }

    private Long getFirstPropertyId() {
        return propertyRepository.findAll().get(0).getId();
    }

    @Test
    @DisplayName("US-0001 - Should return the square meters of a property.")
    void testPropertyArea() throws Exception {
        Long propertyId = getFirstPropertyId();

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .get("/properties/" + propertyId + "/totalArea")
            .accept(MediaType.APPLICATION_JSON);

        mock.perform(request)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalArea").value(170.0));
    }

    @Test
    @DisplayName("US-0001 - Should return exception message when testing the total area of a property with an invalid ID.")
    void testPropertyAreaWithInvalidId() throws Exception {
        Long propertyId = Long.MAX_VALUE;

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .get("/properties/" + propertyId + "/totalArea")
            .accept(MediaType.APPLICATION_JSON);

        mock.perform(request)
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("US-0002 - Should indicate the value of a property according to its rooms.")
    void testValueOfAPropertyAccordingToItsRooms() throws Exception {
        Long propertyId = getFirstPropertyId();
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .get("/properties/" + propertyId + "/value")
            .accept(MediaType.APPLICATION_JSON);

        mock.perform(request)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.value").value("1451290.0"));
    }

    @Test
    @DisplayName("US-0002 - Should return exception message when using an invalid property ID.")
    void testPropertyValueWithAnInvalidId() throws Exception {
        Long propertyId = Long.MAX_VALUE;
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .get("/properties/" + propertyId + "/value")
            .accept(MediaType.APPLICATION_JSON);

        mock.perform(request)
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("US-0003 - Should return the largest room in the property.")
    void testRoomAreas() throws Exception {
        Long propertyId = this.getFirstPropertyId();
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .get("/properties/" + propertyId + "/largestRoom")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

        mock.perform(request)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Cozinha"))
            .andExpect(jsonPath("$.width").value(15.0))
            .andExpect(jsonPath("$.length").value(8.0));
    }

    @Test
    @DisplayName("US-0003 - Should return exception message when property has no rooms.")
    void testLargestRoomWhenPropertyHasNoRooms() throws Exception {
        Long propertyId = this.getFirstPropertyId() + 3L;

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .get("/properties/" + propertyId + "/largestRoom")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

        mock.perform(request)
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("US-0004 - Return the areas of each room of a property")
    void testGetAreaOfAllRooms() throws Exception {
        Long propertyId = this.getFirstPropertyId();

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .get("/properties/" + propertyId + "/roomsArea")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

        mock.perform(request)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(propertyId))
            .andExpect(jsonPath("$.name").value("Bem Viver"))
            .andExpect(jsonPath("$.rooms", hasSize(2)))
            .andExpect(jsonPath("$.rooms[0].name").value("Quarto"))
            .andExpect(jsonPath("$.rooms[0].area").value(50.0));
    }

    @Test
    @DisplayName("US-0004 - Return the areas of each room of a property")
    void testFailureGetAreaOfAllRooms() throws Exception {
        Long propertyId = Long.MAX_VALUE;

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .get("/properties/" + propertyId + "/roomsArea")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

        mock.perform(request)
            .andExpect(status().isUnprocessableEntity());
    }
}

