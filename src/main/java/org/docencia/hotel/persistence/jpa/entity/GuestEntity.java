package org.docencia.hotel.persistence.jpa.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

/**
 * Entidad JPA que representa la tabla de huespedes en la base de datos.
 *
 * Esta clase mapea los campos de la tabla "guest" a atributos
 * de la clase, permitiendo la persistencia y recuperación
 * de datos relacionados con los huespedes.
 *
 * Utiliza anotaciones JPA para definir el esquema de la tabla
 * y las restricciones de los campos.
 * 
 */
@Entity
@Table(name = "guest")
public class GuestEntity {
    /**
     * Identificador unico del huesped.
     */
    @Id
    private String id;

    /**
     * Nombre completo del huesped
     */
    @Column(name = "full_name", nullable = false)
    @NotBlank(message = "Guest name is required")
    private String name;

    /**
     * Correo electronico del huesped
     */
    private String email;

    /**
     * Telefono del huesped
     */
    private String phone;

    /**
     * Constructor por defecto.
     */
    public GuestEntity() {   
    }

    /**
     * Crea una referencia a un huesped a partir de su identificador.
     *
     * @param id identificador del huesped
     */
    public GuestEntity(String id) {
        this.id = id;
    }

    /**
     * Crea un huesped con todos sus datos.
     *
     * @param id    identificador unico del huesped
     * @param name  nombre completo del huesped
     * @param email correo electronico del huesped
     * @param phone telefono del huesped
     */
    public GuestEntity(String id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Calcula el código hash basado únicamente en el identificador
     * del huesped.
     *
     * @return hash del huesped
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Dos huespedes se consideran iguales si comparten
     * el mismo identificador.
     *
     * @param obj objeto a comparar
     * @return true si ambos huespedes tienen el mismo id
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof GuestEntity other))
            return false;
        return id != null && id.equals(other.id);
    }
}
