package org.docencia.hotel.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HotelTest {
    @Test
    void should_create_hotel_with_id_only() {

        Hotel hotel = new Hotel("H1");

        assertEquals("H1", hotel.getId());
        assertNull(hotel.getHotelName());
        assertNull(hotel.getAddress());
    }

    @Test
    void should_create_hotel_with_name_and_address() {

        Hotel hotel = new Hotel("Hotel Puerto", "Calle Mar 123");

        assertEquals("Hotel Puerto", hotel.getHotelName());
        assertEquals("Calle Mar 123", hotel.getAddress());
        assertNull(hotel.getId());
    }

    @Test
    void hotels_with_same_id_should_be_equal() {

        Hotel h1 = new Hotel("H1");
        Hotel h2 = new Hotel("H1");

        assertEquals(h1, h2);
    }

    @Test
    void hotels_with_different_id_should_not_be_equal() {

        Hotel h1 = new Hotel("H1");
        Hotel h2 = new Hotel("H2");

        assertNotEquals(h1, h2);
    }

    @Test
    void hotel_should_not_be_equal_to_null() {

        Hotel hotel = new Hotel("H1");

        assertNotEquals(hotel, null);
    }

    @Test
    void hotel_should_not_be_equal_to_other_class() {

        Hotel hotel = new Hotel("H1");

        assertNotEquals(hotel, "H1");
    }

    @Test
    void equal_hotels_should_have_same_hashcode() {

        Hotel h1 = new Hotel("H1");
        Hotel h2 = new Hotel("H1");

        assertEquals(h1.hashCode(), h2.hashCode());
    }

    @Test
    void hotel_should_be_equal_to_itself() {
        Hotel hotel = new Hotel("H1", "Hotel Puerto", "Calle Mar 123");

        assertEquals(hotel, hotel);
    }

    @Test
    void hotel_with_null_id_should_not_be_equal_to_hotel_with_non_null_id() {
        Hotel h1 = new Hotel(); // id == null
        Hotel h2 = new Hotel("H1"); // id != null

        assertNotEquals(h1, h2); // cubre: if (id == null) { if (other.id != null) return false; }
    }

}
