package org.docencia.hotel.domain.api;

import java.util.List;
import java.util.Optional;

import org.docencia.hotel.domain.model.Hotel;

/**
 * Interfaz que define las operaciones del dominio de hoteles.
 *
 * Esta interfaz actúa como un contrato para las funcionalidades
 * relacionadas con la gestión de hoteles dentro del sistema.
 *
 * No contiene detalles de implementación, que serán
 * proporcionados por las clases que implementen esta interfaz.
 */
public interface HotelDomain {
    
    /**
     * Crea un nuevo hotel en el sistema.
     *
     * @param hotel Objeto Hotel con los datos del hotel a crear
     * @return El hotel creado con su identificador asignado
     */
    Hotel createHotel(Hotel hotel);

    /**
     * Recuperar un hotel por su identificador.
     * 
     * @param id Identificador del hotel a recuperar
     * @return Optional que contiene el hotel si se encuentra, o vacío si no existe
     */
    Optional<Hotel> getHotelById(String id);

    /**
     * Recuperar todos los hoteles del sistema.
     * 
     * @return Lista de todos los hoteles.
     */
    List<Hotel> getAllHotels();

    /**
     * Actualiza los datos de un hotel existente.
     *
     * @param id Identificador del hotel a actualizar  
     * @param hotel Objeto Hotel con los datos actualizados
     * @return El hotel actualizado
     */
    Hotel updateHotel(String id, Hotel hotel);

    /**
     * Elimina un hotel del sistema por su identificador.
     * 
     * @param id Identificador del hotel a eliminar
     * @return true si el hotel fue eliminado, false si no existía
     */
    boolean deleteHotel(String id);

    /**
     * Busca hoteles por su nombre.
     * 
     * @param name Nombre del hotel a buscar
     * @return Lista de hoteles que coinciden con el nombre proporcionado
     */
    List<Hotel> findHotelsByName(String name);

}
