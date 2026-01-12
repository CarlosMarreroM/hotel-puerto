package org.docencia.hotel.web.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Optional;

import org.docencia.hotel.domain.api.BookingDomain;
import org.docencia.hotel.domain.model.Booking;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingDomain bookingDomain;

    // ===== helpers mínimos =====
    private static Booking booking(String id, String guestId, String roomId, String hotelId) {
        Booking b = new Booking();
        b.setId(id);

        // Ajusta si tus campos se llaman distinto (guestId/roomId/hotelId)
        b.setGuestId(guestId);
        b.setRoomId(roomId);

        return b;
    }

    // ===================== POST /api/bookings

    @Test
    void createBooking_returns201_locationHeader_andBody() throws Exception {
        Booking request = booking("B1", "G1", "R1", "H1");
        Booking created = booking("B1", "G1", "R1", "H1");

        when(bookingDomain.createBooking(any(Booking.class))).thenReturn(created);

        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/bookings/B1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("B1"))
                .andExpect(jsonPath("$.guestId").value("G1"))
                .andExpect(jsonPath("$.roomId").value("R1"));

        verify(bookingDomain).createBooking(any(Booking.class));
        verifyNoMoreInteractions(bookingDomain);
    }

    // ===================== GET /api/bookings (sin filtros)

    @Test
    void getBookings_withoutFilters_returnsAllBookings() throws Exception {
        when(bookingDomain.getAllBookings()).thenReturn(List.of(
                booking("B1", "G1", "R1", "H1"),
                booking("B2", "G2", "R2", "H2")
        ));

        mockMvc.perform(get("/api/bookings"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value("B1"))
                .andExpect(jsonPath("$[1].id").value("B2"));

        verify(bookingDomain).getAllBookings();
        verify(bookingDomain, never()).getBookingsByRoomId(any());
        verify(bookingDomain, never()).getBookingsByGuestId(any());
        verify(bookingDomain, never()).getBookingsByHotelId(any());
        verifyNoMoreInteractions(bookingDomain);
    }

    // ===================== GET /api/bookings?roomId=...

    @Test
    void getBookings_withRoomId_returnsBookingsByRoomId() throws Exception {
        when(bookingDomain.getBookingsByRoomId("R1")).thenReturn(List.of(
                booking("B1", "G1", "R1", "H1")
        ));

        mockMvc.perform(get("/api/bookings").param("roomId", "R1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].roomId").value("R1"));

        verify(bookingDomain).getBookingsByRoomId("R1");
        verify(bookingDomain, never()).getAllBookings();
        verify(bookingDomain, never()).getBookingsByGuestId(any());
        verify(bookingDomain, never()).getBookingsByHotelId(any());
        verifyNoMoreInteractions(bookingDomain);
    }

    // ===================== GET /api/bookings?guestId=...

    @Test
    void getBookings_withGuestId_returnsBookingsByGuestId() throws Exception {
        when(bookingDomain.getBookingsByGuestId("G1")).thenReturn(List.of(
                booking("B1", "G1", "R1", "H1")
        ));

        mockMvc.perform(get("/api/bookings").param("guestId", "G1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].guestId").value("G1"));

        verify(bookingDomain).getBookingsByGuestId("G1");
        verify(bookingDomain, never()).getAllBookings();
        verify(bookingDomain, never()).getBookingsByRoomId(any());
        verify(bookingDomain, never()).getBookingsByHotelId(any());
        verifyNoMoreInteractions(bookingDomain);
    }

    // ===================== GET /api/bookings?hotelId=...

    @Test
    void getBookings_withHotelId_returnsBookingsByHotelId() throws Exception {
        when(bookingDomain.getBookingsByHotelId("H1")).thenReturn(List.of(
                booking("B1", "G1", "R1", "H1")
        ));

        mockMvc.perform(get("/api/bookings").param("hotelId", "H1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1));

        verify(bookingDomain).getBookingsByHotelId("H1");
        verify(bookingDomain, never()).getAllBookings();
        verify(bookingDomain, never()).getBookingsByRoomId(any());
        verify(bookingDomain, never()).getBookingsByGuestId(any());
        verifyNoMoreInteractions(bookingDomain);
    }

    // ===================== GET /api/bookings con >1 filtro => 400

    @Test
    void getBookings_withMoreThanOneFilter_returns400_andDoesNotCallDomain() throws Exception {
        mockMvc.perform(get("/api/bookings")
                        .param("roomId", "R1")
                        .param("guestId", "G1"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(bookingDomain);
    }

    @Test
    void getBookings_withAllThreeFilters_returns400_andDoesNotCallDomain() throws Exception {
        mockMvc.perform(get("/api/bookings")
                        .param("roomId", "R1")
                        .param("guestId", "G1")
                        .param("hotelId", "H1"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(bookingDomain);
    }

    // ===================== GET /api/bookings/{id}

    @Test
    void getBookingById_whenExists_returns200() throws Exception {
        when(bookingDomain.getBookingById("B1")).thenReturn(Optional.of(
                booking("B1", "G1", "R1", "H1")
        ));

        mockMvc.perform(get("/api/bookings/B1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("B1"));

        verify(bookingDomain).getBookingById("B1");
        verifyNoMoreInteractions(bookingDomain);
    }

    @Test
    void getBookingById_whenNotExists_returns404() throws Exception {
        when(bookingDomain.getBookingById("NOPE")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/bookings/NOPE"))
                .andExpect(status().isNotFound());

        verify(bookingDomain).getBookingById("NOPE");
        verifyNoMoreInteractions(bookingDomain);
    }

    // ===================== PUT /api/bookings/{id}

    @Test
    void updateBooking_whenExists_returns200_andCallsDomainUpdate() throws Exception {
        when(bookingDomain.getBookingById("B1")).thenReturn(Optional.of(
                booking("B1", "G1", "R1", "H1")
        ));
        when(bookingDomain.updateBooking(eq("B1"), any(Booking.class))).thenReturn(
                booking("B1", "G1", "R1", "H1")
        );

        mockMvc.perform(put("/api/bookings/B1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                booking("IGNORED", "G1", "R1", "H1")
                        )))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("B1"));

        verify(bookingDomain).getBookingById("B1");
        verify(bookingDomain).updateBooking(eq("B1"), any(Booking.class));
        verifyNoMoreInteractions(bookingDomain);
    }

    @Test
    void updateBooking_whenNotExists_returns404_andDoesNotCallUpdate() throws Exception {
        when(bookingDomain.getBookingById("B1")).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/bookings/B1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                booking("B1", "G1", "R1", "H1")
                        )))
                .andExpect(status().isNotFound());

        verify(bookingDomain).getBookingById("B1");
        verify(bookingDomain, never()).updateBooking(any(), any());
        verifyNoMoreInteractions(bookingDomain);
    }

    // ===================== DELETE /api/bookings/{id}

    @Test
    void deleteBooking_whenExists_returns204_andCallsDomainDelete() throws Exception {
        when(bookingDomain.getBookingById("B1")).thenReturn(Optional.of(
                booking("B1", "G1", "R1", "H1")
        ));

        // Si deleteBooking devuelve algo, puedes stubbearlo:
        // when(bookingDomain.deleteBooking("B1")).thenReturn(true);

        mockMvc.perform(delete("/api/bookings/B1"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(bookingDomain).getBookingById("B1");
        verify(bookingDomain).deleteBooking("B1");
        verifyNoMoreInteractions(bookingDomain);
    }

    @Test
    void deleteBooking_whenNotExists_returns404_andDoesNotCallDelete() throws Exception {
        when(bookingDomain.getBookingById("B1")).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/bookings/B1"))
                .andExpect(status().isNotFound());

        verify(bookingDomain).getBookingById("B1");
        verify(bookingDomain, never()).deleteBooking(any());
        verifyNoMoreInteractions(bookingDomain);
    }

    // ===================== DELETE /api/bookings/guest/{guestId}

    @Test
    void deleteBookingsByGuestId_returns200_andDeletedCount() throws Exception {
        when(bookingDomain.deleteBookingsByGuestId("G1")).thenReturn(4);

        mockMvc.perform(delete("/api/bookings/guest/G1"))
                .andExpect(status().isOk())
                .andExpect(content().string("4"));

        verify(bookingDomain).deleteBookingsByGuestId("G1");
        verifyNoMoreInteractions(bookingDomain);
    }

    // ===================== DELETE /api/bookings/room/{roomId}

    @Test
    void deleteBookingsByRoomId_returns200_andDeletedCount() throws Exception {
        when(bookingDomain.deleteBookingsByRoomId("R1")).thenReturn(2);

        mockMvc.perform(delete("/api/bookings/room/R1"))
                .andExpect(status().isOk())
                .andExpect(content().string("2"));

        verify(bookingDomain).deleteBookingsByRoomId("R1");
        verifyNoMoreInteractions(bookingDomain);
    }
}
