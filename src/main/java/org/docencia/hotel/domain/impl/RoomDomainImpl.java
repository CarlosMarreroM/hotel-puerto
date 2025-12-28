package org.docencia.hotel.domain.impl;

import java.util.List;
import java.util.Optional;

import org.docencia.hotel.domain.api.RoomDomain;
import org.docencia.hotel.domain.model.Room;
import org.docencia.hotel.service.api.HotelService;
import org.docencia.hotel.service.api.RoomService;
import org.docencia.hotel.validation.Guard;
import org.springframework.stereotype.Service;

/**
 * Implementación del dominio de habitaciones.
 *
 * Esta clase proporciona la lógica de negocio
 * para la gestión de habitaciones dentro del sistema.
 */
@Service
public class RoomDomainImpl implements RoomDomain {

    /**
     * Servicio de habitaciones.
     */
    private final RoomService roomService;

    /**
     * Servicio de hoteles.
     */
    private final HotelService hotelService;

    /**
     * Constructor de la implementación del dominio de habitaciones.
     * 
     * @param roomService  Servicio de habitaciones
     * @param hotelService Servicio de hoteles
     */
    public RoomDomainImpl(RoomService roomService, HotelService hotelService) {
        this.roomService = roomService;
        this.hotelService = hotelService;
    }

    @Override
    public Room createRoom(Room room) {
        Guard.requireNonNull(room, "room");
        Guard.requireNonBlank(room.getId(), "room id");
        Guard.requireNonBlank(room.getNumber(), "room number");
        Guard.requireNonBlank(room.getHotelId(), "hotel id");

        if (!hotelService.existsById(room.getHotelId())) {
            throw new IllegalArgumentException("Hotel with id " + room.getHotelId() + " does not exist");
        }

        return roomService.save(room);
    }

    @Override
    public Optional<Room> getRoomById(String id) {
        Guard.requireNonBlank(id, "room id");

        return roomService.findById(id);
    }

    @Override
    public List<Room> getAllRooms() {
        return roomService.findAll();
    }

    @Override
    public List<Room> getRoomsByHotel(String hotelId) {
        Guard.requireNonBlank(hotelId, "hotel id");

        if (!hotelService.existsById(hotelId)) {
            throw new IllegalArgumentException("Hotel with id " + hotelId + " does not exist");
        }

        return roomService.findByHotelId(hotelId);
    }

    @Override
    public List<Room> getRoomsByHotelAndType(String hotelId, String type) {
        Guard.requireNonBlank(hotelId, "hotel id");
        Guard.requireNonBlank(type, "room type");

        if (!hotelService.existsById(hotelId)) {
            throw new IllegalArgumentException("Hotel with id " + hotelId + " does not exist");
        }

        return roomService.findByHotelIdAndType(hotelId, type);
    }

    @Override
    public Room updateRoom(String id, Room room) {
        Guard.requireNonBlank(id, "room id");
        Guard.requireNonNull(room, "room");
        Guard.requireNonBlank(room.getNumber(), "room number");
        Guard.requireNonBlank(room.getHotelId(), "hotel id");

        if (!roomService.existsById(id)) {
            throw new IllegalArgumentException("Room with id " + id + " does not exist");
        }

        if (!hotelService.existsById(room.getHotelId())) {
            throw new IllegalArgumentException("Hotel with id " + room.getHotelId() + " does not exist");
        }

        room.setId(id);
        return roomService.save(room);
    }

    @Override
    public boolean deleteRoom(String id) {
        Guard.requireNonBlank(id, "room id");

        return roomService.deleteById(id);
    }

    @Override
    public int deleteRoomsByHotel(String hotelId) {
        Guard.requireNonBlank(hotelId, "hotel id");

        if (!hotelService.existsById(hotelId)) {
            throw new IllegalArgumentException("Hotel with id " + hotelId + " does not exist");
        }

        return roomService.deleteByHotelId(hotelId);
    }
}
