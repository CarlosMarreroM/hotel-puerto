package org.docencia.hotel.service.impl;

import java.util.List;
import java.util.Optional;

import org.docencia.hotel.domain.model.Room;
import org.docencia.hotel.mapper.jpa.RoomMapper;
import org.docencia.hotel.persistence.jpa.entity.RoomEntity;
import org.docencia.hotel.persistence.repository.jpa.RoomRepository;
import org.docencia.hotel.service.api.RoomService;
import org.docencia.hotel.validation.Guard;
import org.springframework.stereotype.Service;

@Service
public class RoomServiceImpl implements RoomService {

    /**
     * Repositorio JPA de habitaciones.
     */
    private final RoomRepository roomRepository;

    /**
     * Mapeador entre la entidad JPA y el modelo de dominio.
     */
    private final RoomMapper roomMapper;

    /**
     * Constructor de la implementación del servicio de habitaciones.
     * 
     * @param roomRepository Repositorio JPA de habitaciones
     * @param roomMapper     Mapeador entre la entidad JPA y el modelo de dominio
     */
    public RoomServiceImpl(RoomRepository roomRepository, RoomMapper roomMapper) {
        this.roomRepository = roomRepository;
        this.roomMapper = roomMapper;
    }

    @Override
    public Room save(Room room) {
        Guard.requireNonNull(room, "room");

        RoomEntity roomEntityToSave = roomMapper.toEntity(room);
        RoomEntity savedRoomEntity = roomRepository.save(roomEntityToSave);
        return roomMapper.toDomain(savedRoomEntity);
    }

    @Override
    public boolean existsById(String id) {
        Guard.requireNonBlank(id, "room id");

        return roomRepository.existsById(id);
    }

    @Override
    public Optional<Room> findById(String id) {
        Guard.requireNonBlank(id, "room id");

        return roomRepository.findById(id)
                .map(roomMapper::toDomain);
    }

    @Override
    public List<Room> findAll() {
        return roomRepository.findAll()
                .stream()
                .map(roomMapper::toDomain)
                .toList();
    }

    @Override
    public List<Room> findByHotelId(String hotelId) {
        Guard.requireNonBlank(hotelId, "hotel id");

        return roomRepository.findByHotel_Id(hotelId)
                .stream()
                .map(roomMapper::toDomain)
                .toList();
    }

    @Override
    public List<Room> findByHotelIdAndType(String hotelId, String type) {
        Guard.requireNonBlank(hotelId, "hotel id");
        Guard.requireNonBlank(type, "room type");

        return roomRepository.findByHotel_IdAndType(hotelId, type)
                .stream()
                .map(roomMapper::toDomain)
                .toList();
    }

    @Override
    public boolean deleteById(String id) {
        Guard.requireNonBlank(id, "room id");

        if (!roomRepository.existsById(id)) {
            return false;
        }

        roomRepository.deleteById(id);
        return true;
    }

    @Override
    public int deleteByHotelId(String hotelId) {
        Guard.requireNonBlank(hotelId, "hotel id");

        return roomRepository.deleteByHotel_Id(hotelId);
    }
}
