package org.docencia.hotel.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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

    @Mock
    private GuestJpaRepository guestJpaRepository;

    @Mock
    private GuestPreferencesRepository guestPreferencesRepository;

    @Mock
    private GuestMapper guestMapper;

    @Mock
    private GuestPreferencesMapper guestPreferencesMapper;

    @InjectMocks
    private GuestServiceImpl service;

    // ===== helpers mínimos =====

    private static Guest guestWithId(String id) {
        Guest g = new Guest();
        g.setId(id);
        return g;
    }

    private static GuestEntity guestEntityWithId(String id) {
        GuestEntity e = new GuestEntity();
        e.setId(id);
        return e;
    }

    private static GuestPreferences prefsWithGuestId(String guestId) {
        GuestPreferences p = new GuestPreferences();
        p.setGuestId(guestId);
        return p;
    }

    private static GuestPreferencesDocument prefsDocWithGuestId(String guestId) {
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
    void save_whenGuestIdNull_throwsNullPointerException_andNoInteractions() {
        Guest g = new Guest(); // id null

        assertThrows(NullPointerException.class, () -> service.save(g));
        verifyNoInteractions(guestJpaRepository, guestPreferencesRepository, guestMapper, guestPreferencesMapper);
    }

    @Test
    void save_whenGuestIdBlank_throwsIllegalArgumentException_andNoInteractions() {
        Guest g = guestWithId("   ");

        assertThrows(IllegalArgumentException.class, () -> service.save(g));
        verifyNoInteractions(guestJpaRepository, guestPreferencesRepository, guestMapper, guestPreferencesMapper);
    }

    @Test
    void save_whenNoPreferences_savesOnlyJpa_andDoesNotTouchMongo() {
        Guest input = guestWithId("g1");
        GuestEntity entityToSave = guestEntityWithId("g1");
        GuestEntity savedEntity = guestEntityWithId("g1");
        Guest mappedGuest = guestWithId("g1"); // lo que devuelve el mapper tras guardar

        when(guestMapper.toEntity(input)).thenReturn(entityToSave);
        when(guestJpaRepository.save(entityToSave)).thenReturn(savedEntity);
        when(guestMapper.toDomain(savedEntity)).thenReturn(mappedGuest);

        Guest result = service.save(input);

        assertSame(mappedGuest, result);

        verify(guestMapper).toEntity(input);
        verify(guestJpaRepository).save(entityToSave);
        verify(guestMapper).toDomain(savedEntity);

        verifyNoInteractions(guestPreferencesRepository);
        verifyNoInteractions(guestPreferencesMapper);

        verifyNoMoreInteractions(guestJpaRepository, guestMapper);
    }

    @Test
    void save_whenPreferencesPresent_savesJpa_thenSavesMongo_andSetsPreferencesInReturnedGuest() {
        // input guest tiene preferences
        GuestPreferences inputPrefs = prefsWithGuestId("g1"); // el service luego fuerza guestId del doc al id guardado
        Guest input = guestWithId("g1");
        input.setPreferences(inputPrefs);

        // JPA mapping/saving
        GuestEntity entityToSave = guestEntityWithId("g1");
        GuestEntity savedEntity = guestEntityWithId("g1");
        Guest savedGuestFromJpa = guestWithId("g1");

        when(guestMapper.toEntity(input)).thenReturn(entityToSave);
        when(guestJpaRepository.save(entityToSave)).thenReturn(savedEntity);
        when(guestMapper.toDomain(savedEntity)).thenReturn(savedGuestFromJpa);

        // Mongo mapping/saving
        GuestPreferencesDocument docToSave = prefsDocWithGuestId("ignored"); // se sobrescribe con savedGuestFromJpa.getId()
        GuestPreferencesDocument savedDoc = prefsDocWithGuestId("g1");
        GuestPreferences mappedBackPrefs = prefsWithGuestId("g1");

        when(guestPreferencesMapper.toDocument(inputPrefs)).thenReturn(docToSave);
        when(guestPreferencesRepository.save(docToSave)).thenReturn(savedDoc);
        when(guestPreferencesMapper.toDomain(savedDoc)).thenReturn(mappedBackPrefs);

        Guest result = service.save(input);

        assertSame(savedGuestFromJpa, result);
        assertSame(mappedBackPrefs, result.getPreferences(), "Debe setear las preferencias ya persistidas en el Guest devuelto");
        assertEquals("g1", docToSave.getGuestId(), "Debe forzar el guestId del documento al id del Guest guardado");

        // verify JPA
        verify(guestMapper).toEntity(input);
        verify(guestJpaRepository).save(entityToSave);
        verify(guestMapper).toDomain(savedEntity);

        // verify Mongo
        verify(guestPreferencesMapper).toDocument(inputPrefs);
        verify(guestPreferencesRepository).save(docToSave);
        verify(guestPreferencesMapper).toDomain(savedDoc);

        verifyNoMoreInteractions(guestJpaRepository, guestPreferencesRepository, guestMapper, guestPreferencesMapper);
    }

    @Test
    void save_whenMapperReturnsGuestWithDifferentId_documentUsesSavedGuestId() {
        GuestPreferences inputPrefs = prefsWithGuestId("g1");
        Guest input = guestWithId("g1");
        input.setPreferences(inputPrefs);

        GuestEntity entityToSave = guestEntityWithId("g1");
        GuestEntity savedEntity = guestEntityWithId("g1");

        // El mapper devuelve un Guest con id diferente (simula que JPA/mapper "normaliza" id)
        Guest savedGuestFromJpa = guestWithId("G-001");

        when(guestMapper.toEntity(input)).thenReturn(entityToSave);
        when(guestJpaRepository.save(entityToSave)).thenReturn(savedEntity);
        when(guestMapper.toDomain(savedEntity)).thenReturn(savedGuestFromJpa);

        GuestPreferencesDocument docToSave = prefsDocWithGuestId("willBeOverwritten");
        GuestPreferencesDocument savedDoc = prefsDocWithGuestId("G-001");
        GuestPreferences mappedBackPrefs = prefsWithGuestId("G-001");

        when(guestPreferencesMapper.toDocument(inputPrefs)).thenReturn(docToSave);
        when(guestPreferencesRepository.save(docToSave)).thenReturn(savedDoc);
        when(guestPreferencesMapper.toDomain(savedDoc)).thenReturn(mappedBackPrefs);

        Guest result = service.save(input);

        assertEquals("G-001", docToSave.getGuestId(), "Debe usar el id del Guest ya guardado (no el del input)");
        assertEquals("G-001", result.getId());
        assertSame(mappedBackPrefs, result.getPreferences());

        verifyNoMoreInteractions(guestJpaRepository, guestPreferencesRepository, guestMapper, guestPreferencesMapper);
    }

    // ===================== updatePreferences =====================

    @Test
    void updatePreferences_whenNull_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> service.updatePreferences(null));
        verifyNoInteractions(guestPreferencesRepository, guestPreferencesMapper);
        verifyNoInteractions(guestJpaRepository, guestMapper);
    }

    @Test
    void updatePreferences_whenGuestIdNull_throwsNullPointerException() {
        GuestPreferences p = new GuestPreferences(); // guestId null
        assertThrows(NullPointerException.class, () -> service.updatePreferences(p));
        verifyNoInteractions(guestPreferencesRepository, guestPreferencesMapper);
        verifyNoInteractions(guestJpaRepository, guestMapper);
    }

    @Test
    void updatePreferences_whenGuestIdBlank_throwsIllegalArgumentException() {
        GuestPreferences p = prefsWithGuestId("   ");
        assertThrows(IllegalArgumentException.class, () -> service.updatePreferences(p));
        verifyNoInteractions(guestPreferencesRepository, guestPreferencesMapper);
        verifyNoInteractions(guestJpaRepository, guestMapper);
    }

    @Test
    void updatePreferences_ok_mapsSavesAndMapsBack() {
        GuestPreferences input = prefsWithGuestId("g1");
        GuestPreferencesDocument doc = prefsDocWithGuestId("g1");
        GuestPreferencesDocument savedDoc = prefsDocWithGuestId("g1");
        GuestPreferences expected = prefsWithGuestId("g1");

        when(guestPreferencesMapper.toDocument(input)).thenReturn(doc);
        when(guestPreferencesRepository.save(doc)).thenReturn(savedDoc);
        when(guestPreferencesMapper.toDomain(savedDoc)).thenReturn(expected);

        GuestPreferences result = service.updatePreferences(input);

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
        verifyNoInteractions(guestJpaRepository, guestMapper, guestPreferencesRepository, guestPreferencesMapper);
    }

    @Test
    void existsById_whenBlank_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> service.existsById("   "));
        verifyNoInteractions(guestJpaRepository, guestMapper, guestPreferencesRepository, guestPreferencesMapper);
    }

    @Test
    void existsById_ok_delegatesToJpa() {
        when(guestJpaRepository.existsById("g1")).thenReturn(true);

        boolean result = service.existsById("g1");

        assertTrue(result);
        verify(guestJpaRepository).existsById("g1");
        verifyNoMoreInteractions(guestJpaRepository);
        verifyNoInteractions(guestMapper, guestPreferencesRepository, guestPreferencesMapper);
    }

    // ===================== findGuestById =====================

    @Test
    void findGuestById_whenNull_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> service.findGuestById(null));
        verifyNoInteractions(guestJpaRepository, guestMapper, guestPreferencesRepository, guestPreferencesMapper);
    }

    @Test
    void findGuestById_whenBlank_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> service.findGuestById("  "));
        verifyNoInteractions(guestJpaRepository, guestMapper, guestPreferencesRepository, guestPreferencesMapper);
    }

    @Test
    void findGuestById_whenNotFound_returnsEmpty_andDoesNotMap() {
        when(guestJpaRepository.findById("g1")).thenReturn(Optional.empty());

        Optional<Guest> result = service.findGuestById("g1");

        assertTrue(result.isEmpty());
        verify(guestJpaRepository).findById("g1");
        verifyNoMoreInteractions(guestJpaRepository);
        verifyNoInteractions(guestMapper, guestPreferencesRepository, guestPreferencesMapper);
    }

    @Test
    void findGuestById_whenFound_mapsToDomain() {
        GuestEntity entity = guestEntityWithId("g1");
        Guest domain = guestWithId("g1");

        when(guestJpaRepository.findById("g1")).thenReturn(Optional.of(entity));
        when(guestMapper.toDomain(entity)).thenReturn(domain);

        Optional<Guest> result = service.findGuestById("g1");

        assertTrue(result.isPresent());
        assertSame(domain, result.get());

        verify(guestJpaRepository).findById("g1");
        verify(guestMapper).toDomain(entity);
        verifyNoMoreInteractions(guestJpaRepository, guestMapper);
        verifyNoInteractions(guestPreferencesRepository, guestPreferencesMapper);
    }

    // ===================== findAllGuests =====================

    @Test
    void findAllGuests_whenEmpty_returnsEmpty_andDoesNotMap() {
        when(guestJpaRepository.findAll()).thenReturn(List.of());

        List<Guest> result = service.findAllGuests();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(guestJpaRepository).findAll();
        verifyNoMoreInteractions(guestJpaRepository);
        verifyNoInteractions(guestMapper, guestPreferencesRepository, guestPreferencesMapper);
    }

    @Test
    void findAllGuests_mapsAllEntities() {
        GuestEntity e1 = guestEntityWithId("g1");
        GuestEntity e2 = guestEntityWithId("g2");
        Guest g1 = guestWithId("g1");
        Guest g2 = guestWithId("g2");

        when(guestJpaRepository.findAll()).thenReturn(List.of(e1, e2));
        when(guestMapper.toDomain(e1)).thenReturn(g1);
        when(guestMapper.toDomain(e2)).thenReturn(g2);

        List<Guest> result = service.findAllGuests();

        assertEquals(2, result.size());
        assertSame(g1, result.get(0));
        assertSame(g2, result.get(1));

        verify(guestJpaRepository).findAll();
        verify(guestMapper).toDomain(e1);
        verify(guestMapper).toDomain(e2);
        verifyNoMoreInteractions(guestJpaRepository, guestMapper);
        verifyNoInteractions(guestPreferencesRepository, guestPreferencesMapper);
    }

    // ===================== findPreferencesByGuestId =====================

    @Test
    void findPreferencesByGuestId_whenNull_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> service.findPreferencesByGuestId(null));
        verifyNoInteractions(guestJpaRepository, guestMapper, guestPreferencesRepository, guestPreferencesMapper);
    }

    @Test
    void findPreferencesByGuestId_whenBlank_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> service.findPreferencesByGuestId("  "));
        verifyNoInteractions(guestJpaRepository, guestMapper, guestPreferencesRepository, guestPreferencesMapper);
    }

    @Test
    void findPreferencesByGuestId_whenNotFound_returnsEmpty_andDoesNotMap() {
        when(guestPreferencesRepository.findById("g1")).thenReturn(Optional.empty());

        Optional<GuestPreferences> result = service.findPreferencesByGuestId("g1");

        assertTrue(result.isEmpty());
        verify(guestPreferencesRepository).findById("g1");
        verifyNoMoreInteractions(guestPreferencesRepository);
        verifyNoInteractions(guestPreferencesMapper, guestJpaRepository, guestMapper);
    }

    @Test
    void findPreferencesByGuestId_whenFound_mapsToDomain() {
        GuestPreferencesDocument doc = prefsDocWithGuestId("g1");
        GuestPreferences prefs = prefsWithGuestId("g1");

        when(guestPreferencesRepository.findById("g1")).thenReturn(Optional.of(doc));
        when(guestPreferencesMapper.toDomain(doc)).thenReturn(prefs);

        Optional<GuestPreferences> result = service.findPreferencesByGuestId("g1");

        assertTrue(result.isPresent());
        assertSame(prefs, result.get());

        verify(guestPreferencesRepository).findById("g1");
        verify(guestPreferencesMapper).toDomain(doc);
        verifyNoMoreInteractions(guestPreferencesRepository, guestPreferencesMapper);
        verifyNoInteractions(guestJpaRepository, guestMapper);
    }

    // ===================== deleteGuestById =====================

    @Test
    void deleteGuestById_whenNull_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> service.deleteGuestById(null));
        verifyNoInteractions(guestJpaRepository, guestMapper, guestPreferencesRepository, guestPreferencesMapper);
    }

    @Test
    void deleteGuestById_whenBlank_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> service.deleteGuestById("  "));
        verifyNoInteractions(guestJpaRepository, guestMapper, guestPreferencesRepository, guestPreferencesMapper);
    }

    @Test
    void deleteGuestById_whenNotExists_returnsFalse_andDoesNotDelete() {
        when(guestJpaRepository.existsById("g404")).thenReturn(false);

        boolean result = service.deleteGuestById("g404");

        assertFalse(result);
        verify(guestJpaRepository).existsById("g404");
        verify(guestJpaRepository, never()).deleteById(any());
        verifyNoMoreInteractions(guestJpaRepository);
        verifyNoInteractions(guestMapper, guestPreferencesRepository, guestPreferencesMapper);
    }

    @Test
    void deleteGuestById_whenExists_deletesAndReturnsTrue() {
        when(guestJpaRepository.existsById("g1")).thenReturn(true);

        boolean result = service.deleteGuestById("g1");

        assertTrue(result);
        verify(guestJpaRepository).existsById("g1");
        verify(guestJpaRepository).deleteById("g1");
        verifyNoMoreInteractions(guestJpaRepository);
        verifyNoInteractions(guestMapper, guestPreferencesRepository, guestPreferencesMapper);
    }

    // ===================== deletePreferencesByGuestId =====================

    @Test
    void deletePreferencesByGuestId_whenNull_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> service.deletePreferencesByGuestId(null));
        verifyNoInteractions(guestJpaRepository, guestMapper, guestPreferencesRepository, guestPreferencesMapper);
    }

    @Test
    void deletePreferencesByGuestId_whenBlank_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> service.deletePreferencesByGuestId("  "));
        verifyNoInteractions(guestJpaRepository, guestMapper, guestPreferencesRepository, guestPreferencesMapper);
    }

    @Test
    void deletePreferencesByGuestId_whenNotExists_returnsFalse_andDoesNotDelete() {
        when(guestPreferencesRepository.existsById("g404")).thenReturn(false);

        boolean result = service.deletePreferencesByGuestId("g404");

        assertFalse(result);
        verify(guestPreferencesRepository).existsById("g404");
        verify(guestPreferencesRepository, never()).deleteById(any());
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
}
