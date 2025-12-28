package org.docencia.hotel.persisten.jpa.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.docencia.hotel.persistence.jpa.entity.RoomEntity;
import org.junit.jupiter.api.Test;

class RoomEntityTest {

    // ===================== equals =====================

    @Test
    void equals_sameReference_true() {
        RoomEntity r = new RoomEntity("r1");
        assertEquals(r, r);
    }

    @Test
    void equals_null_false() {
        RoomEntity r = new RoomEntity("r1");
        assertNotEquals(r, null);
    }

    @Test
    void equals_differentType_false() {
        RoomEntity r = new RoomEntity("r1");
        assertNotEquals(r, "not a RoomEntity");
    }

    @Test
    void equals_sameId_true() {
        RoomEntity a = new RoomEntity("r1");
        RoomEntity b = new RoomEntity("r1");

        assertEquals(a, b);
        assertEquals(b, a);
    }

    @Test
    void equals_differentId_false() {
        RoomEntity a = new RoomEntity("r1");
        RoomEntity b = new RoomEntity("r2");

        assertNotEquals(a, b);
        assertNotEquals(b, a);
    }

    @Test
    void equals_whenIdNull_falseEvenIfBothNull() {
        RoomEntity a = new RoomEntity();
        RoomEntity b = new RoomEntity();

        assertNotEquals(a, b);
    }

    @Test
    void equals_oneNullId_false() {
        RoomEntity a = new RoomEntity();
        RoomEntity b = new RoomEntity("r1");

        assertNotEquals(a, b);
        assertNotEquals(b, a);
    }

    // ===================== hashCode =====================

    @Test
    void hashCode_sameId_sameHash() {
        RoomEntity a = new RoomEntity("r1");
        RoomEntity b = new RoomEntity("r1");

        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void hashCode_differentId_differentHashUsually() {
        RoomEntity a = new RoomEntity("r1");
        RoomEntity b = new RoomEntity("r2");

        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void hashCode_nullId_isStable() {
        RoomEntity a = new RoomEntity();
        int h1 = a.hashCode();
        int h2 = a.hashCode();

        assertEquals(h1, h2);
    }
}
