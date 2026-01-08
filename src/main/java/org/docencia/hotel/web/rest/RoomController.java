package org.docencia.hotel.web.rest;

import java.net.URI;
import java.util.List;

import org.docencia.hotel.domain.api.RoomDomain;
import org.docencia.hotel.domain.model.Room;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Rooms", description = "Operaciones REST de habitaciones")
@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomDomain roomDomain;
    

    public RoomController(RoomDomain roomDomain) {
        this.roomDomain = roomDomain;
    }

    @Operation(summary = "Crear una habitacion", description = "Crea una nueva habitación. El id de la habitación debe ser único.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Habitación creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos (id/nombre vacíos, etc.)"),
            @ApiResponse(responseCode = "409", description = "Ya existe una habitación con ese id")
    })
    @PostMapping
    public ResponseEntity<Room> createRoom(@RequestBody Room room) {
        Room createdRoom = roomDomain.createRoom(room);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdRoom.getId())
                .toUri();

        return ResponseEntity.created(location).body(createdRoom);
    }

    @Operation(summary = "Listar habitaciones", description = "Devuelve todas las habitaciones. Si se le pasa el parametro 'hotelId' filtra por hotel y si se anade 'type' filtra por tipo de habitacion.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de habitaciones devuelta correctamente"),
            @ApiResponse(responseCode = "400", description = "Parámetro de búsqueda inválido")
    })
    @GetMapping
    public ResponseEntity<List<Room>> getRooms(
            @Parameter(description = "Identificador del hotel para filtrar (opcional)") @RequestParam(required = false) String hotelId,
            @Parameter(description = "Tipo de habitación para filtrar (opcional)") @RequestParam(required = false) String type) {

        if (hotelId != null && type != null) {
            return ResponseEntity.ok(roomDomain.getRoomsByHotelAndType(hotelId, type));
        } else if (hotelId != null) {
            return ResponseEntity.ok(roomDomain.getRoomsByHotel(hotelId));
        }

        return ResponseEntity.ok(roomDomain.getAllRooms());
    }

    @Operation(summary = "Listar habitaciones por id", description = "Devuelve la habitacion que coincide con el id proporcionado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de habitaciones devuelta correctamente"),
            @ApiResponse(responseCode = "404", description = "Habitación no encontrada"),
            @ApiResponse(responseCode = "400", description = "Id inválido")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(
            @Parameter(description = "Identificador de la habitación") @PathVariable String id) {
        return roomDomain.getRoomById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Actualizar una habitación", description = "Actualiza los datos de una habitación existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Habitación actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Habitación no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Room> updateRoom(
            @PathVariable String id,
            @RequestBody Room room) {

        if (roomDomain.getRoomById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Room updated = roomDomain.updateRoom(id, room);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Eliminar una habitación", description = "Elimina una habitación por su id.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Habitación eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Habitación no encontrada"),
            @ApiResponse(responseCode = "409", description = "No se puede eliminar la habitación por reglas de negocio")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable String id) {

        if (roomDomain.getRoomById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        roomDomain.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Eliminar habitaciones de un hotel", description = "Elimina todas las habitaciones asociadas a un hotel. "
            + "No se permite si existen reservas asociadas a dichas habitaciones.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Habitaciones eliminadas correctamente"),
            @ApiResponse(responseCode = "400", description = "Hotel no existe o tiene reservas asociadas"),
    })
    @DeleteMapping("/{hotelId}/rooms")
    public ResponseEntity<Integer> deleteRoomsByHotel(
            @Parameter(description = "Identificador del hotel") @PathVariable String hotelId) {

        int deletedCount = roomDomain.deleteRoomsByHotel(hotelId);
        return ResponseEntity.ok(deletedCount);
    }

}
