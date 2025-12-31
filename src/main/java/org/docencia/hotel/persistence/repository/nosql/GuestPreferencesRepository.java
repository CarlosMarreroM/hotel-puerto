package org.docencia.hotel.persistence.repository.nosql;

import org.docencia.hotel.persistence.nosql.document.GuestPreferencesDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repositorio NoSQL para el documento GuestPreferencesDocument.
 * 
 * Proporciona operaciones CRUD y consultas personalizadas
 * para gestionar los datos de las preferencias de los huespedes
 * en la base de datos MongoDB.
 */
public interface GuestPreferencesRepository extends MongoRepository<GuestPreferencesDocument, String>{

}
