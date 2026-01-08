package org.docencia.hotel.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.docencia.hotel.domain.model.Guest;
import org.docencia.hotel.domain.model.GuestPreferences;
import org.docencia.hotel.mapper.jpa.GuestMapper;
import org.docencia.hotel.mapper.nosql.GuestPreferencesMapper;
import org.docencia.hotel.persistence.jpa.entity.GuestEntity;
import org.docencia.hotel.persistence.nosql.document.GuestPreferencesDocument;
import org.docencia.hotel.persistence.repository.jpa.GuestJpaRepository;
import org.docencia.hotel.persistence.repository.nosql.GuestPreferencesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GuestServiceImplTest {

    @Mock private GuestJpaRepository guestJpaRepository;
    @Mock private GuestPreferencesRepository guestPreferencesRepository;
    @Mock private GuestMapper guestMapper;
    @Mock private GuestPreferencesMapper guestPreferencesMapper;

    @InjectMocks
    private GuestServiceImpl service;

    // ===== helpers mínimos =====
    private static Guest guest(String id, String name) {
        Guest g = new Guest();
        g.setId(id);
        g.setName(name);
        return g;
    }

    private static GuestEntity guestEntity(String id) {
        GuestEntity e = new GuestEntity();
        e.setId(id);
        return e;
    }

    private static GuestPreferences prefs(String guestId) {
        GuestPreferences p = new GuestPreferences();
        p.setGuestId(guestId);
        return p;
    }

    private static GuestPreferencesDocument prefsDoc(String guestId) {
        GuestPreferencesDocument d = new GuestPreferencesDocument();
        d.setGuestId(guestId);
        return d;
    }

    // ===================== save =====================

    @Test
    void save_whenGuestNull_throwsNullPointerException_andNoInteractions() {
        assertThrows(NullPointerException.class, () -> service.save(null));
        verifyNoInteractions(guestJpaRepository, guestPreferencesRepository, guestMapper, guestPreferencesMapper);
    }

    @Test
    void save_whenNoPreferences_savesOnlyGuest_andReturnsGuestWithNullPreferences() {
        Guest input = guest("g1", "Ana");
        input.setPreferences(null);

        GuestEntity entity = guestEntity("g1");
        GuestEntity savedEntity = guestEntity("g1");

        Guest mappedBack = guest("g1", "Ana"); // lo que devuelve toDomain

        when(guestMapper.toEntity(input)).thenReturn(entity);
        when(guestJpaRepository.save(entity)).thenReturn(savedEntity);
        when(guestMapper.toDomain(savedEntity)).thenReturn(mappedBack);

        Guest result = service.save(input);

        assertSame(mappedBack, result);
        assertNull(result.getPreferences(), "Si prefs es null, el servicio debe devolver preferences=null");

        verify(guestMapper).toEntity(input);
        verify(guestJpaRepository).save(entity);
        verify(guestMapper).toDomain(savedEntity);

        verifyNoInteractions(guestPreferencesRepository, guestPreferencesMapper);
        verifyNoMoreInteractions(guestJpaRepository, guestMapper);
    }

    @Test
    void save_whenPreferencesPresent_savesGuest_thenSavesPreferences_andReturnsGuestWithPreferencesFromDoc() {
        Guest input = guest("g1", "Ana");
        GuestPreferences inputPrefs = prefs("willBeOverwritten");
        input.setPreferences(inputPrefs);

        GuestEntity entity = guestEntity("g1");
        GuestEntity savedEntity = guestEntity("g1");

        Guest mappedBack = guest("g1", "Ana");

        GuestPreferencesDocument docFromMapper = prefsDoc("willBeOverwritten");
        GuestPreferencesDocument savedDoc = prefsDoc("g1");
        GuestPreferences prefsFromSavedDoc = prefs("g1");

        when(guestMapper.toEntity(input)).thenReturn(entity);
        when(guestJpaRepository.save(entity)).thenReturn(savedEntity);
        when(guestMapper.toDomain(savedEntity)).thenReturn(mappedBack);

        when(guestPreferencesMapper.toDocument(inputPrefs)).thenReturn(docFromMapper);
        when(guestPreferencesRepository.save(docFromMapper)).thenReturn(savedDoc);
        when(guestPreferencesMapper.toDomain(savedDoc)).thenReturn(prefsFromSavedDoc);

        Guest result = service.save(input);

        // Importante: el servicio fuerza guestId
        assertEquals("g1", inputPrefs.getGuestId(), "Debe forzar guestId en el objeto preferences de entrada");
        assertEquals("g1", docFromMapper.getGuestId(), "Debe forzar guestId en el documento antes de guardar");

        assertSame(mappedBack, result);
        assertNotNull(result.getPreferences());
        assertSame(prefsFromSavedDoc, result.getPreferences(), "Debe poner en el guest el resultado mapeado del doc guardado");

        verify(guestMapper).toEntity(input);
        verify(guestJpaRepository).save(entity);
        verify(guestMapper).toDomain(savedEntity);

        verify(guestPreferencesMapper).toDocument(inputPrefs);
        verify(guestPreferencesRepository).save(docFromMapper);
        verify(guestPreferencesMapper).toDomain(savedDoc);

        verifyNoMoreInteractions(guestJpaRepository, guestMapper, guestPreferencesRepository, guestPreferencesMapper);
    }

    // ===================== savedPreferences =====================

    @Test
    void savedPreferences_whenNull_throwsNullPointerException_andNoInteractions() {
        assertThrows(NullPointerException.class, () -> service.savedPreferences(null));
        verifyNoInteractions(guestPreferencesRepository, guestPreferencesMapper, guestJpaRepository, guestMapper);
    }

    @Test
    void savedPreferences_whenGuestIdNull_throwsNullPointerException_andNoInteractions() {
        GuestPreferences p = prefs(null);
        assertThrows(NullPointerException.class, () -> service.savedPreferences(p));
        verifyNoInteractions(guestPreferencesRepository, guestPreferencesMapper, guestJpaRepository, guestMapper);
    }

    @Test
    void savedPreferences_whenGuestIdBlank_throwsIllegalArgumentException_andNoInteractions() {
        GuestPreferences p = prefs("   ");
        assertThrows(IllegalArgumentException.class, () -> service.savedPreferences(p));
        verifyNoInteractions(guestPreferencesRepository, guestPreferencesMapper, guestJpaRepository, guestMapper);
    }

    @Test
    void savedPreferences_ok_maps_saves_andReturnsDomain() {
        GuestPreferences input = prefs("g1");
        GuestPreferencesDocument doc = prefsDoc("g1");
        GuestPreferencesDocument savedDoc = prefsDoc("g1");
        GuestPreferences expected = prefs("g1");

        when(guestPreferencesMapper.toDocument(input)).thenReturn(doc);
        when(guestPreferencesRepository.save(doc)).thenReturn(savedDoc);
        when(guestPreferencesMapper.toDomain(savedDoc)).thenReturn(expected);

        GuestPreferences result = service.savedPreferences(input);

        assertSame(expected, result);

        verify(guestPreferencesMapper).toDocument(input);
        verify(guestPreferencesRepository).save(doc);
        verify(guestPreferencesMapper).toDomain(savedDoc);

        verifyNoMoreInteractions(guestPreferencesRepository, guestPreferencesMapper);
        verifyNoInteractions(guestJpaRepository, guestMapper);
    }

    // ===================== existsById =====================

    @Test
    void existsById_whenNull_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> service.existsById(null));
        verifyNoInteractions(guestJpaRepository, guestPreferencesRepository, guestMapper, guestPreferencesMapper);
    }

    @Test
    void existsById_whenBlank_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> service.existsById("   "));
        verifyNoInteractions(guestJpaRepository, guestPreferencesRepository, guestMapper, guestPreferencesMapper);
    }

    @Test
    void existsById_ok_delegates() {
        when(guestJpaRepository.existsById("g1")).thenReturn(true);

        boolean result = service.existsById("g1");

        assertTrue(result);
        verify(guestJpaRepository).existsById("g1");
        verifyNoMoreInteractions(guestJpaRepository);
        verifyNoInteractions(guestPreferencesRepository, guestMapper, guestPreferencesMapper);
    }

    // ===================== findGuestById =====================

    @Test
    void findGuestById_whenNull_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> service.findGuestById(null));
        verifyNoInteractions(guestJpaRepository, guestPreferencesRepository, guestMapper, guestPreferencesMapper);
    }

    @Test
    void findGuestById_whenBlank_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> service.findGuestById("   "));
        verifyNoInteractions(guestJpaRepository, guestPreferencesRepository, guestMapper, guestPreferencesMapper);
    }

    @Test
    void findGuestById_whenNotFound_returnsEmpty_andDoesNotQueryPreferences() {
        when(guestJpaRepository.findById("g404")).thenReturn(Optional.empty());

        Optional<Guest> result = service.findGuestById("g404");

        assertTrue(result.isEmpty());

        verify(guestJpaRepository).findById("g404");
        verifyNoMoreInteractions(guestJpaRepository);

        verifyNoInteractions(guestPreferencesRepository, guestMapper, guestPreferencesMapper);
    }

    @Test
    void findGuestById_whenFound_andPreferencesMissing_setsPreferencesNull() {
        GuestEntity entity = guestEntity("g1");
        Guest domain = guest("g1", "Ana");

        when(guestJpaRepository.findById("g1")).thenReturn(Optional.of(entity));
        when(guestMapper.toDomain(entity)).thenReturn(domain);

        when(guestPreferencesRepository.findById("g1")).thenReturn(Optional.empty());

        Optional<Guest> result = service.findGuestById("g1");

        assertTrue(result.isPresent());
        assertSame(domain, result.get());
        assertNull(domain.getPreferences(), "Si no hay doc, debe dejar preferences a null");

        verify(guestJpaRepository).findById("g1");
        verify(guestMapper).toDomain(entity);
        verify(guestPreferencesRepository).findById("g1");

        verifyNoMoreInteractions(guestJpaRepository, guestMapper, guestPreferencesRepository);
        verifyNoInteractions(guestPreferencesMapper);
    }

    @Test
    void findGuestById_whenFound_andPreferencesFound_setsPreferences() {
        GuestEntity entity = guestEntity("g1");
        Guest domain = guest("g1", "Ana");

        GuestPreferencesDocument doc = prefsDoc("g1");
        GuestPreferences mappedPrefs = prefs("g1");

        when(guestJpaRepository.findById("g1")).thenReturn(Optional.of(entity));
        when(guestMapper.toDomain(entity)).thenReturn(domain);

        when(guestPreferencesRepository.findById("g1")).thenReturn(Optional.of(doc));
        when(guestPreferencesMapper.toDomain(doc)).thenReturn(mappedPrefs);

        Optional<Guest> result = service.findGuestById("g1");

        assertTrue(result.isPresent());
        assertSame(domain, result.get());
        assertSame(mappedPrefs, domain.getPreferences());

        verify(guestJpaRepository).findById("g1");
        verify(guestMapper).toDomain(entity);
        verify(guestPreferencesRepository).findById("g1");
        verify(guestPreferencesMapper).toDomain(doc);

        verifyNoMoreInteractions(guestJpaRepository, guestMapper, guestPreferencesRepository, guestPreferencesMapper);
    }

    // ===================== findAllGuests =====================

    @Test
    void findAllGuests_whenEmpty_returnsEmpty_andDoesNotQueryPreferences() {
        when(guestJpaRepository.findAll()).thenReturn(List.of());

        List<Guest> result = service.findAllGuests();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(guestJpaRepository).findAll();
        verifyNoMoreInteractions(guestJpaRepository);

        verifyNoInteractions(guestPreferencesRepository, guestMapper, guestPreferencesMapper);
    }

    @Test
    void findAllGuests_whenGuestsExist_loadsPreferencesByIds_andAssigns() {
        GuestEntity e1 = guestEntity("g1");
        GuestEntity e2 = guestEntity("g2");

        Guest g1 = guest("g1", "Ana");
        Guest g2 = guest("g2", "Luis");

        when(guestJpaRepository.findAll()).thenReturn(List.of(e1, e2));
        when(guestMapper.toDomain(e1)).thenReturn(g1);
        when(guestMapper.toDomain(e2)).thenReturn(g2);

        GuestPreferencesDocument d1 = prefsDoc("g1");
        GuestPreferences p1 = prefs("g1");

        when(guestPreferencesRepository.findAllById(List.of("g1", "g2"))).thenReturn(List.of(d1));
        when(guestPreferencesMapper.toDomain(d1)).thenReturn(p1);

        List<Guest> result = service.findAllGuests();

        assertEquals(2, result.size());
        assertSame(g1, result.get(0));
        assertSame(g2, result.get(1));

        assertSame(p1, g1.getPreferences());
        assertNull(g2.getPreferences(), "Si no hay prefs para g2, debe quedar null");

        verify(guestJpaRepository).findAll();
        verify(guestMapper).toDomain(e1);
        verify(guestMapper).toDomain(e2);

        verify(guestPreferencesRepository).findAllById(List.of("g1", "g2"));
        verify(guestPreferencesMapper).toDomain(d1);

        verifyNoMoreInteractions(guestJpaRepository, guestMapper, guestPreferencesRepository, guestPreferencesMapper);
    }

    // ===================== findPreferencesByGuestId =====================

    @Test
    void findPreferencesByGuestId_whenNull_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> service.findPreferencesByGuestId(null));
        verifyNoInteractions(guestPreferencesRepository, guestPreferencesMapper, guestJpaRepository, guestMapper);
    }

    @Test
    void findPreferencesByGuestId_whenBlank_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> service.findPreferencesByGuestId("   "));
        verifyNoInteractions(guestPreferencesRepository, guestPreferencesMapper, guestJpaRepository, guestMapper);
    }

    @Test
    void findPreferencesByGuestId_ok_delegatesAndMaps() {
        GuestPreferencesDocument doc = prefsDoc("g1");
        GuestPreferences pref = prefs("g1");

        when(guestPreferencesRepository.findById("g1")).thenReturn(Optional.of(doc));
        when(guestPreferencesMapper.toDomain(doc)).thenReturn(pref);

        Optional<GuestPreferences> result = service.findPreferencesByGuestId("g1");

        assertTrue(result.isPresent());
        assertSame(pref, result.get());

        verify(guestPreferencesRepository).findById("g1");
        verify(guestPreferencesMapper).toDomain(doc);

        verifyNoMoreInteractions(guestPreferencesRepository, guestPreferencesMapper);
        verifyNoInteractions(guestJpaRepository, guestMapper);
    }

    // ===================== deletePreferencesByGuestId =====================

    @Test
    void deletePreferencesByGuestId_whenNull_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> service.deletePreferencesByGuestId(null));
        verifyNoInteractions(guestPreferencesRepository, guestPreferencesMapper, guestJpaRepository, guestMapper);
    }

    @Test
    void deletePreferencesByGuestId_whenBlank_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> service.deletePreferencesByGuestId("   "));
        verifyNoInteractions(guestPreferencesRepository, guestPreferencesMapper, guestJpaRepository, guestMapper);
    }

    @Test
    void deletePreferencesByGuestId_whenNotExists_returnsFalse_andDoesNotDelete() {
        when(guestPreferencesRepository.existsById("g1")).thenReturn(false);

        boolean result = service.deletePreferencesByGuestId("g1");

        assertFalse(result);

        verify(guestPreferencesRepository).existsById("g1");
        verify(guestPreferencesRepository, never()).deleteById(anyString());

        verifyNoMoreInteractions(guestPreferencesRepository);
        verifyNoInteractions(guestPreferencesMapper, guestJpaRepository, guestMapper);
    }

    @Test
    void deletePreferencesByGuestId_whenExists_deletesAndReturnsTrue() {
        when(guestPreferencesRepository.existsById("g1")).thenReturn(true);

        boolean result = service.deletePreferencesByGuestId("g1");

        assertTrue(result);

        verify(guestPreferencesRepository).existsById("g1");
        verify(guestPreferencesRepository).deleteById("g1");
        verifyNoMoreInteractions(guestPreferencesRepository);

        verifyNoInteractions(guestPreferencesMapper, guestJpaRepository, guestMapper);
    }

    // ===================== deleteGuestById =====================

    @Test
    void deleteGuestById_whenNull_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> service.deleteGuestById(null));
        verifyNoInteractions(guestJpaRepository, guestPreferencesRepository, guestMapper, guestPreferencesMapper);
    }

    @Test
    void deleteGuestById_whenBlank_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> service.deleteGuestById("   "));
        verifyNoInteractions(guestJpaRepository, guestPreferencesRepository, guestMapper, guestPreferencesMapper);
    }

    @Test
    void deleteGuestById_whenNotExists_returnsFalse_andDoesNotDelete() {
        when(guestJpaRepository.existsById("g1")).thenReturn(false);

        boolean result = service.deleteGuestById("g1");

        assertFalse(result);

        verify(guestJpaRepository).existsById("g1");
        verify(guestJpaRepository, never()).deleteById(anyString());

        verifyNoMoreInteractions(guestJpaRepository);
        verifyNoInteractions(guestPreferencesRepository, guestMapper, guestPreferencesMapper);
    }

    @Test
    void deleteGuestById_whenExists_deletesAndReturnsTrue() {
        when(guestJpaRepository.existsById("g1")).thenReturn(true);

        boolean result = service.deleteGuestById("g1");

        assertTrue(result);

        verify(guestJpaRepository).existsById("g1");
        verify(guestJpaRepository).deleteById("g1");
        verifyNoMoreInteractions(guestJpaRepository);

        verifyNoInteractions(guestPreferencesRepository, guestMapper, guestPreferencesMapper);
    }
}
