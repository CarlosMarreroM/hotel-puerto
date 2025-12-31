package org.docencia.hotel.persistence.nosql.document;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class GuestPreferencesDocumentTest {

    // ===================== equals =====================

    @Test
    void equals_sameReference_true() {
        GuestPreferencesDocument d = new GuestPreferencesDocument("g1");
        assertEquals(d, d);
    }

    @Test
    void equals_null_false() {
        GuestPreferencesDocument d = new GuestPreferencesDocument("g1");
        assertNotEquals(d, null);
    }

    @Test
    void equals_differentType_false() {
        GuestPreferencesDocument d = new GuestPreferencesDocument("g1");
        assertNotEquals(d, "not a document");
    }

    @Test
    void equals_sameGuestId_true() {
        GuestPreferencesDocument a = new GuestPreferencesDocument("g1");
        GuestPreferencesDocument b = new GuestPreferencesDocument("g1");

        assertEquals(a, b);
        assertEquals(b, a);
    }

    @Test
    void equals_differentGuestId_false() {
        GuestPreferencesDocument a = new GuestPreferencesDocument("g1");
        GuestPreferencesDocument b = new GuestPreferencesDocument("g2");

        assertNotEquals(a, b);
        assertNotEquals(b, a);
    }

    @Test
    void equals_whenGuestIdNull_falseEvenIfBothNull() {
        GuestPreferencesDocument a = new GuestPreferencesDocument();
        GuestPreferencesDocument b = new GuestPreferencesDocument();

        assertNotEquals(a, b);
    }

    @Test
    void equals_oneNullGuestId_false() {
        GuestPreferencesDocument a = new GuestPreferencesDocument();
        GuestPreferencesDocument b = new GuestPreferencesDocument("g1");

        assertNotEquals(a, b);
        assertNotEquals(b, a);
    }

    // ===================== hashCode =====================

    @Test
    void hashCode_sameGuestId_sameHash() {
        GuestPreferencesDocument a = new GuestPreferencesDocument("g1");
        GuestPreferencesDocument b = new GuestPreferencesDocument("g1");

        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void hashCode_differentGuestId_differentHashUsually() {
        GuestPreferencesDocument a = new GuestPreferencesDocument("g1");
        GuestPreferencesDocument b = new GuestPreferencesDocument("g2");

        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void hashCode_nullGuestId_isStable() {
        GuestPreferencesDocument a = new GuestPreferencesDocument();
        int h1 = a.hashCode();
        int h2 = a.hashCode();

        assertEquals(h1, h2);
    }
}