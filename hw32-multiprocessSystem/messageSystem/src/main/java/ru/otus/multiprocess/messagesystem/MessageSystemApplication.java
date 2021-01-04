package ru.otus.multiprocess.messagesystem;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MessageSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(MessageSystemApplication.class);
    }


    @Bean
    public MessageSystem messageSystem(@Value("${mysocket.messageSystem.port}") Integer messageSystemPort) {
        return new MessageSystemImpl(messageSystemPort);
    }
}
