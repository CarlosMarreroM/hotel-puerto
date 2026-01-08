package org.docencia.hotel.domain.impl;

import java.util.List;
import java.util.Optional;

import org.docencia.hotel.domain.api.GuestDomain;
import org.docencia.hotel.domain.model.Guest;
import org.docencia.hotel.domain.model.GuestPreferences;
import org.docencia.hotel.service.api.BookingService;
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
     * Servicio de reservas.
     */
    private final BookingService bookingService;

    /**
     * Constructor de la clase GuestDomainImpl.
     * 
     * @param guestService Servicio de gestion de huespedes.
     */
    public GuestDomainImpl(GuestService guestService, BookingService bookingService) {
        this.guestService = guestService;
        this.bookingService = bookingService;
    }

    @Override
    public Guest createGuest(Guest guest) {
        Guard.requireNonNull(guest, "guest");
        Guard.requireNonBlank(guest.getId(), "guest id");
        Guard.requireNonBlank(guest.getName(), "guest name");

        if (guestService.existsById(guest.getId())) {
            throw new IllegalStateException("guest already exists: " + guest.getId());
        }

        if (guest.getPreferences() != null) {
            guest.getPreferences().setGuestId(guest.getId());
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

        if (bookingService.existsByGuestId(id)) {
            throw new IllegalArgumentException("cannot delete guest " + id + " because it has bookings");
        }

        guestService.deletePreferencesByGuestId(id);
        return guestService.deleteGuestById(id);
    }

    @Override
    public GuestPreferences updatePreferences(String guestId, GuestPreferences preferences) {
        Guard.requireNonBlank(guestId, "guest id");
        Guard.requireNonNull(preferences, "preferences");

        if (!guestService.existsById(guestId)) {
            throw new IllegalArgumentException("guest not found: " + guestId);
        }

        preferences.setGuestId(guestId);

        return guestService.savedPreferences(preferences);
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

}
