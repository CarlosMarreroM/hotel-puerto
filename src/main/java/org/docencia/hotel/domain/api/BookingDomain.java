package org.docencia.hotel.domain.api;

import java.util.List;
import java.util.Optional;

import org.docencia.hotel.domain.model.Booking;

/**
 * Interfaz que define las operaciones del dominio de reservas.
 *
 * Esta interfaz actúa como un contrato para las funcionalidades
 * relacionadas con la gestión de reservas dentro del sistema.
 *
 * No contiene detalles de implementación, que serán
 * proporcionados por las clases que implementen esta interfaz.
 * 
 */
public interface BookingDomain {

    /**
     * Crea una nueva reserva.
     *
     * @param booking Reserva a crear.
     * @return Reserva creada.
     */
    Booking createBooking(Booking booking);

    /**
     * Obtiene una reserva por su identificador.
     *
     * @param id Identificador de la reserva.
     * @return Reserva si existe.
     */
    Optional<Booking> getBookingById(String id);

    /**
     * Obtiene todas las reservas.
     *
     * @return Lista de reservas.
     */
    List<Booking> getAllBookings();

    /**
     * Obtiene todas las reservas asociadas a una habitación.
     *
     * @param roomId Identificador de la habitación.
     * @return Lista de reservas asociadas a la habitación.
     */
    List<Booking> getBookingsByRoomId(String roomId);

    /**
     * Obtiene todas las reservas asociadas a un huésped.
     *
     * @param guestId Identificador del huésped.
     * @return Lista de reservas asociadas al huésped.
     */
    List<Booking> getBookingsByGuestId(String guestId);

    /**
     * Obtiene todas las reservas asociadas a un hotel
     * a través de sus habitaciones.
     *
     * @param hotelId Identificador del hotel.
     * @return Lista de reservas asociadas al hotel.
     */
    List<Booking> getBookingsByHotelId(String hotelId);

    /**
     * Actualiza una reserva existente.
     *
     * @param id      Identificador de la reserva a actualizar.
     * @param booking Datos nuevos de la reserva.
     * @return Reserva actualizada.
     */
    Booking updateBooking(String id, Booking booking);

    /**
     * Elimina una reserva por id.
     *
     * @param id Identificador de la reserva.
     * @return true si se eliminó, false si no existía.
     */
    boolean deleteBooking(String id);

    /**
     * Elimina todas las reservas asociadas a un huésped.
     *
     * @param guestId Identificador del huésped.
     * @return Número de reservas eliminadas.
     */
    int deleteBookingsByGuestId(String guestId);

    /**
     * Elimina todas las reservas asociadas a una habitación.
     *
     * @param roomId Identificador de la habitación.
     * @return Número de reservas eliminadas.
     */
    int deleteBookingsByRoomId(String roomId);
}
