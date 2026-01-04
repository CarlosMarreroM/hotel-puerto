package org.docencia.hotel.domain.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.docencia.hotel.domain.model.Hotel;
import org.docencia.hotel.service.api.BookingService;
import org.docencia.hotel.service.api.HotelService;
import org.docencia.hotel.service.api.RoomService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HotelDomainImplTest {

    @Mock
    private HotelService hotelService;

    @Mock
    private RoomService roomService;

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private HotelDomainImpl domain;

    // ===== helpers mínimos =====
    private static Hotel hotel(String id, String name) {
        Hotel h = new Hotel();
        h.setId(id);
        h.setHotelName(name);
        return h;
    }

    // ===================== createHotel =====================

    @Test
    void createHotel_whenHotelNull_throwsNullPointerException_andNoInteractions() {
        assertThrows(NullPointerException.class, () -> domain.createHotel(null));
        verifyNoInteractions(hotelService, roomService, bookingService);
    }

    @Test
    void createHotel_whenHotelIdNull_throwsNullPointerException() {
        Hotel h = hotel(null, "Hilton");

        assertThrows(NullPointerException.class, () -> domain.createHotel(h));
        verifyNoInteractions(hotelService, roomService, bookingService);
    }

    @Test
    void createHotel_whenHotelIdBlank_throwsIllegalArgumentException() {
        Hotel h = hotel("   ", "Hilton");

        assertThrows(IllegalArgumentException.class, () -> domain.createHotel(h));
        verifyNoInteractions(hotelService, roomService, bookingService);
    }

    @Test
    void createHotel_whenHotelNameNull_throwsNullPointerException() {
        Hotel h = hotel("h1", null);

        assertThrows(NullPointerException.class, () -> domain.createHotel(h));
        verifyNoInteractions(hotelService, roomService, bookingService);
    }

    @Test
    void createHotel_whenHotelNameBlank_throwsIllegalArgumentException() {
        Hotel h = hotel("h1", "   ");

        assertThrows(IllegalArgumentException.class, () -> domain.createHotel(h));
        verifyNoInteractions(hotelService, roomService, bookingService);
    }

    @Test
    void createHotel_whenHotelAlreadyExists_throwsIllegalStateException_andDoesNotSave() {
        Hotel input = hotel("h1", "Hilton");
        when(hotelService.existsById("h1")).thenReturn(true);

        IllegalStateException ex =
                assertThrows(IllegalStateException.class, () -> domain.createHotel(input));
        assertEquals("hotel already exists: h1", ex.getMessage());

        verify(hotelService).existsById("h1");
        verifyNoMoreInteractions(hotelService);
        verifyNoInteractions(roomService, bookingService);
    }

    @Test
    void createHotel_ok_checksExists_thenDelegatesToHotelServiceSave() {
        Hotel input = hotel("h1", "Hilton");
        Hotel saved = hotel("h1", "Hilton");

        when(hotelService.existsById("h1")).thenReturn(false);
        when(hotelService.save(input)).thenReturn(saved);

        Hotel result = domain.createHotel(input);

        assertSame(saved, result);

        verify(hotelService).existsById("h1");
        verify(hotelService).save(input);
        verifyNoMoreInteractions(hotelService);
        verifyNoInteractions(roomService, bookingService);
    }

    // ===================== getHotelById =====================

    @Test
    void getHotelById_whenIdNull_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> domain.getHotelById(null));
        verifyNoInteractions(hotelService, roomService, bookingService);
    }

    @Test
    void getHotelById_whenIdBlank_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> domain.getHotelById("   "));
        verifyNoInteractions(hotelService, roomService, bookingService);
    }

    @Test
    void getHotelById_ok_delegatesToFindById() {
        Hotel h = hotel("h1", "Hilton");
        when(hotelService.findById("h1")).thenReturn(Optional.of(h));

        Optional<Hotel> result = domain.getHotelById("h1");

        assertTrue(result.isPresent());
        assertSame(h, result.get());

        verify(hotelService).findById("h1");
        verifyNoMoreInteractions(hotelService);
        verifyNoInteractions(roomService, bookingService);
    }

    // ===================== getAllHotels =====================

    @Test
    void getAllHotels_delegatesToFindAll() {
        List<Hotel> expected = List.of(hotel("h1", "Hilton"), hotel("h2", "Ritz"));
        when(hotelService.findAll()).thenReturn(expected);

        List<Hotel> result = domain.getAllHotels();

        assertSame(expected, result);

        verify(hotelService).findAll();
        verifyNoMoreInteractions(hotelService);
        verifyNoInteractions(roomService, bookingService);
    }

    // ===================== getHotelsByName =====================

    @Test
    void getHotelsByName_whenNameNull_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> domain.getHotelsByName(null));
        verifyNoInteractions(hotelService, roomService, bookingService);
    }

    @Test
    void getHotelsByName_whenNameBlank_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> domain.getHotelsByName("   "));
        verifyNoInteractions(hotelService, roomService, bookingService);
    }

    @Test
    void getHotelsByName_ok_delegatesToFindByName() {
        List<Hotel> expected = List.of(hotel("h1", "Hilton"));
        when(hotelService.findByName("Hilton")).thenReturn(expected);

        List<Hotel> result = domain.getHotelsByName("Hilton");

        assertSame(expected, result);

        verify(hotelService).findByName("Hilton");
        verifyNoMoreInteractions(hotelService);
        verifyNoInteractions(roomService, bookingService);
    }

    // ===================== updateHotel =====================

    @Test
    void updateHotel_whenIdNull_throwsNullPointerException() {
        Hotel h = hotel("x", "Hilton");

        assertThrows(NullPointerException.class, () -> domain.updateHotel(null, h));
        verifyNoInteractions(hotelService, roomService, bookingService);
    }

    @Test
    void updateHotel_whenIdBlank_throwsIllegalArgumentException() {
        Hotel h = hotel("x", "Hilton");

        assertThrows(IllegalArgumentException.class, () -> domain.updateHotel("   ", h));
        verifyNoInteractions(hotelService, roomService, bookingService);
    }

    @Test
    void updateHotel_whenHotelNull_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> domain.updateHotel("h1", null));
        verifyNoInteractions(hotelService, roomService, bookingService);
    }

    @Test
    void updateHotel_whenHotelNameNull_throwsNullPointerException() {
        Hotel h = hotel("x", null);

        assertThrows(NullPointerException.class, () -> domain.updateHotel("h1", h));
        verifyNoInteractions(hotelService, roomService, bookingService);
    }

    @Test
    void updateHotel_whenHotelNameBlank_throwsIllegalArgumentException() {
        Hotel h = hotel("x", "   ");

        assertThrows(IllegalArgumentException.class, () -> domain.updateHotel("h1", h));
        verifyNoInteractions(hotelService, roomService, bookingService);
    }

    @Test
    void updateHotel_whenHotelNotExists_throwsIllegalArgumentException_andDoesNotSave() {
        Hotel h = hotel("x", "Hilton");
        when(hotelService.existsById("h404")).thenReturn(false);

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> domain.updateHotel("h404", h));
        assertEquals("hotel not found: h404", ex.getMessage());

        verify(hotelService).existsById("h404");
        verifyNoMoreInteractions(hotelService);
        verifyNoInteractions(roomService, bookingService);
    }

    @Test
    void updateHotel_ok_setsIdAndSaves() {
        Hotel h = hotel("original", "Hilton");
        Hotel saved = hotel("h1", "Hilton");

        when(hotelService.existsById("h1")).thenReturn(true);
        when(hotelService.save(h)).thenReturn(saved);

        Hotel result = domain.updateHotel("h1", h);

        assertEquals("h1", h.getId(), "El dominio debe forzar el id recibido por parámetro");
        assertSame(saved, result);

        verify(hotelService).existsById("h1");
        verify(hotelService).save(h);
        verifyNoMoreInteractions(hotelService);
        verifyNoInteractions(roomService, bookingService);
    }

    // ===================== deleteHotel =====================

    @Test
    void deleteHotel_whenIdNull_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> domain.deleteHotel(null));
        verifyNoInteractions(hotelService, roomService, bookingService);
    }

    @Test
    void deleteHotel_whenIdBlank_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> domain.deleteHotel("   "));
        verifyNoInteractions(hotelService, roomService, bookingService);
    }

    @Test
    void deleteHotel_whenHotelNotExists_returnsFalse_andDoesNotCheckBookings_orDeleteAnything() {
        when(hotelService.existsById("h404")).thenReturn(false);

        boolean result = domain.deleteHotel("h404");

        assertFalse(result);

        verify(hotelService).existsById("h404");
        verifyNoMoreInteractions(hotelService);

        verifyNoInteractions(roomService, bookingService);
    }

    @Test
    void deleteHotel_whenHotelHasBookings_throwsIllegalStateException_andDoesNotDelete() {
        when(hotelService.existsById("h1")).thenReturn(true);
        when(bookingService.existsByHotelId("h1")).thenReturn(true);
        IllegalStateException ex =
                assertThrows(IllegalStateException.class, () -> domain.deleteHotel("h1"));
        assertEquals("cannot delete hotel h1 because it has bookings in its rooms", ex.getMessage());   
        verify(hotelService).existsById("h1");
        verify(bookingService).existsByHotelId("h1");
        verifyNoMoreInteractions(hotelService, bookingService);
        verifyNoInteractions(roomService);
    }

    @Test
    void deleteHotel_whenNoBookings_deletesRoomsThenDeletesHotel_andReturnsResult() {
        when(hotelService.existsById("h1")).thenReturn(true);
        when(bookingService.existsByHotelId("h1")).thenReturn(false);
        when(hotelService.deleteById("h1")).thenReturn(true);

        boolean result = domain.deleteHotel("h1");

        assertTrue(result);

        verify(hotelService).existsById("h1");
        verify(bookingService).existsByHotelId("h1");
        verify(roomService).deleteByHotelId("h1");
        verify(hotelService).deleteById("h1");

        verifyNoMoreInteractions(hotelService, roomService, bookingService);
    }

    @Test
    void deleteHotel_whenNoBookings_deletesRoomsThenDeletesHotel_andReturnsFalseIfServiceReturnsFalse() {
        when(hotelService.existsById("h1")).thenReturn(true);
        when(bookingService.existsByHotelId("h1")).thenReturn(false);
        when(hotelService.deleteById("h1")).thenReturn(false);

        boolean result = domain.deleteHotel("h1");

        assertFalse(result);

        verify(hotelService).existsById("h1");
        verify(bookingService).existsByHotelId("h1");
        verify(roomService).deleteByHotelId("h1");
        verify(hotelService).deleteById("h1");

        verifyNoMoreInteractions(hotelService, roomService, bookingService);
    }
}
