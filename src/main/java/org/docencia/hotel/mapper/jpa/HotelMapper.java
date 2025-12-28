package org.docencia.hotel.mapper.jpa;

import org.mapstruct.Mapper;
import org.docencia.hotel.domain.model.Hotel;
import org.docencia.hotel.persistence.jpa.entity.HotelEntity;

/**
 * Mapeador entre la entidad JPA HotelEntity y el modelo de dominio Hotel.
 *
 * Utiliza MapStruct para generar automáticamente la implementación
 * de los métodos de mapeo entre ambas clases.
 */
@Mapper(componentModel = "spring")
public interface HotelMapper {
    /**
     * Convierte un objeto de dominio Hotel a su entidad JPA HotelEntity.
     * 
     * @param domain Objeto del dominio Hotel
     * @return Entidad JPA HotelEntity
     */
    HotelEntity toEntity(Hotel domain);

    /**
     * Convierte una entidad JPA HotelEntity a su objeto de dominio Hotel.
     * 
     * @param entity Entidad JPA HotelEntity
     * @return Objeto del dominio Hotel
     */
    Hotel toDomain(HotelEntity entity);
}
