package org.docencia.hotel.persistence.nosql.document;

import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Documento NoSQL que representa las preferencias de un huesped
 * en la base de datos MongoDB.
 * 
 * Esta clase mapea los campos del documento "guest_preferences" a
 * atributos de la clase, permitiendo la persistencia y recuperacion
 * de datos relacionados con las preferencias de los huespedes.
 * 
 * Utiliza anotaciones de Spring Data MongoDB para definir el esquema
 * del documento.
 */
@Document(collection = "guest_preferences")
public class GuestPreferencesDocument {

    /**
     * Identificador unico del huesped al que pertenecen estas preferencias.
     */
    @Id
    private String guestId;

    /**
     * Huesped prefiere habitacion para fumadores.
     */
    private boolean prefersSmokingRoom;

    /**
     * Tipo de cama preferida por el huesped.
     */
    private String bedTypePreference;

    /**
     * Huesped necesita caracteristicas de accesibilidad.
     */
    private boolean needsAccessibilityFeatures;

    /**
     * Constructor por defecto.
     */
    public GuestPreferencesDocument() {
    }

    /**
     * Crea una instancia de preferencias de huesped a partir del identificador del huesped.
     * @param guestId
     */
    public GuestPreferencesDocument(String guestId) {
        this.guestId = guestId;
    }

    /**
     * Crea una instancia completa de preferencias de huesped.
     * 
     * @param guestId                     Identificador unico del huesped.
     * @param prefersSmokingRoom          Indica si el huesped prefiere habitacion para fumadores.
     * @param bedTypePreference           Tipo de cama preferida por el huesped.
     * @param needsAccessibilityFeatures  Indica si el huesped necesita caracteristicas de accesibilidad.
     */
    public GuestPreferencesDocument(String guestId, boolean prefersSmokingRoom, String bedTypePreference,
            boolean needsAccessibilityFeatures) {
        this.guestId = guestId;
        this.prefersSmokingRoom = prefersSmokingRoom;
        this.bedTypePreference = bedTypePreference;
        this.needsAccessibilityFeatures = needsAccessibilityFeatures;
    }

    public String getGuestId() {
        return guestId;
    }

    public void setGuestId(String guestId) {
        this.guestId = guestId;
    }

    public boolean isPrefersSmokingRoom() {
        return prefersSmokingRoom;
    }

    public void setPrefersSmokingRoom(boolean prefersSmokingRoom) {
        this.prefersSmokingRoom = prefersSmokingRoom;
    }

    public String getBedTypePreference() {
        return bedTypePreference;
    }

    public void setBedTypePreference(String bedTypePreference) {
        this.bedTypePreference = bedTypePreference;
    }

    public boolean isNeedsAccessibilityFeatures() {
        return needsAccessibilityFeatures;
    }

    public void setNeedsAccessibilityFeatures(boolean needsAccessibilityFeatures) {
        this.needsAccessibilityFeatures = needsAccessibilityFeatures;
    }

    /**
     * Calcula el código hash basado únicamente en el identificador
     * del huesped.
     *
     * @return hash del huesped
     */
    @Override
    public int hashCode() {
        return Objects.hash(guestId);
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
        if (!(obj instanceof GuestPreferencesDocument other))
            return false;
        return guestId != null && guestId.equals(other.guestId);
    }
}
