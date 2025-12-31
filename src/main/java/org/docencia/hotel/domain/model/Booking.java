package org.docencia.hotel.domain.model;

import java.util.Objects;

/**
 * Representa una reserva dentro del dominio del sistema.
 *
 * Una reserva es una entidad de negocio identificada de forma única
 * y caracterizada por la habitacion reservada, el cliente que realiza
 * la reserva, y las fechas de entrada y salida.
 *
 * Esta clase forma parte del modelo de dominio y no contiene
 * dependencias con capas de persistencia o presentación.
 */
public class Booking {

    /**
     * Identificador unico de la reserva
     */
    private String id;

    /**
     * Identificacion de la habitacion reservada
     */
    private String roomId;

    /**
     * Identificacion del cliente que realiza la reserva
     */
    private String guestId;

    /**
     * Fecha de entrada
     */
    private String checkIn;

    /**
     * Fecha de salida
     */
    private String checkOut;

    /**
     * Constructor por defecto
     */
    public Booking() {
    }

    /**
     * Constructor con id
     * 
     * @param id Identificador unico de la reserva
     */
    public Booking(String id) {
        this.id = id;
    }

    /**
     * Constructor con todos los atributos
     * 
     * @param id Identificador unico de la reserva
     * @param roomId Identificacion de la habitacion reservada
     * @param guestId Identificacion del cliente que realiza la reserva
     * @param checkIn Fecha de entrada
     * @param checkOut Fecha de salida
     */
    public Booking(String id, String roomId, String guestId, String checkIn, String checkOut) {
        this.id = id;
        this.roomId = roomId;
        this.guestId = guestId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getGuestId() {
        return guestId;
    }

    public void setGuestId(String guestId) {
        this.guestId = guestId;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }

    /**
     * Calcula el código hash basado únicamente en el identificador
     * de la reserva.
     *
     * @return hash del reserva.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Dos reservas se consideran iguales si comparten
     * el mismo identificador.
     *
     * @param obj objeto a comparar
     * @return true si ambas reservas tienen el mismo id
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Booking other))
            return false;
        return id != null && id.equals(other.id);
    }
}
