package org.docencia.hotel.service.api;

import java.util.List;
import java.util.Optional;

import org.docencia.hotel.domain.model.Room;

public interface RoomService {
    /**
     * Guarda una nueva habitacion en el sistema.
     * 
     * @param room Datos de la habitacion a crear
     * @return Habitacion creada con su identificador unico
     */
    Room save(Room room);

    /**
     * Verifica si una habitacion existe por su identificador unico.
     * 
     * @param id Identificador unico de la habitacion
     * @return true si la habitacion existe, false en caso contrario
     */
    boolean existsById(String id);

    /**
     * Busca una habitacion por su identificador unico.
     * 
     * @param id Identificador unico de la habitacion
     * @return Optional que contiene la habitacion si se encuentra, o vacio si no
     * existe
     */
    Optional<Room> findById(String id);

    /**
     * Recupera todas las habitaciones del sistema.
     * 
     * @return Lista de todas las habitaciones
     */
    List<Room> findAll();

    /**
     * Busca todas las habitaciones asociadas a un hotel especifico.
     * 
     * @param hotelId Identificador del hotel
     * @return Lista de habitaciones asociadas al hotel
     */
    List<Room> findByHotelId(String hotelId);

    /**
     * Busca todas las habitaciones de un hotel en especifico y con un tipo determinado.
     * 
     * @param hotelId Identificador del hotel
     * @param type Tipo de habitacion
     * @return Lista de habitaciones que coinciden con los criterios
     */
    List<Room> findByHotelIdAndType(String hotelId, String type);

    /**
     * Elimina una habitacion por su identificador unico.
     * 
     * @param id Identificador unico de la habitacion a eliminar
     * @return true si la habitacion fue eliminada, false si no existia
     */
    boolean deleteById(String id);

    /**
     * Elimina todas las habitaciones asociadas a un hotel.
     *
     * @param hotelId id del hotel
     * @return número de habitaciones eliminadas
     */
    int deleteByHotelId(String hotelId);
}