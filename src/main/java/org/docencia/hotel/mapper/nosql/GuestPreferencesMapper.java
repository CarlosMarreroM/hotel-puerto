package org.docencia.hotel.mapper.nosql;

import org.mapstruct.Mapper;
import org.docencia.hotel.domain.model.GuestPreferences;
import org.docencia.hotel.persistence.nosql.document.GuestPreferencesDocument;

/**
 * Mapper para convertir entre el documento NoSQL GuestPreferencesDocument
 * y el modelo de dominio GuestPreferences.
 */
@Mapper(componentModel = "spring")
public interface GuestPreferencesMapper {
    /**
     * Convierte un modelo de dominio GuestPreferences a un documento NoSQL GuestPreferencesDocument.
     * 
     * @param domain Modelo de dominio a convertir.
     * @return Documento NoSQL convertido.
     */
    GuestPreferencesDocument toDocument(GuestPreferences domain);

    /**
     * Convierte un documento NoSQL GuestPreferencesDocument a un modelo de dominio GuestPreferences.
     * 
     * @param doc Documento NoSQL a convertir.
     * @return Modelo de dominio convertido.
     */
    GuestPreferences toDomain(GuestPreferencesDocument doc);
}
