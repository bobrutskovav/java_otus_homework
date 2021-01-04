package ru.otus.multiprocess.backend.messaging;

import lombok.extern.slf4j.Slf4j;
import ru.otus.multiprocess.backend.core.dto.UserDto;
import ru.otus.multiprocess.backend.core.model.User;
import ru.otus.multiprocess.backend.core.service.DBServiceUser;
import ru.otus.multiprocess.messagesystem.RequestHandler;
import ru.otus.multiprocess.messagesystem.message.Message;
import ru.otus.multiprocess.messagesystem.message.MessageBuilder;
import ru.otus.multiprocess.messagesystem.message.MessageHelper;
import ru.otus.multiprocess.messagesystem.message.MessageType;

import java.util.Optional;

@Slf4j
public class CreateUserRequestHandler implements RequestHandler<UserDto> {

    private final DBServiceUser dbServiceUser;

    public CreateUserRequestHandler(DBServiceUser dbServiceUser) {
        this.dbServiceUser = dbServiceUser;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        log.info("Got Create User Message {}", msg);
        UserDto userDto = MessageHelper.getPayload(msg);
        long newUserid = dbServiceUser.saveUser(UserDto.toUser(userDto));
        User createdUser = dbServiceUser.getUser(newUserid).orElseThrow(() -> new RuntimeException("Can't find created user!"));
        userDto = UserDto.fromUser(createdUser);
        return Optional.of(MessageBuilder.buildReplyMessage(MessageType.CREATE_USER, msg, userDto));
    }
}
