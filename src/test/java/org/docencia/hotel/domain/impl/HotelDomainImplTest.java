package org.docencia.hotel.domain.impl;

import org.docencia.hotel.domain.model.Hotel;
import org.docencia.hotel.service.api.HotelService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelDomainImplTest {

    @Mock
    private HotelService hotelService;

    @InjectMocks
    private HotelDomainImpl hotelDomain;

    @Test
    void createHotel_should_throw_when_hotel_is_null() {
        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> hotelDomain.createHotel(null));

        assertEquals("hotel must not be null", ex.getMessage());
        verifyNoInteractions(hotelService);
    }

    @Test
    void createHotel_should_throw_when_hotel_id_is_null() {
        Hotel hotel = new Hotel(null, "Hotel Puerto", "Calle Mar 123");

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> hotelDomain.createHotel(hotel));

        assertEquals("hotel id must not be blank", ex.getMessage());
        verifyNoInteractions(hotelService);
    }

    @Test
    void createHotel_should_throw_when_hotel_id_is_blank() {
        Hotel hotel = new Hotel("   ", "Hotel Puerto", "Calle Mar 123");

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> hotelDomain.createHotel(hotel));

        assertEquals("hotel id must not be blank", ex.getMessage());
        verifyNoInteractions(hotelService);
    }

    @Test
    void createHotel_should_throw_when_hotel_name_is_null() {
        Hotel hotel = new Hotel("H1", null, "Calle Mar 123");

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> hotelDomain.createHotel(hotel));

        assertEquals("hotel name must not be blank", ex.getMessage());
        verifyNoInteractions(hotelService);
    }

    @Test
    void createHotel_should_throw_when_hotel_name_is_blank() {
        Hotel hotel = new Hotel("H1", "   ", "Calle Mar 123");

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> hotelDomain.createHotel(hotel));

        assertEquals("hotel name must not be blank", ex.getMessage());
        verifyNoInteractions(hotelService);
    }

    @Test
    void createHotel_should_return_existing_hotel_when_it_already_exists() {
        Hotel input = new Hotel("H1", "Hotel Puerto", "Calle Mar 123");
        Hotel existing = new Hotel("H1", "Hotel Puerto", "Calle Mar 123");

        when(hotelService.findById("H1")).thenReturn(Optional.of(existing));

        Hotel result = hotelDomain.createHotel(input);

        assertSame(existing, result);

        verify(hotelService).findById("H1");
        verify(hotelService, never()).save(any());
        verifyNoMoreInteractions(hotelService);
    }

    @Test
    void createHotel_should_save_and_return_hotel_when_it_does_not_exist() {
        Hotel input = new Hotel("H1", "Hotel Puerto", "Calle Mar 123");
        Hotel saved = new Hotel("H1", "Hotel Puerto", "Calle Mar 123");

        when(hotelService.findById("H1")).thenReturn(Optional.empty());
        when(hotelService.save(input)).thenReturn(saved);

        Hotel result = hotelDomain.createHotel(input);

        assertSame(saved, result);

        verify(hotelService).findById("H1");
        verify(hotelService).save(input);
        verifyNoMoreInteractions(hotelService);
    }

    @Test
    void getHotelById_should_throw_when_id_is_null() {
        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> hotelDomain.getHotelById(null));

        assertEquals("hotel id must not be null", ex.getMessage());
        verifyNoInteractions(hotelService);
    }

    @Test
    void getHotelById_should_throw_when_id_is_blank() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> hotelDomain.getHotelById("   "));

        assertEquals("hotel id must not be blank", ex.getMessage());
        verifyNoInteractions(hotelService);
    }

    @Test
    void getHotelById_should_delegate_to_service_when_id_is_valid() {
        Hotel expected = new Hotel("H1", "Hotel Puerto", "Calle Mar 123");
        when(hotelService.findById("H1")).thenReturn(Optional.of(expected));

        Optional<Hotel> result = hotelDomain.getHotelById("H1");

        assertTrue(result.isPresent());
        assertSame(expected, result.get());

        verify(hotelService).findById("H1");
        verifyNoMoreInteractions(hotelService);
    }

    @Test
    void getHotelById_should_return_empty_when_service_returns_empty() {
        when(hotelService.findById("H1")).thenReturn(Optional.empty());

        Optional<Hotel> result = hotelDomain.getHotelById("H1");

        assertTrue(result.isEmpty());

        verify(hotelService).findById("H1");
        verifyNoMoreInteractions(hotelService);
    }

    @Test
    void getAllHotels_should_delegate_to_service() {
        Hotel h1 = new Hotel("H1", "Hotel 1", "Dir 1");
        Hotel h2 = new Hotel("H2", "Hotel 2", "Dir 2");
        List<Hotel> expected = List.of(h1, h2);

        when(hotelService.findAll()).thenReturn(expected);

        List<Hotel> result = hotelDomain.getAllHotels();

        assertSame(expected, result);
        verify(hotelService).findAll();
        verifyNoMoreInteractions(hotelService);
    }

    @Test
    void updateHotel_should_throw_when_id_is_null() {
        Hotel hotel = new Hotel("H1", "Hotel Puerto", "Calle Mar 123");

        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> hotelDomain.updateHotel(null, hotel));

        assertEquals("hotel id must not be null", ex.getMessage());
        verifyNoInteractions(hotelService);
    }

    @Test
    void updateHotel_should_throw_when_hotel_is_null() {
        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> hotelDomain.updateHotel("H1", null));

        assertEquals("hotel must not be null", ex.getMessage());
        verifyNoInteractions(hotelService);
    }

    @Test
    void updateHotel_should_throw_when_id_is_blank() {
        Hotel hotel = new Hotel("H1", "Hotel Puerto", "Calle Mar 123");

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> hotelDomain.updateHotel("   ", hotel));

        assertEquals("hotel id must not be blank", ex.getMessage());
        verifyNoInteractions(hotelService);
    }

    @Test
    void updateHotel_should_throw_when_hotel_name_is_null() {
        Hotel hotel = new Hotel("H1", null, "Calle Mar 123");

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> hotelDomain.updateHotel("H1", hotel));

        assertEquals("hotel name must not be blank", ex.getMessage());
        verifyNoInteractions(hotelService);
    }

    @Test
    void updateHotel_should_throw_when_hotel_name_is_blank() {
        Hotel hotel = new Hotel("H1", "   ", "Calle Mar 123");

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> hotelDomain.updateHotel("H1", hotel));

        assertEquals("hotel name must not be blank", ex.getMessage());
        verifyNoInteractions(hotelService);
    }

    @Test
    void updateHotel_should_throw_when_hotel_does_not_exist() {
        Hotel hotel = new Hotel("H1", "Hotel Puerto", "Calle Mar 123");

        when(hotelService.existsById("H1")).thenReturn(false);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> hotelDomain.updateHotel("H1", hotel));

        assertEquals("hotel not found: H1", ex.getMessage());

        verify(hotelService).existsById("H1");
        verify(hotelService, never()).save(any());
        verifyNoMoreInteractions(hotelService);
    }

    @Test
    void updateHotel_should_set_id_and_save_when_hotel_exists() {
        Hotel hotel = new Hotel(null, "Hotel Puerto", "Calle Mar 123");
        Hotel saved = new Hotel("H1", "Hotel Puerto", "Calle Mar 123");

        when(hotelService.existsById("H1")).thenReturn(true);
        when(hotelService.save(hotel)).thenReturn(saved);

        Hotel result = hotelDomain.updateHotel("H1", hotel);

        assertSame(saved, result);
        assertEquals("H1", hotel.getId());

        verify(hotelService).existsById("H1");
        verify(hotelService).save(hotel);
        verifyNoMoreInteractions(hotelService);
    }

    @Test
    void deleteHotel_should_throw_when_id_is_null() {
        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> hotelDomain.deleteHotel(null));

        assertEquals("hotel id must not be null", ex.getMessage());
        verifyNoInteractions(hotelService);
    }

    @Test
    void deleteHotel_should_throw_when_id_is_blank() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> hotelDomain.deleteHotel("   "));

        assertEquals("hotel id must not be blank", ex.getMessage());
        verifyNoInteractions(hotelService);
    }

    @Test
    void deleteHotel_should_delegate_to_service_when_id_is_valid() {
        when(hotelService.deleteById("H1")).thenReturn(true);

        boolean result = hotelDomain.deleteHotel("H1");

        assertTrue(result);

        verify(hotelService).deleteById("H1");
        verifyNoMoreInteractions(hotelService);
    }

    @Test
    void findHotelsByName_should_throw_when_name_is_null() {
        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> hotelDomain.findHotelsByName(null));

        assertEquals("name must not be null", ex.getMessage());
        verifyNoInteractions(hotelService);
    }

    @Test
    void findHotelsByName_should_throw_when_name_is_blank() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> hotelDomain.findHotelsByName("   "));

        assertEquals("name must not be blank", ex.getMessage());
        verifyNoInteractions(hotelService);
    }

    @Test
    void findHotelsByName_should_delegate_to_service_when_name_is_valid() {
        List<Hotel> expected = List.of(
                new Hotel("H1", "Hotel Puerto", "Dir 1"),
                new Hotel("H2", "Hotel Puerto", "Dir 2"));

        when(hotelService.findByName("Hotel Puerto")).thenReturn(expected);

        List<Hotel> result = hotelDomain.findHotelsByName("Hotel Puerto");

        assertSame(expected, result);

        verify(hotelService).findByName("Hotel Puerto");
        verifyNoMoreInteractions(hotelService);
    }
}
