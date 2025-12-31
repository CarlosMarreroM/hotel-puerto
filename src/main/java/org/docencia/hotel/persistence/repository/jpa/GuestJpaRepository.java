package org.docencia.hotel.persistence.repository.jpa;

import org.docencia.hotel.persistence.jpa.entity.GuestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para la entidad GuestEntity.
 * 
 * Proporciona operaciones CRUD y consultas personalizadas
 * para gestionar los datos de los huespedes en la base de datos relacional.
 */
@Repository
public interface GuestJpaRepository extends JpaRepository<GuestEntity, String>{

}
