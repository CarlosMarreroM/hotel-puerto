package org.docencia.hotel.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class GuestTest {

    // ===================== equals =====================

    @Test
    void equals_sameReference_true() {
        Guest g = new Guest("g1");
        assertEquals(g, g);
    }

    @Test
    void equals_null_false() {
        Guest g = new Guest("g1");
        assertNotEquals(g, null);
    }

    @Test
    void equals_differentType_false() {
        Guest g = new Guest("g1");
        assertNotEquals(g, "not a guest");
    }

    @Test
    void equals_sameId_true() {
        Guest a = new Guest("g1");
        Guest b = new Guest("g1");

        assertEquals(a, b);
        assertEquals(b, a);
    }

    @Test
    void equals_differentId_false() {
        Guest a = new Guest("g1");
        Guest b = new Guest("g2");

        assertNotEquals(a, b);
        assertNotEquals(b, a);
    }

    @Test
    void equals_whenIdNull_falseEvenIfBothNull() {
        Guest a = new Guest();
        Guest b = new Guest();

        // tu equals exige id != null
        assertNotEquals(a, b);
    }

    @Test
    void equals_oneNullId_false() {
        Guest a = new Guest();
        Guest b = new Guest("g1");

        assertNotEquals(a, b);
        assertNotEquals(b, a);
    }

    // ===================== hashCode =====================

    @Test
    void hashCode_sameId_sameHash() {
        Guest a = new Guest("g1");
        Guest b = new Guest("g1");

        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void hashCode_differentId_differentHashUsually() {
        Guest a = new Guest("g1");
        Guest b = new Guest("g2");

        // Colisiones son posibles en teoría, pero con Strings distintas aquí es estable en la práctica.
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void hashCode_nullId_isStable() {
        Guest a = new Guest(); // id null
        int h1 = a.hashCode();
        int h2 = a.hashCode();

        assertEquals(h1, h2);
    }
}
