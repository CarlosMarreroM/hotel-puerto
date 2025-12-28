package org.docencia.hotel.service.impl;

import org.docencia.hotel.domain.model.Hotel;
import org.docencia.hotel.mapper.jpa.HotelMapper;
import org.docencia.hotel.persistence.jpa.entity.HotelEntity;
import org.docencia.hotel.persistence.repository.jpa.HotelRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class HotelServiceImplTest {

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private HotelMapper hotelMapper;

    @InjectMocks
    private HotelServiceImpl hotelService;

    @Test
    void save_should_map_save_and_return_domain() {
        Hotel input = new Hotel("H1", "Hotel Puerto", "Calle Mar 123");

        HotelEntity entityToSave = mock(HotelEntity.class);
        HotelEntity savedEntity = mock(HotelEntity.class);

        Hotel expected = new Hotel("H1", "Hotel Puerto", "Calle Mar 123");

        when(hotelMapper.toEntity(input)).thenReturn(entityToSave);
        when(hotelRepository.save(entityToSave)).thenReturn(savedEntity);
        when(hotelMapper.toDomain(savedEntity)).thenReturn(expected);

        Hotel result = hotelService.save(input);

        assertSame(expected, result);

        verify(hotelMapper).toEntity(input);
        verify(hotelRepository).save(entityToSave);
        verify(hotelMapper).toDomain(savedEntity);
        verifyNoMoreInteractions(hotelRepository, hotelMapper);
    }

    @Test
    void save_should_throw_when_hotel_is_null() {
        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> hotelService.save(null));

        assertEquals("hotel must not be null", ex.getMessage());
        verifyNoInteractions(hotelRepository, hotelMapper);
    }

    @Test
    void existsById_should_throw_when_id_is_null() {
        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> hotelService.existsById(null));

        assertEquals("hotel id must not be null", ex.getMessage());
        verifyNoInteractions(hotelRepository, hotelMapper);
    }

    @Test
    void existsById_should_throw_when_id_is_blank() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> hotelService.existsById("   "));

        assertEquals("hotel id must not be blank", ex.getMessage());
        verifyNoInteractions(hotelRepository, hotelMapper);
    }

    @Test
    void existsById_should_delegate_to_repository_when_id_is_valid() {
        when(hotelRepository.existsById("H1")).thenReturn(true);

        boolean result = hotelService.existsById("H1");

        assertTrue(result);

        verify(hotelRepository).existsById("H1");
        verifyNoMoreInteractions(hotelRepository);
        verifyNoInteractions(hotelMapper);
    }

    @Test
    void findById_should_throw_when_id_is_null() {
        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> hotelService.findById(null));

        assertEquals("hotel id must not be null", ex.getMessage());
        verifyNoInteractions(hotelRepository, hotelMapper);
    }

    @Test
    void findById_should_throw_when_id_is_blank() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> hotelService.findById("   "));

        assertEquals("hotel id must not be blank", ex.getMessage());
        verifyNoInteractions(hotelRepository, hotelMapper);
    }

    @Test
    void findById_should_return_empty_when_repository_returns_empty() {
        when(hotelRepository.findById("H1")).thenReturn(Optional.empty());

        Optional<Hotel> result = hotelService.findById("H1");

        assertTrue(result.isEmpty());

        verify(hotelRepository).findById("H1");
        verifyNoInteractions(hotelMapper);
        verifyNoMoreInteractions(hotelRepository);
    }

    @Test
    void findById_should_return_mapped_domain_when_entity_exists() {
        HotelEntity entity = mock(HotelEntity.class);
        Hotel expected = new Hotel("H1", "Hotel Puerto", "Calle Mar 123");

        when(hotelRepository.findById("H1")).thenReturn(Optional.of(entity));
        when(hotelMapper.toDomain(entity)).thenReturn(expected);

        Optional<Hotel> result = hotelService.findById("H1");

        assertTrue(result.isPresent());
        assertSame(expected, result.get());

        verify(hotelRepository).findById("H1");
        verify(hotelMapper).toDomain(entity);
        verifyNoMoreInteractions(hotelRepository, hotelMapper);
    }

    @Test
    void findAll_should_return_empty_list_when_repository_returns_empty() {
        when(hotelRepository.findAll()).thenReturn(List.of());

        List<Hotel> result = hotelService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(hotelRepository).findAll();
        verifyNoInteractions(hotelMapper);
        verifyNoMoreInteractions(hotelRepository);
    }

    @Test
    void findAll_should_map_all_entities_to_domain() {
        HotelEntity e1 = mock(HotelEntity.class);
        HotelEntity e2 = mock(HotelEntity.class);

        Hotel d1 = new Hotel("H1", "Hotel 1", "Dir 1");
        Hotel d2 = new Hotel("H2", "Hotel 2", "Dir 2");

        when(hotelRepository.findAll()).thenReturn(List.of(e1, e2));
        when(hotelMapper.toDomain(e1)).thenReturn(d1);
        when(hotelMapper.toDomain(e2)).thenReturn(d2);

        List<Hotel> result = hotelService.findAll();

        assertEquals(2, result.size());
        assertSame(d1, result.get(0));
        assertSame(d2, result.get(1));

        verify(hotelRepository).findAll();
        verify(hotelMapper).toDomain(e1);
        verify(hotelMapper).toDomain(e2);
        verifyNoMoreInteractions(hotelRepository, hotelMapper);
    }

    @Test
    void findByName_should_throw_when_name_is_null() {
        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> hotelService.findByName(null));

        assertEquals("name must not be null", ex.getMessage());
        verifyNoInteractions(hotelRepository, hotelMapper);
    }

    @Test
    void findByName_should_throw_when_name_is_blank() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> hotelService.findByName("   "));

        assertEquals("name must not be blank", ex.getMessage());
        verifyNoInteractions(hotelRepository, hotelMapper);
    }

    @Test
    void findByName_should_return_empty_list_when_repository_returns_empty() {
        when(hotelRepository.findByHotelName("Hotel Puerto")).thenReturn(List.of());

        List<Hotel> result = hotelService.findByName("Hotel Puerto");

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(hotelRepository).findByHotelName("Hotel Puerto");
        verifyNoInteractions(hotelMapper);
        verifyNoMoreInteractions(hotelRepository);
    }

    @Test
    void findByName_should_map_all_entities_to_domain() {
        HotelEntity e1 = mock(HotelEntity.class);
        HotelEntity e2 = mock(HotelEntity.class);

        Hotel d1 = new Hotel("H1", "Hotel Puerto", "Dir 1");
        Hotel d2 = new Hotel("H2", "Hotel Puerto", "Dir 2");

        when(hotelRepository.findByHotelName("Hotel Puerto")).thenReturn(List.of(e1, e2));
        when(hotelMapper.toDomain(e1)).thenReturn(d1);
        when(hotelMapper.toDomain(e2)).thenReturn(d2);

        List<Hotel> result = hotelService.findByName("Hotel Puerto");

        assertEquals(2, result.size());
        assertSame(d1, result.get(0));
        assertSame(d2, result.get(1));

        verify(hotelRepository).findByHotelName("Hotel Puerto");
        verify(hotelMapper).toDomain(e1);
        verify(hotelMapper).toDomain(e2);
        verifyNoMoreInteractions(hotelRepository, hotelMapper);
    }

    @Test
    void deleteById_should_throw_when_id_is_null() {
        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> hotelService.deleteById(null));

        assertEquals("hotel id must not be null", ex.getMessage());
        verifyNoInteractions(hotelRepository, hotelMapper);
    }

    @Test
    void deleteById_should_throw_when_id_is_blank() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> hotelService.deleteById(" \t"));

        assertEquals("hotel id must not be blank", ex.getMessage());
        verifyNoInteractions(hotelRepository, hotelMapper);
    }

    @Test
    void deleteById_should_return_false_when_hotel_does_not_exist() {
        when(hotelRepository.existsById("H1")).thenReturn(false);

        boolean result = hotelService.deleteById("H1");

        assertFalse(result);

        verify(hotelRepository).existsById("H1");
        verify(hotelRepository, never()).deleteById(anyString());
        verifyNoInteractions(hotelMapper);
        verifyNoMoreInteractions(hotelRepository);
    }

    @Test
    void deleteById_should_delete_and_return_true_when_hotel_exists() {
        when(hotelRepository.existsById("H1")).thenReturn(true);

        boolean result = hotelService.deleteById("H1");

        assertTrue(result);

        verify(hotelRepository).existsById("H1");
        verify(hotelRepository).deleteById("H1");
        verifyNoInteractions(hotelMapper);
        verifyNoMoreInteractions(hotelRepository);
    }
}