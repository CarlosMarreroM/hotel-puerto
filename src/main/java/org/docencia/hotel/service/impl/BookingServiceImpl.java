package org.docencia.hotel.service.impl;

import java.util.List;
import java.util.Optional;

import org.docencia.hotel.domain.model.Booking;
import org.docencia.hotel.mapper.jpa.BookingMapper;
import org.docencia.hotel.persistence.jpa.entity.BookingEntity;
import org.docencia.hotel.persistence.repository.jpa.BookingRepository;
import org.docencia.hotel.service.api.BookingService;
import org.docencia.hotel.validation.Guard;
import org.springframework.stereotype.Service;

@Service
public class BookingServiceImpl implements BookingService {
    /**
     * Repositorio JPA de reservas.
     */
    private final BookingRepository bookingRepository;

    /**
     * Mapeador entre entidad JPA y modelo de dominio.
     */
    private final BookingMapper bookingMapper;

    public BookingServiceImpl(BookingRepository bookingRepository, BookingMapper bookingMapper) {
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
    }

    @Override
    public Booking save(Booking booking) {
        Guard.requireNonNull(booking, "booking");

        BookingEntity entityToSave = bookingMapper.toEntity(booking);
        BookingEntity saved = bookingRepository.save(entityToSave);

        return bookingMapper.toDomain(saved);
    }

    @Override
    public boolean existsById(String id) {
        Guard.requireNonBlank(id, "booking id");

        return bookingRepository.existsById(id);
    }

    @Override
    public boolean existsByGuestId(String guestId) {
        Guard.requireNonBlank(guestId, "guest id");
        return bookingRepository.existsByGuestId(guestId);
    }

    @Override
    public boolean existsByRoomId(String roomId) {
        Guard.requireNonBlank(roomId, "room id");
        return bookingRepository.existsByRoomId(roomId);
    }

    @Override
    public List<Booking> findAll() {
        return bookingRepository.findAll()
                .stream()
                .map(bookingMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Booking> findById(String id) {
        Guard.requireNonBlank(id, "booking id");

        return bookingRepository.findById(id)
                .map(bookingMapper::toDomain);
    }

    @Override
    public List<Booking> findAllByRoomId(String roomId) {
        Guard.requireNonBlank(roomId, "room id");

        return bookingRepository.findByRoomId(roomId)
                .stream()
                .map(bookingMapper::toDomain)
                .toList();
    }

    @Override
    public List<Booking> findAllByGuestId(String guestId) {
        Guard.requireNonBlank(guestId, "guest id");

        return bookingRepository.findByGuestId(guestId)
                .stream()
                .map(bookingMapper::toDomain)
                .toList();
    }

    @Override
    public List<Booking> findAllByHotelId(String hotelId) {
        Guard.requireNonBlank(hotelId, "hotel id");

        return bookingRepository.findByRoomHotelId(hotelId)
                .stream()
                .map(bookingMapper::toDomain)
                .toList();
    }

    @Override
    public boolean deleteById(String id) {
        Guard.requireNonBlank(id, "booking id");

        if (!bookingRepository.existsById(id)) {
            return false;
        }

        bookingRepository.deleteById(id);
        return true;
    }

    @Override
    public int deleteByGuestId(String guestId) {
        Guard.requireNonBlank(guestId, "guest id");

        return bookingRepository.deleteByGuestId(guestId);
    }

    @Override
    public int deleteByRoomId(String roomId) {
        Guard.requireNonBlank(roomId, "room id");

        return bookingRepository.deleteByRoomId(roomId);
    }
}