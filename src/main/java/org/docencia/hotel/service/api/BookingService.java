package org.docencia.hotel.service.api;

import java.util.List;
import java.util.Optional;

import org.docencia.hotel.domain.model.Booking;

/**
 * Servicio para gestionar reservas de hotel.
 * 
 * Proporciona métodos para crear, leer, actualizar
 * y eliminar reservas, así como para consultar reservas
 * por diferentes criterios.
 */
public interface BookingService {
    /** 
     * Guarda una reserva.
     * 
     * @param booking Reserva a guardar.
     * @return Reserva guardada.
     */
    Booking save(Booking booking);

    /**
     * Verifica si una reserva existe por su ID.
     * 
     * @param id ID de la reserva.
     * @return true si la reserva existe, false en caso contrario.
     */
    boolean existsById(String id);

    /**
     * Verifica si existen reservas asociadas a un huésped específico.
     * 
     * @param guestId ID del huésped.
     * @return true si existen reservas para el huésped, false en caso contrario.
     */
    boolean existsByGuestId(String guestId);

    /**
     * Verifica si existen reservas asociadas a una habitación específica.
     * 
     * @param roomId ID de la habitación.
     * @return true si existen reservas para la habitación, false en caso contrario.
     */
    boolean existsByRoomId(String roomId);

    /**
     * Obtiene todas las reservas.
     * 
     * @return Lista de todas las reservas.
     */
    List<Booking> findAll();

    /**
     * Obtiene una reserva por su ID.
     * 
     * @param id ID de la reserva.
     * @return Reserva encontrada, o vacío si no existe.
     */
    Optional<Booking> findById(String id);

    /**
     * Obtiene todas las reservas asociadas a una habitación específica.
     * 
     * @param roomId ID de la habitación.
     * @return Lista de reservas asociadas a la habitación.
     */
    List<Booking> findAllByRoomId(String roomId);

    /**
     * Obtiene todas las reservas asociadas a un huésped específico.
     * 
     * @param guestId ID del huésped.
     * @return Lista de reservas asociadas al huésped.
     */
    List<Booking> findAllByGuestId(String guestId);

    /**
     * Obtiene todas las reservas asociadas a un hotel específico.
     * 
     * @param hotelId ID del hotel.
     * @return Lista de reservas asociadas al hotel.
     */
    List<Booking> findAllByHotelId(String hotelId);

    /**
     * Elimina una reserva por su ID.
     * 
     * @param id ID de la reserva a eliminar.
     * @return true si la reserva fue eliminada, false si no existía.
     */
    boolean deleteById(String id);

    /**
     * Elimina todas las reservas asociadas a un huésped específico.
     * 
     * @param guestId ID del huésped.
     * @return Número de reservas eliminadas.
     */
    int deleteByGuestId(String guestId);

    /**
     * Elimina todas las reservas asociadas a una habitación específica.
     * 
     * @param roomId ID de la habitación.
     * @return Número de reservas eliminadas.
     */
    int deleteByRoomId(String roomId);
}