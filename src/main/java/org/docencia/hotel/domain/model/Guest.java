package org.docencia.hotel.domain.model;

import java.util.Objects;

/**
 * Representa un huesped dentro del dominio del sistema.
 *
 * Un huesped es una entidad de negocio identificada de forma única
 * y caracterizada por su nombre, correo electronico y telefono.
 *
 * Esta clase forma parte del modelo de dominio y no contiene
 * dependencias con capas de persistencia o presentación.
 */
public class Guest {
    /**
     * Identificador unico del huesped dentro del dominio.
     */
    private String id;

    /**
     * Nombre completo del huesped
     */
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
     * Preferencias del huesped
     */
    private GuestPreferences preferences;

    /**
     * Constructor por defecto.
     */
    public Guest() {   
    }

    /**
     * Crea una referencia a un huesped a partir de su identificador.
     *
     * @param id identificador del huesped
     */
    public Guest(String id) {
        this.id = id;
    }

    /**
     * Crea un huesped con los datos basicos.
     *
     * @param id    identificador unico del huesped
     * @param name  nombre completo del huesped
     * @param email correo electronico del huesped
     * @param phone telefono del huesped
     */
    public Guest(String id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    /**
     * Crea un huesped con todos sus datos.
     *
     * @param id    identificador unico del huesped
     * @param name  nombre completo del huesped
     * @param email correo electronico del huesped
     * @param phone telefono del huesped
     * @param preferences preferencias del huesped
     */
    public Guest(String id, String name, String email, String phone, GuestPreferences preferences) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.preferences = preferences;
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

    public GuestPreferences getPreferences() {
        return preferences;
    }

    public void setPreferences(GuestPreferences preferences) {
        this.preferences = preferences;
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
        if (!(obj instanceof Guest other))
            return false;
        return id != null && id.equals(other.id);
    }    
}
