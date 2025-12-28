package org.docencia.hotel.mapper.jpa;

import javax.annotation.processing.Generated;
import org.docencia.hotel.domain.model.Hotel;
import org.docencia.hotel.persistence.jpa.entity.HotelEntity;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-28T03:13:33+0000",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.17 (Azul Systems, Inc.)"
)
@Component
public class HotelMapperImpl implements HotelMapper {

    @Override
    public HotelEntity toEntity(Hotel domain) {
        if ( domain == null ) {
            return null;
        }

        HotelEntity hotelEntity = new HotelEntity();

        hotelEntity.setId( domain.getId() );
        hotelEntity.setHotelName( domain.getHotelName() );
        hotelEntity.setAddress( domain.getAddress() );

        return hotelEntity;
    }

    @Override
    public Hotel toDomain(HotelEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Hotel hotel = new Hotel();

        hotel.setId( entity.getId() );
        hotel.setHotelName( entity.getHotelName() );
        hotel.setAddress( entity.getAddress() );

        return hotel;
    }
}
