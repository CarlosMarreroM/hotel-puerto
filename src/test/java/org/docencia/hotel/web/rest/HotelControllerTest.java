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

import org.docencia.hotel.domain.api.HotelDomain;
import org.docencia.hotel.domain.model.Hotel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(HotelController.class)
class HotelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private HotelDomain hotelDomain;

    // ===== helpers mínimos =====

    private static Hotel hotel(String id, String name) {
        Hotel h = new Hotel();
        h.setId(id);
        h.setHotelName(name);
        return h;
    }

    // ===================== POST /api/hotels

    @Test
    void createHotel_returns201_locationHeader_andBody() throws Exception {
        Hotel request = hotel("H1", "Hotel Puerto");
        Hotel created = hotel("H1", "Hotel Puerto");

        when(hotelDomain.createHotel(any(Hotel.class))).thenReturn(created);

        mockMvc.perform(post("/api/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/hotels/H1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("H1"))
                .andExpect(jsonPath("$.hotelName").value("Hotel Puerto"));

        verify(hotelDomain).createHotel(any(Hotel.class));
        verifyNoMoreInteractions(hotelDomain);
    }

    // ===================== GET /api/hotels (sin filtro)

    @Test
    void getHotels_withoutName_returnsAllHotels() throws Exception {
        when(hotelDomain.getAllHotels()).thenReturn(List.of(
                hotel("H1", "A"),
                hotel("H2", "B")
        ));

        mockMvc.perform(get("/api/hotels"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value("H1"))
                .andExpect(jsonPath("$[1].id").value("H2"));

        verify(hotelDomain).getAllHotels();
        verify(hotelDomain, never()).getHotelsByName(any());
        verifyNoMoreInteractions(hotelDomain);
    }

    // ===================== GET /api/hotels?name=...

    @Test
    void getHotels_withName_returnsFilteredHotels() throws Exception {
        when(hotelDomain.getHotelsByName("Hotel Puerto")).thenReturn(List.of(
                hotel("H1", "Hotel Puerto")
        ));

        mockMvc.perform(get("/api/hotels").param("name", "Hotel Puerto"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value("H1"))
                .andExpect(jsonPath("$[0].hotelName").value("Hotel Puerto"));

        verify(hotelDomain).getHotelsByName("Hotel Puerto");
        verify(hotelDomain, never()).getAllHotels();
        verifyNoMoreInteractions(hotelDomain);
    }

    // ===================== GET /api/hotels/{id}

    @Test
    void getHotelById_whenExists_returns200() throws Exception {
        when(hotelDomain.getHotelById("H1"))
                .thenReturn(Optional.of(hotel("H1", "Hotel Puerto")));

        mockMvc.perform(get("/api/hotels/H1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("H1"))
                .andExpect(jsonPath("$.hotelName").value("Hotel Puerto"));

        verify(hotelDomain).getHotelById("H1");
        verifyNoMoreInteractions(hotelDomain);
    }

    @Test
    void getHotelById_whenNotExists_returns404() throws Exception {
        when(hotelDomain.getHotelById("NOPE"))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/hotels/NOPE"))
                .andExpect(status().isNotFound());

        verify(hotelDomain).getHotelById("NOPE");
        verifyNoMoreInteractions(hotelDomain);
    }

    // ===================== PUT /api/hotels/{id}

    @Test
    void updateHotel_whenExists_returns200_andCallsDomainUpdate() throws Exception {
        when(hotelDomain.getHotelById("H1")).thenReturn(Optional.of(hotel("H1", "Old")));
        when(hotelDomain.updateHotel(eq("H1"), any(Hotel.class)))
                .thenReturn(hotel("H1", "New Name"));

        Hotel request = hotel("IGNORED", "New Name"); // el id real viene por path

        mockMvc.perform(put("/api/hotels/H1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("H1"))
                .andExpect(jsonPath("$.hotelName").value("New Name"));

        verify(hotelDomain).getHotelById("H1");
        verify(hotelDomain).updateHotel(eq("H1"), any(Hotel.class));
        verifyNoMoreInteractions(hotelDomain);
    }

    @Test
    void updateHotel_whenNotExists_returns404_andDoesNotCallUpdate() throws Exception {
        when(hotelDomain.getHotelById("H1")).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/hotels/H1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hotel("H1", "X"))))
                .andExpect(status().isNotFound());

        verify(hotelDomain).getHotelById("H1");
        verify(hotelDomain, never()).updateHotel(any(), any());
        verifyNoMoreInteractions(hotelDomain);
    }

    // ===================== DELETE /api/hotels/{id}

    @Test
    void deleteHotel_whenExists_returns204_andCallsDomainDelete() throws Exception {
        when(hotelDomain.getHotelById("H1")).thenReturn(Optional.of(hotel("H1", "Hotel Puerto")));

        mockMvc.perform(delete("/api/hotels/H1"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(hotelDomain).getHotelById("H1");
        verify(hotelDomain).deleteHotel("H1");
        verifyNoMoreInteractions(hotelDomain);
    }

    @Test
    void deleteHotel_whenNotExists_returns404_andDoesNotCallDelete() throws Exception {
        when(hotelDomain.getHotelById("H1")).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/hotels/H1"))
                .andExpect(status().isNotFound());

        verify(hotelDomain).getHotelById("H1");
        verify(hotelDomain, never()).deleteHotel(any());
        verifyNoMoreInteractions(hotelDomain);
    }
}
