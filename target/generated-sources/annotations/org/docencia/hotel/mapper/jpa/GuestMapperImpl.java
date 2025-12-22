package org.docencia.hotel.mapper.jpa;

import javax.annotation.processing.Generated;
import org.docencia.hotel.domain.model.Guest;
import org.docencia.hotel.persistence.jpa.entity.GuestEntity;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-22T00:42:45+0000",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.44.0.v20251118-1623, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class GuestMapperImpl implements GuestMapper {

    @Override
    public GuestEntity toEntity(Guest domain) {
        if ( domain == null ) {
            return null;
        }

        GuestEntity guestEntity = new GuestEntity();

        return guestEntity;
    }

    @Override
    public Guest toDomain(GuestEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Guest guest = new Guest();

        return guest;
    }
}
