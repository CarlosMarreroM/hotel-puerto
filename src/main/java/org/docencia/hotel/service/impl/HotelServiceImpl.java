package org.docencia.hotel.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.docencia.hotel.domain.model.Hotel;
import org.docencia.hotel.mapper.jpa.HotelMapper;
import org.docencia.hotel.persistence.jpa.entity.HotelEntity;
import org.docencia.hotel.persistence.repository.jpa.HotelRepository;
import org.docencia.hotel.service.api.HotelService;
import org.springframework.stereotype.Service;

@Service
public class HotelServiceImpl implements HotelService {

    /**
     * Repositorio JPA de hoteles.
     */
    private final HotelRepository hotelRepository;

    /**
     * Mapeador entre la entidad JPA y el modelo de dominio.
     */
    private final HotelMapper hotelMapper;

    /**
     * Constructor de la implementación del servicio de hoteles.
     * 
     * @param hotelRepository Repositorio JPA de hoteles
     * @param hotelMapper     Mapeador entre la entidad JPA y el modelo de dominio
     */
    public HotelServiceImpl(HotelRepository hotelRepository, HotelMapper hotelMapper) {
        this.hotelRepository = hotelRepository;
        this.hotelMapper = hotelMapper;
    }

    @Override
    public Hotel save(Hotel hotel) {
        Objects.requireNonNull(hotel, "hotel must not be null");

        HotelEntity hotelEntityToSave = hotelMapper.toEntity(hotel);
        HotelEntity savHotelEntity = hotelRepository.save(hotelEntityToSave);
        return hotelMapper.toDomain(savHotelEntity);
    }

    @Override
    public boolean existsById(String id) {
        Objects.requireNonNull(id, "hotel id must not be null");

        if (id.isBlank()) {
            throw new IllegalArgumentException("hotel id must not be blank");
        }

        return hotelRepository.existsById(id);
    }

    @Override
    public Optional<Hotel> findById(String id) {
        Objects.requireNonNull(id, "hotel id must not be null");

        if (id.isBlank()) {
            throw new IllegalArgumentException("hotel id must not be blank");
        }

        return hotelRepository.findById(id).map(hotelMapper::toDomain);
    }

    @Override
    public List<Hotel> findAll() {
        return hotelRepository.findAll()
                .stream()
                .map(hotelMapper::toDomain)
                .toList();
    }

    @Override
    public List<Hotel> findByName(String name) {
        Objects.requireNonNull(name, "name must not be null");
        if (name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }

        return hotelRepository.findByHotelName(name)
                .stream()
                .map(hotelMapper::toDomain)
                .toList();
    }

    @Override
    public boolean deleteById(String id) {
        Objects.requireNonNull(id, "hotel id must not be null");

        if (id.isBlank()) {
            throw new IllegalArgumentException("hotel id must not be blank");
        }

        if (!hotelRepository.existsById(id)) {
            return false;
        }

        hotelRepository.deleteById(id);
        return true;
    }
}