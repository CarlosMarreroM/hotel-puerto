package org.docencia.hotel.domain.api;

import java.util.List;
import java.util.Optional;

import org.docencia.hotel.domain.model.Guest;
import org.docencia.hotel.domain.model.GuestPreferences;

/**
 * Interfaz que define las operaciones del dominio de huespedes.
 *
 * Esta interfaz actúa como un contrato para las funcionalidades
 * relacionadas con la gestión de huespedes dentro del sistema.
 *
 * No contiene detalles de implementación, que serán
 * proporcionados por las clases que implementen esta interfaz.
 */
public interface GuestDomain {

    /**
     * Crea un nuevo huesped en el sistema.
     * 
     * @param guest Huesped a crear.
     * @return Huesped creado.
     */
    Guest createGuest(Guest guest);

    /**
     * Obtiene un huesped por su identificador.
     * 
     * @param id Identificador del huesped.
     * @return Huesped encontrado, o vacio si no existe.
     */
    Optional<Guest> getGuestById(String id);

    /**
     * Obtiene todos los huespedes del sistema.
     * 
     * @return Lista de todos los huespedes.
     */
    List<Guest> getAllGuests();

    /**
     * Actualiza los datos de un huesped.
     * 
     * @param id    Identificador del huesped a actualizar.
     * @param guest Nuevos datos del huesped.
     * @return Huesped actualizado, o vacio si no existe.
     */
    Guest updateGuest(String id, Guest guest);

    /**
     * Elimina un huesped por su identificador.
     * 
     * @param id Identificador del huesped a eliminar.
     * @return true si el huesped fue eliminado, false si no existia.
     */
    boolean deleteGuest(String id);
    
    /**
     * Actualiza las preferencias de un huesped.
     * 
     * @param guestId     Identificador del huesped.
     * @param preferences Preferencias a guardar o actualizar.
     * @return Preferencias guardadas o actualizadas.
     */
    GuestPreferences updatePreferences(String guestId, GuestPreferences preferences);

    /**
     * Obtiene las preferencias de un huesped por su identificador.
     * 
     * @param guestId Identificador del huesped.
     * @return Preferencias del huesped encontradas, o vacio si no existen.
     */
    Optional<GuestPreferences> getPreferencesByGuestId(String guestId);

    /**
     * Elimina las preferencias de un huesped por su identificador.
     * 
     * @param guestId Identificador del huesped cuyas preferencias se van a eliminar.
     * @return true si las preferencias fueron eliminadas, false si no existian.
     */
    boolean deletePreferencesByGuestId(String guestId);

}
