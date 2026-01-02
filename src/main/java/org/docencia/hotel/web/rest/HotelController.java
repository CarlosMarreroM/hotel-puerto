package org.docencia.hotel.web.rest;

import java.net.URI;
import java.util.List;

import org.docencia.hotel.domain.api.HotelDomain;
import org.docencia.hotel.domain.model.Hotel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @Operation(
        summary = "Crear un hotel",
        description = "Crea un hotel nuevo. En esta implementación, el id debe venir informado en el body. "
                    + "Si el id ya existe, devuelve 409 Conflict."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Hotel creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos (id/nombre vacíos, etc.)"),
        @ApiResponse(responseCode = "409", description = "Ya existe un hotel con ese id")
    })
    @PostMapping
    public ResponseEntity<Hotel> createHotel(@RequestBody Hotel hotel) {
        // La validación fuerte y la regla de "si existe -> error" deben estar en Dominio.
        // Aquí solo llamamos al Dominio y construimos la respuesta HTTP.
        Hotel created = hotelDomain.createHotel(hotel);

        // 201 Created + Location
        URI location = URI.create("/api/hotels/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }

    // -------------------------------------------------------------------------
    // READ (LIST + FILTER)
    // -------------------------------------------------------------------------

    @Operation(
        summary = "Listar hoteles",
        description = "Devuelve todos los hoteles. Si se pasa el parámetro 'name', filtra por nombre exacto."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de hoteles devuelta correctamente"),
        @ApiResponse(responseCode = "400", description = "Parámetro de búsqueda inválido")
    })
    @GetMapping
    public ResponseEntity<List<Hotel>> getHotels(
            @Parameter(description = "Nombre del hotel para filtrar (opcional)")
            @RequestParam(required = false) String name
    ) {
        // Si viene name, delegamos en el caso de uso de búsqueda por nombre
        if (name != null) {
            
            return ResponseEntity.ok(hotelDomain.getHotelsByName(name));
        }

        // Si no viene name, listamos todos
        return ResponseEntity.ok(hotelDomain.getAllHotels());
    }
    


}