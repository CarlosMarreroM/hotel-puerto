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

import org.docencia.hotel.domain.api.GuestDomain;
import org.docencia.hotel.domain.model.Guest;
import org.docencia.hotel.domain.model.GuestPreferences;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(GuestController.class)
class GuestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GuestDomain guestDomain;

    // ===== helpers mínimos =====
    private static Guest guest(String id, String name) {
        Guest g = new Guest();
        g.setId(id);
        g.setName(name);
        return g;
    }

    private static GuestPreferences prefs(String guestId) {
        GuestPreferences p = new GuestPreferences();
        p.setGuestId(guestId);
        return p;
    }

    // ===================== POST /api/guests

    @Test
    void createGuest_returns201_locationHeader_andBody() throws Exception {
        Guest request = guest("G1", "Carlos");
        Guest created = guest("G1", "Carlos");

        when(guestDomain.createGuest(any(Guest.class))).thenReturn(created);

        mockMvc.perform(post("/api/guests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/guests/G1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("G1"))
                .andExpect(jsonPath("$.name").value("Carlos"));

        verify(guestDomain).createGuest(any(Guest.class));
        verifyNoMoreInteractions(guestDomain);
    }

    // ===================== GET /api/guests

    @Test
    void getAllGuests_returns200_andList() throws Exception {
        when(guestDomain.getAllGuests()).thenReturn(List.of(
                guest("G1", "A"),
                guest("G2", "B")
        ));

        mockMvc.perform(get("/api/guests"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value("G1"))
                .andExpect(jsonPath("$[1].id").value("G2"));

        verify(guestDomain).getAllGuests();
        verifyNoMoreInteractions(guestDomain);
    }

    // ===================== GET /api/guests/{id}

    @Test
    void getGuestById_whenExists_returns200() throws Exception {
        when(guestDomain.getGuestById("G1")).thenReturn(Optional.of(
                guest("G1", "Carlos")
        ));

        mockMvc.perform(get("/api/guests/G1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("G1"))
                .andExpect(jsonPath("$.name").value("Carlos"));

        verify(guestDomain).getGuestById("G1");
        verifyNoMoreInteractions(guestDomain);
    }

    @Test
    void getGuestById_whenNotExists_returns404() throws Exception {
        when(guestDomain.getGuestById("NOPE")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/guests/NOPE"))
                .andExpect(status().isNotFound());

        verify(guestDomain).getGuestById("NOPE");
        verifyNoMoreInteractions(guestDomain);
    }

    // ===================== GET /api/guests/{id}/preferences

    @Test
    void getPreferences_whenExists_returns200() throws Exception {
        GuestPreferences p = prefs("G1");
        when(guestDomain.getPreferencesByGuestId("G1")).thenReturn(Optional.of(p));

        mockMvc.perform(get("/api/guests/G1/preferences"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.guestId").value("G1"));

        verify(guestDomain).getPreferencesByGuestId("G1");
        verifyNoMoreInteractions(guestDomain);
    }

    @Test
    void getPreferences_whenNotExists_returns404() throws Exception {
        when(guestDomain.getPreferencesByGuestId("G1")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/guests/G1/preferences"))
                .andExpect(status().isNotFound());

        verify(guestDomain).getPreferencesByGuestId("G1");
        verifyNoMoreInteractions(guestDomain);
    }

    // ===================== PUT /api/guests/{id}

    @Test
    void updateGuest_whenExists_returns200() throws Exception {
        when(guestDomain.updateGuest(eq("G1"), any(Guest.class))).thenReturn(
                guest("G1", "Nuevo")
        );
        when(guestDomain.getGuestById("G1")).thenReturn(Optional.of(
                guest("G1", "Viejo")
        ));

        mockMvc.perform(put("/api/guests/G1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(guest("IGNORED", "Nuevo"))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("G1"))
                .andExpect(jsonPath("$.name").value("Nuevo"));

        verify(guestDomain).updateGuest(eq("G1"), any(Guest.class));
        verify(guestDomain).getGuestById("G1");
        verifyNoMoreInteractions(guestDomain);
    }

    @Test
    void updateGuest_whenNotExists_returns404_butStillCallsUpdate_dueToControllerOrder() throws Exception {
        when(guestDomain.updateGuest(eq("G1"), any(Guest.class))).thenReturn(
                guest("G1", "Nuevo")
        );
        when(guestDomain.getGuestById("G1")).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/guests/G1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(guest("IGNORED", "Nuevo"))))
                .andExpect(status().isNotFound());

        verify(guestDomain).updateGuest(eq("G1"), any(Guest.class));
        verify(guestDomain).getGuestById("G1");
        verifyNoMoreInteractions(guestDomain);
    }

    // ===================== PUT /api/guests/{id}/preferences

    @Test
    void upsertPreferences_whenExists_returns200() throws Exception {
        GuestPreferences req = prefs("IGNORED");
        GuestPreferences saved = prefs("G1");

        when(guestDomain.updatePreferences(eq("G1"), any(GuestPreferences.class))).thenReturn(saved);
        when(guestDomain.getGuestById("G1")).thenReturn(Optional.of(guest("G1", "Carlos")));

        mockMvc.perform(put("/api/guests/G1/preferences")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.guestId").value("G1"));

        verify(guestDomain).updatePreferences(eq("G1"), any(GuestPreferences.class));
        verify(guestDomain).getGuestById("G1");
        verifyNoMoreInteractions(guestDomain);
    }

    @Test
    void upsertPreferences_whenNotExists_returns404_butStillCallsUpdate_dueToControllerOrder() throws Exception {
        when(guestDomain.updatePreferences(eq("G1"), any(GuestPreferences.class)))
                .thenReturn(prefs("G1"));
        when(guestDomain.getGuestById("G1")).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/guests/G1/preferences")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(prefs("IGNORED"))))
                .andExpect(status().isNotFound());

        verify(guestDomain).updatePreferences(eq("G1"), any(GuestPreferences.class));
        verify(guestDomain).getGuestById("G1");
        verifyNoMoreInteractions(guestDomain);
    }

    // ===================== DELETE /api/guests/{id}/preferences

    @Test
    void deletePreferences_whenDeleted_returns204() throws Exception {
        when(guestDomain.deletePreferencesByGuestId("G1")).thenReturn(true);

        mockMvc.perform(delete("/api/guests/G1/preferences"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(guestDomain).deletePreferencesByGuestId("G1");
        verifyNoMoreInteractions(guestDomain);
    }

    @Test
    void deletePreferences_whenNotDeleted_returns404() throws Exception {
        when(guestDomain.deletePreferencesByGuestId("G1")).thenReturn(false);

        mockMvc.perform(delete("/api/guests/G1/preferences"))
                .andExpect(status().isNotFound());

        verify(guestDomain).deletePreferencesByGuestId("G1");
        verifyNoMoreInteractions(guestDomain);
    }

    // ===================== DELETE /api/guests/{id}

    @Test
    void deleteGuest_whenDeleted_returns204() throws Exception {
        when(guestDomain.deleteGuest("G1")).thenReturn(true);

        mockMvc.perform(delete("/api/guests/G1"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(guestDomain).deleteGuest("G1");
        verifyNoMoreInteractions(guestDomain);
    }

    @Test
    void deleteGuest_whenNotDeleted_returns404() throws Exception {
        when(guestDomain.deleteGuest("G1")).thenReturn(false);

        mockMvc.perform(delete("/api/guests/G1"))
                .andExpect(status().isNotFound());

        verify(guestDomain).deleteGuest("G1");
        verifyNoMoreInteractions(guestDomain);
    }
}
