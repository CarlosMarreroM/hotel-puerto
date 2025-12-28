package org.docencia.hotel.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RoomTest {

    // ===================== equals =====================

    @Test
    void equals_sameReference_true() {
        Room r = new Room("r1");
        assertEquals(r, r);
    }

    @Test
    void equals_null_false() {
        Room r = new Room("r1");
        assertNotEquals(r, null);
    }

    @Test
    void equals_differentType_false() {
        Room r = new Room("r1");
        assertNotEquals(r, "not a room");
    }

    @Test
    void equals_sameId_true() {
        Room a = new Room("r1");
        Room b = new Room("r1");

        assertEquals(a, b);
        assertEquals(b, a);
    }

    @Test
    void equals_differentId_false() {
        Room a = new Room("r1");
        Room b = new Room("r2");

        assertNotEquals(a, b);
        assertNotEquals(b, a);
    }

    @Test
    void equals_whenIdNull_falseEvenIfBothNull() {
        Room a = new Room();
        Room b = new Room();

        assertNotEquals(a, b);
    }

    @Test
    void equals_oneNullId_false() {
        Room a = new Room();
        Room b = new Room("r1");

        assertNotEquals(a, b);
        assertNotEquals(b, a);
    }

    // ===================== hashCode =====================

    @Test
    void hashCode_sameId_sameHash() {
        Room a = new Room("r1");
        Room b = new Room("r1");

        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void hashCode_differentId_differentHashUsually() {
        Room a = new Room("r1");
        Room b = new Room("r2");

        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void hashCode_nullId_isStable() {
        Room a = new Room();
        int h1 = a.hashCode();
        int h2 = a.hashCode();

        assertEquals(h1, h2);
    }
}
