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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping
@RestController("/auth")
public class UserLoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity loginUserWithoutCode(@RequestBody @Valid LoginWithoutCodeDTO loginDTO){

        var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.name(), loginDTO.senha());
        var auth = this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/register")
    public ResponseEntity saveUser(@RequestBody @Valid UserRecordDto userRecordDto) {
        if(userService.findByNameAndSenha(userRecordDto.name(), userRecordDto.senha()).isPresent()){
            return ResponseEntity.badRequest().build();
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(userRecordDto.senha());
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userRecordDto, userModel);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/login/code")
    public ResponseEntity<String> loginUserWithCode(@RequestBody @Valid LoginWithCode loginDTO){
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(loginDTO, userModel);

        return ResponseEntity.status(HttpStatus.OK).body(userService.verifyCode(userModel));
    }

}
