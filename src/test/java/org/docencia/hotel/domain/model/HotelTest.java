package org.docencia.hotel.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class HotelTest {

    // ===================== equals =====================

    @Test
    void equals_sameReference_true() {
        Hotel h = new Hotel("h1");
        assertEquals(h, h);
    }

    @Test
    void equals_null_false() {
        Hotel h = new Hotel("h1");
        assertNotEquals(h, null);
    }

    @Test
    void equals_differentType_false() {
        Hotel h = new Hotel("h1");
        assertNotEquals(h, "not a hotel");
    }

    @Test
    void equals_sameId_true() {
        Hotel a = new Hotel("h1");
        Hotel b = new Hotel("h1");

        assertEquals(a, b);
        assertEquals(b, a);
    }

    @Test
    void equals_differentId_false() {
        Hotel a = new Hotel("h1");
        Hotel b = new Hotel("h2");

        assertNotEquals(a, b);
        assertNotEquals(b, a);
    }

    @Test
    void equals_whenIdNull_falseEvenIfBothNull() {
        Hotel a = new Hotel();
        Hotel b = new Hotel();

        assertNotEquals(a, b);
    }

    @Test
    void equals_oneNullId_false() {
        Hotel a = new Hotel();
        Hotel b = new Hotel("h1");

        assertNotEquals(a, b);
        assertNotEquals(b, a);
    }

    // ===================== hashCode =====================

    @Test
    void hashCode_sameId_sameHash() {
        Hotel a = new Hotel("h1");
        Hotel b = new Hotel("h1");

        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void hashCode_differentId_differentHashUsually() {
        Hotel a = new Hotel("h1");
        Hotel b = new Hotel("h2");

        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void hashCode_nullId_isStable() {
        Hotel a = new Hotel(); 
        int h1 = a.hashCode();
        int h2 = a.hashCode();

        assertEquals(h1, h2);
    }
}

