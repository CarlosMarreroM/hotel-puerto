package org.docencia.hotel.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.docencia.hotel.domain.model.Booking;
import org.docencia.hotel.mapper.jpa.BookingMapper;
import org.docencia.hotel.persistence.jpa.entity.BookingEntity;
import org.docencia.hotel.persistence.repository.jpa.BookingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private BookingMapper bookingMapper;

    @InjectMocks
    private BookingServiceImpl service;

    // ===== helpers mínimos =====
    private static Booking anyBooking() {
        return new Booking(); // asumes ctor vacío
    }

    private static BookingEntity anyBookingEntity() {
        return new BookingEntity(); // asumes ctor vacío
    }

    // ===================== save =====================

    @Test
    void save_whenBookingNull_throwsNullPointerException_andNoInteractions() {
        assertThrows(NullPointerException.class, () -> service.save(null));
        verifyNoInteractions(bookingRepository, bookingMapper);
    }

    @Test
    void save_ok_mapsSavesAndMapsBack() {
        Booking input = anyBooking();
        BookingEntity toSave = anyBookingEntity();
        BookingEntity savedEntity = anyBookingEntity();
        Booking expected = anyBooking();

        when(bookingMapper.toEntity(input)).thenReturn(toSave);
        when(bookingRepository.save(toSave)).thenReturn(savedEntity);
        when(bookingMapper.toDomain(savedEntity)).thenReturn(expected);

        Booking result = service.save(input);

        assertSame(expected, result);
        verify(bookingMapper).toEntity(input);
        verify(bookingRepository).save(toSave);
        verify(bookingMapper).toDomain(savedEntity);

        verifyNoMoreInteractions(bookingRepository, bookingMapper);
    }

    // ===================== existsById =====================

    @Test
    void existsById_whenIdNull_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> service.existsById(null));
        verifyNoInteractions(bookingRepository, bookingMapper);
    }

    @Test
    void existsById_whenIdBlank_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> service.existsById("   "));
        verifyNoInteractions(bookingRepository, bookingMapper);
    }

    @Test
    void existsById_ok_delegates() {
        when(bookingRepository.existsById("b1")).thenReturn(true);

        boolean result = service.existsById("b1");

        assertTrue(result);
        verify(bookingRepository).existsById("b1");
        verifyNoMoreInteractions(bookingRepository);
        verifyNoInteractions(bookingMapper);
    }

    // ===================== existsByGuestId =====================

    @Test
    void existsByGuestId_whenNull_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> service.existsByGuestId(null));
        verifyNoInteractions(bookingRepository, bookingMapper);
    }

    @Test
    void existsByGuestId_whenBlank_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> service.existsByGuestId("  "));
        verifyNoInteractions(bookingRepository, bookingMapper);
    }

    @Test
    void existsByGuestId_ok_delegates() {
        when(bookingRepository.existsByGuestId("g1")).thenReturn(true);

        boolean result = service.existsByGuestId("g1");

        assertTrue(result);
        verify(bookingRepository).existsByGuestId("g1");
        verifyNoMoreInteractions(bookingRepository);
        verifyNoInteractions(bookingMapper);
    }

    // ===================== existsByRoomId =====================

    @Test
    void existsByRoomId_whenNull_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> service.existsByRoomId(null));
        verifyNoInteractions(bookingRepository, bookingMapper);
    }

    @Test
    void existsByRoomId_whenBlank_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> service.existsByRoomId("  "));
        verifyNoInteractions(bookingRepository, bookingMapper);
    }

    @Test
    void existsByRoomId_ok_delegates() {
        when(bookingRepository.existsByRoomId("r1")).thenReturn(true);

        boolean result = service.existsByRoomId("r1");

        assertTrue(result);
        verify(bookingRepository).existsByRoomId("r1");
        verifyNoMoreInteractions(bookingRepository);
        verifyNoInteractions(bookingMapper);
    }

    // ===================== existsByHotelId =====================

    @Test
    void existsByHotelId_whenNull_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> service.existsByHotelId(null));
        verifyNoInteractions(bookingRepository, bookingMapper);
    }

    @Test
    void existsByHotelId_whenBlank_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> service.existsByHotelId("  "));
        verifyNoInteractions(bookingRepository, bookingMapper);
    }

    @Test
    void existsByHotelId_ok_delegatesToExistsByRoomHotelId() {
        when(bookingRepository.existsByRoomHotelId("h1")).thenReturn(true);

        boolean result = service.existsByHotelId("h1");

        assertTrue(result);
        verify(bookingRepository).existsByRoomHotelId("h1");
        verifyNoMoreInteractions(bookingRepository);
        verifyNoInteractions(bookingMapper);
    }

    // ===================== findAll =====================

    @Test
    void findAll_whenEmpty_returnsEmpty_andDoesNotMap() {
        when(bookingRepository.findAll()).thenReturn(List.of());

        List<Booking> result = service.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(bookingRepository).findAll();
        verifyNoMoreInteractions(bookingRepository);
        verifyNoInteractions(bookingMapper);
    }

    @Test
    void findAll_mapsAllEntities() {
        BookingEntity e1 = anyBookingEntity();
        BookingEntity e2 = anyBookingEntity();
        Booking b1 = anyBooking();
        Booking b2 = anyBooking();

        when(bookingRepository.findAll()).thenReturn(List.of(e1, e2));
        when(bookingMapper.toDomain(e1)).thenReturn(b1);
        when(bookingMapper.toDomain(e2)).thenReturn(b2);

        List<Booking> result = service.findAll();

        assertEquals(2, result.size());
        assertSame(b1, result.get(0));
        assertSame(b2, result.get(1));

        verify(bookingRepository).findAll();
        verify(bookingMapper).toDomain(e1);
        verify(bookingMapper).toDomain(e2);
        verifyNoMoreInteractions(bookingRepository, bookingMapper);
    }

    // ===================== findById =====================

    @Test
    void findById_whenNull_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> service.findById(null));
        verifyNoInteractions(bookingRepository, bookingMapper);
    }

    @Test
    void findById_whenBlank_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> service.findById("  "));
        verifyNoInteractions(bookingRepository, bookingMapper);
    }

    @Test
    void findById_whenNotFound_returnsEmpty_andDoesNotMap() {
        when(bookingRepository.findById("b1")).thenReturn(Optional.empty());

        Optional<Booking> result = service.findById("b1");

        assertTrue(result.isEmpty());
        verify(bookingRepository).findById("b1");
        verifyNoMoreInteractions(bookingRepository);
        verifyNoInteractions(bookingMapper);
    }

    @Test
    void findById_whenFound_mapsToDomain() {
        BookingEntity entity = anyBookingEntity();
        Booking domain = anyBooking();

        when(bookingRepository.findById("b1")).thenReturn(Optional.of(entity));
        when(bookingMapper.toDomain(entity)).thenReturn(domain);

        Optional<Booking> result = service.findById("b1");

        assertTrue(result.isPresent());
        assertSame(domain, result.get());

        verify(bookingRepository).findById("b1");
        verify(bookingMapper).toDomain(entity);
        verifyNoMoreInteractions(bookingRepository, bookingMapper);
    }

    // ===================== findAllByRoomId =====================

    @Test
    void findAllByRoomId_whenNull_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> service.findAllByRoomId(null));
        verifyNoInteractions(bookingRepository, bookingMapper);
    }

    @Test
    void findAllByRoomId_whenBlank_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> service.findAllByRoomId("  "));
        verifyNoInteractions(bookingRepository, bookingMapper);
    }

    @Test
    void findAllByRoomId_ok_mapsList() {
        BookingEntity e1 = anyBookingEntity();
        BookingEntity e2 = anyBookingEntity();
        Booking b1 = anyBooking();
        Booking b2 = anyBooking();

        when(bookingRepository.findByRoomId("r1")).thenReturn(List.of(e1, e2));
        when(bookingMapper.toDomain(e1)).thenReturn(b1);
        when(bookingMapper.toDomain(e2)).thenReturn(b2);

        List<Booking> result = service.findAllByRoomId("r1");

        assertEquals(List.of(b1, b2), result);

        verify(bookingRepository).findByRoomId("r1");
        verify(bookingMapper).toDomain(e1);
        verify(bookingMapper).toDomain(e2);
        verifyNoMoreInteractions(bookingRepository, bookingMapper);
    }

    // ===================== findAllByGuestId =====================

    @Test
    void findAllByGuestId_whenNull_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> service.findAllByGuestId(null));
        verifyNoInteractions(bookingRepository, bookingMapper);
    }

    @Test
    void findAllByGuestId_whenBlank_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> service.findAllByGuestId("  "));
        verifyNoInteractions(bookingRepository, bookingMapper);
    }

    @Test
    void findAllByGuestId_ok_mapsList() {
        BookingEntity e1 = anyBookingEntity();
        BookingEntity e2 = anyBookingEntity();
        Booking b1 = anyBooking();
        Booking b2 = anyBooking();

        when(bookingRepository.findByGuestId("g1")).thenReturn(List.of(e1, e2));
        when(bookingMapper.toDomain(e1)).thenReturn(b1);
        when(bookingMapper.toDomain(e2)).thenReturn(b2);

        List<Booking> result = service.findAllByGuestId("g1");

        assertEquals(List.of(b1, b2), result);

        verify(bookingRepository).findByGuestId("g1");
        verify(bookingMapper).toDomain(e1);
        verify(bookingMapper).toDomain(e2);
        verifyNoMoreInteractions(bookingRepository, bookingMapper);
    }

    // ===================== findAllByHotelId =====================

    @Test
    void findAllByHotelId_whenNull_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> service.findAllByHotelId(null));
        verifyNoInteractions(bookingRepository, bookingMapper);
    }

    @Test
    void findAllByHotelId_whenBlank_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> service.findAllByHotelId("  "));
        verifyNoInteractions(bookingRepository, bookingMapper);
    }

    @Test
    void findAllByHotelId_ok_mapsList() {
        BookingEntity e1 = anyBookingEntity();
        BookingEntity e2 = anyBookingEntity();
        Booking b1 = anyBooking();
        Booking b2 = anyBooking();

        when(bookingRepository.findByRoomHotelId("h1")).thenReturn(List.of(e1, e2));
        when(bookingMapper.toDomain(e1)).thenReturn(b1);
        when(bookingMapper.toDomain(e2)).thenReturn(b2);

        List<Booking> result = service.findAllByHotelId("h1");

        assertEquals(List.of(b1, b2), result);

        verify(bookingRepository).findByRoomHotelId("h1");
        verify(bookingMapper).toDomain(e1);
        verify(bookingMapper).toDomain(e2);
        verifyNoMoreInteractions(bookingRepository, bookingMapper);
    }

    // ===================== deleteById =====================

    @Test
    void deleteById_whenNull_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> service.deleteById(null));
        verifyNoInteractions(bookingRepository, bookingMapper);
    }

    @Test
    void deleteById_whenBlank_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> service.deleteById("  "));
        verifyNoInteractions(bookingRepository, bookingMapper);
    }

    @Test
    void deleteById_whenNotExists_returnsFalse_andDoesNotDelete() {
        when(bookingRepository.existsById("b404")).thenReturn(false);

        boolean result = service.deleteById("b404");

        assertFalse(result);
        verify(bookingRepository).existsById("b404");
        verify(bookingRepository, never()).deleteById(any());
        verifyNoMoreInteractions(bookingRepository);
        verifyNoInteractions(bookingMapper);
    }

    @Test
    void deleteById_whenExists_deletesAndReturnsTrue() {
        when(bookingRepository.existsById("b1")).thenReturn(true);

        boolean result = service.deleteById("b1");

        assertTrue(result);
        verify(bookingRepository).existsById("b1");
        verify(bookingRepository).deleteById("b1");
        verifyNoMoreInteractions(bookingRepository);
        verifyNoInteractions(bookingMapper);
    }

    // ===================== deleteByGuestId =====================

    @Test
    void deleteByGuestId_whenNull_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> service.deleteByGuestId(null));
        verifyNoInteractions(bookingRepository, bookingMapper);
    }

    @Test
    void deleteByGuestId_whenBlank_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> service.deleteByGuestId("  "));
        verifyNoInteractions(bookingRepository, bookingMapper);
    }

    @Test
    void deleteByGuestId_ok_delegatesAndReturnsCount() {
        when(bookingRepository.deleteByGuestId("g1")).thenReturn(3);

        int result = service.deleteByGuestId("g1");

        assertEquals(3, result);
        verify(bookingRepository).deleteByGuestId("g1");
        verifyNoMoreInteractions(bookingRepository);
        verifyNoInteractions(bookingMapper);
    }

    // ===================== deleteByRoomId =====================

    @Test
    void deleteByRoomId_whenNull_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> service.deleteByRoomId(null));
        verifyNoInteractions(bookingRepository, bookingMapper);
    }

    @Test
    void deleteByRoomId_whenBlank_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> service.deleteByRoomId("  "));
        verifyNoInteractions(bookingRepository, bookingMapper);
    }

    @Test
    void deleteByRoomId_ok_delegatesAndReturnsCount() {
        when(bookingRepository.deleteByRoomId("r1")).thenReturn(2);

        int result = service.deleteByRoomId("r1");

        assertEquals(2, result);
        verify(bookingRepository).deleteByRoomId("r1");
        verifyNoMoreInteractions(bookingRepository);
        verifyNoInteractions(bookingMapper);
    }
}
