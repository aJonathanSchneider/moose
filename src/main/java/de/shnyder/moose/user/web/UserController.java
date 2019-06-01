package de.shnyder.moose.user.web;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.openapitools.api.UsersApi;
import org.openapitools.model.ApiResultOkDeleteUsersModel;
import org.openapitools.model.ApiResultOkGetUserModel;
import org.openapitools.model.ApiResultOkGetUsersModel;
import org.openapitools.model.ApiResultOkPostUsersModel;
import org.openapitools.model.ApiResultOkPutUsersModel;
import org.openapitools.model.ApiSubDeleteOkModel;
import org.openapitools.model.NewUserModel;
import org.openapitools.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import de.shnyder.moose.MooseError;
import de.shnyder.moose.user.domain.model.BaseUserDataDAO;
import de.shnyder.moose.user.service.UserService;

@Controller
@RequestMapping("/")
@CrossOrigin("*")
// @CrossOrigin(origins={"http://localhost:3000", "http://127.0.0.1:3000"})
class UserController implements UsersApi {

    protected String userApiVersion = "0.0.1";

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper mapper;

    @Override
    public ResponseEntity<ApiResultOkPostUsersModel> createUser(@Valid NewUserModel newUserModel) {
        BaseUserDataDAO dao = userService.registerUser(newUserModel.getUsername());
        ApiResultOkPostUsersModel rv = new ApiResultOkPostUsersModel();
        rv.version(this.userApiVersion);
        rv.setResult(this.convertToDto(dao));
        return new ResponseEntity<ApiResultOkPostUsersModel>(rv, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResultOkDeleteUsersModel> deleteUserById(Long id) {
        userService.deleteUserById(id);
        ApiSubDeleteOkModel deleteModel = new ApiSubDeleteOkModel();
        deleteModel.deletedId(id);
        ApiResultOkDeleteUsersModel rv = new ApiResultOkDeleteUsersModel();
        rv.setVersion(this.userApiVersion);
        rv.setResult(deleteModel);
        return new ResponseEntity<ApiResultOkDeleteUsersModel>(rv, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResultOkGetUsersModel> listUsers() {
        ApiResultOkGetUsersModel rv = new ApiResultOkGetUsersModel();
        rv.setVersion(this.userApiVersion);
        List<UserModel> targetCollection = new ArrayList<UserModel>();
        this.userService.listAllUsers().forEach((dao) -> {
            targetCollection.add(this.convertToDto(dao));
        });
        rv.setResult(targetCollection);
        return new ResponseEntity<ApiResultOkGetUsersModel>(rv, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResultOkGetUserModel> showUserById(Long id) {
        ApiResultOkGetUserModel rv = new ApiResultOkGetUserModel();
        rv.version(userApiVersion).result(convertToDto(userService.getUserById(id)));
        return new ResponseEntity<ApiResultOkGetUserModel>(rv, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResultOkPutUsersModel> updateUserById(Long id, @Valid UserModel userModel) {
        ApiResultOkPutUsersModel rv = new ApiResultOkPutUsersModel();
        if(id != userModel.getId()) throw new MooseError("path id and body id not equal", MooseError.ERR_USERS_FAULT_USERCANFIX);
        rv.version(userApiVersion).result(convertToDto(userService.getUserById(id)));
        return UsersApi.super.updateUserById(id, userModel);
    }

    private UserModel convertToDto(BaseUserDataDAO dao) {
        UserModel dto = mapper.map(dao, UserModel.class);
        return dto;
    }
}