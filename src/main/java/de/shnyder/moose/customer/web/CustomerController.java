package de.shnyder.moose.customer.web;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.openapitools.api.ProjectsApi;
import org.openapitools.model.ApiResultOkDeleteProjectsModel;
import org.openapitools.model.ApiResultOkGetProjectModel;
import org.openapitools.model.ApiResultOkGetProjectsModel;
import org.openapitools.model.ApiResultOkPostProjectsModel;
import org.openapitools.model.ApiResultOkPutProjectsModel;
import org.openapitools.model.ApiSubDeleteOkModel;
import org.openapitools.model.NewProjectModel;
import org.openapitools.model.ProjectModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import de.shnyder.moose.MooseError;
import de.shnyder.moose.project.domain.model.BaseProjectDataDAO;
import de.shnyder.moose.project.service.ProjectService;

@Controller
@RequestMapping("/")
@CrossOrigin("*")
// @CrossOrigin(origins={"http://localhost:3000", "http://127.0.0.1:3000"})
class ProjectController implements ProjectsApi {

    protected String projectApiVersion = "0.0.1";

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ModelMapper mapper;

    @Override
    public ResponseEntity<ApiResultOkPostProjectsModel> createProject(@Valid NewProjectModel newProjectModel) {
        BaseProjectDataDAO dao = projectService.registerProject(newProjectModel.getProjectname());
        ApiResultOkPostProjectsModel rv = new ApiResultOkPostProjectsModel();
        rv.version(this.projectApiVersion);
        rv.setResult(this.convertToDto(dao));
        return new ResponseEntity<ApiResultOkPostProjectsModel>(rv, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResultOkDeleteProjectsModel> deleteProjectById(Long id) {
        projectService.deleteProjectById(id);
        ApiSubDeleteOkModel deleteModel = new ApiSubDeleteOkModel();
        deleteModel.deletedId(id);
        ApiResultOkDeleteProjectsModel rv = new ApiResultOkDeleteProjectsModel();
        rv.setVersion(this.projectApiVersion);
        rv.setResult(deleteModel);
        return new ResponseEntity<ApiResultOkDeleteProjectsModel>(rv, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResultOkGetProjectsModel> listProjects() {
        ApiResultOkGetProjectsModel rv = new ApiResultOkGetProjectsModel();
        rv.setVersion(this.projectApiVersion);
        List<ProjectModel> targetCollection = new ArrayList<ProjectModel>();
        this.projectService.listAllProjects().forEach((dao) -> {
            targetCollection.add(this.convertToDto(dao));
        });
        rv.setResult(targetCollection);
        return new ResponseEntity<ApiResultOkGetProjectsModel>(rv, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResultOkGetProjectModel> showProjectById(Long id) {
        ApiResultOkGetProjectModel rv = new ApiResultOkGetProjectModel();
        rv.version(projectApiVersion).result(convertToDto(projectService.getProjectById(id)));
        return new ResponseEntity<ApiResultOkGetProjectModel>(rv, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResultOkPutProjectsModel> updateProjectById(Long id, @Valid ProjectModel projectModel) {
        ApiResultOkPutProjectsModel rv = new ApiResultOkPutProjectsModel();
        if(id != projectModel.getId()) throw new MooseError("path id and body id not equal", MooseError.ERR_USERS_FAULT_USERCANFIX);
        rv.version(projectApiVersion).result(convertToDto(projectService.getProjectById(id)));
        return ProjectsApi.super.updateProjectById(id, projectModel);
    }

    private ProjectModel convertToDto(BaseProjectDataDAO dao) {
        ProjectModel dto = mapper.map(dao, ProjectModel.class);
        return dto;
    }
}