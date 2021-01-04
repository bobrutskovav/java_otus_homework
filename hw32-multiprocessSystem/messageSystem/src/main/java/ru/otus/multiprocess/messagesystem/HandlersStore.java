package ru.otus.multiprocess.messagesystem;


import ru.otus.multiprocess.messagesystem.client.ResultDataType;
import ru.otus.multiprocess.messagesystem.message.MessageType;

public interface HandlersStore {
    RequestHandler<? extends ResultDataType> getHandlerByType(String messageTypeName);

    void addHandler(MessageType messageType, RequestHandler<? extends ResultDataType> handler);
}
