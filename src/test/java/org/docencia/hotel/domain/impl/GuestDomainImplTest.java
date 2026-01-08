package org.docencia.hotel.domain.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.docencia.hotel.domain.model.Guest;
import org.docencia.hotel.domain.model.GuestPreferences;
import org.docencia.hotel.service.api.BookingService;
import org.docencia.hotel.service.api.GuestService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GuestDomainImplTest {

    @Mock
    private GuestService guestService;

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private GuestDomainImpl domain;

    // ===== helpers mínimos =====
    private static Guest guest(String id, String name) {
        Guest g = new Guest();
        g.setId(id);
        g.setName(name);
        return g;
    }

    private static GuestPreferences prefs(String guestId) {
        GuestPreferences p = new GuestPreferences();
        p.setGuestId(guestId);
        return p;
    }

    // ===================== createGuest =====================

    @Test
    void createGuest_whenGuestNull_throwsNullPointerException_andNoInteractions() {
        assertThrows(NullPointerException.class, () -> domain.createGuest(null));
        verifyNoInteractions(guestService, bookingService);
    }

    @Test
    void createGuest_whenGuestIdNull_throwsNullPointerException_andNoInteractions() {
        Guest g = guest(null, "Ana");

        assertThrows(NullPointerException.class, () -> domain.createGuest(g));
        verifyNoInteractions(guestService, bookingService);
    }

    @Test
    void createGuest_whenGuestIdBlank_throwsIllegalArgumentException_andNoInteractions() {
        Guest g = guest("   ", "Ana");

        assertThrows(IllegalArgumentException.class, () -> domain.createGuest(g));
        verifyNoInteractions(guestService, bookingService);
    }

    @Test
    void createGuest_whenGuestNameNull_throwsNullPointerException_andNoInteractions() {
        Guest g = guest("g1", null);

        assertThrows(NullPointerException.class, () -> domain.createGuest(g));
        verifyNoInteractions(guestService, bookingService);
    }

    @Test
    void createGuest_whenGuestNameBlank_throwsIllegalArgumentException_andNoInteractions() {
        Guest g = guest("g1", "   ");

        assertThrows(IllegalArgumentException.class, () -> domain.createGuest(g));
        verifyNoInteractions(guestService, bookingService);
    }

    @Test
    void createGuest_whenGuestAlreadyExists_throwsIllegalStateException_andDoesNotSave() {
        Guest input = guest("g1", "Ana");
        when(guestService.existsById("g1")).thenReturn(true);

        IllegalStateException ex =
                assertThrows(IllegalStateException.class, () -> domain.createGuest(input));
        assertEquals("guest already exists: g1", ex.getMessage());

        verify(guestService).existsById("g1");
        verifyNoMoreInteractions(guestService);
        verifyNoInteractions(bookingService);
    }

    @Test
    void createGuest_whenNoPreferences_checksExists_thenDelegatesToSave() {
        Guest input = guest("g1", "Ana");
        Guest saved = guest("g1", "Ana");

        when(guestService.existsById("g1")).thenReturn(false);
        when(guestService.save(input)).thenReturn(saved);

        Guest result = domain.createGuest(input);

        assertSame(saved, result);

        verify(guestService).existsById("g1");
        verify(guestService).save(input);
        verifyNoMoreInteractions(guestService);
        verifyNoInteractions(bookingService);
    }

    @Test
    void createGuest_whenPreferencesPresent_setsGuestIdInPreferences_thenDelegatesToSave() {
        Guest input = guest("g1", "Ana");
        GuestPreferences gp = prefs("willBeOverwritten");
        input.setPreferences(gp);

        Guest saved = guest("g1", "Ana");
        when(guestService.existsById("g1")).thenReturn(false);
        when(guestService.save(input)).thenReturn(saved);

        Guest result = domain.createGuest(input);

        assertEquals("g1", gp.getGuestId(), "Debe forzar guestId en preferences al crear");
        assertSame(saved, result);

        verify(guestService).existsById("g1");
        verify(guestService).save(input);
        verifyNoMoreInteractions(guestService);
        verifyNoInteractions(bookingService);
    }

    // ===================== getGuestById =====================

    @Test
    void getGuestById_whenIdNull_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> domain.getGuestById(null));
        verifyNoInteractions(guestService, bookingService);
    }

    @Test
    void getGuestById_whenIdBlank_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> domain.getGuestById("  "));
        verifyNoInteractions(guestService, bookingService);
    }

    @Test
    void getGuestById_ok_delegates() {
        Guest g = guest("g1", "Ana");
        when(guestService.findGuestById("g1")).thenReturn(Optional.of(g));

        Optional<Guest> result = domain.getGuestById("g1");

        assertTrue(result.isPresent());
        assertSame(g, result.get());

        verify(guestService).findGuestById("g1");
        verifyNoMoreInteractions(guestService);
        verifyNoInteractions(bookingService);
    }

    // ===================== getAllGuests =====================

    @Test
    void getAllGuests_delegates() {
        List<Guest> expected = List.of(guest("g1", "Ana"));
        when(guestService.findAllGuests()).thenReturn(expected);

        List<Guest> result = domain.getAllGuests();

        assertSame(expected, result);

        verify(guestService).findAllGuests();
        verifyNoMoreInteractions(guestService);
        verifyNoInteractions(bookingService);
    }

    // ===================== updateGuest =====================

    @Test
    void updateGuest_whenIdNull_throwsNullPointerException() {
        Guest g = guest("x", "Ana");

        assertThrows(NullPointerException.class, () -> domain.updateGuest(null, g));
        verifyNoInteractions(guestService, bookingService);
    }

    @Test
    void updateGuest_whenIdBlank_throwsIllegalArgumentException() {
        Guest g = guest("x", "Ana");

        assertThrows(IllegalArgumentException.class, () -> domain.updateGuest("  ", g));
        verifyNoInteractions(guestService, bookingService);
    }

    @Test
    void updateGuest_whenGuestNull_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> domain.updateGuest("g1", null));
        verifyNoInteractions(guestService, bookingService);
    }

    @Test
    void updateGuest_whenNameNull_throwsNullPointerException() {
        Guest g = guest("x", null);

        assertThrows(NullPointerException.class, () -> domain.updateGuest("g1", g));
        verifyNoInteractions(guestService, bookingService);
    }

    @Test
    void updateGuest_whenNameBlank_throwsIllegalArgumentException() {
        Guest g = guest("x", "   ");

        assertThrows(IllegalArgumentException.class, () -> domain.updateGuest("g1", g));
        verifyNoInteractions(guestService, bookingService);
    }

    @Test
    void updateGuest_whenGuestNotExists_throwsIllegalArgumentException_andDoesNotSave() {
        Guest g = guest("x", "Ana");
        when(guestService.existsById("g404")).thenReturn(false);

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> domain.updateGuest("g404", g));
        assertEquals("guest not found: g404", ex.getMessage());

        verify(guestService).existsById("g404");
        verifyNoMoreInteractions(guestService);
        verifyNoInteractions(bookingService);
    }

    @Test
    void updateGuest_ok_setsId_andSaves() {
        Guest g = guest("original", "Ana");
        Guest saved = guest("g1", "Ana");

        when(guestService.existsById("g1")).thenReturn(true);
        when(guestService.save(g)).thenReturn(saved);

        Guest result = domain.updateGuest("g1", g);

        assertEquals("g1", g.getId(), "Debe forzar el id recibido por parámetro");
        assertSame(saved, result);

        verify(guestService).existsById("g1");
        verify(guestService).save(g);
        verifyNoMoreInteractions(guestService);
        verifyNoInteractions(bookingService);
    }

    @Test
    void updateGuest_ok_whenPreferencesPresent_setsGuestIdInPreferences() {
        Guest g = guest("original", "Ana");
        GuestPreferences gp = prefs("willBeOverwritten");
        g.setPreferences(gp);

        when(guestService.existsById("g1")).thenReturn(true);
        when(guestService.save(g)).thenReturn(g);

        domain.updateGuest("g1", g);

        assertEquals("g1", g.getId());
        assertEquals("g1", gp.getGuestId(), "Debe forzar guestId en preferences al actualizar");

        verify(guestService).existsById("g1");
        verify(guestService).save(g);
        verifyNoMoreInteractions(guestService);
        verifyNoInteractions(bookingService);
    }

    // ===================== deleteGuest =====================

    @Test
    void deleteGuest_whenIdNull_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> domain.deleteGuest(null));
        verifyNoInteractions(guestService, bookingService);
    }

    @Test
    void deleteGuest_whenIdBlank_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> domain.deleteGuest("  "));
        verifyNoInteractions(guestService, bookingService);
    }

    @Test
    void deleteGuest_whenGuestNotExists_returnsFalse_andDoesNotCheckBookings_orDeleteAnything() {
        when(guestService.existsById("g404")).thenReturn(false);

        boolean result = domain.deleteGuest("g404");

        assertFalse(result);

        verify(guestService).existsById("g404");
        verifyNoMoreInteractions(guestService);
        verifyNoInteractions(bookingService);
    }

    @Test
    void deleteGuest_whenHasBookings_throwsIllegalArgumentException_andDoesNotDeleteAnything() {
        when(guestService.existsById("g1")).thenReturn(true);
        when(bookingService.existsByGuestId("g1")).thenReturn(true);

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> domain.deleteGuest("g1"));
        assertEquals("cannot delete guest g1 because it has bookings", ex.getMessage());

        verify(guestService).existsById("g1");
        verify(bookingService).existsByGuestId("g1");

        verify(guestService, never()).deletePreferencesByGuestId(any());
        verify(guestService, never()).deleteGuestById(any());

        verifyNoMoreInteractions(guestService, bookingService);
    }

    @Test
    void deleteGuest_whenGuestExists_andNoBookings_deletesPreferences_thenDeletesGuest_andReturnsResult() {
        when(guestService.existsById("g1")).thenReturn(true);
        when(bookingService.existsByGuestId("g1")).thenReturn(false);
        when(guestService.deleteGuestById("g1")).thenReturn(true);

        boolean result = domain.deleteGuest("g1");

        assertTrue(result);

        verify(guestService).existsById("g1");
        verify(bookingService).existsByGuestId("g1");
        verify(guestService).deletePreferencesByGuestId("g1");
        verify(guestService).deleteGuestById("g1");

        verifyNoMoreInteractions(guestService, bookingService);
    }

    // ===================== UpdatePreferences =====================

    @Test
    void updatePreferences_whenGuestIdNull_throwsNullPointerException() {
        GuestPreferences p = prefs("x");

        assertThrows(NullPointerException.class, () -> domain.updatePreferences(null, p));
        verifyNoInteractions(guestService, bookingService);
    }

    @Test
    void updatePreferences_whenGuestIdBlank_throwsIllegalArgumentException() {
        GuestPreferences p = prefs("x");

        assertThrows(IllegalArgumentException.class, () -> domain.updatePreferences("  ", p));
        verifyNoInteractions(guestService, bookingService);
    }

    @Test
    void updatePreferences_whenPreferencesNull_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> domain.updatePreferences("g1", null));
        verifyNoInteractions(guestService, bookingService);
    }

    @Test
    void updatePreferences_whenGuestNotExists_throwsIllegalArgumentException_andDoesNotUpdate() {
        GuestPreferences p = prefs("x");
        when(guestService.existsById("g404")).thenReturn(false);

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> domain.updatePreferences("g404", p));
        assertEquals("guest not found: g404", ex.getMessage());

        verify(guestService).existsById("g404");
        verifyNoMoreInteractions(guestService);
        verifyNoInteractions(bookingService);
    }

    @Test
    void updatePreferences_ok_setsGuestId_thenDelegatesToServiceSavedPreferences() {
        GuestPreferences p = prefs("willBeOverwritten");
        GuestPreferences expected = prefs("g1");

        when(guestService.existsById("g1")).thenReturn(true);
        when(guestService.savedPreferences(p)).thenReturn(expected);

        GuestPreferences result = domain.updatePreferences("g1", p);

        assertEquals("g1", p.getGuestId(), "Debe forzar guestId antes de actualizar");
        assertSame(expected, result);

        verify(guestService).existsById("g1");
        verify(guestService).savedPreferences(p);
        verifyNoMoreInteractions(guestService);
        verifyNoInteractions(bookingService);
    }

    // ===================== getPreferencesByGuestId =====================

    @Test
    void getPreferencesByGuestId_whenNull_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> domain.getPreferencesByGuestId(null));
        verifyNoInteractions(guestService, bookingService);
    }

    @Test
    void getPreferencesByGuestId_whenBlank_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> domain.getPreferencesByGuestId("  "));
        verifyNoInteractions(guestService, bookingService);
    }

    @Test
    void getPreferencesByGuestId_ok_delegates() {
        GuestPreferences p = prefs("g1");
        when(guestService.findPreferencesByGuestId("g1")).thenReturn(Optional.of(p));

        Optional<GuestPreferences> result = domain.getPreferencesByGuestId("g1");

        assertTrue(result.isPresent());
        assertSame(p, result.get());

        verify(guestService).findPreferencesByGuestId("g1");
        verifyNoMoreInteractions(guestService);
        verifyNoInteractions(bookingService);
    }

    // ===================== deletePreferencesByGuestId =====================

    @Test
    void deletePreferencesByGuestId_whenNull_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> domain.deletePreferencesByGuestId(null));
        verifyNoInteractions(guestService, bookingService);
    }

    @Test
    void deletePreferencesByGuestId_whenBlank_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> domain.deletePreferencesByGuestId("  "));
        verifyNoInteractions(guestService, bookingService);
    }

    @Test
    void deletePreferencesByGuestId_ok_delegates() {
        when(guestService.deletePreferencesByGuestId("g1")).thenReturn(true);

        boolean result = domain.deletePreferencesByGuestId("g1");

        assertTrue(result);

        verify(guestService).deletePreferencesByGuestId("g1");
        verifyNoMoreInteractions(guestService);
        verifyNoInteractions(bookingService);
    }
}
