package org.docencia.hotel.domain.impl;

import java.util.List;
import java.util.Optional;

import org.docencia.hotel.domain.api.RoomDomain;
import org.docencia.hotel.domain.model.Room;
import org.docencia.hotel.service.api.BookingService;
import org.docencia.hotel.service.api.HotelService;
import org.docencia.hotel.service.api.RoomService;
import org.docencia.hotel.validation.Guard;
import org.springframework.stereotype.Service;

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
     * Servicio de reservas.
     */
    private final BookingService bookingService;

    /**
     * Constructor de la implementación del dominio de habitaciones.
     * 
     * @param roomService    Servicio de habitaciones
     * @param hotelService   Servicio de hoteles
     * @param bookingService Servicio de reservas
     */
    public RoomDomainImpl(RoomService roomService, HotelService hotelService, BookingService bookingService) {
        this.roomService = roomService;
        this.hotelService = hotelService;
        this.bookingService = bookingService;
    }

    @Override
    public Room createRoom(Room room) {
        Guard.requireNonNull(room, "room");
        Guard.requireNonBlank(room.getId(), "room id");
        Guard.requireNonBlank(room.getNumber(), "room number");
        Guard.requireNonBlank(room.getHotelId(), "hotel id");

        if (roomService.existsById(room.getId())) {
            throw new IllegalStateException("room already exists: " + room.getId());
        }

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

        if (!roomService.existsById(id)) {
            return false;
        }

        if (bookingService.existsByRoomId(id)) {
            throw new IllegalArgumentException("cannot delete room " + id + " because it has bookings");
        }

        return roomService.deleteById(id);
    }

    @Override
    public int deleteRoomsByHotel(String hotelId) {
        Guard.requireNonBlank(hotelId, "hotel id");

        if (!hotelService.existsById(hotelId)) {
            throw new IllegalArgumentException("Hotel with id " + hotelId + " does not exist");
        }

        if (bookingService.existsByHotelId(hotelId)) {
            throw new IllegalArgumentException(
                    "cannot delete rooms for hotel " + hotelId + " because there are bookings");
        }

        return roomService.deleteByHotelId(hotelId);
    }
}
