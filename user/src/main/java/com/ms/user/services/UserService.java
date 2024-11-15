package com.ms.user.services;

import com.ms.user.InfraSecurity.TokenService;
import com.ms.user.models.UserModel;
import com.ms.user.producers.UserProducer;
import com.ms.user.repositories.UserRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserProducer userProducer;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public UserService(UserProducer userProducer, UserRepository userRepository, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.userProducer = userProducer;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    public Optional<UserModel> register(UserModel userModel){
        Optional<UserModel> byName = userRepository.findByName(userModel.getName());

        if(byName.isPresent()){
            return byName;
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(userModel.getSenha());
        userModel.setSenha(encryptedPassword);
        userRepository.save(userModel);

        userProducer.publishMessageEmail(userModel);
        return Optional.of(userModel);
    }

    public Optional<UserModel> loginAndSendEmail(UserModel userModel){

        Optional<UserModel> byName = userRepository.findByName(userModel.getName());

        if(byName.isEmpty()){
            return byName;
        }

        var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userModel.getName(), userModel.getSenha());
        var auth = this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        var token = tokenService.generateToken((UserModel) auth.getPrincipal());

        userProducer.publishCodeEmail(byName.get(), token);

        return byName;
    }

    public List<UserModel> getAll(){
        return userRepository.findAll();
    }

    public Optional<UserModel> findByid(UUID id){
        return  userRepository.findById(id);
    }



}
