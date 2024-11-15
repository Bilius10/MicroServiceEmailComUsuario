package com.ms.user.controllers;

import com.ms.user.Enum.UserRole;
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
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> loginUserWithoutCode(@RequestBody @Valid LoginWithoutCodeDTO loginDTO){

        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(loginDTO, userModel);

        Optional<UserModel> userModelOptional = userService.loginAndSendEmail(userModel);
        if(userModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Esse usuario não existe");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Verifique seu email");
    }

    @PostMapping("/register")
    public ResponseEntity<Object> saveUser(@RequestBody @Valid UserRecordDto userRecordDto) {

        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userRecordDto, userModel);

        Optional<UserModel> register = userService.register(userModel);

        if(register.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ja existe um usuario com esse nome");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(register);
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
