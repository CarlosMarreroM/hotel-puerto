package org.docencia.hotel.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BookingTest {

    // ===================== equals =====================

    @Test
    void equals_sameReference_true() {
        Booking b = new Booking("b1");
        assertEquals(b, b);
    }

    @Test
    void equals_null_false() {
        Booking b = new Booking("b1");
        assertNotEquals(b, null);
    }

    @Test
    void equals_differentType_false() {
        Booking b = new Booking("b1");
        assertNotEquals(b, "not a booking");
    }

    @Test
    void equals_sameId_true() {
        Booking a = new Booking("b1");
        Booking c = new Booking("b1");

        assertEquals(a, c);
        assertEquals(c, a);
    }

    @Test
    void equals_differentId_false() {
        Booking a = new Booking("b1");
        Booking c = new Booking("b2");

        assertNotEquals(a, c);
        assertNotEquals(c, a);
    }

    @Test
    void equals_whenIdNull_falseEvenIfBothNull() {
        Booking a = new Booking();
        Booking c = new Booking();

        // tu equals exige id != null
        assertNotEquals(a, c);
    }

    @Test
    void equals_oneNullId_false() {
        Booking a = new Booking();
        Booking c = new Booking("b1");

        assertNotEquals(a, c);
        assertNotEquals(c, a);
    }

    // ===================== hashCode =====================

    @Test
    void hashCode_sameId_sameHash() {
        Booking a = new Booking("b1");
        Booking c = new Booking("b1");

        assertEquals(a.hashCode(), c.hashCode());
    }

    @Test
    void hashCode_differentId_differentHashUsually() {
        Booking a = new Booking("b1");
        Booking c = new Booking("b2");

        // Colisiones son posibles en teoría, pero con Strings distintas aquí es estable en la práctica.
        assertNotEquals(a.hashCode(), c.hashCode());
    }

    @Test
    void hashCode_nullId_isStable() {
        Booking a = new Booking(); // id null
        int h1 = a.hashCode();
        int h2 = a.hashCode();

        assertEquals(h1, h2);
    }
}

