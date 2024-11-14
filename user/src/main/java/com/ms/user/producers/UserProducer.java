package com.ms.user.producers;

import com.ms.user.models.EmailEntity;
import com.ms.user.models.UserModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class UserProducer {

    final RabbitTemplate rabbitTemplate;

    public UserProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value(value = "${broker.queue.email.name}")
    private String routingKey;

    public void publishMessageEmail(UserModel userModel) {
        var emailDto = new EmailEntity();
        emailDto.setUserId(userModel.getUserId());
        emailDto.setEmailTo(userModel.getEmail());
        emailDto.setSubject("Cadastro realizado com sucesso!");
        emailDto.setText(userModel.getName() + ", seja bem vindo(a)! \nAgradecemos o seu cadastro, aproveite agora todos os recursos da nossa plataforma!");

        rabbitTemplate.convertAndSend("", routingKey, emailDto);
    }

    public void publishCodeEmail(UserModel userModel, String token){
        EmailEntity emailEntity = new EmailEntity();
        emailEntity.setUserId(userModel.getUserId());
        emailEntity.setEmailTo(userModel.getEmail());
        emailEntity.setSubject("Confirmação de login");

        emailEntity.setText(userModel.getName()+", seu codigo de login é "+token);
        rabbitTemplate.convertAndSend("", routingKey, emailEntity);

    }

}
