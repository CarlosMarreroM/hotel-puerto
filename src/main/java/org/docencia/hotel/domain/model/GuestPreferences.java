package org.docencia.hotel.domain.model;

import java.util.Objects;

/**
 * Clase que representa las preferencias de un huesped en el hotel.
 *
 * Contiene informacion sobre las preferencias del huesped
 * relacionadas con su estancia, como tipo de habitacion,
 * necesidades especiales, etc.
 */
public class GuestPreferences {
    /**
     * Identificador unico del huesped al que pertenecen estas preferencias.
     */
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
    public GuestPreferences() {
    }

    /**
     * Crea una instancia de preferencias de huesped a partir del identificador del huesped.
     * @param guestId
     */
    public GuestPreferences(String guestId) {
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
    public GuestPreferences(String guestId, boolean prefersSmokingRoom, String bedTypePreference,
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
        if (!(obj instanceof GuestPreferences other))
            return false;
        return guestId != null && guestId.equals(other.guestId);
    }
}
