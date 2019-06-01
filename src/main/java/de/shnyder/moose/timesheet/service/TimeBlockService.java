package de.shnyder.moose.timesheet.service;

import java.sql.Time;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.shnyder.moose.MooseError;
import de.shnyder.moose.timesheet.domain.model.TimeBlockDAO;
import de.shnyder.moose.timesheet.domain.model.TimeBlockRepository;
import de.shnyder.moose.user.domain.model.BaseUserDataDAO;
import de.shnyder.moose.user.service.UserService;

@Service
public class TimeBlockService {

	@Autowired
	private TimeBlockRepository timeBlockRepository;

	@Autowired
	protected UserService userService;

	@Transactional
	public TimeBlockDAO saveNewTimeBlock(TimeBlockDAO dao, Long userId) throws MooseError {
		BaseUserDataDAO user = getValidUser(userId);
		TimeBlockDAO newDao = new TimeBlockDAO();
		newDao.setStartTime(dao.getStartTime());
		newDao.setEndTime(dao.getEndTime());
		newDao.setUser(user);
		timeBlockRepository.save(newDao);
		return newDao;
	}

	public void deleteTimeBlockById(long id) throws MooseError {
		timeBlockRepository.deleteById(id);
	}

	public Iterable<TimeBlockDAO> listAllTimeBlocks() throws MooseError {
		return timeBlockRepository.findAll();
	}

	public Iterable<TimeBlockDAO> listTimeBlocksBetween(Time since, Time until) throws MooseError {
		return timeBlockRepository.findAllByStartTimeBetween(since, until);
	}

	public TimeBlockDAO getTimeBlockById(long id) throws MooseError {
		Optional<TimeBlockDAO> optionalDAO = timeBlockRepository.findById(id);
		if (optionalDAO.isPresent())
			return optionalDAO.get();
		throw new MooseError("timeblock not found", MooseError.ERR_NOT_FOUND);
	}

	@Transactional
	public TimeBlockDAO updateTimeBlock(TimeBlockDAO dao) {
		TimeBlockDAO repoDAO = timeBlockRepository.findById(dao.getId()).get();
		repoDAO.setStartTime(dao.getStartTime());
		repoDAO.setEndTime(dao.getEndTime());
		timeBlockRepository.save(repoDAO);
		return repoDAO;
	}

	private BaseUserDataDAO getValidUser(Long id) {
		return userService.getUserById(id);
	}
}