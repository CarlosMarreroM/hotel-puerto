package org.docencia.hotel.persisten.jpa.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.docencia.hotel.persistence.jpa.entity.HotelEntity;

class HotelEntityTest {

    @Test
    void should_be_equal_to_itself() {
        HotelEntity e = new HotelEntity("H1", "Hotel Puerto", "Calle Mar 123");
        assertEquals(e, e);
    }

    @Test
    void should_not_be_equal_to_null() {
        HotelEntity e = new HotelEntity("H1");
        assertNotEquals(e, null);
    }

    @Test
    void should_not_be_equal_to_other_class() {
        HotelEntity e = new HotelEntity("H1");
        assertNotEquals(e, "H1");
    }

    @Test
    void entities_with_same_non_null_id_should_be_equal() {
        HotelEntity e1 = new HotelEntity("H1");
        HotelEntity e2 = new HotelEntity("H1");

        assertEquals(e1, e2);
        assertEquals(e1.hashCode(), e2.hashCode());
    }

    @Test
    void entities_with_different_id_should_not_be_equal() {
        HotelEntity e1 = new HotelEntity("H1");
        HotelEntity e2 = new HotelEntity("H2");

        assertNotEquals(e1, e2);
    }

    @Test
    void entity_with_null_id_should_not_equal_entity_with_non_null_id() {
        HotelEntity e1 = new HotelEntity();      // id null
        HotelEntity e2 = new HotelEntity("H1");  // id non-null

        assertNotEquals(e1, e2);
        assertNotEquals(e2, e1);
    }

    @Test
    void entities_with_null_id_should_not_be_equal() {
        HotelEntity e1 = new HotelEntity();
        HotelEntity e2 = new HotelEntity();

        assertNotEquals(e1, e2); // por tu equals: id != null && ...
    }
}
