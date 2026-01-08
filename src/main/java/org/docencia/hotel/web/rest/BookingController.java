package org.docencia.hotel.web.rest;

import java.net.URI;
import java.util.List;

import org.docencia.hotel.domain.api.BookingDomain;
import org.docencia.hotel.domain.model.Booking;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Bookings", description = "Operaciones REST de reservas")
@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingDomain bookingDomain;

    public BookingController(BookingDomain bookingDomain) {
        this.bookingDomain = bookingDomain;
    }

    @Operation(summary = "Crear una reserva",
            description = "Crea una nueva reserva. Valida existencia de guest y room, y fechas (checkIn/checkOut).")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reserva creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos (ids vacíos, fechas inválidas, etc.)"),
            @ApiResponse(responseCode = "409", description = "Ya existe una reserva con ese id"),
            @ApiResponse(responseCode = "404", description = "Guest/Room/Hotel no encontrado")
    })
    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) {
        Booking created = bookingDomain.createBooking(booking);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    @Operation(summary = "Listar reservas",
            description = "Devuelve todas las reservas. "
                    + "Opcionalmente filtra por roomId, guestId o hotelId (solo uno a la vez).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de reservas devuelta correctamente"),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos o combinaciones no permitidas"),
            @ApiResponse(responseCode = "404", description = "Room/Guest/Hotel no encontrado (según filtro)")
    })
    @GetMapping
    public ResponseEntity<List<Booking>> getBookings(
            @Parameter(description = "Filtrar por id de habitación (opcional)") @RequestParam(required = false) String roomId,
            @Parameter(description = "Filtrar por id de huésped (opcional)") @RequestParam(required = false) String guestId,
            @Parameter(description = "Filtrar por id de hotel (opcional)") @RequestParam(required = false) String hotelId) {

        int filters = (roomId != null ? 1 : 0) + (guestId != null ? 1 : 0) + (hotelId != null ? 1 : 0);

        if (filters > 1) {
            return ResponseEntity.badRequest().build();
        }

        if (roomId != null) {
            return ResponseEntity.ok(bookingDomain.getBookingsByRoomId(roomId));
        }
        if (guestId != null) {
            return ResponseEntity.ok(bookingDomain.getBookingsByGuestId(guestId));
        }
        if (hotelId != null) {
            return ResponseEntity.ok(bookingDomain.getBookingsByHotelId(hotelId));
        }

        return ResponseEntity.ok(bookingDomain.getAllBookings());
    }

    @Operation(summary = "Obtener reserva por id",
            description = "Devuelve la reserva que coincide con el id proporcionado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reserva encontrada"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada"),
            @ApiResponse(responseCode = "400", description = "Id inválido")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(
            @Parameter(description = "Identificador de la reserva") @PathVariable String id) {

        return bookingDomain.getBookingById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Actualizar una reserva",
            description = "Actualiza una reserva existente. Valida existencia y fechas.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reserva actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada"),
            @ApiResponse(responseCode = "409", description = "Conflicto/validación de negocio (si aplica)")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Booking> updateBooking(
            @PathVariable String id,
            @RequestBody Booking booking) {

        // Igual que en tus otros controllers: devolvemos 404 si no existe
        if (bookingDomain.getBookingById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Booking updated = bookingDomain.updateBooking(id, booking);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Eliminar una reserva",
            description = "Elimina una reserva por su id.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Reserva eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada"),
            @ApiResponse(responseCode = "400", description = "Id inválido")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable String id) {
        if (bookingDomain.getBookingById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        bookingDomain.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Eliminar reservas por huésped",
            description = "Elimina todas las reservas asociadas a un huésped. Devuelve cuántas se eliminaron.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reservas eliminadas correctamente"),
            @ApiResponse(responseCode = "400", description = "Id inválido"),
            @ApiResponse(responseCode = "404", description = "Huésped no encontrado")
    })
    @DeleteMapping("/guest/{guestId}")
    public ResponseEntity<Integer> deleteBookingsByGuestId(
            @Parameter(description = "Identificador del huésped") @PathVariable String guestId) {

        int deletedCount = bookingDomain.deleteBookingsByGuestId(guestId);
        return ResponseEntity.ok(deletedCount);
    }

    @Operation(summary = "Eliminar reservas por habitación",
            description = "Elimina todas las reservas asociadas a una habitación. Devuelve cuántas se eliminaron.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reservas eliminadas correctamente"),
            @ApiResponse(responseCode = "400", description = "Id inválido"),
            @ApiResponse(responseCode = "404", description = "Habitación no encontrada")
    })
    @DeleteMapping("/room/{roomId}")
    public ResponseEntity<Integer> deleteBookingsByRoomId(
            @Parameter(description = "Identificador de la habitación") @PathVariable String roomId) {

        int deletedCount = bookingDomain.deleteBookingsByRoomId(roomId);
        return ResponseEntity.ok(deletedCount);
    }
}
