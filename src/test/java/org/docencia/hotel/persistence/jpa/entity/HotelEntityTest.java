package org.docencia.hotel.persistence.jpa.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class HotelEntityTest {

    // ===================== equals =====================

    @Test
    void equals_sameReference_true() {
        HotelEntity h = new HotelEntity("h1");
        assertEquals(h, h);
    }

    @Test
    void equals_null_false() {
        HotelEntity h = new HotelEntity("h1");
        assertNotEquals(h, null);
    }

    @Test
    void equals_differentType_false() {
        HotelEntity h = new HotelEntity("h1");
        assertNotEquals(h, "not a HotelEntity");
    }

    @Test
    void equals_sameId_true() {
        HotelEntity a = new HotelEntity("h1");
        HotelEntity b = new HotelEntity("h1");

        assertEquals(a, b);
        assertEquals(b, a);
    }

    @Test
    void equals_differentId_false() {
        HotelEntity a = new HotelEntity("h1");
        HotelEntity b = new HotelEntity("h2");

        assertNotEquals(a, b);
        assertNotEquals(b, a);
    }

    @Test
    void equals_whenIdNull_falseEvenIfBothNull() {
        HotelEntity a = new HotelEntity();
        HotelEntity b = new HotelEntity();

        assertNotEquals(a, b);
    }

    @Test
    void equals_oneNullId_false() {
        HotelEntity a = new HotelEntity();
        HotelEntity b = new HotelEntity("h1");

        assertNotEquals(a, b);
        assertNotEquals(b, a);
    }

    // ===================== hashCode =====================

    @Test
    void hashCode_sameId_sameHash() {
        HotelEntity a = new HotelEntity("h1");
        HotelEntity b = new HotelEntity("h1");

        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void hashCode_differentId_differentHashUsually() {
        HotelEntity a = new HotelEntity("h1");
        HotelEntity b = new HotelEntity("h2");

        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void hashCode_nullId_isStable() {
        HotelEntity a = new HotelEntity();
        int h1 = a.hashCode();
        int h2 = a.hashCode();

        assertEquals(h1, h2);
    }
}

