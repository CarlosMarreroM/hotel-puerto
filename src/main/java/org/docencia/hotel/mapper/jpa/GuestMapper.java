package org.docencia.hotel.mapper.jpa;

import org.mapstruct.Mapper;
import org.docencia.hotel.domain.model.Guest;
import org.docencia.hotel.persistence.jpa.entity.GuestEntity;

@Mapper(componentModel = "spring")
public interface GuestMapper {
    GuestEntity toEntity(Guest domain);
    Guest toDomain(GuestEntity entity);
}
