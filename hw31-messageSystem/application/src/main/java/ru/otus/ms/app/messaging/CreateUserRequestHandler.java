package ru.otus.ms.app.messaging;

import lombok.extern.slf4j.Slf4j;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageBuilder;
import ru.otus.messagesystem.message.MessageHelper;
import ru.otus.messagesystem.message.MessageType;
import ru.otus.ms.app.core.dto.UserDto;
import ru.otus.ms.app.core.model.User;
import ru.otus.ms.app.core.service.DBServiceUser;

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
