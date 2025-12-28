package org.docencia.hotel.mapper.jpa;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.docencia.hotel.domain.model.Room;
import org.docencia.hotel.persistence.jpa.entity.HotelEntity;
import org.docencia.hotel.persistence.jpa.entity.RoomEntity;

/**
 * Mapper para convertir entre la entidad JPA RoomEntity
 * y el modelo de dominio Room.
 */
@Mapper(componentModel = "spring")
public interface RoomMapper {
    /**
     * Convierte un objeto Room del modelo de dominio
     * a una entidad JPA RoomEntity.
     * 
     * @param domain Objeto Room del modelo de dominio
     * @return Entidad JPA RoomEntity
     */
    @Mapping(target = "hotel", source = "hotelId")
    RoomEntity toEntity(Room domain);

    /**
     * Convierte una entidad JPA RoomEntity
     * a un objeto Room del modelo de dominio.
     * 
     * @param entity Entidad JPA RoomEntity
     * @return Objeto Room del modelo de dominio
     */
    @Mapping(target = "hotelId", source = "hotel.id") 
    Room toDomain(RoomEntity entity);

    /**
     * Mapea el identificador del hotel a la entidad HotelEntity.
     * 
     * @param hotelId Identificador del hotel
     * @return Entidad HotelEntity correspondiente
     */
    default HotelEntity map(String hotelId) {
        if (hotelId == null || hotelId.isBlank()) {
            return null;
        }
        return new HotelEntity(hotelId);
    }

    /**
     * Mapea la entidad HotelEntity al identificador del hotel.
     * 
     * @param hotel Entidad HotelEntity
     * @return Identificador del hotel
     */
    default String map(HotelEntity hotel) {
        return hotel != null ? hotel.getId() : null;
    }
}
