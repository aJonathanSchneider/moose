package de.shnyder.moose.user.service;

import java.sql.Time;

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

	public boolean isUserRegistered() {
		return userDataRepository.exists();
	}

	@Transactional
	public BaseUserDataDAO registerUser(String username) throws MooseError {
		if(userDataRepository.exists()){
			throw new MooseError("tried to register a user that already exists");
		}
		BaseUserDataDAO baseUserDataDAO = new BaseUserDataDAO();
		baseUserDataDAO.setUsername(username);
		java.util.Date today = new java.util.Date();
		baseUserDataDAO.setLastLogin(new Time(today.getTime()));
		userDataRepository.save(baseUserDataDAO);
		return baseUserDataDAO;
	}
}