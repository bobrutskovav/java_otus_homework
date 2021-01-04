package ru.otus.multiprocess.messagesystem.client;

public interface CallbackRegistry {
    void put(CallbackId id, MessageCallback<? extends ResultDataType> callback);

    MessageCallback<? extends ResultDataType> getAndRemove(CallbackId id);
}
