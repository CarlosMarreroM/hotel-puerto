package org.docencia.hotel.persistence.jpa.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

/**
 * Entidad JPA que representa la tabla de hoteles en la base de datos.
 *
 * Esta clase mapea los campos de la tabla "hotel" a atributos
 * de la clase, permitiendo la persistencia y recuperación
 * de datos relacionados con hoteles.
 *
 * Utiliza anotaciones JPA para definir el esquema de la tabla
 * y las restricciones de los campos.
 */
@Entity
@Table(name = "hotel")
public class HotelEntity {
    /**
     * Identificador unico del hotel.
     */
    @Id
    private String id;

    /**
     * Nombre del hotel.
     */
    @Column(name = "name", nullable = false)
    @NotBlank(message = "Hotel name is required")
    private String hotelName;

    /**
     * Direccion del hotel.
     */
    private String address;

    /**
     * Lista de habitaciones del hotel.
     */
    //@OneToMany(mappedBy = "hotel") --- IGNORE ---
    // private List<RoomEntity> rooms = new ArrayList<>();

    /**
     * Constructor por defecto
     */
    public HotelEntity() {
    }

    /**
     * Crea una referencia a un hotel a partir de su identificador.
     *
     * @param id identificador del hotel
     */
    public HotelEntity(String id) {
        this.id = id;
    }

    /**
     * Crea un hotel con sus datos principales.
     *
     * @param id        identificador unico del hotel
     * @param hotelName nombre comercial del hotel
     * @param address   dirección física del hotel
     */
    public HotelEntity(String id, String hotelName, String address) {
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
     * Compara dos hoteles basándose en su identificador.
     *
     * @param obj objeto a comparar
     * @return true si ambos hoteles tienen el mismo id
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof HotelEntity))
            return false;
        HotelEntity that = (HotelEntity) obj;
        return id != null && id.equals(that.id);
    }
}
