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

import org.docencia.hotel.domain.api.RoomDomain;
import org.docencia.hotel.domain.model.Room;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(RoomController.class)
class RoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RoomDomain roomDomain;

    // ===== helpers mínimos =====
    private static Room room(String id, String hotelId, String roomNumber, String type) {
        Room r = new Room();
        r.setId(id);
        r.setHotelId(hotelId);
        r.setNumber(roomNumber);
        try {
            r.getClass().getMethod("setType", String.class).invoke(r, type);
        } catch (Exception ignored) {
        }
        return r;
    }

    // ===================== POST /api/rooms

    @Test
    void createRoom_returns201_locationHeader_andBody() throws Exception {
        Room request = room("R1", "H1", "101", "SINGLE");
        Room created = room("R1", "H1", "101", "SINGLE");

        when(roomDomain.createRoom(any(Room.class))).thenReturn(created);

        mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/rooms/R1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("R1"))
                .andExpect(jsonPath("$.hotelId").value("H1"))
                .andExpect(jsonPath("$.number").value("101"));

        verify(roomDomain).createRoom(any(Room.class));
        verifyNoMoreInteractions(roomDomain);
    }

    // ===================== GET /api/rooms (sin filtros)

    @Test
    void getRooms_withoutFilters_returnsAllRooms() throws Exception {
        when(roomDomain.getAllRooms()).thenReturn(List.of(
                room("R1", "H1", "101", "SINGLE"),
                room("R2", "H2", "201", "DOUBLE")
        ));

        mockMvc.perform(get("/api/rooms"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value("R1"))
                .andExpect(jsonPath("$[1].id").value("R2"));

        verify(roomDomain).getAllRooms();
        verify(roomDomain, never()).getRoomsByHotel(any());
        verify(roomDomain, never()).getRoomsByHotelAndType(any(), any());
        verifyNoMoreInteractions(roomDomain);
    }

    // ===================== GET /api/rooms?hotelId=...

    @Test
    void getRooms_withHotelId_returnsRoomsByHotel() throws Exception {
        when(roomDomain.getRoomsByHotel("H1")).thenReturn(List.of(
                room("R1", "H1", "101", "SINGLE"),
                room("R2", "H1", "102", "DOUBLE")
        ));

        mockMvc.perform(get("/api/rooms").param("hotelId", "H1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].hotelId").value("H1"))
                .andExpect(jsonPath("$[1].hotelId").value("H1"));

        verify(roomDomain).getRoomsByHotel("H1");
        verify(roomDomain, never()).getAllRooms();
        verify(roomDomain, never()).getRoomsByHotelAndType(any(), any());
        verifyNoMoreInteractions(roomDomain);
    }

    // ===================== GET /api/rooms?hotelId=...&type=...

    @Test
    void getRooms_withHotelIdAndType_returnsRoomsByHotelAndType() throws Exception {
        when(roomDomain.getRoomsByHotelAndType("H1", "SINGLE")).thenReturn(List.of(
                room("R1", "H1", "101", "SINGLE")
        ));

        mockMvc.perform(get("/api/rooms")
                        .param("hotelId", "H1")
                        .param("type", "SINGLE"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value("R1"))
                .andExpect(jsonPath("$[0].hotelId").value("H1"));

        verify(roomDomain).getRoomsByHotelAndType("H1", "SINGLE");
        verify(roomDomain, never()).getRoomsByHotel(any());
        verify(roomDomain, never()).getAllRooms();
        verifyNoMoreInteractions(roomDomain);
    }

    // ===================== GET /api/rooms/{id}

    @Test
    void getRoomById_whenExists_returns200() throws Exception {
        when(roomDomain.getRoomById("R1")).thenReturn(Optional.of(
                room("R1", "H1", "101", "SINGLE")
        ));

        mockMvc.perform(get("/api/rooms/R1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("R1"))
                .andExpect(jsonPath("$.hotelId").value("H1"))
                .andExpect(jsonPath("$.number").value("101"));

        verify(roomDomain).getRoomById("R1");
        verifyNoMoreInteractions(roomDomain);
    }

    @Test
    void getRoomById_whenNotExists_returns404() throws Exception {
        when(roomDomain.getRoomById("NOPE")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/rooms/NOPE"))
                .andExpect(status().isNotFound());

        verify(roomDomain).getRoomById("NOPE");
        verifyNoMoreInteractions(roomDomain);
    }

    // ===================== PUT /api/rooms/{id}

    @Test
    void updateRoom_whenExists_returns200_andCallsDomainUpdate() throws Exception {
        when(roomDomain.getRoomById("R1")).thenReturn(Optional.of(
                room("R1", "H1", "101", "SINGLE")
        ));
        when(roomDomain.updateRoom(eq("R1"), any(Room.class))).thenReturn(
                room("R1", "H1", "999", "DOUBLE")
        );

        mockMvc.perform(put("/api/rooms/R1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                room("IGNORED", "H1", "999", "DOUBLE")
                        )))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("R1"))
                .andExpect(jsonPath("$.number").value("999"));

        verify(roomDomain).getRoomById("R1");
        verify(roomDomain).updateRoom(eq("R1"), any(Room.class));
        verifyNoMoreInteractions(roomDomain);
    }

    @Test
    void updateRoom_whenNotExists_returns404_andDoesNotCallUpdate() throws Exception {
        when(roomDomain.getRoomById("R1")).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/rooms/R1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                room("R1", "H1", "101", "SINGLE")
                        )))
                .andExpect(status().isNotFound());

        verify(roomDomain).getRoomById("R1");
        verify(roomDomain, never()).updateRoom(any(), any());
        verifyNoMoreInteractions(roomDomain);
    }

    // ===================== DELETE /api/rooms/{id}

    @Test
    void deleteRoom_whenExists_returns204_andCallsDomainDelete() throws Exception {
        when(roomDomain.getRoomById("R1")).thenReturn(Optional.of(
                room("R1", "H1", "101", "SINGLE")
        ));

        // Si tu deleteRoom devuelve boolean/int/lo-que-sea y quieres stubbear:
        // when(roomDomain.deleteRoom("R1")).thenReturn(true);

        mockMvc.perform(delete("/api/rooms/R1"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(roomDomain).getRoomById("R1");
        verify(roomDomain).deleteRoom("R1");
        verifyNoMoreInteractions(roomDomain);
    }

    @Test
    void deleteRoom_whenNotExists_returns404_andDoesNotCallDelete() throws Exception {
        when(roomDomain.getRoomById("R1")).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/rooms/R1"))
                .andExpect(status().isNotFound());

        verify(roomDomain).getRoomById("R1");
        verify(roomDomain, never()).deleteRoom(any());
        verifyNoMoreInteractions(roomDomain);
    }

    // ===================== DELETE /api/rooms/{hotelId}/rooms  (bulk delete)

    @Test
    void deleteRoomsByHotel_returns200_andDeletedCount() throws Exception {
        when(roomDomain.deleteRoomsByHotel("H1")).thenReturn(3);

        mockMvc.perform(delete("/api/rooms/H1/rooms"))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));

        verify(roomDomain).deleteRoomsByHotel("H1");
        verifyNoMoreInteractions(roomDomain);
    }
}
