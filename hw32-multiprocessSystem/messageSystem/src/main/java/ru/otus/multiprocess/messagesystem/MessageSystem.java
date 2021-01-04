package ru.otus.multiprocess.messagesystem;


import ru.otus.multiprocess.messagesystem.message.Message;

public interface MessageSystem {

    void addClient(String clientName, String clientHost, int clientPort);

    void removeClient(String clientName);

    boolean newMessage(Message msg);

    void dispose() throws InterruptedException;

    void dispose(Runnable callback) throws InterruptedException;

    void start();

    int currentQueueSize();
}

