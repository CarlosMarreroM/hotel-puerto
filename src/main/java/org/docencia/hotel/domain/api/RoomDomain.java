package org.docencia.hotel.domain.api;

import java.util.List;
import java.util.Optional;

import org.docencia.hotel.domain.model.Room;

/**
 * Interfaz que define las operaciones del dominio de habitaciones.
 *
 * Esta interfaz actúa como un contrato para las funcionalidades
 * relacionadas con la gestión de habitaciones dentro del sistema.
 *
 * No contiene detalles de implementación, que serán
 * proporcionados por las clases que implementen esta interfaz.
 */
public interface RoomDomain {
    
    /**
     * Crea una nueva habitacion en el sistema.
     *
     * @param room Objeto Room con los datos de la habitacion a crear
     * @return La habitacion creada con su identificador asignado
     */
    Room createRoom(Room room);

    /**
     * Recuperar una habitacion por su identificador.
     * 
     * @param id Identificador de la habitacion a recuperar
     * @return Optional que contiene la habitacion si se encuentra, o vacío si no existe
     */
    Optional<Room> getRoomById(String id);

    /**
     * Recuperar todas las habitaciones del sistema.
     * 
     * @return Lista de todas las habitaciones.
     */
    List<Room> getAllRooms();

    /**
     * Recupera todas las habitaciones de un hotel.
     * 
     * @param hotelId Identificador del hotel cuyas habitaciones se desean recuperar
     * @return Lista de habitaciones del hotel especificado
     */
    List<Room> getRoomsByHotel(String hotelId);

    /**
     * Encuentra habitaciones por el identificador del hotel y el tipo de habitacion.
     * 
     * @param hotelId Identificador del hotel
     * @param type Tipo de habitacion
     * @return Lista de habitaciones que coinciden con el hotelId y el tipo especificados
     */
    List<Room> getRoomsByHotelAndType(String hotelId, String type);

    /**
     * Actualiza los datos de una habitacion existente.
     *
     * @param id Identificador de la habitacion a actualizar  
     * @param room Objeto Room con los datos actualizados
     * @return La habitacion actualizada
     */
    Room updateRoom(String id, Room room);

    /**
     * Elimina una habitacion del sistema por su identificador.
     * 
     * @param id Identificador de la habitacion a eliminar
     * @return true si la habitacion fue eliminada, false si no existía
     */
    boolean deleteRoom(String id);

    /**
     * Elimina todas las habitaciones asociadas a un hotel específico.
     * 
     * @param hotelId Identificador del hotel cuyas habitaciones se desean eliminar
     * @return Número de habitaciones eliminadas
     */
    int deleteRoomsByHotel(String hotelId);

    
}
