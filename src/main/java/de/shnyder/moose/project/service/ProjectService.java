package de.shnyder.moose.project.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.shnyder.moose.MooseError;
import de.shnyder.moose.project.domain.model.BaseProjectDataDAO;
import de.shnyder.moose.project.domain.model.ProjectDataRepository;

/**
 * ProjectService
 */
@Service
public class ProjectService {

	@Autowired
	private ProjectDataRepository projectDataRepository;

	public boolean isProjectRegistered(String projectname) {
		return projectDataRepository.findByProjectname(projectname) != null;
	}

	@Transactional
	public BaseProjectDataDAO registerProject(String projectname) throws MooseError {
		if(this.isProjectRegistered(projectname)){
			throw new MooseError("tried to register a project that already exists");
		}
		BaseProjectDataDAO baseProjectDataDAO = new BaseProjectDataDAO();
		baseProjectDataDAO.setProjectname(projectname);
		projectDataRepository.save(baseProjectDataDAO);
		return baseProjectDataDAO;
	}

	public void deleteProjectById(long id) throws MooseError {
		projectDataRepository.deleteById(id);
	}

	public Iterable<BaseProjectDataDAO> listAllProjects() throws MooseError {
		return projectDataRepository.findAll();
	}

	public BaseProjectDataDAO getProjectById(long id) throws MooseError {
		Optional<BaseProjectDataDAO> optionalDAO = projectDataRepository.findById(id);
		if(optionalDAO.isPresent()) return optionalDAO.get();
		throw new MooseError("project not found", MooseError.ERR_NOT_FOUND);
	}

	@Transactional
	public BaseProjectDataDAO updateProject(BaseProjectDataDAO dao) {
		BaseProjectDataDAO repoDAO = projectDataRepository.findById(dao.getId()).get();
		repoDAO.setProjectname(dao.getProjectname());
		projectDataRepository.save(repoDAO);
		return repoDAO;
	}
}