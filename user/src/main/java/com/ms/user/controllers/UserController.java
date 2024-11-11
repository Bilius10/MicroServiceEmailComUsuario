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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


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

    @GetMapping
    public ResponseEntity<List<UserModel>> getAll(){
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findByid(@PathVariable UUID id){

        Optional<UserModel> byid = userService.findByid(id);

        if(byid.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuario não encontrado");
        }

        return ResponseEntity.status(HttpStatus.OK).body(byid);
    }




}
