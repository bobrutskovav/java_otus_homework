package ru.otus.ms.app.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ru.otus.messagesystem.client.MessageCallback;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageType;
import ru.otus.ms.app.core.dto.AllUsersDto;
import ru.otus.ms.app.core.dto.UserDto;


@Controller
@Slf4j
public class UsersWebSocketController {


    private final SimpMessagingTemplate webSocket;
    private final String targetServiceName;
    private final MsClient frontEndClient;

    public UsersWebSocketController(SimpMessagingTemplate webSocket, @Value("${clientname.backend}") String targetServiceName,
                                    @Qualifier("frontEndMsClient") MsClient frontEndClient) {
        this.webSocket = webSocket;
        this.targetServiceName = targetServiceName;
        this.frontEndClient = frontEndClient;
    }


    @MessageMapping("/getusers")
    public void users() {
        Message getAllUserMsg = frontEndClient.produceMessage(targetServiceName, null, MessageType.ALL_USER_DATA, resultDataType -> {
            if (resultDataType instanceof AllUsersDto) {
                AllUsersDto allUsersDto = (AllUsersDto) resultDataType;
                log.info("Sending all users {}", allUsersDto);
                webSocket.convertAndSend("/topic/users", allUsersDto);
            }
        });
        frontEndClient.sendMessage(getAllUserMsg);
    }


    @MessageMapping("/createuser")
    public void createUser(@Payload UserDto userDto) {
        Message createUserMsg = frontEndClient.produceMessage(targetServiceName, userDto, MessageType.CREATE_USER, new MessageCallback<UserDto>() {
            @Override
            public void accept(UserDto userDto) {
                log.info("Sending new user {}", userDto);
                webSocket.convertAndSend("/topic/newUser", userDto);
            }
        });
        frontEndClient.sendMessage(createUserMsg);
    }

}
