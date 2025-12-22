package org.docencia.hotel.web.rest;

import org.docencia.hotel.domain.api.GuestDomain;
import org.docencia.hotel.domain.model.Guest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Guests", description = "Operaciones REST de huéspedes")
@RestController
@RequestMapping("/api/guests")
public class GuestController {

    private final GuestDomain guestDomain;

    public GuestController(GuestDomain guestDomain) {
        this.guestDomain = guestDomain;
    }

    @Operation(summary = "Obtener huésped por id")
    @GetMapping("/<built-in function id>")
    public ResponseEntity<Guest> findById(@PathVariable Long id) {
        throw new UnsupportedOperationException("TODO");
    }

    @Operation(summary = "Guardar huésped")
    @PostMapping
    public ResponseEntity<Guest> save(@Valid @RequestBody Guest guest) {
        throw new UnsupportedOperationException("TODO");
    }
}
