package org.docencia.hotel.domain.model;

import java.util.Objects;

/**
 * Clase que representa una habitación de hotel.
 * 
 * Una habitación es una entidad de negocio identificada de forma única
 * y caracterizada por su número, tipo, precio por noche y el hotel al que pertenece.
 * 
 * Esta clase forma parte del modelo de dominio y no contiene
 * dependencias con capas de persistencia o presentación.
 */
public class Room {
    /**
     * Identificador unico de las habitaiones
     */
    private String id;

    /**
     * Numero de la habitacion
     */
    private String number;

    /**
     * Tipo de habitacion
     */
    private String type;

    /**
     * Precio de la habitacion por noche
     */
    private double pricePerNight;

    /**
     * Identificador del hotel al que pertenece la habitacion
     */
    private String hotelId;

    /**
     * Constructor por defecto
     */
    public Room() {
    }

    /**
     * Crea una referencia a una habitacion a partir de su identificador.
     *
     * @param id identificador de la habitacion
     */
    public Room(String id) {
        this.id = id;
    }

    /**
     * Crea una habitacion con todos sus datos.
     * 
     * @param id identificador de la habitacion
     * @param number numero de la habitacion
     * @param type tipo de la habitacion
     * @param pricePerNight precio por noche de la habitacion
     * @param hotelId identificador del hotel al que pertenece la habitacion
     */
    public Room(String id, String number, String type, double pricePerNight, String hotelId) {
        this.id = id;
        this.number = number;
        this.type = type;
        this.pricePerNight = pricePerNight;
        this.hotelId = hotelId;
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

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    /**
     * Calcula el código hash basado únicamente en el identificador
     * de la habitación.
     *
     * @return hash de la habitación
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Dos habitaciones se consideran iguales si comparten
     * el mismo identificador.
     *
     * @param obj objeto a comparar
     * @return true si ambas habitaciones tienen el mismo id
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Room other))
            return false;
        return id != null && id.equals(other.id);
    }
}
