package de.shnyder.moose.project.domain.model;

import org.springframework.data.repository.CrudRepository;

/**
 * ProjectDataRepository
 */
public interface ProjectDataRepository extends CrudRepository<BaseProjectDataDAO, Long> {

	BaseProjectDataDAO findByProjectname(String projectname);
	
}