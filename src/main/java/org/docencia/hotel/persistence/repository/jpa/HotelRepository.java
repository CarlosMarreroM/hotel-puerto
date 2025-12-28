package org.docencia.hotel.persistence.repository.jpa;

import java.util.List;

import org.docencia.hotel.persistence.jpa.entity.HotelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para la entidad Hotel.
 *
 * Esta interfaz extiende JpaRepository, proporcionando
 * métodos CRUD y de consulta para la entidad HotelEntity.
 *
 * Spring Data JPA generará automáticamente la implementación
 * de esta interfaz en tiempo de ejecución.
 */

@Repository
public interface HotelRepository extends JpaRepository<HotelEntity, String> {
    /**
     * Busca hoteles por su nombre.
     * 
     * @param hotelName Nombre del hotel
     * @return Lista de hoteles que coinciden con el nombre 
     */
    List<HotelEntity> findByHotelName(String hotelName);
}
