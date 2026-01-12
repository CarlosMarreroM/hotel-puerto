package org.docencia.hotel.mapper.jpa;

import javax.annotation.processing.Generated;
import org.docencia.hotel.domain.model.Booking;
import org.docencia.hotel.persistence.jpa.entity.BookingEntity;
import org.docencia.hotel.persistence.jpa.entity.GuestEntity;
import org.docencia.hotel.persistence.jpa.entity.RoomEntity;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-12T00:08:39+0000",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.17 (Azul Systems, Inc.)"
)
@Component
public class BookingMapperImpl implements BookingMapper {

    @Override
    public BookingEntity toEntity(Booking domain) {
        if ( domain == null ) {
            return null;
        }

        BookingEntity bookingEntity = new BookingEntity();

        bookingEntity.setRoom( mapRoom( domain.getRoomId() ) );
        bookingEntity.setGuest( mapGuest( domain.getGuestId() ) );
        bookingEntity.setId( domain.getId() );
        bookingEntity.setCheckIn( domain.getCheckIn() );
        bookingEntity.setCheckOut( domain.getCheckOut() );

        return bookingEntity;
    }

    @Override
    public Booking toDomain(BookingEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Booking booking = new Booking();

        booking.setRoomId( entityRoomId( entity ) );
        booking.setGuestId( entityGuestId( entity ) );
        booking.setId( entity.getId() );
        booking.setCheckIn( entity.getCheckIn() );
        booking.setCheckOut( entity.getCheckOut() );

        return booking;
    }

    private String entityRoomId(BookingEntity bookingEntity) {
        if ( bookingEntity == null ) {
            return null;
        }
        RoomEntity room = bookingEntity.getRoom();
        if ( room == null ) {
            return null;
        }
        String id = room.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityGuestId(BookingEntity bookingEntity) {
        if ( bookingEntity == null ) {
            return null;
        }
        GuestEntity guest = bookingEntity.getGuest();
        if ( guest == null ) {
            return null;
        }
        String id = guest.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
