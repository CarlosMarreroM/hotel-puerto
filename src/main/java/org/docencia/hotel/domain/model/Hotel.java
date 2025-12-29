package org.docencia.hotel.domain.model;

import java.util.Objects;

/**
 * Representa un hotel dentro del dominio del sistema.
 *
 * Un hotel es una entidad de negocio identificada de forma única
 * y caracterizada por su nombre, dirección y categoría.
 *
 * Esta clase forma parte del modelo de dominio y no contiene
 * dependencias con capas de persistencia o presentación.
 */
public class Hotel {
    /**
     * Identificador único del hotel dentro del dominio.
     */
    private String id;

    /**
     * Nombre del hotel.
     */
    private String hotelName;

    /**
     * Dirección física del hotel.
     */
    private String address;

    /**
     * Constructor por defecto.
     */
    public Hotel() {
    }

    /**
     * Crea una referencia a un hotel a partir de su identificador.
     *
     * @param id identificador del hotel
     */
    public Hotel(String id) {
        this.id = id;
    }

    /**
     * Crea un hotel con sus datos principales.
     *
     * @param hotelName nombre comercial del hotel
     * @param address   dirección física del hotel
     */
    public Hotel(String hotelName, String address) {
        this.hotelName = hotelName;
        this.address = address;
    }

    /**
     * Crea un hotel con todos sus datos.
     *
     * @param id        identificador único del hotel
     * @param hotelName nombre comercial del hotel
     * @param address   dirección física del hotel
     */
    public Hotel(String id, String hotelName, String address) {
        this.id = id;
        this.hotelName = hotelName;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    /**
     * Calcula el código hash basado únicamente en el identificador
     * del hotel.
     *
     * @return hash del hotel
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Dos hoteles se consideran iguales si comparten
     * el mismo identificador.
     *
     * @param obj objeto a comparar
     * @return true si ambos hoteles tienen el mismo id
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Hotel other))
            return false;
        return id != null && id.equals(other.id);
    }
}
