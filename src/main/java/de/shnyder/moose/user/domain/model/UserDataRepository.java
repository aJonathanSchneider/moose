package de.shnyder.moose.user.domain.model;

import java.sql.Time;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * UserDataRepository
 */
public interface UserDataRepository extends CrudRepository<BaseUserDataDAO, Long> {

	@Query("select u from BaseUserDataDAO u where u.username = ?#{ principal?.name }")
	BaseUserDataDAO findByUsername();

	@Query("UPDATE BaseUserDataDAO SET last_login=:lastLogin WHERE username = ?#{ principal?.name }")
	public void updateLastLogin(Time lastLogin);

	@Query("select case when count(u)> 0 then true else false end from BaseUserDataDAO u where u.username = ?#{ principal?.name }")
	//@Query("select 1 from BaseUserDataDAO as m where m.username = ?#{ principal?.name }")
	boolean exists();

}