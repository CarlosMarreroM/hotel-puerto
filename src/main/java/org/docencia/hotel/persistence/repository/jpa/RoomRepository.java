package org.docencia.hotel.persistence.repository.jpa;

import java.util.List;

import org.docencia.hotel.persistence.jpa.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para la entidad RoomEntity.
 * 
 * Esta interfaz extiende JpaRepository para proporcionar
 * operaciones CRUD y consultas personalizadas para las habitaciones.
 */
@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, String> {
    /**
     * Encuentra todas las habitaciones que pertenecen a un hotel específico.
     * 
     * @param hotelId Identificador del hotel
     * @return Lista de habitaciones asociadas al hotel
     */
    List<RoomEntity> findByHotel_Id(String hotelId);

    /**
     * Encuentra todas las habitaciones de un hotel en especifico y con un tipo determinado.
     * @param hotelId
     * @param type
     * @return
     */
    List<RoomEntity> findByHotel_IdAndType(String hotelId, String type);

    /**
     * Elimina todas las habitaciones asociadas a un hotel específico.
     * 
     * @param hotelId Identificador del hotel
     * @return Número de habitaciones eliminadas
     */
    int deleteByHotel_Id(String hotelId);
}
