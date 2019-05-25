package de.shnyder.moose.user.web;

import java.util.List;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.openapitools.api.UsersApi;
import org.openapitools.model.ApiResultOkDeleteUsersModel;
import org.openapitools.model.ApiResultOkGetUserModel;
import org.openapitools.model.ApiResultOkGetUsersModel;
import org.openapitools.model.ApiResultOkPostUsersModel;
import org.openapitools.model.NewUserModel;
import org.openapitools.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;

import de.shnyder.moose.MooseError;
import de.shnyder.moose.user.domain.model.BaseUserDataDAO;
import de.shnyder.moose.user.service.UserService;

@Controller
@RequestMapping("/")
@CrossOrigin("*")
// @CrossOrigin(origins={"http://localhost:3000", "http://127.0.0.1:3000"})
class UserController implements UsersApi {

    @Autowired
    private ModelMapper mapper;

    /*
     * BaseUserDataDAO dao = userService.registerUser(newUserModel.getUsername());
     * ApiResultOkPostUsersModel rv = new ApiResultOkPostUsersModel(); return rv;
     */

    private UserModel convertToDto(BaseUserDataDAO dao) {
        UserModel dto = mapper.map(dao, UserModel.class);
        return dto;
    }
}