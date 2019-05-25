package de.shnyder.moose.user.service;

import java.sql.Time;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.shnyder.moose.MooseError;
import de.shnyder.moose.user.domain.model.BaseUserDataDAO;
import de.shnyder.moose.user.domain.model.UserDataRepository;

/**
 * UserService
 */
@Service
public class UserService {

	@Autowired
	private UserDataRepository userDataRepository;

	public boolean isUserRegistered(String username) {
		return userDataRepository.findByUsername(username) != null;
	}

	@Transactional
	public BaseUserDataDAO registerUser(String username) throws MooseError {
		if(this.isUserRegistered(username)){
			throw new MooseError("tried to register a user that already exists");
		}
		BaseUserDataDAO baseUserDataDAO = new BaseUserDataDAO();
		baseUserDataDAO.setUsername(username);
		java.util.Date today = new java.util.Date();
		baseUserDataDAO.setLastLogin(new Time(today.getTime()));
		userDataRepository.save(baseUserDataDAO);
		return baseUserDataDAO;
	}

	public void deleteUserById(long id) throws MooseError {
		userDataRepository.deleteById(id);
	}

	public Iterable<BaseUserDataDAO> listAllUsers() throws MooseError {
		return userDataRepository.findAll();
	}

	public BaseUserDataDAO getUserById(long id) throws MooseError {
		Optional<BaseUserDataDAO> optionalDAO = userDataRepository.findById(id);
		if(optionalDAO.isPresent()) return optionalDAO.get();
		throw new MooseError("user not found", MooseError.ERR_NOT_FOUND);
	}
}