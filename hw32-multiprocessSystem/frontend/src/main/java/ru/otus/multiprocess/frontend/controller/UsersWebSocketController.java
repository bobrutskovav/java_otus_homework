package ru.otus.multiprocess.frontend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ru.otus.multiprocess.backend.core.dto.AllUsersDto;
import ru.otus.multiprocess.backend.core.dto.UserDto;
import ru.otus.multiprocess.messagesystem.client.MessageCallback;
import ru.otus.multiprocess.messagesystem.client.MsClient;
import ru.otus.multiprocess.messagesystem.message.Message;
import ru.otus.multiprocess.messagesystem.message.MessageType;


@Controller
@Slf4j
public class UsersWebSocketController {


    private final SimpMessagingTemplate webSocket;
    private final String targetServiceName;
    private final MsClient frontEndClient;

    public UsersWebSocketController(SimpMessagingTemplate webSocket,
                                    MsClient frontEndClient) {

        this.webSocket = webSocket;
        this.targetServiceName = "BACK_END_SOCKET_CLIENT";
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
