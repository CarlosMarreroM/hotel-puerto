package org.docencia.hotel.domain.impl;

import java.util.List;
import java.util.Optional;

import org.docencia.hotel.domain.api.GuestDomain;
import org.docencia.hotel.domain.model.Guest;
import org.docencia.hotel.domain.model.GuestPreferences;
import org.docencia.hotel.service.api.GuestService;
import org.docencia.hotel.validation.Guard;
import org.springframework.stereotype.Service;

@Service
public class GuestDomainImpl implements GuestDomain {

    /**
     * Servicio de gestion de huespedes.
     */
    private final GuestService guestService;

    /**
     * Constructor de la clase GuestDomainImpl.
     * 
     * @param guestService Servicio de gestion de huespedes.
     */
    public GuestDomainImpl(GuestService guestService) {
        this.guestService = guestService;
    }

    @Override
    public Guest createGuest(Guest guest) {
        Guard.requireNonNull(guest, "guest");
        Guard.requireNonBlank(guest.getId(), "guest id");
        Guard.requireNonBlank(guest.getName(), "guest name");

        GuestPreferences preferences = guest.getPreferences();

        if (preferences != null) {
            preferences.setGuestId(guest.getId());
        }

        return guestService.save(guest);
    }

    @Override
    public Optional<Guest> getGuestById(String id) {
        Guard.requireNonBlank(id, "guest id");
        return guestService.findGuestById(id);
    }

    @Override
    public List<Guest> getAllGuests() {
        return guestService.findAllGuests();
    }

    @Override
    public Guest updateGuest(String id, Guest guest) {
        Guard.requireNonBlank(id, "guest id");
        Guard.requireNonNull(guest, "guest");
        Guard.requireNonBlank(guest.getName(), "guest name");

        if (!guestService.existsById(id)) {
            throw new IllegalArgumentException("guest not found: " + id);
        }

        guest.setId(id);

        if (guest.getPreferences() != null) {
            guest.getPreferences().setGuestId(id);
        }

        return guestService.save(guest);
    }

    @Override
    public boolean deleteGuest(String id) {
        Guard.requireNonBlank(id, "guest id");

        if (!guestService.existsById(id)) {
            return false;
        }

        guestService.deletePreferencesByGuestId(id);

        return guestService.deleteGuestById(id);
    }

    @Override
    public GuestPreferences UpdatePreferences(String guestId, GuestPreferences preferences) {
        Guard.requireNonBlank(guestId, "guest id");
        Guard.requireNonNull(preferences, "preferences");

        if (!guestService.existsById(guestId)) {
            throw new IllegalArgumentException("guest not found: " + guestId);
        }

        preferences.setGuestId(guestId);

        return guestService.updatePreferences(preferences);
    }

    @Override
    public Optional<GuestPreferences> getPreferencesByGuestId(String guestId) {
        Guard.requireNonBlank(guestId, "guest id");
        return guestService.findPreferencesByGuestId(guestId);
    }

    @Override
    public boolean deletePreferencesByGuestId(String guestId) {
        Guard.requireNonBlank(guestId, "guest id");
        return guestService.deletePreferencesByGuestId(guestId);
    }

    @Override
    public Optional<Guest> getGuestByIdWithPreferences(String id) {
        Guard.requireNonBlank(id, "guest id");

        Optional<Guest> guestOpt = guestService.findGuestById(id);
        if (guestOpt.isEmpty()) {
            return Optional.empty();
        }

        Guest guest = guestOpt.get();
        GuestPreferences prefs = guestService.findPreferencesByGuestId(id).orElse(null);
        guest.setPreferences(prefs);

        return Optional.of(guest);
    }
}
