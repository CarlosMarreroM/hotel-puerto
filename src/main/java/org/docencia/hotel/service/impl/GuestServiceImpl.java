package org.docencia.hotel.service.impl;

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
import org.docencia.hotel.service.api.GuestService;
import org.docencia.hotel.validation.Guard;
import org.springframework.stereotype.Service;

/**
 * Implementacion del servicio de gestion de huespedes.
 * 
 * Proporciona metodos para guardar y gestionar
 * los datos de los huespedes y sus preferencias
 * utilizando repositorios JPA y NoSQL.
 */
@Service
public class GuestServiceImpl implements GuestService {
    /**
     * Repositorio JPA para la entidad GuestEntity.
     */
    private final GuestJpaRepository guestJpaRepository;

    /**
     * Repositorio NoSQL para el documento GuestPreferencesDocument.
     */
    private final GuestPreferencesRepository guestPreferencesRepository;

    /**
     * Mapeador entre Guest y GuestEntity.
     */
    private final GuestMapper guestMapper;

    /**
     * Mapeador entre GuestPreferences y GuestPreferencesDocument.
     */
    private final GuestPreferencesMapper guestPreferencesMapper;

    /**
     * Constructor de la clase GuestServiceImpl.
     * 
     * @param guestJpaRepository         Repositorio JPA para GuestEntity.
     * @param guestPreferencesRepository Repositorio NoSQL para
     *                                   GuestPreferencesDocument.
     * @param guestMapper                Mapeador entre Guest y GuestEntity.
     * @param guestPreferencesMapper     Mapeador entre GuestPreferences y
     *                                   GuestPreferencesDocument.
     */
    public GuestServiceImpl(GuestJpaRepository guestJpaRepository,
            GuestPreferencesRepository guestPreferencesRepository, GuestMapper guestMapper,
            GuestPreferencesMapper guestPreferencesMapper) {
        this.guestJpaRepository = guestJpaRepository;
        this.guestPreferencesRepository = guestPreferencesRepository;
        this.guestMapper = guestMapper;
        this.guestPreferencesMapper = guestPreferencesMapper;
    }

    @Override
    public Guest save(Guest guest) {
        Guard.requireNonNull(guest, "Guest");

        GuestEntity guestEntity = guestMapper.toEntity(guest);
        GuestEntity savedEntity = guestJpaRepository.save(guestEntity);
        Guest savedGuest = guestMapper.toDomain(savedEntity);

        GuestPreferences preferences = guest.getPreferences();

        if (preferences != null) {
            GuestPreferencesDocument document = guestPreferencesMapper.toDocument(preferences);
            document.setGuestId(savedGuest.getId());
            GuestPreferencesDocument saveDocument = guestPreferencesRepository.save(document);
            savedGuest.setPreferences(guestPreferencesMapper.toDomain(saveDocument));
        }

        return savedGuest;
    }

    @Override
    public GuestPreferences savedPreferences(GuestPreferences preferences) {
        Guard.requireNonNull(preferences, "GuestPreferences");
        Guard.requireNonBlank(preferences.getGuestId(), "GuestPreferences guest id");

        GuestPreferencesDocument document = guestPreferencesMapper.toDocument(preferences);
        GuestPreferencesDocument savedDocument = guestPreferencesRepository.save(document);

        return guestPreferencesMapper.toDomain(savedDocument);
    }

    @Override
    public boolean existsById(String id) {
        Guard.requireNonBlank(id, "guest id");

        return guestJpaRepository.existsById(id);
    }

    @Override
    public Optional<Guest> findGuestById(String id) {
        Guard.requireNonBlank(id, "guest id");

        return guestJpaRepository.findById(id)
                .map(guestMapper::toDomain);
    }

    @Override
    public List<Guest> findAllGuests() {
        return guestJpaRepository.findAll()
                .stream()
                .map(guestMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<GuestPreferences> findPreferencesByGuestId(String guestId) {
        Guard.requireNonBlank(guestId, "guest id");

        return guestPreferencesRepository.findById(guestId)
                .map(guestPreferencesMapper::toDomain);
    }

    @Override
    public boolean deleteGuestById(String id) {
        Guard.requireNonBlank(id, "guest id");

        if (!guestJpaRepository.existsById(id))
            return false;

        guestJpaRepository.deleteById(id);

        return true;
    }

    @Override
    public boolean deletePreferencesByGuestId(String guestId) {
        Guard.requireNonBlank(guestId, "guest id");

        if (!guestPreferencesRepository.existsById(guestId))
            return false;

        guestPreferencesRepository.deleteById(guestId);

        return true;
    }
}