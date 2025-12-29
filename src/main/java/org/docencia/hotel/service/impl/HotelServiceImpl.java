package org.docencia.hotel.service.impl;

import java.util.List;
import java.util.Optional;

import org.docencia.hotel.domain.model.Hotel;
import org.docencia.hotel.mapper.jpa.HotelMapper;
import org.docencia.hotel.persistence.jpa.entity.HotelEntity;
import org.docencia.hotel.persistence.repository.jpa.HotelRepository;
import org.docencia.hotel.service.api.HotelService;
import org.docencia.hotel.validation.Guard;
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
        Guard.requireNonNull(hotel, "hotel");

        HotelEntity hotelEntityToSave = hotelMapper.toEntity(hotel);
        HotelEntity savHotelEntity = hotelRepository.save(hotelEntityToSave);
        return hotelMapper.toDomain(savHotelEntity);
    }

    @Override
    public boolean existsById(String id) {
        Guard.requireNonBlank(id, "hotel id");

        return hotelRepository.existsById(id);
    }

    @Override
    public Optional<Hotel> findById(String id) {
        Guard.requireNonBlank(id, "hotel id");

        return hotelRepository.findById(id).
                map(hotelMapper::toDomain);
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
        Guard.requireNonBlank(name, "hotel name");

        return hotelRepository.findByHotelName(name)
                .stream()
                .map(hotelMapper::toDomain)
                .toList();
    }

    @Override
    public boolean deleteById(String id) {
        Guard.requireNonBlank(id, "hotel id");

        if (!hotelRepository.existsById(id)) {
            return false;
        }

        hotelRepository.deleteById(id);
        return true;
    }
}