package ru.otus.multiprocess.frontend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.multiprocess.backend.messaging.ResponseHandler;
import ru.otus.multiprocess.messagesystem.HandlersStore;
import ru.otus.multiprocess.messagesystem.HandlersStoreImpl;
import ru.otus.multiprocess.messagesystem.client.CallbackRegistry;
import ru.otus.multiprocess.messagesystem.client.CallbackRegistryImpl;
import ru.otus.multiprocess.messagesystem.client.MsClient;
import ru.otus.multiprocess.messagesystem.client.MsClientImpl;
import ru.otus.multiprocess.messagesystem.message.MessageType;

@Configuration
public class Config {


    @Bean
    public MsClient msClient(@Value("${mysocket.clientName}") String clientName,
                             @Value("${mysocket.clientPort}") Integer clientPort,
                             @Value("${mysocket.messageSystem.host}") String messageSystemHost,
                             @Value("${mysocket.messageSystem.port}") Integer messageSystemPort) {

        CallbackRegistry callbackRegistry = new CallbackRegistryImpl();
        HandlersStore requestHandlerFrontEndStore = new HandlersStoreImpl();
        requestHandlerFrontEndStore.addHandler(MessageType.ALL_USER_DATA, new ResponseHandler(callbackRegistry));
        requestHandlerFrontEndStore.addHandler(MessageType.CREATE_USER, new ResponseHandler(callbackRegistry));
        return new MsClientImpl(clientName, messageSystemHost, messageSystemPort,
                clientPort, requestHandlerFrontEndStore,
                callbackRegistry);
    }

}
