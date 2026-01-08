package org.docencia.hotel.web.rest;

import java.net.URI;
import java.util.List;

import org.docencia.hotel.domain.api.GuestDomain;
import org.docencia.hotel.domain.model.Guest;
import org.docencia.hotel.domain.model.GuestPreferences;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Guests", description = "Operaciones REST de huéspedes")
@RestController
@RequestMapping("/api/guests")
public class GuestController {

    private final GuestDomain guestDomain;

    public GuestController(GuestDomain guestDomain) {
        this.guestDomain = guestDomain;
    }

    @Operation(summary = "Crear un huésped", description = "Crea un huésped en H2 y, si se incluyen preferencias, las guarda en Mongo usando el mismo id.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Huésped creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "409", description = "Ya existe un huésped con ese id")
    })
    @PostMapping
    public ResponseEntity<Guest> createGuest(@RequestBody Guest guest) {
        Guest created = guestDomain.createGuest(guest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    @Operation(summary = "Listar huéspedes", description = "Devuelve todos los huéspedes con sus preferencias si existen")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de huéspedes devuelta correctamente")
    })
    @GetMapping
    public ResponseEntity<List<Guest>> getAllGuests() {
        return ResponseEntity.ok(guestDomain.getAllGuests());
    }

    @Operation(summary = "Obtener huésped por id", description = "Devuelve un huésped con sus preferencias si existen")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Huésped encontrado"),
            @ApiResponse(responseCode = "404", description = "Huésped no encontrado"),
            @ApiResponse(responseCode = "400", description = "Id inválido")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Guest> getGuestById(@PathVariable String id) {
        return guestDomain.getGuestById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtener preferencias de un huésped",
            description = "Devuelve solo las preferencias del huésped (Mongo).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Preferencias encontradas"),
            @ApiResponse(responseCode = "404", description = "No existen preferencias para ese huésped"),
            @ApiResponse(responseCode = "400", description = "Id inválido")
    })
    @GetMapping("/{id}/preferences")
    public ResponseEntity<GuestPreferences> getPreferences(@PathVariable String id) {
        return guestDomain.getPreferencesByGuestId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Actualizar huésped",
            description = "Actualiza los datos del huésped ")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Huésped actualizado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Huésped no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Guest> updateGuest(@PathVariable String id, @RequestBody Guest guest) {
        Guest updated = guestDomain.updateGuest(id, guest);

        if(guestDomain.getGuestById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Crear/actualizar preferencias de un huésped",
            description = "Guarda o actualiza las preferencias del huésped en Mongo. El guestId se fuerza al id del path.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Preferencias guardadas/actualizadas"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Huésped no encontrado")
    })
    @PutMapping("/{id}/preferences")
    public ResponseEntity<GuestPreferences> upsertPreferences(
            @PathVariable String id,
            @RequestBody GuestPreferences preferences) {

        GuestPreferences saved = guestDomain.updatePreferences(id, preferences);

        if(guestDomain.getGuestById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(saved);
    }

    @Operation(summary = "Eliminar preferencias de un huésped",
            description = "Elimina las preferencias del huésped (Mongo).")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Preferencias eliminadas"),
            @ApiResponse(responseCode = "404", description = "No existen preferencias para ese huésped"),
            @ApiResponse(responseCode = "400", description = "Id inválido")
    })
    @DeleteMapping("/{id}/preferences")
    public ResponseEntity<Void> deletePreferences(@PathVariable String id) {
        if (!guestDomain.deletePreferencesByGuestId(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Eliminar huésped",
            description = "Elimina el huésped en H2 y, si tiene preferencias, también las elimina en Mongo.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Huésped eliminado"),
            @ApiResponse(responseCode = "404", description = "Huésped no encontrado"),
            @ApiResponse(responseCode = "409", description = "No se puede borrar porque tiene bookings"),
            @ApiResponse(responseCode = "400", description = "Id inválido")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGuest(@PathVariable String id) {
        if (!guestDomain.deleteGuest(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();      
    }

}
