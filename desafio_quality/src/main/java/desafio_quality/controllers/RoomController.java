package desafio_quality.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import desafio_quality.dtos.RoomDTO;
import desafio_quality.dtos.UpsertRoomDTO;
import desafio_quality.services.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("rooms")
public class RoomController {

    private final RoomService roomService;


    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @Operation(summary = "Get all rooms")
    @GetMapping
    public List<RoomDTO> getAllRooms() {
        return this.roomService.getAllRooms();
    }

    @Operation(summary = "Get a specific room by ID")
    @Parameter(name = "roomId", required = true, description = "The room ID", example = "1")
    @GetMapping("{roomId}")
    public RoomDTO getRoomById(@PathVariable Long roomId) {
        return this.roomService.getRoomById(roomId);
    }

    @Operation(summary = "Create a room")
    @PostMapping("property/{propertyId}")
    @ResponseStatus(HttpStatus.CREATED)
    public RoomDTO createRoom(
            @PathVariable Long propertyId,
            @RequestBody @Valid UpsertRoomDTO upsertRoomDto) {
        return this.roomService.createRoom(propertyId, upsertRoomDto);
    }

    @Operation(summary = "Update an existing room")
    @PutMapping("{roomId}")
    public RoomDTO updateRoom(
            @PathVariable Long roomId,
            @RequestBody @Valid UpsertRoomDTO upsertRoomDto) {
        return this.roomService.updateRoom(roomId, upsertRoomDto);
    }

    @Operation(summary = "Delete an existing room")
    @DeleteMapping("{roomId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRoom(@PathVariable Long roomId){
        this.roomService.deleteRoom(roomId);
    }
}
