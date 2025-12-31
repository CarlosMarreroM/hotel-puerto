package org.docencia.hotel.persistence.repository.jpa;

import java.util.List;

import org.docencia.hotel.persistence.jpa.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA para la entidad BookingEntity.
 * 
 * Proporciona operaciones CRUD y consultas personalizadas
 * para gestionar las reservas en la base de datos relacional.
 */
public interface BookingRepository extends JpaRepository<BookingEntity, String> {
    /**
     * Verifica si existen reservas asociadas a una habitación específica.
     * 
     * @param roomId ID de la habitación.
     * @return true si existen reservas para la habitación, false en caso contrario.
     */
    boolean existsByRoomId(String roomId);

    /**
     * Obtiene todas las reservas asociadas a una habitación específica.
     * 
     * @param roomId ID de la habitación.
     * @return Lista de reservas asociadas a la habitación.
     */
    List<BookingEntity> findByRoomId(String roomId);

    /**
     * Obtiene todas las reservas asociadas a un huésped específico.
     * 
     * @param guestId ID del huésped.
     * @return Lista de reservas asociadas al huésped.
     */
    List<BookingEntity> findByGuestId(String guestId);

    /**
     * Obtiene todas las reservas asociadas a un hotel específico
     * a través de las habitaciones vinculadas a ese hotel.
     * 
     * @param hotelId ID del hotel.
     * @return Lista de reservas asociadas al hotel.
     */
    List<BookingEntity> findByRoomHotelId(String hotelId);

    /**
     * Verifica si existen reservas asociadas a un huésped específico.
     * 
     * @param guestId ID del huésped.
     * @return true si existen reservas para el huésped, false en caso contrario.
     */
    boolean existsByGuestId(String guestId);

    /**
     * Elimina todas las reservas asociadas a una habitación específica.
     * 
     * @param roomId ID de la habitación.
     * @return Número de reservas eliminadas.
     */
    int deleteByRoomId(String roomId);

    /**
     * Elimina todas las reservas asociadas a un huésped específico.
     * 
     * @param guestId ID del huésped.
     * @return Número de reservas eliminadas.
     */
    int deleteByGuestId(String guestId);
}
