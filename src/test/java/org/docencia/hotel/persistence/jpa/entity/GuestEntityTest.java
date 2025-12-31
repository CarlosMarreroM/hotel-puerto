package org.docencia.hotel.persistence.jpa.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class GuestEntityTest {

    // ===================== equals =====================

    @Test
    void equals_sameReference_true() {
        GuestEntity g = new GuestEntity("g1");
        assertEquals(g, g);
    }

    @Test
    void equals_null_false() {
        GuestEntity g = new GuestEntity("g1");
        assertNotEquals(g, null);
    }

    @Test
    void equals_differentType_false() {
        GuestEntity g = new GuestEntity("g1");
        assertNotEquals(g, "not a GuestEntity");
    }

    @Test
    void equals_sameId_true() {
        GuestEntity a = new GuestEntity("g1");
        GuestEntity b = new GuestEntity("g1");

        assertEquals(a, b);
        assertEquals(b, a);
    }

    @Test
    void equals_differentId_false() {
        GuestEntity a = new GuestEntity("g1");
        GuestEntity b = new GuestEntity("g2");

        assertNotEquals(a, b);
        assertNotEquals(b, a);
    }

    @Test
    void equals_whenIdNull_falseEvenIfBothNull() {
        GuestEntity a = new GuestEntity();
        GuestEntity b = new GuestEntity();

        assertNotEquals(a, b);
    }

    @Test
    void equals_oneNullId_false() {
        GuestEntity a = new GuestEntity();
        GuestEntity b = new GuestEntity("g1");

        assertNotEquals(a, b);
        assertNotEquals(b, a);
    }

    // ===================== hashCode =====================

    @Test
    void hashCode_sameId_sameHash() {
        GuestEntity a = new GuestEntity("g1");
        GuestEntity b = new GuestEntity("g1");

        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void hashCode_differentId_differentHashUsually() {
        GuestEntity a = new GuestEntity("g1");
        GuestEntity b = new GuestEntity("g2");

        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void hashCode_nullId_isStable() {
        GuestEntity a = new GuestEntity();
        int h1 = a.hashCode();
        int h2 = a.hashCode();

        assertEquals(h1, h2);
    }
}
