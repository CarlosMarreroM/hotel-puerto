package org.docencia.hotel.domain.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.docencia.hotel.domain.api.HotelDomain;
import org.docencia.hotel.domain.model.Hotel;
import org.docencia.hotel.service.api.HotelService;
import org.springframework.stereotype.Service;

@Service
public class HotelDomainImpl implements HotelDomain {

    /**
     * Servicio de hoteles.
     */
    private final HotelService hotelService;

    /**
     * Constructor de la implementación del dominio de hoteles.
     * 
     * @param hotelService Servicio de hoteles
     */
    public HotelDomainImpl(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @Override
    public Hotel createHotel(Hotel hotel) {
        Objects.requireNonNull(hotel, "hotel must not be null");

        if (hotel.getId() == null || hotel.getId().isBlank()) {
            throw new IllegalArgumentException("hotel id must not be blank");
        }

        if (hotel.getHotelName() == null || hotel.getHotelName().isBlank()) {
            throw new IllegalArgumentException("hotel name must not be blank");
        }

        return hotelService.findById(hotel.getId())
                .orElseGet(() -> hotelService.save(hotel));
    }

    @Override
    public Optional<Hotel> getHotelById(String id) {
        Objects.requireNonNull(id, "hotel id must not be null");

        if (id.isBlank()) {
            throw new IllegalArgumentException("hotel id must not be blank");
        }

        return hotelService.findById(id);
    }

    @Override
    public List<Hotel> getAllHotels() {
        return hotelService.findAll();
    }

    @Override
    public Hotel updateHotel(String id, Hotel hotel) {
        Objects.requireNonNull(id, "hotel id must not be null");
        Objects.requireNonNull(hotel, "hotel must not be null");

        if (id.isBlank()) {
            throw new IllegalArgumentException("hotel id must not be blank");
        }

        if (hotel.getHotelName() == null || hotel.getHotelName().isBlank()) {
            throw new IllegalArgumentException("hotel name must not be blank");
        }

        if (!hotelService.existsById(id)) {
            throw new IllegalArgumentException("hotel not found: " + id);
        }

        hotel.setId(id);

        return hotelService.save(hotel);
    }

    @Override
    public boolean deleteHotel(String id) {
        Objects.requireNonNull(id, "hotel id must not be null");

        if (id.isBlank()) {
            throw new IllegalArgumentException("hotel id must not be blank");
        }

        return hotelService.deleteById(id);
    }

    @Override
    public List<Hotel> findHotelsByName(String name) {
        Objects.requireNonNull(name, "name must not be null");

        if (name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }

        return hotelService.findByName(name);
    }
}
