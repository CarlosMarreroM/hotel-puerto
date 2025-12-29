package org.docencia.hotel.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.docencia.hotel.domain.model.Hotel;
import org.docencia.hotel.mapper.jpa.HotelMapper;
import org.docencia.hotel.persistence.jpa.entity.HotelEntity;
import org.docencia.hotel.persistence.repository.jpa.HotelRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HotelServiceImplTest {

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private HotelMapper hotelMapper;

    @InjectMocks
    private HotelServiceImpl service;

    // ===== helpers mínimos =====
    private static Hotel anyHotel() {
        return new Hotel(); // ajusta si no tienes ctor vacío
    }

    private static HotelEntity anyHotelEntity() {
        return new HotelEntity(); // ajusta si no tienes ctor vacío
    }

    // ===================== save =====================

    @Test
    void save_whenHotelIsNull_throwsNullPointerException_andNoInteractions() {
        assertThrows(NullPointerException.class, () -> service.save(null));

        verifyNoInteractions(hotelRepository);
        verifyNoInteractions(hotelMapper);
    }

    @Test
    void save_ok_mapsSavesAndMapsBack() {
        Hotel input = anyHotel();
        HotelEntity toSave = anyHotelEntity();
        HotelEntity savedEntity = anyHotelEntity();
        Hotel expected = anyHotel();

        when(hotelMapper.toEntity(input)).thenReturn(toSave);
        when(hotelRepository.save(toSave)).thenReturn(savedEntity);
        when(hotelMapper.toDomain(savedEntity)).thenReturn(expected);

        Hotel result = service.save(input);

        assertSame(expected, result);
        verify(hotelMapper).toEntity(input);
        verify(hotelRepository).save(toSave);
        verify(hotelMapper).toDomain(savedEntity);
        verifyNoMoreInteractions(hotelRepository, hotelMapper);
    }

    // ===================== existsById =====================

    @Test
    void existsById_whenIdNull_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> service.existsById(null));

        verifyNoInteractions(hotelRepository);
        verifyNoInteractions(hotelMapper);
    }

    @Test
    void existsById_whenIdBlank_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> service.existsById("   "));

        verifyNoInteractions(hotelRepository);
        verifyNoInteractions(hotelMapper);
    }

    @Test
    void existsById_ok_delegatesToRepository() {
        when(hotelRepository.existsById("h1")).thenReturn(true);

        boolean result = service.existsById("h1");

        assertTrue(result);
        verify(hotelRepository).existsById("h1");
        verifyNoMoreInteractions(hotelRepository);
        verifyNoInteractions(hotelMapper);
    }

    // ===================== findById =====================

    @Test
    void findById_whenIdNull_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> service.findById(null));

        verifyNoInteractions(hotelRepository);
        verifyNoInteractions(hotelMapper);
    }

    @Test
    void findById_whenIdBlank_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> service.findById(""));

        verifyNoInteractions(hotelRepository);
        verifyNoInteractions(hotelMapper);
    }

    @Test
    void findById_whenNotFound_returnsEmpty_andDoesNotMap() {
        when(hotelRepository.findById("h1")).thenReturn(Optional.empty());

        Optional<Hotel> result = service.findById("h1");

        assertTrue(result.isEmpty());
        verify(hotelRepository).findById("h1");
        verifyNoMoreInteractions(hotelRepository);
        verifyNoInteractions(hotelMapper);
    }

    @Test
    void findById_whenFound_mapsToDomain() {
        HotelEntity entity = anyHotelEntity();
        Hotel domain = anyHotel();

        when(hotelRepository.findById("h1")).thenReturn(Optional.of(entity));
        when(hotelMapper.toDomain(entity)).thenReturn(domain);

        Optional<Hotel> result = service.findById("h1");

        assertTrue(result.isPresent());
        assertSame(domain, result.get());
        verify(hotelRepository).findById("h1");
        verify(hotelMapper).toDomain(entity);
        verifyNoMoreInteractions(hotelRepository, hotelMapper);
    }

    // ===================== findAll =====================

    @Test
    void findAll_mapsAllEntities() {
        HotelEntity e1 = anyHotelEntity();
        HotelEntity e2 = anyHotelEntity();
        Hotel h1 = anyHotel();
        Hotel h2 = anyHotel();

        when(hotelRepository.findAll()).thenReturn(List.of(e1, e2));
        when(hotelMapper.toDomain(e1)).thenReturn(h1);
        when(hotelMapper.toDomain(e2)).thenReturn(h2);

        List<Hotel> result = service.findAll();

        assertEquals(2, result.size());
        assertSame(h1, result.get(0));
        assertSame(h2, result.get(1));

        verify(hotelRepository).findAll();
        verify(hotelMapper).toDomain(e1);
        verify(hotelMapper).toDomain(e2);
        verifyNoMoreInteractions(hotelRepository, hotelMapper);
    }

    @Test
    void findAll_whenEmpty_returnsEmpty_andDoesNotMap() {
        when(hotelRepository.findAll()).thenReturn(List.of());

        List<Hotel> result = service.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(hotelRepository).findAll();
        verifyNoMoreInteractions(hotelRepository);
        verifyNoInteractions(hotelMapper);
    }

    // ===================== findByName =====================

    @Test
    void findByName_whenNameNull_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> service.findByName(null));

        verifyNoInteractions(hotelRepository);
        verifyNoInteractions(hotelMapper);
    }

    @Test
    void findByName_whenNameBlank_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> service.findByName("   "));

        verifyNoInteractions(hotelRepository);
        verifyNoInteractions(hotelMapper);
    }

    @Test
    void findByName_ok_mapsList() {
        HotelEntity e1 = anyHotelEntity();
        HotelEntity e2 = anyHotelEntity();
        Hotel h1 = anyHotel();
        Hotel h2 = anyHotel();

        when(hotelRepository.findByHotelName("Hilton")).thenReturn(List.of(e1, e2));
        when(hotelMapper.toDomain(e1)).thenReturn(h1);
        when(hotelMapper.toDomain(e2)).thenReturn(h2);

        List<Hotel> result = service.findByName("Hilton");

        assertEquals(List.of(h1, h2), result);

        verify(hotelRepository).findByHotelName("Hilton");
        verify(hotelMapper).toDomain(e1);
        verify(hotelMapper).toDomain(e2);
        verifyNoMoreInteractions(hotelRepository, hotelMapper);
    }

    @Test
    void findByName_whenEmpty_returnsEmpty_andDoesNotMap() {
        when(hotelRepository.findByHotelName("Hilton")).thenReturn(List.of());

        List<Hotel> result = service.findByName("Hilton");

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(hotelRepository).findByHotelName("Hilton");
        verifyNoMoreInteractions(hotelRepository);
        verifyNoInteractions(hotelMapper);
    }

    // ===================== deleteById =====================

    @Test
    void deleteById_whenIdNull_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> service.deleteById(null));

        verifyNoInteractions(hotelRepository);
        verifyNoInteractions(hotelMapper);
    }

    @Test
    void deleteById_whenIdBlank_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> service.deleteById(""));

        verifyNoInteractions(hotelRepository);
        verifyNoInteractions(hotelMapper);
    }

    @Test
    void deleteById_whenNotExists_returnsFalse_andDoesNotDelete() {
        when(hotelRepository.existsById("h404")).thenReturn(false);

        boolean result = service.deleteById("h404");

        assertFalse(result);
        verify(hotelRepository).existsById("h404");
        verify(hotelRepository, never()).deleteById(any());
        verifyNoMoreInteractions(hotelRepository);
        verifyNoInteractions(hotelMapper);
    }

    @Test
    void deleteById_whenExists_deletesAndReturnsTrue() {
        when(hotelRepository.existsById("h1")).thenReturn(true);

        boolean result = service.deleteById("h1");

        assertTrue(result);
        verify(hotelRepository).existsById("h1");
        verify(hotelRepository).deleteById("h1");
        verifyNoMoreInteractions(hotelRepository);
        verifyNoInteractions(hotelMapper);
    }
}
