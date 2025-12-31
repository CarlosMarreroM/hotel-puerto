package org.docencia.hotel.domain.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.docencia.hotel.domain.model.Booking;
import org.docencia.hotel.service.api.BookingService;
import org.docencia.hotel.service.api.GuestService;
import org.docencia.hotel.service.api.HotelService;
import org.docencia.hotel.service.api.RoomService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookingDomainImplTest {

    @Mock
    private BookingService bookingService;

    @Mock
    private RoomService roomService;

    @Mock
    private GuestService guestService;

    @Mock
    private HotelService hotelService;

    @InjectMocks
    private BookingDomainImpl domain;

    // ===== helpers mínimos =====
    private static Booking booking(String id, String roomId, String guestId, String in, String out) {
        Booking b = new Booking();
        b.setId(id);
        b.setRoomId(roomId);
        b.setGuestId(guestId);
        b.setCheckIn(in);
        b.setCheckOut(out);
        return b;
    }

    private void stubsAllExistForCreateOrUpdate(Booking b) {
        when(guestService.existsById(b.getGuestId())).thenReturn(true);
        when(roomService.existsById(b.getRoomId())).thenReturn(true);
    }

    // ===================== createBooking =====================

    @Test
    void createBooking_whenBookingNull_throwsNullPointerException_andNoInteractions() {
        assertThrows(NullPointerException.class, () -> domain.createBooking(null));
        verifyNoInteractions(bookingService, roomService, guestService, hotelService);
    }

    @Test
    void createBooking_whenIdNull_throwsNullPointerException_andNoInteractions() {
        Booking b = booking(null, "r1", "g1", null, null);

        assertThrows(NullPointerException.class, () -> domain.createBooking(b));
        verifyNoInteractions(bookingService, roomService, guestService, hotelService);
    }

    @Test
    void createBooking_whenIdBlank_throwsIllegalArgumentException_andNoInteractions() {
        Booking b = booking("   ", "r1", "g1", null, null);

        assertThrows(IllegalArgumentException.class, () -> domain.createBooking(b));
        verifyNoInteractions(bookingService, roomService, guestService, hotelService);
    }

    @Test
    void createBooking_whenRoomIdNull_throwsNullPointerException_andNoInteractions() {
        Booking b = booking("b1", null, "g1", null, null);

        assertThrows(NullPointerException.class, () -> domain.createBooking(b));
        verifyNoInteractions(bookingService, roomService, guestService, hotelService);
    }

    @Test
    void createBooking_whenGuestIdNull_throwsNullPointerException_andNoInteractions() {
        Booking b = booking("b1", "r1", null, null, null);

        assertThrows(NullPointerException.class, () -> domain.createBooking(b));
        verifyNoInteractions(bookingService, roomService, guestService, hotelService);
    }

    @Test
    void createBooking_whenGuestDoesNotExist_throwsIllegalArgumentException_andDoesNotSave() {
        Booking b = booking("b1", "r1", "g404", null, null);

        when(guestService.existsById("g404")).thenReturn(false);

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> domain.createBooking(b));
        assertEquals("guest not found: g404", ex.getMessage());

        verify(guestService).existsById("g404");
        verifyNoMoreInteractions(guestService);
        verifyNoInteractions(roomService, bookingService, hotelService);
    }

    @Test
    void createBooking_whenRoomDoesNotExist_throwsIllegalArgumentException_andDoesNotSave() {
        Booking b = booking("b1", "r404", "g1", null, null);

        when(guestService.existsById("g1")).thenReturn(true);
        when(roomService.existsById("r404")).thenReturn(false);

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> domain.createBooking(b));
        assertEquals("room not found: r404", ex.getMessage());

        verify(guestService).existsById("g1");
        verify(roomService).existsById("r404");
        verifyNoMoreInteractions(guestService, roomService);
        verifyNoInteractions(bookingService, hotelService);
    }

    @Test
    void createBooking_whenDatesBothNullOrBlank_allowsAndSaves() {
        Booking b = booking("b1", "r1", "g1", null, null);

        stubsAllExistForCreateOrUpdate(b);
        when(bookingService.existsById("b1")).thenReturn(false);

        Booking saved = booking("b1", "r1", "g1", null, null);
        when(bookingService.save(b)).thenReturn(saved);

        Booking result = domain.createBooking(b);

        assertSame(saved, result);
        verify(guestService).existsById("g1");
        verify(roomService).existsById("r1");
        verify(bookingService).existsById("b1");
        verify(bookingService).save(b);
        verifyNoMoreInteractions(bookingService, roomService, guestService);
        verifyNoInteractions(hotelService);
    }

    @Test
    void createBooking_whenOnlyCheckInProvided_throwsIllegalArgumentException() {
        Booking b = booking("b1", "r1", "g1", "2025-01-01", null);

        stubsAllExistForCreateOrUpdate(b);

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> domain.createBooking(b));
        assertEquals("checkIn and checkOut must be provided together", ex.getMessage());

        verify(guestService).existsById("g1");
        verify(roomService).existsById("r1");
        verifyNoMoreInteractions(roomService, guestService);
        verifyNoInteractions(bookingService, hotelService);
    }

    @Test
    void createBooking_whenOnlyCheckOutProvided_throwsIllegalArgumentException() {
        Booking b = booking("b1", "r1", "g1", null, "2025-01-02");

        stubsAllExistForCreateOrUpdate(b);

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> domain.createBooking(b));
        assertEquals("checkIn and checkOut must be provided together", ex.getMessage());

        verify(guestService).existsById("g1");
        verify(roomService).existsById("r1");
        verifyNoMoreInteractions(roomService, guestService);
        verifyNoInteractions(bookingService, hotelService);
    }

    @Test
    void createBooking_whenInvalidDateFormat_throwsIllegalArgumentException() {
        Booking b = booking("b1", "r1", "g1", "01-01-2025", "02-01-2025");

        stubsAllExistForCreateOrUpdate(b);

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> domain.createBooking(b));
        assertEquals("invalid date format. Expected yyyy-MM-dd", ex.getMessage());

        verify(guestService).existsById("g1");
        verify(roomService).existsById("r1");
        verifyNoMoreInteractions(roomService, guestService);
        verifyNoInteractions(bookingService, hotelService);
    }

    @Test
    void createBooking_whenCheckInNotBeforeCheckOut_throwsIllegalArgumentException() {
        Booking b = booking("b1", "r1", "g1", "2025-01-02", "2025-01-02");

        stubsAllExistForCreateOrUpdate(b);

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> domain.createBooking(b));
        assertEquals("checkIn must be before checkOut", ex.getMessage());

        verify(guestService).existsById("g1");
        verify(roomService).existsById("r1");
        verifyNoMoreInteractions(roomService, guestService);
        verifyNoInteractions(bookingService, hotelService);
    }

    @Test
    void createBooking_whenAlreadyExists_throwsIllegalArgumentException_andDoesNotSave() {
        Booking b = booking("b1", "r1", "g1", "2025-01-01", "2025-01-02");

        stubsAllExistForCreateOrUpdate(b);
        when(bookingService.existsById("b1")).thenReturn(true);

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> domain.createBooking(b));
        assertEquals("booking already exists: b1", ex.getMessage());

        verify(guestService).existsById("g1");
        verify(roomService).existsById("r1");
        verify(bookingService).existsById("b1");
        verify(bookingService, never()).save(any());
        verifyNoMoreInteractions(bookingService, roomService, guestService);
        verifyNoInteractions(hotelService);
    }

    @Test
    void createBooking_ok_withValidDates_saves() {
        Booking b = booking("b1", "r1", "g1", "2025-01-01", "2025-01-02");

        stubsAllExistForCreateOrUpdate(b);
        when(bookingService.existsById("b1")).thenReturn(false);

        Booking saved = booking("b1", "r1", "g1", "2025-01-01", "2025-01-02");
        when(bookingService.save(b)).thenReturn(saved);

        Booking result = domain.createBooking(b);

        assertSame(saved, result);

        verify(guestService).existsById("g1");
        verify(roomService).existsById("r1");
        verify(bookingService).existsById("b1");
        verify(bookingService).save(b);
        verifyNoMoreInteractions(bookingService, roomService, guestService);
        verifyNoInteractions(hotelService);
    }

    // ===================== getBookingById =====================

    @Test
    void getBookingById_whenNull_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> domain.getBookingById(null));
        verifyNoInteractions(bookingService, roomService, guestService, hotelService);
    }

    @Test
    void getBookingById_whenBlank_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> domain.getBookingById("  "));
        verifyNoInteractions(bookingService, roomService, guestService, hotelService);
    }

    @Test
    void getBookingById_ok_delegates() {
        Booking b = booking("b1", "r1", "g1", null, null);
        when(bookingService.findById("b1")).thenReturn(Optional.of(b));

        Optional<Booking> result = domain.getBookingById("b1");

        assertTrue(result.isPresent());
        assertSame(b, result.get());

        verify(bookingService).findById("b1");
        verifyNoMoreInteractions(bookingService);
        verifyNoInteractions(roomService, guestService, hotelService);
    }

    // ===================== getAllBookings =====================

    @Test
    void getAllBookings_delegates() {
        List<Booking> expected = List.of(booking("b1", "r1", "g1", null, null));
        when(bookingService.findAll()).thenReturn(expected);

        List<Booking> result = domain.getAllBookings();

        assertSame(expected, result);
        verify(bookingService).findAll();
        verifyNoMoreInteractions(bookingService);
        verifyNoInteractions(roomService, guestService, hotelService);
    }

    // ===================== getBookingsByRoomId =====================

    @Test
    void getBookingsByRoomId_whenNull_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> domain.getBookingsByRoomId(null));
        verifyNoInteractions(bookingService, roomService, guestService, hotelService);
    }

    @Test
    void getBookingsByRoomId_whenBlank_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> domain.getBookingsByRoomId("  "));
        verifyNoInteractions(bookingService, roomService, guestService, hotelService);
    }

    @Test
    void getBookingsByRoomId_whenRoomNotFound_throwsIllegalArgumentException() {
        when(roomService.existsById("r404")).thenReturn(false);

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> domain.getBookingsByRoomId("r404"));
        assertEquals("room not found: r404", ex.getMessage());

        verify(roomService).existsById("r404");
        verifyNoMoreInteractions(roomService);
        verifyNoInteractions(bookingService, guestService, hotelService);
    }

    @Test
    void getBookingsByRoomId_ok_requiresRoomAndDelegates() {
        when(roomService.existsById("r1")).thenReturn(true);
        List<Booking> expected = List.of(booking("b1", "r1", "g1", null, null));
        when(bookingService.findAllByRoomId("r1")).thenReturn(expected);

        List<Booking> result = domain.getBookingsByRoomId("r1");

        assertSame(expected, result);

        verify(roomService).existsById("r1");
        verify(bookingService).findAllByRoomId("r1");
        verifyNoMoreInteractions(roomService, bookingService);
        verifyNoInteractions(guestService, hotelService);
    }

    // ===================== getBookingsByGuestId =====================

    @Test
    void getBookingsByGuestId_whenGuestNotFound_throwsIllegalArgumentException() {
        when(guestService.existsById("g404")).thenReturn(false);

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> domain.getBookingsByGuestId("g404"));
        assertEquals("guest not found: g404", ex.getMessage());

        verify(guestService).existsById("g404");
        verifyNoMoreInteractions(guestService);
        verifyNoInteractions(bookingService, roomService, hotelService);
    }

    @Test
    void getBookingsByGuestId_ok_requiresGuestAndDelegates() {
        when(guestService.existsById("g1")).thenReturn(true);
        List<Booking> expected = List.of(booking("b1", "r1", "g1", null, null));
        when(bookingService.findAllByGuestId("g1")).thenReturn(expected);

        List<Booking> result = domain.getBookingsByGuestId("g1");

        assertSame(expected, result);

        verify(guestService).existsById("g1");
        verify(bookingService).findAllByGuestId("g1");
        verifyNoMoreInteractions(guestService, bookingService);
        verifyNoInteractions(roomService, hotelService);
    }

    // ===================== getBookingsByHotelId =====================

    @Test
    void getBookingsByHotelId_whenHotelNotFound_throwsIllegalArgumentException() {
        when(hotelService.existsById("h404")).thenReturn(false);

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> domain.getBookingsByHotelId("h404"));
        assertEquals("hotel not found: h404", ex.getMessage());

        verify(hotelService).existsById("h404");
        verifyNoMoreInteractions(hotelService);
        verifyNoInteractions(bookingService, roomService, guestService);
    }

    @Test
    void getBookingsByHotelId_ok_requiresHotelAndDelegates() {
        when(hotelService.existsById("h1")).thenReturn(true);
        List<Booking> expected = List.of(booking("b1", "r1", "g1", null, null));
        when(bookingService.findAllByHotelId("h1")).thenReturn(expected);

        List<Booking> result = domain.getBookingsByHotelId("h1");

        assertSame(expected, result);

        verify(hotelService).existsById("h1");
        verify(bookingService).findAllByHotelId("h1");
        verifyNoMoreInteractions(hotelService, bookingService);
        verifyNoInteractions(roomService, guestService);
    }

    // ===================== updateBooking =====================

    @Test
    void updateBooking_whenBookingNotExists_throwsIllegalArgumentException_andDoesNotSave() {
        Booking b = booking("ignored", "r1", "g1", "2025-01-01", "2025-01-02");

        when(bookingService.existsById("b404")).thenReturn(false);

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> domain.updateBooking("b404", b));
        assertEquals("booking not found: b404", ex.getMessage());

        verify(bookingService).existsById("b404");
        verifyNoMoreInteractions(bookingService);
        verifyNoInteractions(roomService, guestService, hotelService);
    }

    @Test
    void updateBooking_ok_setsId_validatesAndSaves() {
        Booking b = booking("original", "r1", "g1", "2025-01-01", "2025-01-02");

        when(bookingService.existsById("b1")).thenReturn(true);
        when(guestService.existsById("g1")).thenReturn(true);
        when(roomService.existsById("r1")).thenReturn(true);

        Booking saved = booking("b1", "r1", "g1", "2025-01-01", "2025-01-02");
        when(bookingService.save(b)).thenReturn(saved);

        Booking result = domain.updateBooking("b1", b);

        assertEquals("b1", b.getId(), "Debe forzar el id recibido por parámetro");
        assertSame(saved, result);

        verify(bookingService).existsById("b1");
        verify(guestService).existsById("g1");
        verify(roomService).existsById("r1");
        verify(bookingService).save(b);
        verifyNoMoreInteractions(bookingService, roomService, guestService);
        verifyNoInteractions(hotelService);
    }

    @Test
    void updateBooking_whenDatesInvalid_throwsIllegalArgumentException_andDoesNotSave() {
        Booking b = booking("original", "r1", "g1", "2025-01-02", "2025-01-01");

        when(bookingService.existsById("b1")).thenReturn(true);
        when(guestService.existsById("g1")).thenReturn(true);
        when(roomService.existsById("r1")).thenReturn(true);

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> domain.updateBooking("b1", b));
        assertEquals("checkIn must be before checkOut", ex.getMessage());

        verify(bookingService).existsById("b1");
        verify(guestService).existsById("g1");
        verify(roomService).existsById("r1");
        verify(bookingService, never()).save(any());
        verifyNoMoreInteractions(bookingService, roomService, guestService);
        verifyNoInteractions(hotelService);
    }

    // ===================== deleteBooking =====================

    @Test
    void deleteBooking_whenNull_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> domain.deleteBooking(null));
        verifyNoInteractions(bookingService, roomService, guestService, hotelService);
    }

    @Test
    void deleteBooking_whenBlank_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> domain.deleteBooking("  "));
        verifyNoInteractions(bookingService, roomService, guestService, hotelService);
    }

    @Test
    void deleteBooking_delegatesToService() {
        when(bookingService.deleteById("b1")).thenReturn(true);

        boolean result = domain.deleteBooking("b1");

        assertTrue(result);
        verify(bookingService).deleteById("b1");
        verifyNoMoreInteractions(bookingService);
        verifyNoInteractions(roomService, guestService, hotelService);
    }

    // ===================== deleteBookingsByGuestId =====================

    @Test
    void deleteBookingsByGuestId_whenGuestNotFound_throwsIllegalArgumentException() {
        when(guestService.existsById("g404")).thenReturn(false);

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> domain.deleteBookingsByGuestId("g404"));
        assertEquals("guest not found: g404", ex.getMessage());

        verify(guestService).existsById("g404");
        verifyNoMoreInteractions(guestService);
        verifyNoInteractions(bookingService, roomService, hotelService);
    }

    @Test
    void deleteBookingsByGuestId_ok_requiresGuestAndDelegates() {
        when(guestService.existsById("g1")).thenReturn(true);
        when(bookingService.deleteByGuestId("g1")).thenReturn(4);

        int result = domain.deleteBookingsByGuestId("g1");

        assertEquals(4, result);
        verify(guestService).existsById("g1");
        verify(bookingService).deleteByGuestId("g1");
        verifyNoMoreInteractions(guestService, bookingService);
        verifyNoInteractions(roomService, hotelService);
    }

    // ===================== deleteBookingsByRoomId =====================

    @Test
    void deleteBookingsByRoomId_whenRoomNotFound_throwsIllegalArgumentException() {
        when(roomService.existsById("r404")).thenReturn(false);

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class, () -> domain.deleteBookingsByRoomId("r404"));
        assertEquals("room not found: r404", ex.getMessage());

        verify(roomService).existsById("r404");
        verifyNoMoreInteractions(roomService);
        verifyNoInteractions(bookingService, guestService, hotelService);
    }

    @Test
    void deleteBookingsByRoomId_ok_requiresRoomAndDelegates() {
        when(roomService.existsById("r1")).thenReturn(true);
        when(bookingService.deleteByRoomId("r1")).thenReturn(2);

        int result = domain.deleteBookingsByRoomId("r1");

        assertEquals(2, result);
        verify(roomService).existsById("r1");
        verify(bookingService).deleteByRoomId("r1");
        verifyNoMoreInteractions(roomService, bookingService);
        verifyNoInteractions(guestService, hotelService);
    }
}
