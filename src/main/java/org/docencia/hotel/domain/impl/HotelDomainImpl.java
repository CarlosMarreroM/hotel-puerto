package org.docencia.hotel.domain.impl;

import java.util.List;
import java.util.Optional;

import org.docencia.hotel.domain.api.HotelDomain;
import org.docencia.hotel.domain.model.Hotel;
import org.docencia.hotel.service.api.HotelService;
import org.docencia.hotel.service.api.RoomService;
import org.docencia.hotel.validation.Guard;
import org.springframework.stereotype.Service;

@Service
public class HotelDomainImpl implements HotelDomain {

    /**
     * Servicio de hoteles.
     */
    private final HotelService hotelService;

    /**
     * Servicio de habitaciones.
     */
    private final RoomService roomService;

    /**
     * Constructor de la implementación del dominio de hoteles.
     * 
     * @param hotelService Servicio de hoteles
     * @param roomService  Servicio de habitaciones
     */
    public HotelDomainImpl(HotelService hotelService, RoomService roomService) {
        this.hotelService = hotelService;
        this.roomService = roomService;
    }

    @Override
    public Hotel createHotel(Hotel hotel) {
        Guard.requireNonNull(hotel, "hotel");
        Guard.requireNonBlank(hotel.getId(), "hotel id");
        Guard.requireNonBlank(hotel.getHotelName(), "hotel name");

        return hotelService.save(hotel);
    }

    @Override
    public Optional<Hotel> getHotelById(String id) {
        Guard.requireNonBlank(id, "hotel id");

        return hotelService.findById(id);
    }

    @Override
    public List<Hotel> getAllHotels() {
        return hotelService.findAll();
    }

    @Override
    public List<Hotel> getHotelsByName(String name) {
        Guard.requireNonBlank(name, "name");

        return hotelService.findByName(name);
    }

    @Override
    public Hotel updateHotel(String id, Hotel hotel) {
        Guard.requireNonBlank(id, "hotel id");
        Guard.requireNonNull(hotel, "hotel");
        Guard.requireNonBlank(hotel.getHotelName(), "hotel name");

        if (!hotelService.existsById(id)) {
            throw new IllegalArgumentException("hotel not found: " + id);
        }

        hotel.setId(id);
        return hotelService.save(hotel);
    }

    @Override
    public boolean deleteHotel(String id) {
        Guard.requireNonBlank(id, "hotel id");

        if (!hotelService.existsById(id)) {
            return false;
        }

        roomService.deleteByHotelId(id);
        return hotelService.deleteById(id);
    }
}