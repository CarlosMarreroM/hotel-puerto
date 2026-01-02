package org.docencia.hotel.domain.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.docencia.hotel.domain.model.Room;
import org.docencia.hotel.service.api.BookingService;
import org.docencia.hotel.service.api.HotelService;
import org.docencia.hotel.service.api.RoomService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RoomDomainImplTest {

    @Mock
    private RoomService roomService;

    @Mock
    private HotelService hotelService;

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private RoomDomainImpl domain;

    // ===== helpers mínimos =====
    private static Room room(String id, String number, String hotelId) {
        Room r = new Room(); // ajusta si no tienes ctor vacío
        r.setId(id);
        r.setNumber(number);
        r.setHotelId(hotelId);
        return r;
    }

    // ===================== createRoom =====================
    @Nested
    class CreateRoomTests {

        @Test
        @DisplayName("createRoom: room null -> NullPointerException, sin llamadas a servicios")
        void createRoom_roomNull_throws() {
            assertThrows(NullPointerException.class, () -> domain.createRoom(null));
            verifyNoInteractions(roomService, hotelService, bookingService);
        }

        @Test
        @DisplayName("createRoom: room id null -> NullPointerException")
        void createRoom_roomIdNull_throws() {
            Room r = room(null, "101", "h1");

            assertThrows(NullPointerException.class, () -> domain.createRoom(r));
            verifyNoInteractions(roomService, hotelService, bookingService);
        }

        @Test
        @DisplayName("createRoom: room id blank -> IllegalArgumentException")
        void createRoom_roomIdBlank_throws() {
            Room r = room("   ", "101", "h1");

            assertThrows(IllegalArgumentException.class, () -> domain.createRoom(r));
            verifyNoInteractions(roomService, hotelService, bookingService);
        }

        @Test
        @DisplayName("createRoom: number null -> NullPointerException")
        void createRoom_numberNull_throws() {
            Room r = room("r1", null, "h1");

            assertThrows(NullPointerException.class, () -> domain.createRoom(r));
            verifyNoInteractions(roomService, hotelService, bookingService);
        }

        @Test
        @DisplayName("createRoom: number blank -> IllegalArgumentException")
        void createRoom_numberBlank_throws() {
            Room r = room("r1", "   ", "h1");

            assertThrows(IllegalArgumentException.class, () -> domain.createRoom(r));
            verifyNoInteractions(roomService, hotelService, bookingService);
        }

        @Test
        @DisplayName("createRoom: hotelId null -> NullPointerException")
        void createRoom_hotelIdNull_throws() {
            Room r = room("r1", "101", null);

            assertThrows(NullPointerException.class, () -> domain.createRoom(r));
            verifyNoInteractions(roomService, hotelService, bookingService);
        }

        @Test
        @DisplayName("createRoom: hotelId blank -> IllegalArgumentException")
        void createRoom_hotelIdBlank_throws() {
            Room r = room("r1", "101", "   ");

            assertThrows(IllegalArgumentException.class, () -> domain.createRoom(r));
            verifyNoInteractions(roomService, hotelService, bookingService);
        }

        @Test
        @DisplayName("createRoom: si la room ya existe -> IllegalStateException y no valida hotel ni guarda")
        void createRoom_whenRoomAlreadyExists_throwsAndDoesNotSave() {
            Room r = room("r1", "101", "h1");
            when(roomService.existsById("r1")).thenReturn(true);

            IllegalStateException ex = assertThrows(IllegalStateException.class, () -> domain.createRoom(r));
            assertEquals("room already exists: r1", ex.getMessage());

            verify(roomService).existsById("r1");
            verifyNoMoreInteractions(roomService);
            verifyNoInteractions(hotelService, bookingService);
        }

        @Test
        @DisplayName("createRoom: si hotel no existe -> IllegalArgumentException y no guarda")
        void createRoom_whenHotelNotExists_throwsAndDoesNotSave() {
            Room r = room("r1", "101", "h404");
            when(roomService.existsById("r1")).thenReturn(false);
            when(hotelService.existsById("h404")).thenReturn(false);

            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> domain.createRoom(r));
            assertEquals("Hotel with id h404 does not exist", ex.getMessage());

            verify(roomService).existsById("r1");
            verify(hotelService).existsById("h404");
            verifyNoMoreInteractions(roomService, hotelService);
            verify(roomService, never()).save(any());
            verifyNoInteractions(bookingService);
        }

        @Test
        @DisplayName("createRoom: ok -> comprueba exists, comprueba hotel, guarda y devuelve lo que devuelve el service")
        void createRoom_ok_saves() {
            Room input = room("r1", "101", "h1");
            Room saved = room("r1", "101", "h1");

            when(roomService.existsById("r1")).thenReturn(false);
            when(hotelService.existsById("h1")).thenReturn(true);
            when(roomService.save(input)).thenReturn(saved);

            Room result = domain.createRoom(input);

            assertSame(saved, result);

            verify(roomService).existsById("r1");
            verify(hotelService).existsById("h1");
            verify(roomService).save(input);
            verifyNoMoreInteractions(roomService, hotelService);
            verifyNoInteractions(bookingService);
        }
    }

    // ===================== getRoomById =====================
    @Nested
    class GetRoomByIdTests {

        @Test
        @DisplayName("getRoomById: id null -> NullPointerException")
        void getRoomById_null_throws() {
            assertThrows(NullPointerException.class, () -> domain.getRoomById(null));
            verifyNoInteractions(roomService, hotelService, bookingService);
        }

        @Test
        @DisplayName("getRoomById: id blank -> IllegalArgumentException")
        void getRoomById_blank_throws() {
            assertThrows(IllegalArgumentException.class, () -> domain.getRoomById("   "));
            verifyNoInteractions(roomService, hotelService, bookingService);
        }

        @Test
        @DisplayName("getRoomById: id válido -> delega en roomService.findById")
        void getRoomById_ok_delegates() {
            Room r = room("r1", "101", "h1");
            when(roomService.findById("r1")).thenReturn(Optional.of(r));

            Optional<Room> result = domain.getRoomById("r1");

            assertTrue(result.isPresent());
            assertSame(r, result.get());

            verify(roomService).findById("r1");
            verifyNoMoreInteractions(roomService);
            verifyNoInteractions(hotelService, bookingService);
        }
    }

    // ===================== getAllRooms =====================
    @Test
    @DisplayName("getAllRooms: delega en roomService.findAll")
    void getAllRooms_delegates() {
        List<Room> expected = List.of(room("r1", "101", "h1"));
        when(roomService.findAll()).thenReturn(expected);

        List<Room> result = domain.getAllRooms();

        assertSame(expected, result);

        verify(roomService).findAll();
        verifyNoMoreInteractions(roomService);
        verifyNoInteractions(hotelService, bookingService);
    }

    // ===================== getRoomsByHotel =====================
    @Nested
    class GetRoomsByHotelTests {

        @Test
        @DisplayName("getRoomsByHotel: hotelId null -> NullPointerException")
        void getRoomsByHotel_null_throws() {
            assertThrows(NullPointerException.class, () -> domain.getRoomsByHotel(null));
            verifyNoInteractions(roomService, hotelService, bookingService);
        }

        @Test
        @DisplayName("getRoomsByHotel: hotelId blank -> IllegalArgumentException")
        void getRoomsByHotel_blank_throws() {
            assertThrows(IllegalArgumentException.class, () -> domain.getRoomsByHotel("   "));
            verifyNoInteractions(roomService, hotelService, bookingService);
        }

        @Test
        @DisplayName("getRoomsByHotel: si hotel no existe -> IllegalArgumentException y no consulta rooms")
        void getRoomsByHotel_hotelNotExists_throws() {
            when(hotelService.existsById("h404")).thenReturn(false);

            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> domain.getRoomsByHotel("h404"));
            assertEquals("Hotel with id h404 does not exist", ex.getMessage());

            verify(hotelService).existsById("h404");
            verifyNoMoreInteractions(hotelService);
            verifyNoInteractions(roomService, bookingService);
        }

        @Test
        @DisplayName("getRoomsByHotel: si hotel existe -> delega en roomService.findByHotelId")
        void getRoomsByHotel_ok_delegates() {
            List<Room> expected = List.of(room("r1", "101", "h1"));
            when(hotelService.existsById("h1")).thenReturn(true);
            when(roomService.findByHotelId("h1")).thenReturn(expected);

            List<Room> result = domain.getRoomsByHotel("h1");

            assertSame(expected, result);

            verify(hotelService).existsById("h1");
            verify(roomService).findByHotelId("h1");
            verifyNoMoreInteractions(hotelService, roomService);
            verifyNoInteractions(bookingService);
        }
    }

    // ===================== getRoomsByHotelAndType =====================
    @Nested
    class GetRoomsByHotelAndTypeTests {

        @Test
        @DisplayName("getRoomsByHotelAndType: hotelId null -> NullPointerException")
        void getRoomsByHotelAndType_hotelIdNull_throws() {
            assertThrows(NullPointerException.class, () -> domain.getRoomsByHotelAndType(null, "DOUBLE"));
            verifyNoInteractions(roomService, hotelService, bookingService);
        }

        @Test
        @DisplayName("getRoomsByHotelAndType: type null -> NullPointerException")
        void getRoomsByHotelAndType_typeNull_throws() {
            assertThrows(NullPointerException.class, () -> domain.getRoomsByHotelAndType("h1", null));
            verifyNoInteractions(roomService, hotelService, bookingService);
        }

        @Test
        @DisplayName("getRoomsByHotelAndType: hotelId blank -> IllegalArgumentException")
        void getRoomsByHotelAndType_hotelIdBlank_throws() {
            assertThrows(IllegalArgumentException.class, () -> domain.getRoomsByHotelAndType("   ", "DOUBLE"));
            verifyNoInteractions(roomService, hotelService, bookingService);
        }

        @Test
        @DisplayName("getRoomsByHotelAndType: type blank -> IllegalArgumentException")
        void getRoomsByHotelAndType_typeBlank_throws() {
            assertThrows(IllegalArgumentException.class, () -> domain.getRoomsByHotelAndType("h1", "   "));
            verifyNoInteractions(roomService, hotelService, bookingService);
        }

        @Test
        @DisplayName("getRoomsByHotelAndType: si hotel no existe -> IllegalArgumentException y no consulta rooms")
        void getRoomsByHotelAndType_hotelNotExists_throws() {
            when(hotelService.existsById("h404")).thenReturn(false);

            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> domain.getRoomsByHotelAndType("h404", "DOUBLE"));
            assertEquals("Hotel with id h404 does not exist", ex.getMessage());

            verify(hotelService).existsById("h404");
            verifyNoMoreInteractions(hotelService);
            verifyNoInteractions(roomService, bookingService);
        }

        @Test
        @DisplayName("getRoomsByHotelAndType: si hotel existe -> delega en roomService.findByHotelIdAndType")
        void getRoomsByHotelAndType_ok_delegates() {
            List<Room> expected = List.of(room("r1", "101", "h1"));
            when(hotelService.existsById("h1")).thenReturn(true);
            when(roomService.findByHotelIdAndType("h1", "DOUBLE")).thenReturn(expected);

            List<Room> result = domain.getRoomsByHotelAndType("h1", "DOUBLE");

            assertSame(expected, result);

            verify(hotelService).existsById("h1");
            verify(roomService).findByHotelIdAndType("h1", "DOUBLE");
            verifyNoMoreInteractions(hotelService, roomService);
            verifyNoInteractions(bookingService);
        }
    }

    // ===================== updateRoom =====================
    @Nested
    class UpdateRoomTests {

        @Test
        @DisplayName("updateRoom: id null -> NullPointerException")
        void updateRoom_idNull_throws() {
            Room r = room("x", "101", "h1");
            assertThrows(NullPointerException.class, () -> domain.updateRoom(null, r));
            verifyNoInteractions(roomService, hotelService, bookingService);
        }

        @Test
        @DisplayName("updateRoom: id blank -> IllegalArgumentException")
        void updateRoom_idBlank_throws() {
            Room r = room("x", "101", "h1");
            assertThrows(IllegalArgumentException.class, () -> domain.updateRoom("   ", r));
            verifyNoInteractions(roomService, hotelService, bookingService);
        }

        @Test
        @DisplayName("updateRoom: room null -> NullPointerException")
        void updateRoom_roomNull_throws() {
            assertThrows(NullPointerException.class, () -> domain.updateRoom("r1", null));
            verifyNoInteractions(roomService, hotelService, bookingService);
        }

        @Test
        @DisplayName("updateRoom: number null -> NullPointerException")
        void updateRoom_numberNull_throws() {
            Room r = room("x", null, "h1");
            assertThrows(NullPointerException.class, () -> domain.updateRoom("r1", r));
            verifyNoInteractions(roomService, hotelService, bookingService);
        }

        @Test
        @DisplayName("updateRoom: number blank -> IllegalArgumentException")
        void updateRoom_numberBlank_throws() {
            Room r = room("x", "   ", "h1");
            assertThrows(IllegalArgumentException.class, () -> domain.updateRoom("r1", r));
            verifyNoInteractions(roomService, hotelService, bookingService);
        }

        @Test
        @DisplayName("updateRoom: hotelId null -> NullPointerException")
        void updateRoom_hotelIdNull_throws() {
            Room r = room("x", "101", null);
            assertThrows(NullPointerException.class, () -> domain.updateRoom("r1", r));
            verifyNoInteractions(roomService, hotelService, bookingService);
        }

        @Test
        @DisplayName("updateRoom: hotelId blank -> IllegalArgumentException")
        void updateRoom_hotelIdBlank_throws() {
            Room r = room("x", "101", "   ");
            assertThrows(IllegalArgumentException.class, () -> domain.updateRoom("r1", r));
            verifyNoInteractions(roomService, hotelService, bookingService);
        }

        @Test
        @DisplayName("updateRoom: si room no existe -> IllegalArgumentException y no valida hotel ni guarda")
        void updateRoom_roomNotExists_throwsAndDoesNotSave() {
            Room r = room("x", "101", "h1");
            when(roomService.existsById("r404")).thenReturn(false);

            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> domain.updateRoom("r404", r));
            assertEquals("Room with id r404 does not exist", ex.getMessage());

            verify(roomService).existsById("r404");
            verifyNoMoreInteractions(roomService);
            verifyNoInteractions(hotelService, bookingService);
        }

        @Test
        @DisplayName("updateRoom: si hotel no existe -> IllegalArgumentException y no guarda")
        void updateRoom_hotelNotExists_throwsAndDoesNotSave() {
            Room r = room("x", "101", "h404");
            when(roomService.existsById("r1")).thenReturn(true);
            when(hotelService.existsById("h404")).thenReturn(false);

            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> domain.updateRoom("r1", r));
            assertEquals("Hotel with id h404 does not exist", ex.getMessage());

            verify(roomService).existsById("r1");
            verify(hotelService).existsById("h404");
            verifyNoMoreInteractions(roomService, hotelService);

            verify(roomService, never()).save(any());
            verifyNoInteractions(bookingService);
        }

        @Test
        @DisplayName("updateRoom: ok -> fuerza room.id = id y guarda")
        void updateRoom_ok_setsIdAndSaves() {
            Room r = room("original", "101", "h1");
            Room saved = room("r1", "101", "h1");

            when(roomService.existsById("r1")).thenReturn(true);
            when(hotelService.existsById("h1")).thenReturn(true);
            when(roomService.save(r)).thenReturn(saved);

            Room result = domain.updateRoom("r1", r);

            assertEquals("r1", r.getId(), "El dominio debe forzar el id recibido por parámetro");
            assertSame(saved, result);

            verify(roomService).existsById("r1");
            verify(hotelService).existsById("h1");
            verify(roomService).save(r);
            verifyNoMoreInteractions(roomService, hotelService);
            verifyNoInteractions(bookingService);
        }
    }

    // ===================== deleteRoom =====================
    @Nested
    class DeleteRoomTests {

        @Test
        @DisplayName("deleteRoom: id null -> NullPointerException")
        void deleteRoom_null_throws() {
            assertThrows(NullPointerException.class, () -> domain.deleteRoom(null));
            verifyNoInteractions(roomService, hotelService, bookingService);
        }

        @Test
        @DisplayName("deleteRoom: id blank -> IllegalArgumentException")
        void deleteRoom_blank_throws() {
            assertThrows(IllegalArgumentException.class, () -> domain.deleteRoom("   "));
            verifyNoInteractions(roomService, hotelService, bookingService);
        }

        @Test
        @DisplayName("deleteRoom: si room no existe -> false y no mira bookings ni borra")
        void deleteRoom_whenRoomNotExists_returnsFalse() {
            when(roomService.existsById("r404")).thenReturn(false);

            boolean result = domain.deleteRoom("r404");

            assertFalse(result);

            verify(roomService).existsById("r404");
            verifyNoMoreInteractions(roomService);
            verifyNoInteractions(hotelService, bookingService);
        }

        @Test
        @DisplayName("deleteRoom: si hay bookings -> IllegalArgumentException y no borra")
        void deleteRoom_whenHasBookings_throwsAndDoesNotDelete() {
            when(roomService.existsById("r1")).thenReturn(true);
            when(bookingService.existsByRoomId("r1")).thenReturn(true);

            IllegalArgumentException ex =
                    assertThrows(IllegalArgumentException.class, () -> domain.deleteRoom("r1"));
            assertEquals("cannot delete room r1 because it has bookings", ex.getMessage());

            verify(roomService).existsById("r1");
            verify(bookingService).existsByRoomId("r1");
            verify(roomService, never()).deleteById(any());

            verifyNoMoreInteractions(roomService, bookingService);
            verifyNoInteractions(hotelService);
        }

        @Test
        @DisplayName("deleteRoom: sin bookings -> borra y devuelve el resultado del service")
        void deleteRoom_whenNoBookings_deletesAndReturnsResult() {
            when(roomService.existsById("r1")).thenReturn(true);
            when(bookingService.existsByRoomId("r1")).thenReturn(false);
            when(roomService.deleteById("r1")).thenReturn(true);

            boolean result = domain.deleteRoom("r1");

            assertTrue(result);

            verify(roomService).existsById("r1");
            verify(bookingService).existsByRoomId("r1");
            verify(roomService).deleteById("r1");

            verifyNoMoreInteractions(roomService, bookingService);
            verifyNoInteractions(hotelService);
        }
    }

    // ===================== deleteRoomsByHotel =====================
    @Nested
    class DeleteRoomsByHotelTests {

        @Test
        @DisplayName("deleteRoomsByHotel: hotelId null -> NullPointerException")
        void deleteRoomsByHotel_null_throws() {
            assertThrows(NullPointerException.class, () -> domain.deleteRoomsByHotel(null));
            verifyNoInteractions(roomService, hotelService, bookingService);
        }

        @Test
        @DisplayName("deleteRoomsByHotel: hotelId blank -> IllegalArgumentException")
        void deleteRoomsByHotel_blank_throws() {
            assertThrows(IllegalArgumentException.class, () -> domain.deleteRoomsByHotel("   "));
            verifyNoInteractions(roomService, hotelService, bookingService);
        }

        @Test
        @DisplayName("deleteRoomsByHotel: si hotel no existe -> IllegalArgumentException y no borra")
        void deleteRoomsByHotel_hotelNotExists_throws() {
            when(hotelService.existsById("h404")).thenReturn(false);

            IllegalArgumentException ex =
                    assertThrows(IllegalArgumentException.class, () -> domain.deleteRoomsByHotel("h404"));
            assertEquals("Hotel with id h404 does not exist", ex.getMessage());

            verify(hotelService).existsById("h404");
            verifyNoMoreInteractions(hotelService);
            verifyNoInteractions(roomService, bookingService);
        }

        @Test
        @DisplayName("deleteRoomsByHotel: si hay bookings en el hotel -> IllegalArgumentException y no borra rooms")
        void deleteRoomsByHotel_whenHasBookings_throwsAndDoesNotDelete() {
            when(hotelService.existsById("h1")).thenReturn(true);
            when(bookingService.existsByHotelId("h1")).thenReturn(true);

            IllegalArgumentException ex =
                    assertThrows(IllegalArgumentException.class, () -> domain.deleteRoomsByHotel("h1"));
            assertEquals("cannot delete rooms for hotel h1 because there are bookings", ex.getMessage());

            verify(hotelService).existsById("h1");
            verify(bookingService).existsByHotelId("h1");
            verify(roomService, never()).deleteByHotelId(any());

            verifyNoMoreInteractions(hotelService, bookingService);
            verifyNoInteractions(roomService);
        }

        @Test
        @DisplayName("deleteRoomsByHotel: ok -> valida hotel, valida bookings, delega en roomService.deleteByHotelId")
        void deleteRoomsByHotel_ok_delegates() {
            when(hotelService.existsById("h1")).thenReturn(true);
            when(bookingService.existsByHotelId("h1")).thenReturn(false);
            when(roomService.deleteByHotelId("h1")).thenReturn(2);

            int result = domain.deleteRoomsByHotel("h1");

            assertEquals(2, result);

            verify(hotelService).existsById("h1");
            verify(bookingService).existsByHotelId("h1");
            verify(roomService).deleteByHotelId("h1");

            verifyNoMoreInteractions(hotelService, roomService, bookingService);
        }
    }
}
