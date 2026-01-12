package org.docencia.hotel.web.rest;

import java.net.URI;
import java.util.List;

import org.docencia.hotel.domain.api.HotelDomain;
import org.docencia.hotel.domain.model.Hotel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Hotels", description = "Operaciones REST de hoteles")
@RestController
@RequestMapping("/api/hotels")
public class HotelController {

    private final HotelDomain hotelDomain;

    public HotelController(HotelDomain hotelDomain) {
        this.hotelDomain = hotelDomain;
    }

    @Operation(summary = "Crear un hotel", description = "Crea un hotel nuevo. El id del hotel debe ser único.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Hotel creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos (id/nombre vacíos, etc.)"),
            @ApiResponse(responseCode = "409", description = "Ya existe un hotel con ese id")
    })
    @PostMapping
    public ResponseEntity<Hotel> createHotel(@RequestBody Hotel hotel) {
        Hotel createdHotel = hotelDomain.createHotel(hotel);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdHotel.getId())
                .toUri();

        return ResponseEntity.created(location).body(createdHotel);
    }

    @Operation(summary = "Listar hoteles", description = "Devuelve todos los hoteles. Si se pasa el parámetro 'name', filtra por nombre exacto.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de hoteles devuelta correctamente"),
            @ApiResponse(responseCode = "400", description = "Parámetro de búsqueda inválido")
    })
    @GetMapping
    public ResponseEntity<List<Hotel>> getHotels(
            @Parameter(description = "Nombre del hotel para filtrar (opcional)") @RequestParam(required = false) String name) {

        if (name != null) {
            return ResponseEntity.ok(hotelDomain.getHotelsByName(name));
        }

        return ResponseEntity.ok(hotelDomain.getAllHotels());
    }

    @Operation(summary = "Listar hoteles por id", description = "Devuelve el hotel que coincide con el id proporcionado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de hoteles devuelta correctamente"),
            @ApiResponse(responseCode = "404", description = "Hotel no encontrado"),
            @ApiResponse(responseCode = "400", description = "Id inválido")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Hotel> getHotelById(
            @Parameter(description = "Identificador del hotel") @PathVariable String id) {
        return hotelDomain.getHotelById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }

    @Operation(summary = "Actualizar un hotel", description = "Actualiza los datos de un hotel existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Hotel actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Hotel no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Hotel> updateHotel(
            @PathVariable String id,
            @RequestBody Hotel hotel) {

        if (hotelDomain.getHotelById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Hotel updated = hotelDomain.updateHotel(id, hotel);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Eliminar un hotel", description = "Elimina un hotel por su id.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Hotel eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Hotel no encontrado"),
            @ApiResponse(responseCode = "409", description = "No se puede eliminar el hotel por reglas de negocio")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHotel(@PathVariable String id) {

        if (hotelDomain.getHotelById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        hotelDomain.deleteHotel(id);
        return ResponseEntity.noContent().build();
    }
}