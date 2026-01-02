package org.docencia.hotel.service.api;

import java.util.List;
import java.util.Optional;

import org.docencia.hotel.domain.model.Guest;
import org.docencia.hotel.domain.model.GuestPreferences;

/**
 * Interfaz que define las operaciones del servicio de huespedes.
 *
 * Esta interfaz actúa como un contrato para las funcionalidades
 * relacionadas con la gestión de huespedes dentro del sistema.
 *
 * No contiene detalles de implementación, que serán
 * proporcionados por las clases que implementen esta interfaz.
 */
public interface GuestService {
    /**
     * Guarda un huesped en el sistema.
     * 
     * @param guest Huesped a guardar.
     * @return Huesped guardado.
     */
    Guest save(Guest guest);

    /**
     * Actualiza las preferencias de un huesped.
     * 
     * @param preferences Preferencias del huesped a actualizar.
     * @return Preferencias actualizadas.
     */
    GuestPreferences savedPreferences(GuestPreferences preferences);

    /**
     * Comprueba si un huesped existe por su identificador.
     * 
     * @param id Identificador del huesped.
     * @return true si el huesped existe, false en caso contrario.
     */
    boolean existsById(String id);

    /**
     * Busca un huesped por su identificador.
     * 
     * @param id Identificador del huesped.
     * @return Huesped encontrado, o vacio si no existe.
     */
    Optional<Guest> findGuestById(String id);

    /**
     * Obtiene todos los huespedes del sistema.
     * 
     * @return Lista de todos los huespedes.
     */
    List<Guest> findAllGuests();

    /**
     * Busca las preferencias de un huesped por su identificador.
     * 
     * @param guestId Identificador del huesped.
     * @return Preferencias del huesped encontradas, o vacio si no existen.
     */
    Optional<GuestPreferences> findPreferencesByGuestId(String guestId);

    /**
     * Elimina un huesped por su identificador.
     * 
     * @param id Identificador del huesped a eliminar.
     * @return true si el huesped fue eliminado, false si no existia.
     */
    boolean deleteGuestById(String id);

    /**
     * Elimina las preferencias de un huesped por su identificador.
     * 
     * @param guestId Identificador del huesped cuyas preferencias se van a eliminar.
     * @return true si las preferencias fueron eliminadas, false si no existian.
     */
    boolean deletePreferencesByGuestId(String guestId);
    
}
