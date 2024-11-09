package com.ms.user.controllers;

import com.ms.user.dtos.LoginWithCode;
import com.ms.user.dtos.LoginWithoutCodeDTO;
import com.ms.user.dtos.UserRecordDto;
import com.ms.user.models.UserModel;
import com.ms.user.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/users")
    public ResponseEntity<UserModel> saveUser(@RequestBody @Valid UserRecordDto userRecordDto) {
        var userModel = new UserModel();
        BeanUtils.copyProperties(userRecordDto, userModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(userModel));
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUserWithoutCode(@RequestBody @Valid LoginWithoutCodeDTO loginDTO){
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(loginDTO, userModel);

        return ResponseEntity.status(HttpStatus.OK).body(userService.login(userModel));
    }

    @PostMapping("/login/code")
    public ResponseEntity<String> loginUserWithCode(@RequestBody @Valid LoginWithCode loginDTO){
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(loginDTO, userModel);

        return ResponseEntity.status(HttpStatus.OK).body(userService.verifyCode(userModel));
    }




}
