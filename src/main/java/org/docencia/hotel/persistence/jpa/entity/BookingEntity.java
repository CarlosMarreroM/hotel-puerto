package org.docencia.hotel.persistence.jpa.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Representa una reserva en la base de datos.
 *
 * Esta clase es una entidad JPA que mapea la tabla "booking"
 * y sus columnas a los atributos de la clase.
 *
 * Contiene relaciones ManyToOne con las entidades RoomEntity
 * y GuestEntity para representar la habitacion reservada
 * y el cliente que realiza la reserva, respectivamente.
 */
@Entity
@Table(name = "booking")
public class BookingEntity {

    /**
     * Identificador unico de la reserva
     */
    @Id
    private String id;

    /**
     * Habitacion reservada
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_id", nullable = false)
    private RoomEntity room;

    /**
     * Cliente que realiza la reserva
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "guest_id", nullable = false)
    private GuestEntity guest;

    /**
     * Fecha de entrada
     */
    @Column(name = "check_in")
    private String checkIn;

    /**
     * Fecha de salida
     */
    @Column(name = "check_out")
    private String checkOut;

    /**
     * Constructor por defecto
     */
    public BookingEntity() {
    }

    /**
     * Constructor con id
     * 
     * @param id Identificador unico de la reserva
     */
    public BookingEntity(String id) {
        this.id = id;
    }

    /**
     * Constructor con todos los atributos
     * 
     * @param id Identificador unico de la reserva
     * @param room Habitacion reservada
     * @param guest Cliente que realiza la reserva
     * @param checkIn Fecha de entrada
     * @param checkOut Fecha de salida
     */
    public BookingEntity(String id, RoomEntity room, GuestEntity guest, String checkIn, String checkOut) {
        this.id = id;
        this.room = room;
        this.guest = guest;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RoomEntity getRoom() {
        return room;
    }

    public void setRoom(RoomEntity room) {
        this.room = room;
    }

    public GuestEntity getGuest() {
        return guest;
    }

    public void setGuest(GuestEntity guest) {
        this.guest = guest;
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
        if (!(obj instanceof BookingEntity other))
            return false;
        return id != null && id.equals(other.id);
    }
}
