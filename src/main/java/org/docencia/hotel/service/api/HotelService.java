package org.docencia.hotel.service.api;

import java.util.List;
import java.util.Optional;

import org.docencia.hotel.domain.model.Hotel;

/**
 * Interfaz que define las operaciones del servicio de hoteles.
 *
 * Esta interfaz actúa como un contrato para las funcionalidades
 * relacionadas con la gestión de hoteles dentro del sistema.
 *
 */
public interface HotelService {
    
    /**
     * Guarda un hotel en el sistema.
     * 
     * @param hotel Hotel a guardar
     * @return El hotel guardado
     */
    Hotel save(Hotel hotel);

    /**
     * Verifica si un hotel existe por su identificador.
     * 
     * @param id Identificador del hotel
     * @return true si el hotel existe, false en caso contrario
     */
    boolean existsById(String id);

    /**
     * Busca un hotel por su identificador.
     * 
     * @param id Identificador del hotel a buscar
     * @return Optional que contiene el hotel si se encuentra, o vacío si no existe
     */
    Optional<Hotel> findById(String id);

    /**
     * Recupera todos los hoteles del sistema.
     * 
     * @return Lista de todos los hoteles
     */
    List<Hotel> findAll();

    /**
     * Busca hoteles por su nombre.
     * 
     * @param name Nombre del hotel a buscar.
     * @return Lista de hoteles que coinciden con el nombre proporcionado.
     */
    List<Hotel> findByName(String name);

    /**
     * Elimina un hotel por su identificador.
     * 
     * @param id Identificador del hotel a eliminar
     * @return true si el hotel fue eliminado, false si no existia
     */
    boolean deleteById(String id);
}
