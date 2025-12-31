package org.docencia.hotel.persistence.jpa.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BookingEntityTest {

    // ===================== equals =====================

    @Test
    void equals_sameReference_true() {
        BookingEntity b = new BookingEntity("b1");
        assertEquals(b, b);
    }

    @Test
    void equals_null_false() {
        BookingEntity b = new BookingEntity("b1");
        assertNotEquals(b, null);
    }

    @Test
    void equals_differentType_false() {
        BookingEntity b = new BookingEntity("b1");
        assertNotEquals(b, "not a BookingEntity");
    }

    @Test
    void equals_sameId_true() {
        BookingEntity a = new BookingEntity("b1");
        BookingEntity c = new BookingEntity("b1");

        assertEquals(a, c);
        assertEquals(c, a);
    }

    @Test
    void equals_differentId_false() {
        BookingEntity a = new BookingEntity("b1");
        BookingEntity c = new BookingEntity("b2");

        assertNotEquals(a, c);
        assertNotEquals(c, a);
    }

    @Test
    void equals_whenIdNull_falseEvenIfBothNull() {
        BookingEntity a = new BookingEntity();
        BookingEntity c = new BookingEntity();

        assertNotEquals(a, c);
    }

    @Test
    void equals_oneNullId_false() {
        BookingEntity a = new BookingEntity();
        BookingEntity c = new BookingEntity("b1");

        assertNotEquals(a, c);
        assertNotEquals(c, a);
    }

    // ===================== hashCode =====================

    @Test
    void hashCode_sameId_sameHash() {
        BookingEntity a = new BookingEntity("b1");
        BookingEntity c = new BookingEntity("b1");

        assertEquals(a.hashCode(), c.hashCode());
    }

    @Test
    void hashCode_differentId_differentHashUsually() {
        BookingEntity a = new BookingEntity("b1");
        BookingEntity c = new BookingEntity("b2");

        // Colisiones son posibles en teoría, pero con Strings distintas aquí es estable en la práctica.
        assertNotEquals(a.hashCode(), c.hashCode());
    }

    @Test
    void hashCode_nullId_isStable() {
        BookingEntity a = new BookingEntity(); // id null
        int h1 = a.hashCode();
        int h2 = a.hashCode();

        assertEquals(h1, h2);
    }
}
