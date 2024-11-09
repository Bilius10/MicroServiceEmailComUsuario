package com.ms.user.services;

import com.ms.user.models.UserModel;
import com.ms.user.producers.UserProducer;
import com.ms.user.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    final UserRepository userRepository;
    final UserProducer userProducer;

    public UserService(UserRepository userRepository, UserProducer userProducer) {
        this.userRepository = userRepository;
        this.userProducer = userProducer;
    }

    @Transactional
    public UserModel save(UserModel userModel){
        userModel = userRepository.save(userModel);
        userProducer.publishMessageEmail(userModel);
        return userModel;
    }


    @Transactional
    public String login(UserModel userModel){
        Optional<UserModel> userModelNew = userRepository.findByNameAndSenha(userModel.getName(), userModel.getSenha());

        if(userModelNew.get().getEmail() == null){
            return "Usuario não encontrado";
        }

        String codigo = userProducer.publishCodeEmail(userModel);
        userRepository.updateByCodeTemporario(codigo, userModel.getName(), userModel.getSenha());

        return "Verifique seu email";
    }


}
