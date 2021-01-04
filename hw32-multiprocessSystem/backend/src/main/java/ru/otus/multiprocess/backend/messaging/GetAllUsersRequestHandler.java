package ru.otus.multiprocess.backend.messaging;

import lombok.extern.slf4j.Slf4j;
import ru.otus.multiprocess.backend.core.dto.AllUsersDto;
import ru.otus.multiprocess.backend.core.dto.UserDto;
import ru.otus.multiprocess.backend.core.service.DBServiceUser;
import ru.otus.multiprocess.messagesystem.RequestHandler;
import ru.otus.multiprocess.messagesystem.message.Message;
import ru.otus.multiprocess.messagesystem.message.MessageBuilder;
import ru.otus.multiprocess.messagesystem.message.MessageType;

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
