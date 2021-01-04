package ru.otus.multiprocess.messagesystem;


import ru.otus.multiprocess.messagesystem.client.ResultDataType;
import ru.otus.multiprocess.messagesystem.message.Message;

import java.util.Optional;

public interface RequestHandler<T extends ResultDataType> {
    Optional<Message> handle(Message msg);
}
