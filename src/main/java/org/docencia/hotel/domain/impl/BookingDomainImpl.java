package org.docencia.hotel.domain.impl;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

import org.docencia.hotel.domain.api.BookingDomain;
import org.docencia.hotel.domain.model.Booking;
import org.docencia.hotel.service.api.BookingService;
import org.docencia.hotel.service.api.GuestService;
import org.docencia.hotel.service.api.HotelService;
import org.docencia.hotel.service.api.RoomService;
import org.docencia.hotel.validation.Guard;
import org.springframework.stereotype.Service;

@Service
public class BookingDomainImpl implements BookingDomain {

    private final BookingService bookingService;
    private final RoomService roomService;
    private final GuestService guestService;
    private final HotelService hotelService;

    public BookingDomainImpl(
            BookingService bookingService,
            RoomService roomService,
            GuestService guestService,
            HotelService hotelService) {
        this.bookingService = bookingService;
        this.roomService = roomService;
        this.guestService = guestService;
        this.hotelService = hotelService;
    }

    @Override
    public Booking createBooking(Booking booking) {
        Guard.requireNonNull(booking, "booking");
        Guard.requireNonBlank(booking.getId(), "booking id");
        Guard.requireNonBlank(booking.getRoomId(), "room id");
        Guard.requireNonBlank(booking.getGuestId(), "guest id");

        requireGuestExists(booking.getGuestId());
        requireRoomExists(booking.getRoomId());
        validateDates(booking.getCheckIn(), booking.getCheckOut());

        if (bookingService.existsById(booking.getId())) {
            throw new IllegalArgumentException("booking already exists: " + booking.getId());
        }

        return bookingService.save(booking);
    }

    @Override
    public Optional<Booking> getBookingById(String id) {
        Guard.requireNonBlank(id, "booking id");

        return bookingService.findById(id);
    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingService.findAll();
    }

    @Override
    public List<Booking> getBookingsByRoomId(String roomId) {
        Guard.requireNonBlank(roomId, "room id");

        requireRoomExists(roomId);

        return bookingService.findAllByRoomId(roomId);
    }

    @Override
    public List<Booking> getBookingsByGuestId(String guestId) {
        Guard.requireNonBlank(guestId, "guest id");

        requireGuestExists(guestId);

        return bookingService.findAllByGuestId(guestId);
    }

    @Override
    public List<Booking> getBookingsByHotelId(String hotelId) {
        Guard.requireNonBlank(hotelId, "hotel id");

        requireHotelExists(hotelId);

        return bookingService.findAllByHotelId(hotelId);
    }

    @Override
    public Booking updateBooking(String id, Booking booking) {
        Guard.requireNonBlank(id, "booking id");
        Guard.requireNonNull(booking, "booking");
        Guard.requireNonBlank(booking.getRoomId(), "room id");
        Guard.requireNonBlank(booking.getGuestId(), "guest id");

        requireBookingExists(id);
        requireGuestExists(booking.getGuestId());
        requireRoomExists(booking.getRoomId());
        validateDates(booking.getCheckIn(), booking.getCheckOut());

        booking.setId(id);

        return bookingService.save(booking);
    }

    @Override
    public boolean deleteBooking(String id) {
        Guard.requireNonBlank(id, "booking id");

        return bookingService.deleteById(id);
    }

    @Override
    public int deleteBookingsByGuestId(String guestId) {
        Guard.requireNonBlank(guestId, "guest id");

        requireGuestExists(guestId);

        return bookingService.deleteByGuestId(guestId);
    }

    @Override
    public int deleteBookingsByRoomId(String roomId) {
        Guard.requireNonBlank(roomId, "room id");

        requireRoomExists(roomId);

        return bookingService.deleteByRoomId(roomId);
    }

    /**
     * Verifica que la reserva exista.
     *
     * @param bookingId Identificador de la reserva.
     */
    private void requireBookingExists(String bookingId) {
        if (!bookingService.existsById(bookingId)) {
            throw new IllegalArgumentException("booking not found: " + bookingId);
        }
    }

    /**
     * Verifica que la habitación exista.
     *
     * @param roomId Identificador de la habitación.
     */
    private void requireRoomExists(String roomId) {
        if (!roomService.existsById(roomId)) {
            throw new IllegalArgumentException("room not found: " + roomId);
        }
    }

    /**
     * Verifica que el huésped exista.
     *
     * @param guestId Identificador del huésped.
     */
    private void requireGuestExists(String guestId) {
        if (!guestService.existsById(guestId)) {
            throw new IllegalArgumentException("guest not found: " + guestId);
        }
    }

    /**
     * Verifica que el hotel exista.
     *
     * @param hotelId Identificador del hotel.
     */
    private void requireHotelExists(String hotelId) {
        if (!hotelService.existsById(hotelId)) {
            throw new IllegalArgumentException("hotel not found: " + hotelId);
        }
    }

    /**
     * Valida que las fechas de check-in y check-out sean correctas.
     *
     * @param checkIn  Fecha de check-in en formato yyyy-MM-dd.
     * @param checkOut Fecha de check-out en formato yyyy-MM-dd.
     */
    private void validateDates(String checkIn, String checkOut) {
        boolean hasCheckIn = checkIn != null && !checkIn.isBlank();
        boolean hasCheckOut = checkOut != null && !checkOut.isBlank();

        if (!hasCheckIn && !hasCheckOut) {
            return;
        }

        if (hasCheckIn != hasCheckOut) {
            throw new IllegalArgumentException("checkIn and checkOut must be provided together");
        }
        try {
            LocalDate in = LocalDate.parse(checkIn);
            LocalDate out = LocalDate.parse(checkOut);

            if (!in.isBefore(out)) {
                throw new IllegalArgumentException("checkIn must be before checkOut");
            }
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("invalid date format. Expected yyyy-MM-dd");
        }
    }
}
