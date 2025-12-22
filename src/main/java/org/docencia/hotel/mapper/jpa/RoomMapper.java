package org.docencia.hotel.mapper.jpa;

import org.mapstruct.Mapper;
import org.docencia.hotel.domain.model.Room;
import org.docencia.hotel.persistence.jpa.entity.RoomEntity;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    RoomEntity toEntity(Room domain);
    Room toDomain(RoomEntity entity);
}
