package org.docencia.hotel.mapper.jpa;

import org.docencia.hotel.domain.model.Booking;
import org.docencia.hotel.persistence.jpa.entity.BookingEntity;
import org.docencia.hotel.persistence.jpa.entity.GuestEntity;
import org.docencia.hotel.persistence.jpa.entity.RoomEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper para convertir entre la entidad JPA BookingEntity
 * y el modelo de dominio Booking.
 */
@Mapper(componentModel = "spring")
public interface BookingMapper {

    /**
     * Convierte un modelo de dominio Booking a una entidad JPA BookingEntity.
     * 
     * @param domain Modelo de dominio a convertir.
     * @return Entidad JPA convertida.
     */
    @Mapping(target = "room", source = "roomId")
    @Mapping(target = "guest", source = "guestId")
    BookingEntity toEntity(Booking domain);

    /**
     * Convierte una entidad JPA BookingEntity a un modelo de dominio Booking.
     * 
     * @param entity Entidad JPA a convertir.
     * @return Modelo de dominio convertido.
     */
    @Mapping(target = "roomId", source = "room.id")
    @Mapping(target = "guestId", source = "guest.id")
    Booking toDomain(BookingEntity entity);

    /**
     * Mapea el ID de la habitación a una entidad RoomEntity.
     * 
     * @param roomId ID de la habitación.
     * @return Entidad RoomEntity correspondiente.
     */
    default RoomEntity mapRoom(String roomId) {
        if (roomId == null || roomId.isBlank()) {
            return null;
        }
        return new RoomEntity(roomId);
    }

    /**
     * Mapea el ID del huésped a una entidad GuestEntity.
     * 
     * @param guestId ID del huésped.
     * @return Entidad GuestEntity correspondiente.
     */
    default GuestEntity mapGuest(String guestId) {
        if (guestId == null || guestId.isBlank()) {
            return null;
        }
        return new GuestEntity(guestId);
    }
}
