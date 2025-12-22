package org.docencia.hotel.mapper.jpa;

import org.mapstruct.Mapper;
import org.docencia.hotel.domain.model.Hotel;
import org.docencia.hotel.persistence.jpa.entity.HotelEntity;

@Mapper(componentModel = "spring")
public interface HotelMapper {
    HotelEntity toEntity(Hotel domain);
    Hotel toDomain(HotelEntity entity);
}
