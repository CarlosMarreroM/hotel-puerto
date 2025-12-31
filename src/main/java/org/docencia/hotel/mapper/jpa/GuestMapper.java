package org.docencia.hotel.mapper.jpa;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.docencia.hotel.domain.model.Guest;
import org.docencia.hotel.persistence.jpa.entity.GuestEntity;

/**
 * Mapper para convertir entre la entidad JPA GuestEntity
 * y el modelo de dominio Guest.
 */
@Mapper(componentModel = "spring")
public interface GuestMapper {

    /**
     * Convierte una entidad JPA GuestEntity a un modelo de dominio Guest.
     * 
     * @param entity Entidad JPA a convertir.
     * @return Modelo de dominio convertido.
     */
    @Mapping(target = "preferences", ignore = true)
    Guest toDomain(GuestEntity entity);

    /**
     * Convierte un modelo de dominio Guest a una entidad JPA GuestEntity.
     * 
     * @param domain Modelo de dominio a convertir.
     * @return Entidad JPA convertida.
     */
    GuestEntity toEntity(Guest domain);
}