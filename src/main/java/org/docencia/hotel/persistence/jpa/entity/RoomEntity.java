package org.docencia.hotel.persistence.jpa.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

/**
 * Entidad JPA que representa una habitacion en el sistema.
 * 
 * Esta clase mapea la tabla "room" de la base de datos
 * y define los atributos y relaciones de una habitacion.
 */
@Entity
@Table(name = "room")
public class RoomEntity {
    /**
     * Identificador unico de las habitaiones
     */
    @Id
    private String id;

    /**
     * Numero de la habitacion
     */
    @Column(nullable = false)
    @NotBlank(message = "Room number is required")
    private String number;

    /**
     * Tipo de habitacion
     */
    private String type;

    /**
     * Precio de la habitacion por noche
     */
    @Column(name = "price_per_night")
    private double pricePerNight;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hotel_id", nullable = false)
    private HotelEntity hotel;

    /**
     * Constructor por defecto
     */
    public RoomEntity() {
    }

    /**
     * Constructor con el id de la habitacion
     * 
     * @param id Identificador unico de la habitacion
     */
    public RoomEntity(String id) {
        this.id = id;
    }

    /**
     * Constructor con todos los atributos
     * 
     * @param id            Identificador unico de la habitacion
     * @param number        Numero de la habitacion
     * @param type          Tipo de habitacion
     * @param pricePerNight Precio de la habitacion por noche
     * @param hotel         Hotel al que pertenece la habitacion
     */
    public RoomEntity(String id, String number, String type, double pricePerNight, HotelEntity hotel) {
        this.id = id;
        this.number = number;
        this.type = type;
        this.pricePerNight = pricePerNight;
        this.hotel = hotel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public HotelEntity getHotel() {
        return hotel;
    }

    public void setHotel(HotelEntity hotel) {
        this.hotel = hotel;
    }

    /**
     * Genera un hash code basado en el identificador unico de la habitacion.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Compara dos objetos RoomEntity basandose en su identificador unico.
     * 
     * @param obj Objeto a comparar
     * @return true si los objetos son iguales, false en caso contrario
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof RoomEntity other))
            return false;
        return id != null && id.equals(other.id);
    }
}
