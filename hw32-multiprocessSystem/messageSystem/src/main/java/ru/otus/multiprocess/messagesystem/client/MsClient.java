package ru.otus.multiprocess.messagesystem.client;


import ru.otus.multiprocess.messagesystem.message.Message;
import ru.otus.multiprocess.messagesystem.message.MessageType;

public interface MsClient {

    boolean sendMessage(Message msg);

    void handle(Message msg);

    String getName();

    <T extends ResultDataType> Message produceMessage(String to, T data, MessageType msgType, MessageCallback<T> callback);
}
