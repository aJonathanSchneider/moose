package de.shnyder.moose.user.domain.model;

import org.springframework.data.repository.CrudRepository;

/**
 * UserDataRepository
 */
public interface UserDataRepository extends CrudRepository<BaseUserDataDAO, Long> {

	BaseUserDataDAO findByUsername(String username);
	
}