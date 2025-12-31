package org.docencia.hotel.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class GuestPreferencesTest {

    // ===================== equals =====================

    @Test
    void equals_sameReference_true() {
        GuestPreferences p = new GuestPreferences("g1");
        assertEquals(p, p);
    }

    @Test
    void equals_null_false() {
        GuestPreferences p = new GuestPreferences("g1");
        assertNotEquals(p, null);
    }

    @Test
    void equals_differentType_false() {
        GuestPreferences p = new GuestPreferences("g1");
        assertNotEquals(p, "not preferences");
    }

    @Test
    void equals_sameGuestId_true() {
        GuestPreferences a = new GuestPreferences("g1");
        GuestPreferences b = new GuestPreferences("g1");

        assertEquals(a, b);
        assertEquals(b, a);
    }

    @Test
    void equals_differentGuestId_false() {
        GuestPreferences a = new GuestPreferences("g1");
        GuestPreferences b = new GuestPreferences("g2");

        assertNotEquals(a, b);
        assertNotEquals(b, a);
    }

    @Test
    void equals_whenGuestIdNull_falseEvenIfBothNull() {
        GuestPreferences a = new GuestPreferences();
        GuestPreferences b = new GuestPreferences();

        assertNotEquals(a, b);
    }

    @Test
    void equals_oneNullGuestId_false() {
        GuestPreferences a = new GuestPreferences();
        GuestPreferences b = new GuestPreferences("g1");

        assertNotEquals(a, b);
        assertNotEquals(b, a);
    }

    // ===================== hashCode =====================

    @Test
    void hashCode_sameGuestId_sameHash() {
        GuestPreferences a = new GuestPreferences("g1");
        GuestPreferences b = new GuestPreferences("g1");

        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void hashCode_differentGuestId_differentHashUsually() {
        GuestPreferences a = new GuestPreferences("g1");
        GuestPreferences b = new GuestPreferences("g2");

        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void hashCode_nullGuestId_isStable() {
        GuestPreferences a = new GuestPreferences();
        int h1 = a.hashCode();
        int h2 = a.hashCode();

        assertEquals(h1, h2);
    }
}
