package ru.otus.ms.app;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.otus.messagesystem.HandlersStore;
import ru.otus.messagesystem.HandlersStoreImpl;
import ru.otus.messagesystem.MessageSystem;
import ru.otus.messagesystem.MessageSystemImpl;
import ru.otus.messagesystem.client.CallbackRegistry;
import ru.otus.messagesystem.client.CallbackRegistryImpl;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.client.MsClientImpl;
import ru.otus.messagesystem.message.MessageType;
import ru.otus.ms.app.cachehw.HwCache;
import ru.otus.ms.app.cachehw.MyCache;
import ru.otus.ms.app.core.model.AddressDataSet;
import ru.otus.ms.app.core.model.PhoneDataSet;
import ru.otus.ms.app.core.model.User;
import ru.otus.ms.app.core.service.DBServiceUser;
import ru.otus.ms.app.hibernate.HibernateUtils;
import ru.otus.ms.app.hibernate.sessionmanager.CustomSessionFactory;
import ru.otus.ms.app.hibernate.sessionmanager.HibernateCustomSessionFactory;
import ru.otus.ms.app.messaging.CreateUserRequestHandler;
import ru.otus.ms.app.messaging.GetAllUsersRequestHandler;
import ru.otus.ms.app.messaging.ResponseHandler;

@Configuration
@ComponentScan
public class Config {

    private static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";


    @Bean
    public HwCache<String, User> userHwCache() {
        return new MyCache<>();
    }

    @Bean
    public CustomSessionFactory sessionFactory() {
        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory(HIBERNATE_CFG_FILE,
                User.class, AddressDataSet.class, PhoneDataSet.class);
        return new HibernateCustomSessionFactory(sessionFactory);
    }

    @Bean
    public MessageSystem messageSystem() {
        return new MessageSystemImpl();


    }

    @Bean
    public CallbackRegistry callbackRegistry() {
        return new CallbackRegistryImpl();
    }


    @Bean
    public MsClient databaseMsClient(MessageSystem messageSystem,
                                     CallbackRegistry callbackRegistry,
                                     DBServiceUser dbServiceUser,
                                     @Value("${clientname.backend}") String clientName) {

        HandlersStore requestHandlerDatabaseStore = new HandlersStoreImpl();
        requestHandlerDatabaseStore.addHandler(MessageType.CREATE_USER, new CreateUserRequestHandler(dbServiceUser));
        requestHandlerDatabaseStore.addHandler(MessageType.ALL_USER_DATA, new GetAllUsersRequestHandler(dbServiceUser));

        MsClient dbMsClient = new MsClientImpl(clientName,
                messageSystem, requestHandlerDatabaseStore, callbackRegistry);

        messageSystem.addClient(dbMsClient);
        return dbMsClient;
    }


    @Bean
    public MsClient frontEndMsClient(MessageSystem messageSystem,
                                     CallbackRegistry callbackRegistry,
                                     @Value("${clientname.frontend}") String clientName) {

        HandlersStore requestHandlerFrontEndStore = new HandlersStoreImpl();
        requestHandlerFrontEndStore.addHandler(MessageType.ALL_USER_DATA, new ResponseHandler(callbackRegistry));
        requestHandlerFrontEndStore.addHandler(MessageType.CREATE_USER, new ResponseHandler(callbackRegistry));

        MsClient frontEndMsClient = new MsClientImpl(clientName, messageSystem, requestHandlerFrontEndStore, callbackRegistry);
        messageSystem.addClient(frontEndMsClient);
        return frontEndMsClient;
    }
}
