package ru.otus.ms.app.messaging;

import lombok.extern.slf4j.Slf4j;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageBuilder;
import ru.otus.messagesystem.message.MessageType;
import ru.otus.ms.app.core.dto.AllUsersDto;
import ru.otus.ms.app.core.dto.UserDto;
import ru.otus.ms.app.core.service.DBServiceUser;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class GetAllUsersRequestHandler implements RequestHandler<UserDto> {


    private final DBServiceUser dbServiceUser;

    public GetAllUsersRequestHandler(DBServiceUser dbServiceUser) {

        this.dbServiceUser = dbServiceUser;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        log.info("Got request for Get All Users");
        List<UserDto> userDtos = dbServiceUser.getAllUsers()
                .stream().map(UserDto::fromUser)
                .collect(Collectors.toList());
        return Optional.of(MessageBuilder.buildReplyMessage(MessageType.ALL_USER_DATA, msg, new AllUsersDto(userDtos)));
    }
}
