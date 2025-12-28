package org.docencia.hotel.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.docencia.hotel.domain.model.Room;
import org.docencia.hotel.mapper.jpa.RoomMapper;
import org.docencia.hotel.persistence.jpa.entity.RoomEntity;
import org.docencia.hotel.persistence.repository.jpa.RoomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RoomServiceImplTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private RoomMapper roomMapper;

    @InjectMocks
    private RoomServiceImpl service;

    private static Room anyRoom() {
        return new Room(); // ajusta si no tienes ctor vacío
    }

    private static RoomEntity anyRoomEntity() {
        return new RoomEntity(); // ajusta si no tienes ctor vacío
    }

    // ===================== save =====================
    @Test
    @DisplayName("save: cuando room es null -> NullPointerException y no interactúa con repo/mapper")
    void save_whenRoomIsNull_throwsAndNoInteractions() {
        assertThrows(NullPointerException.class, () -> service.save(null));

        verifyNoInteractions(roomRepository);
        verifyNoInteractions(roomMapper);
    }

    @Test
    @DisplayName("save: mapea domain->entity, guarda en repo y devuelve entity->domain")
    void save_ok_mapsSavesAndMapsBack() {
        Room input = anyRoom();
        RoomEntity toSave = anyRoomEntity();
        RoomEntity saved = anyRoomEntity();
        Room expected = anyRoom();

        when(roomMapper.toEntity(input)).thenReturn(toSave);
        when(roomRepository.save(toSave)).thenReturn(saved);
        when(roomMapper.toDomain(saved)).thenReturn(expected);

        Room result = service.save(input);

        assertSame(expected, result);
        verify(roomMapper).toEntity(input);
        verify(roomRepository).save(toSave);
        verify(roomMapper).toDomain(saved);
        verifyNoMoreInteractions(roomRepository, roomMapper);
    }

    // ===================== existsById =====================
    @Nested
    class ExistsByIdTests {

        @Test
        @DisplayName("existsById: id null -> NullPointerException y no llama al repo")
        void existsById_nullId_throws() {
            assertThrows(NullPointerException.class, () -> service.existsById(null));
            verifyNoInteractions(roomRepository);
            verifyNoInteractions(roomMapper);
        }

        @Test
        @DisplayName("existsById: id blank -> IllegalArgumentException y no llama al repo")
        void existsById_blankId_throws() {
            assertThrows(IllegalArgumentException.class, () -> service.existsById("   "));
            verifyNoInteractions(roomRepository);
            verifyNoInteractions(roomMapper);
        }

        @Test
        @DisplayName("existsById: id válido -> delega en repo")
        void existsById_ok_delegates() {
            when(roomRepository.existsById("r1")).thenReturn(true);

            boolean result = service.existsById("r1");

            assertTrue(result);
            verify(roomRepository).existsById("r1");
            verifyNoMoreInteractions(roomRepository);
            verifyNoInteractions(roomMapper);
        }
    }

    // ===================== findById =====================
    @Nested
    class FindByIdTests {

        @Test
        @DisplayName("findById: id null -> NullPointerException y no llama al repo/mapper")
        void findById_nullId_throws() {
            assertThrows(NullPointerException.class, () -> service.findById(null));
            verifyNoInteractions(roomRepository);
            verifyNoInteractions(roomMapper);
        }

        @Test
        @DisplayName("findById: id blank -> IllegalArgumentException y no llama al repo/mapper")
        void findById_blankId_throws() {
            assertThrows(IllegalArgumentException.class, () -> service.findById(""));
            verifyNoInteractions(roomRepository);
            verifyNoInteractions(roomMapper);
        }

        @Test
        @DisplayName("findById: no existe -> devuelve Optional.empty y no mapea")
        void findById_notFound_returnsEmpty() {
            when(roomRepository.findById("r1")).thenReturn(Optional.empty());

            Optional<Room> result = service.findById("r1");

            assertTrue(result.isEmpty());
            verify(roomRepository).findById("r1");
            verifyNoMoreInteractions(roomRepository);
            verifyNoInteractions(roomMapper);
        }

        @Test
        @DisplayName("findById: existe -> mapea entity->domain")
        void findById_found_mapsToDomain() {
            RoomEntity entity = anyRoomEntity();
            Room domain = anyRoom();

            when(roomRepository.findById("r1")).thenReturn(Optional.of(entity));
            when(roomMapper.toDomain(entity)).thenReturn(domain);

            Optional<Room> result = service.findById("r1");

            assertTrue(result.isPresent());
            assertSame(domain, result.get());
            verify(roomRepository).findById("r1");
            verify(roomMapper).toDomain(entity);
            verifyNoMoreInteractions(roomRepository, roomMapper);
        }
    }

    // ===================== findAll =====================
    @Test
    @DisplayName("findAll: devuelve lista mapeada entity->domain")
    void findAll_mapsAllEntities() {
        RoomEntity e1 = anyRoomEntity();
        RoomEntity e2 = anyRoomEntity();
        Room r1 = anyRoom();
        Room r2 = anyRoom();

        when(roomRepository.findAll()).thenReturn(List.of(e1, e2));
        when(roomMapper.toDomain(e1)).thenReturn(r1);
        when(roomMapper.toDomain(e2)).thenReturn(r2);

        List<Room> result = service.findAll();

        assertEquals(2, result.size());
        assertSame(r1, result.get(0));
        assertSame(r2, result.get(1));

        verify(roomRepository).findAll();
        verify(roomMapper).toDomain(e1);
        verify(roomMapper).toDomain(e2);
        verifyNoMoreInteractions(roomRepository, roomMapper);
    }

    @Test
    @DisplayName("findAll: si repo devuelve vacío -> devuelve vacío y no llama mapper")
    void findAll_whenEmpty_returnsEmptyAndNoMapping() {
        when(roomRepository.findAll()).thenReturn(List.of());

        List<Room> result = service.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(roomRepository).findAll();
        verifyNoMoreInteractions(roomRepository);
        verifyNoInteractions(roomMapper);
    }

    // ===================== findByHotelId =====================
    @Nested
    class FindByHotelIdTests {

        @Test
        @DisplayName("findByHotelId: hotelId null -> NullPointerException")
        void findByHotelId_null_throws() {
            assertThrows(NullPointerException.class, () -> service.findByHotelId(null));
            verifyNoInteractions(roomRepository, roomMapper);
        }

        @Test
        @DisplayName("findByHotelId: hotelId blank -> IllegalArgumentException")
        void findByHotelId_blank_throws() {
            assertThrows(IllegalArgumentException.class, () -> service.findByHotelId("   "));
            verifyNoInteractions(roomRepository, roomMapper);
        }

        @Test
        @DisplayName("findByHotelId: mapea lista de entidades a dominio")
        void findByHotelId_ok_mapsList() {
            RoomEntity e1 = anyRoomEntity();
            RoomEntity e2 = anyRoomEntity();
            Room r1 = anyRoom();
            Room r2 = anyRoom();

            when(roomRepository.findByHotel_Id("h1")).thenReturn(List.of(e1, e2));
            when(roomMapper.toDomain(e1)).thenReturn(r1);
            when(roomMapper.toDomain(e2)).thenReturn(r2);

            List<Room> result = service.findByHotelId("h1");

            assertEquals(List.of(r1, r2), result);
            verify(roomRepository).findByHotel_Id("h1");
            verify(roomMapper).toDomain(e1);
            verify(roomMapper).toDomain(e2);
            verifyNoMoreInteractions(roomRepository, roomMapper);
        }
    }

    // ===================== findByHotelIdAndType =====================
    @Nested
    class FindByHotelIdAndTypeTests {

        @Test
        @DisplayName("findByHotelIdAndType: hotelId null -> NullPointerException")
        void findByHotelIdAndType_hotelIdNull_throws() {
            assertThrows(NullPointerException.class, () -> service.findByHotelIdAndType(null, "DOUBLE"));
            verifyNoInteractions(roomRepository, roomMapper);
        }

        @Test
        @DisplayName("findByHotelIdAndType: type null -> NullPointerException")
        void findByHotelIdAndType_typeNull_throws() {
            assertThrows(NullPointerException.class, () -> service.findByHotelIdAndType("h1", null));
            verifyNoInteractions(roomRepository, roomMapper);
        }

        @Test
        @DisplayName("findByHotelIdAndType: hotelId blank -> IllegalArgumentException")
        void findByHotelIdAndType_hotelIdBlank_throws() {
            assertThrows(IllegalArgumentException.class, () -> service.findByHotelIdAndType("  ", "DOUBLE"));
            verifyNoInteractions(roomRepository, roomMapper);
        }

        @Test
        @DisplayName("findByHotelIdAndType: type blank -> IllegalArgumentException")
        void findByHotelIdAndType_typeBlank_throws() {
            assertThrows(IllegalArgumentException.class, () -> service.findByHotelIdAndType("h1", "   "));
            verifyNoInteractions(roomRepository, roomMapper);
        }

        @Test
        @DisplayName("findByHotelIdAndType: mapea lista de entidades a dominio")
        void findByHotelIdAndType_ok_mapsList() {
            RoomEntity e1 = anyRoomEntity();
            RoomEntity e2 = anyRoomEntity();
            Room r1 = anyRoom();
            Room r2 = anyRoom();

            when(roomRepository.findByHotel_IdAndType("h1", "DOUBLE")).thenReturn(List.of(e1, e2));
            when(roomMapper.toDomain(e1)).thenReturn(r1);
            when(roomMapper.toDomain(e2)).thenReturn(r2);

            List<Room> result = service.findByHotelIdAndType("h1", "DOUBLE");

            assertEquals(List.of(r1, r2), result);
            verify(roomRepository).findByHotel_IdAndType("h1", "DOUBLE");
            verify(roomMapper).toDomain(e1);
            verify(roomMapper).toDomain(e2);
            verifyNoMoreInteractions(roomRepository, roomMapper);
        }
    }

    // ===================== deleteById =====================
    @Nested
    class DeleteByIdTests {

        @Test
        @DisplayName("deleteById: id null -> NullPointerException")
        void deleteById_null_throws() {
            assertThrows(NullPointerException.class, () -> service.deleteById(null));
            verifyNoInteractions(roomRepository);
            verifyNoInteractions(roomMapper);
        }

        @Test
        @DisplayName("deleteById: id blank -> IllegalArgumentException")
        void deleteById_blank_throws() {
            assertThrows(IllegalArgumentException.class, () -> service.deleteById(""));
            verifyNoInteractions(roomRepository);
            verifyNoInteractions(roomMapper);
        }

        @Test
        @DisplayName("deleteById: si no existe -> devuelve false y NO borra")
        void deleteById_whenNotExists_returnsFalseAndDoesNotDelete() {
            when(roomRepository.existsById("r1")).thenReturn(false);

            boolean result = service.deleteById("r1");

            assertFalse(result);
            verify(roomRepository).existsById("r1");
            verify(roomRepository, never()).deleteById(any());
            verifyNoMoreInteractions(roomRepository);
            verifyNoInteractions(roomMapper);
        }

        @Test
        @DisplayName("deleteById: si existe -> borra y devuelve true")
        void deleteById_whenExists_deletesAndReturnsTrue() {
            when(roomRepository.existsById("r1")).thenReturn(true);

            boolean result = service.deleteById("r1");

            assertTrue(result);
            verify(roomRepository).existsById("r1");
            verify(roomRepository).deleteById("r1");
            verifyNoMoreInteractions(roomRepository);
            verifyNoInteractions(roomMapper);
        }
    }

    // ===================== deleteByHotelId =====================
    @Nested
    class DeleteByHotelIdTests {

        @Test
        @DisplayName("deleteByHotelId: hotelId null -> NullPointerException")
        void deleteByHotelId_null_throws() {
            assertThrows(NullPointerException.class, () -> service.deleteByHotelId(null));
            verifyNoInteractions(roomRepository, roomMapper);
        }

        @Test
        @DisplayName("deleteByHotelId: hotelId blank -> IllegalArgumentException")
        void deleteByHotelId_blank_throws() {
            assertThrows(IllegalArgumentException.class, () -> service.deleteByHotelId("   "));
            verifyNoInteractions(roomRepository, roomMapper);
        }

        @Test
        @DisplayName("deleteByHotelId: delega en repo y devuelve el número de borrados")
        void deleteByHotelId_ok_delegatesAndReturnsCount() {
            when(roomRepository.deleteByHotel_Id("h1")).thenReturn(3);

            int result = service.deleteByHotelId("h1");

            assertEquals(3, result);
            verify(roomRepository).deleteByHotel_Id("h1");
            verifyNoMoreInteractions(roomRepository);
            verifyNoInteractions(roomMapper);
        }
    }
}
