package com.ms.user.services;

import com.ms.user.models.UserModel;
import com.ms.user.producers.UserProducer;
import com.ms.user.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    final UserRepository userRepository;
    final UserProducer userProducer;

    public UserService(UserRepository userRepository, UserProducer userProducer) {
        this.userRepository = userRepository;
        this.userProducer = userProducer;
    }

    public UserModel save(UserModel userModel){
        userModel = userRepository.save(userModel);
        userProducer.publishMessageEmail(userModel);
        return userModel;
    }

    public String login(UserModel userModel){
        Optional<UserModel> userModelNew = userRepository.findByNameAndSenha(userModel.getName(), userModel.getSenha());

        if(userModelNew.isEmpty()){
            return "Usuario não encontrado";
        }

        String codigo = userProducer.publishCodeEmail(userModelNew.get());
        userRepository.updateByCodeTemporario(codigo, userModel.getName(), userModel.getSenha());

        return "Verifique seu email";
    }

    public String verifyCode(UserModel userModel){
        Optional<UserModel> userModelNew = userRepository.findByNameAndSenha(userModel.getName(), userModel.getSenha());

        if(userModelNew.isEmpty()){
            return "Usuario não encontrado";
        }

        if(!userModelNew.get().getCodeTemporario().equals(userModel.getCodeTemporario())){
            return "Codigo errado";
        }

        userRepository.updateByCodeTemporario("", userModel.getName(), userModel.getSenha());

        return "Login feito com sucesso";
    }

    public List<UserModel> getAll(){
        return userRepository.findAll();
    }

    public Optional<UserModel> findByid(UUID id){
        return  userRepository.findById(id);

    }

    public Optional<UserModel> findByNameAndSenha(String name, String senha){
        return userRepository.findByNameAndSenha(name, senha);
    }


}
