package org.docencia.hotel.mapper.jpa;

import org.docencia.hotel.domain.model.Hotel;
import org.docencia.hotel.persistence.jpa.entity.HotelEntity;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class HotelMapperTest {

    private final HotelMapper mapper = Mappers.getMapper(HotelMapper.class);

    @Test
    void toEntity_should_map_all_fields() {
        Hotel domain = new Hotel("H1", "Hotel Puerto", "Calle Mar 123");

        HotelEntity entity = mapper.toEntity(domain);

        assertNotNull(entity);
        assertEquals("H1", entity.getId());
        assertEquals("Hotel Puerto", entity.getHotelName());
        assertEquals("Calle Mar 123", entity.getAddress());
    }

    @Test
    void toDomain_should_map_all_fields() {
        HotelEntity entity = new HotelEntity("H1", "Hotel Puerto", "Calle Mar 123");

        Hotel domain = mapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals("H1", domain.getId());
        assertEquals("Hotel Puerto", domain.getHotelName());
        assertEquals("Calle Mar 123", domain.getAddress());
    }

    @Test
    void toEntity_should_return_null_when_input_is_null() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    void toDomain_should_return_null_when_input_is_null() {
        assertNull(mapper.toDomain(null));
    }
}
