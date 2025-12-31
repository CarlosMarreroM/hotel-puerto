package org.docencia.hotel.mapper.jpa;

import javax.annotation.processing.Generated;
import org.docencia.hotel.domain.model.Room;
import org.docencia.hotel.persistence.jpa.entity.HotelEntity;
import org.docencia.hotel.persistence.jpa.entity.RoomEntity;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-31T22:18:38+0000",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.44.0.v20251118-1623, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class RoomMapperImpl implements RoomMapper {

    @Override
    public RoomEntity toEntity(Room domain) {
        if ( domain == null ) {
            return null;
        }

        RoomEntity roomEntity = new RoomEntity();

        roomEntity.setHotel( map( domain.getHotelId() ) );
        roomEntity.setId( domain.getId() );
        roomEntity.setNumber( domain.getNumber() );
        roomEntity.setType( domain.getType() );
        roomEntity.setPricePerNight( domain.getPricePerNight() );

        return roomEntity;
    }

    @Override
    public Room toDomain(RoomEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Room room = new Room();

        room.setHotelId( entityHotelId( entity ) );
        room.setId( entity.getId() );
        room.setNumber( entity.getNumber() );
        room.setType( entity.getType() );
        room.setPricePerNight( entity.getPricePerNight() );

        return room;
    }

    private String entityHotelId(RoomEntity roomEntity) {
        if ( roomEntity == null ) {
            return null;
        }
        HotelEntity hotel = roomEntity.getHotel();
        if ( hotel == null ) {
            return null;
        }
        String id = hotel.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
