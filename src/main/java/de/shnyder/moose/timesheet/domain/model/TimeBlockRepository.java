package de.shnyder.moose.timesheet.domain.model;

import java.sql.Time;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface TimeBlockRepository extends CrudRepository<TimeBlockDAO, Long> {
	
	List<TimeBlockDAO> findAllByStartTimeBetween(
		Time since,
		Time until
		);
}